package grubbs.anglersodyssey.fish;

import grubbs.anglersodyssey.advancement.AnglersAdvancementHelper;
import grubbs.anglersodyssey.journal.FishJournalState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class FishItem extends Item {

    public static final String SHINY_NBT_KEY = "anglers_odyssey_shiny";
    public static final String WEIGHT_NBT_KEY = "anglers_odyssey_weight";
    public static final String LENGTH_NBT_KEY = "anglers_odyssey_length";
    public static final String RECORD_CHECKED_NBT_KEY = "anglers_odyssey_record_checked";

    private final FishData fishData;

    public FishItem(Settings settings, FishData fishData) {
        super(settings);
        this.fishData = fishData;
    }

    @Override
    public Text getName(ItemStack stack) {
        Text name = Text.translatable(this.getTranslationKey(stack))
                .formatted(fishData.rarity().getColor());

        if (fishData.rarity() == FishRarity.LEGENDARY) {
            name = Text.literal("≋ ")
                    .formatted(Formatting.GOLD)
                    .append(name)
                    .append(Text.literal(" ≋").formatted(Formatting.GOLD));
        }

        if (isShiny(stack)) {
            name = Text.literal("✦ ")
                    .formatted(Formatting.AQUA)
                    .append(name)
                    .append(Text.literal(" ✦").formatted(Formatting.AQUA));
        }

        return name;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return fishData.rarity() == FishRarity.LEGENDARY;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(
                Text.literal(fishData.rarity().getDisplayName())
                        .formatted(fishData.rarity().getColor())
        );

        tooltip.add(
                Text.literal("Found in: " + fishData.biome())
                        .formatted(Formatting.DARK_AQUA)
        );

        if (hasMeasurements(stack)) {
            tooltip.add(
                    Text.literal("Weight: " + getWeight(stack) + " lbs")
                            .formatted(Formatting.GRAY)
            );

            tooltip.add(
                    Text.literal("Length: " + getLength(stack) + " in")
                            .formatted(Formatting.GRAY)
            );

            if (isTrophy(stack)) {
                tooltip.add(
                        Text.literal("★ Trophy Catch ★")
                                .formatted(Formatting.GOLD, Formatting.BOLD)
                );
            }
        }

        if (isShiny(stack)) {
            tooltip.add(
                    Text.literal("✦ Shiny Variant ✦")
                            .formatted(Formatting.AQUA, Formatting.BOLD)
            );

            tooltip.add(
                    Text.literal("Extremely rare catch")
                            .formatted(Formatting.GRAY, Formatting.ITALIC)
            );
        }

        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient()) return;
        if (!(world instanceof ServerWorld serverWorld)) return;
        if (!(entity instanceof PlayerEntity player)) return;

        if (!hasMeasurements(stack)) {
            applyRandomMeasurements(stack, world.getRandom());
        }

        NbtCompound nbt = stack.getOrCreateNbt();

        if (nbt.getBoolean(RECORD_CHECKED_NBT_KEY)) {
            return;
        }

        Identifier fishId = Registries.ITEM.getId(this);
        boolean shiny = isShiny(stack);

        FishJournalState journal = FishJournalState.get(serverWorld);

        boolean newSpecies = journal.registerCatch(player.getUuid(), fishId, shiny);

        if (newSpecies) {
            int discovered = journal.getDiscoveredCount(player.getUuid());
            int total = FishDatabase.FISH.length;

            player.sendMessage(
                    Text.literal("New species discovered: ")
                            .formatted(Formatting.AQUA)
                            .append(getName(stack))
                            .append(Text.literal(" — " + discovered + " / " + total)
                                    .formatted(Formatting.GREEN)),
                    false
            );

            player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 0.25F, 1.6F);
        }

        if (shiny && player instanceof ServerPlayerEntity serverPlayer) {
            AnglersAdvancementHelper.grant(serverPlayer, "first_shiny");
        }

        boolean newRecord = FishRecordsState.get(serverWorld)
                .updateRecord(player.getUuid(), fishId, getWeight(stack), getLength(stack));

        nbt.putBoolean(RECORD_CHECKED_NBT_KEY, true);

        if (newRecord) {
            player.sendMessage(
                    Text.literal("New record catch: ")
                            .formatted(Formatting.GOLD)
                            .append(getName(stack))
                            .append(Text.literal(" — " + getWeight(stack) + " lbs, " + getLength(stack) + " in")
                                    .formatted(Formatting.YELLOW)),
                    true
            );

            player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.35F, 1.4F);
        }

        if (isTrophy(stack)) {
            int trophies = journal.incrementTrophyCatches(player.getUuid());

            if (player instanceof ServerPlayerEntity serverPlayer) {
                AnglersAdvancementHelper.grant(serverPlayer, "trophy_1");

                if (trophies >= 5) {
                    AnglersAdvancementHelper.grant(serverPlayer, "trophy_5");
                }

                if (trophies >= 10) {
                    AnglersAdvancementHelper.grant(serverPlayer, "trophy_10");
                }

                if (trophies >= 50) {
                    AnglersAdvancementHelper.grant(serverPlayer, "trophy_50");
                }

                if (trophies >= 100) {
                    AnglersAdvancementHelper.grant(serverPlayer, "trophy_100");
                }
            }

            player.sendMessage(
                    Text.literal("Trophy catch: ")
                            .formatted(Formatting.GOLD, Formatting.BOLD)
                            .append(getName(stack))
                            .append(Text.literal(" — " + getWeight(stack) + " lbs, " + getLength(stack) + " in")
                                    .formatted(Formatting.YELLOW)),
                    false
            );

            player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BELL.value(), SoundCategory.PLAYERS, 0.25F, 1.8F);
        }
    }

    public static boolean isShiny(ItemStack stack) {
        return stack.hasNbt()
                && stack.getNbt() != null
                && stack.getNbt().getBoolean(SHINY_NBT_KEY);
    }

    public static boolean hasMeasurements(ItemStack stack) {
        return stack.hasNbt()
                && stack.getNbt() != null
                && stack.getNbt().contains(WEIGHT_NBT_KEY)
                && stack.getNbt().contains(LENGTH_NBT_KEY);
    }

    public static double getWeight(ItemStack stack) {
        if (!stack.hasNbt() || stack.getNbt() == null) {
            return 0.0D;
        }

        return stack.getNbt().getDouble(WEIGHT_NBT_KEY);
    }

    public static int getLength(ItemStack stack) {
        if (!stack.hasNbt() || stack.getNbt() == null) {
            return 0;
        }

        return stack.getNbt().getInt(LENGTH_NBT_KEY);
    }

    public boolean isTrophy(ItemStack stack) {
        if (!hasMeasurements(stack)) {
            return false;
        }

        SizeRange range = getSizeRangeForRarity(fishData.rarity());
        double trophyThreshold = range.maxWeight * 0.95D;

        return getWeight(stack) >= trophyThreshold;
    }

    private void applyRandomMeasurements(ItemStack stack, Random random) {
        SizeRange range = getSizeRangeForRarity(fishData.rarity());

        double weight = range.minWeight + random.nextDouble() * (range.maxWeight - range.minWeight);
        weight = Math.round(weight * 10.0D) / 10.0D;

        int length = range.minLength + random.nextInt(range.maxLength - range.minLength + 1);

        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putDouble(WEIGHT_NBT_KEY, weight);
        nbt.putInt(LENGTH_NBT_KEY, length);
    }

    private static SizeRange getSizeRangeForRarity(FishRarity rarity) {
        return switch (rarity) {
            case COMMON -> new SizeRange(0.2D, 5.0D, 4, 18);
            case UNCOMMON -> new SizeRange(1.0D, 15.0D, 8, 32);
            case RARE -> new SizeRange(5.0D, 50.0D, 16, 60);
            case EPIC -> new SizeRange(20.0D, 200.0D, 30, 120);
            case LEGENDARY -> new SizeRange(75.0D, 1000.0D, 60, 300);
        };
    }

    private record SizeRange(double minWeight, double maxWeight, int minLength, int maxLength) {
    }
}