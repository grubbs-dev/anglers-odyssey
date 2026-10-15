package grubbs.anglersodyssey.fish;

import grubbs.anglersodyssey.advancement.AnglersAdvancementHelper;
import grubbs.anglersodyssey.journal.FishJournalState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import java.util.List;
import java.util.function.Consumer;

public class FishItem extends Item {

    public static final String SHINY_NBT_KEY = "anglers_odyssey_shiny";
    public static final String WEIGHT_NBT_KEY = "anglers_odyssey_weight";
    public static final String LENGTH_NBT_KEY = "anglers_odyssey_length";
    public static final String RECORD_CHECKED_NBT_KEY = "anglers_odyssey_record_checked";

    private final FishData fishData;

    public FishItem(Properties properties, FishData fishData) {
        super(properties);
        this.fishData = fishData;
    }

    @Override
    public Component getName(ItemStack stack) {
        Component name = Component.translatable(this.getDescriptionId())
                .withStyle(fishData.rarity().getColor());

        if (fishData.rarity() == FishRarity.LEGENDARY) {
            name = Component.literal("≋ ")
                    .withStyle(ChatFormatting.GOLD)
                    .append(name)
                    .append(Component.literal(" ≋").withStyle(ChatFormatting.GOLD));
        }

        if (isShiny(stack)) {
            name = Component.literal("✦ ")
                    .withStyle(ChatFormatting.AQUA)
                    .append(name)
                    .append(Component.literal(" ✦").withStyle(ChatFormatting.AQUA));
        }

        return name;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return fishData.rarity() == FishRarity.LEGENDARY;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag type) {
        tooltip.accept(
                Component.literal(fishData.rarity().getDisplayName())
                        .withStyle(fishData.rarity().getColor())
        );

        tooltip.accept(
                Component.literal("Found in: " + fishData.biome())
                        .withStyle(ChatFormatting.DARK_AQUA)
        );

        if (hasMeasurements(stack)) {
            tooltip.accept(
                    Component.literal("Weight: " + getWeight(stack) + " lbs")
                            .withStyle(ChatFormatting.GRAY)
            );

            tooltip.accept(
                    Component.literal("Length: " + getLength(stack) + " in")
                            .withStyle(ChatFormatting.GRAY)
            );

            if (isTrophy(stack)) {
                tooltip.accept(
                        Component.literal("★ Trophy Catch ★")
                                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
                );
            }
        }

        if (isShiny(stack)) {
            tooltip.accept(
                    Component.literal("✦ Shiny Variant ✦")
                            .withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD)
            );

            tooltip.accept(
                    Component.literal("Extremely rare catch")
                            .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)
            );
        }

        super.appendHoverText(stack, context, display, tooltip, type);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel serverLevel, Entity entity, net.minecraft.world.entity.EquipmentSlot slot) {
        if (!(entity instanceof Player player)) return;

        if (!hasMeasurements(stack)) {
            applyRandomMeasurements(stack, serverLevel.getRandom());
        }

        CompoundTag nbt = customData(stack);

        if (nbt.getBoolean(RECORD_CHECKED_NBT_KEY).orElse(false)) {
            return;
        }

        Identifier fishId = BuiltInRegistries.ITEM.getKey(this);
        boolean shiny = isShiny(stack);

        FishJournalState journal = FishJournalState.get(serverLevel);

        boolean newSpecies = journal.registerCatch(player.getUUID(), fishId, shiny);

if (newSpecies) {

    int discovered = journal.getDiscoveredCount(player.getUUID());
    int total = FishDatabase.FISH.length;

    player.sendSystemMessage(
            Component.literal("New species discovered: ")
                    .withStyle(ChatFormatting.AQUA)
                    .append(getName(stack))
                    .append(Component.literal(" — " + discovered + " / " + total)
                            .withStyle(ChatFormatting.GREEN))
    );

    player.playSound(
            SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,
            0.25F,
            1.6F
    );

        if (player instanceof ServerPlayer serverPlayer) {

            RodProgression.updateForDiscovery(serverPlayer, discovered);

        if (discovered >= FishDatabase.FISH.length) {
            AnglersAdvancementHelper.grant(
                    serverPlayer,
                    "journal_complete"
            );
        }
    }
        }

        if (shiny && player instanceof ServerPlayer serverPlayer) {
            AnglersAdvancementHelper.grant(serverPlayer, "first_shiny");
        }

        boolean newRecord = FishRecordsState.get(serverLevel)
                .updateRecord(player.getUUID(), fishId, getWeight(stack), getLength(stack));

        nbt.putBoolean(RECORD_CHECKED_NBT_KEY, true);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));

        if (newRecord) {
            player.sendSystemMessage(
                    Component.literal("New record catch: ")
                            .withStyle(ChatFormatting.GOLD)
                            .append(getName(stack))
                            .append(Component.literal(" — " + getWeight(stack) + " lbs, " + getLength(stack) + " in")
                                    .withStyle(ChatFormatting.YELLOW))
            );

            player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.35F, 1.4F);
        }

        if (isTrophy(stack)) {
            int trophies = journal.incrementTrophyCatches(player.getUUID());

            if (player instanceof ServerPlayer serverPlayer) {
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

            player.sendSystemMessage(
                    Component.literal("Trophy catch: ")
                            .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
                            .append(getName(stack))
                            .append(Component.literal(" — " + getWeight(stack) + " lbs, " + getLength(stack) + " in")
                                    .withStyle(ChatFormatting.YELLOW))
            );

            player.playSound(SoundEvents.NOTE_BLOCK_BELL.value(), 0.25F, 1.8F);
        }
    }

    public static boolean isShiny(ItemStack stack) {
            return customData(stack).getBoolean(SHINY_NBT_KEY).orElse(false);
    }

    public static boolean hasMeasurements(ItemStack stack) {
        CompoundTag nbt = customData(stack);
        return nbt.contains(WEIGHT_NBT_KEY) && nbt.contains(LENGTH_NBT_KEY);
    }

    public static double getWeight(ItemStack stack) {
        return customData(stack).getDouble(WEIGHT_NBT_KEY).orElse(0.0D);
    }

    public static int getLength(ItemStack stack) {
        return customData(stack).getInt(LENGTH_NBT_KEY).orElse(0);
    }

    public boolean isTrophy(ItemStack stack) {
        if (!hasMeasurements(stack)) {
            return false;
        }

        SizeRange range = getSizeRangeForRarity(fishData.rarity());
        double trophyThreshold = range.maxWeight * 0.95D;

        return getWeight(stack) >= trophyThreshold;
    }

    private void applyRandomMeasurements(ItemStack stack, RandomSource random) {
        SizeRange range = getSizeRangeForRarity(fishData.rarity());

        double weight = range.minWeight + random.nextDouble() * (range.maxWeight - range.minWeight);
        weight = Math.round(weight * 10.0D) / 10.0D;

        int length = range.minLength + random.nextInt(range.maxLength - range.minLength + 1);

        CompoundTag nbt = customData(stack);
        nbt.putDouble(WEIGHT_NBT_KEY, weight);
        nbt.putInt(LENGTH_NBT_KEY, length);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
    }

    private static CompoundTag customData(ItemStack stack) {
        CustomData component = stack.get(DataComponents.CUSTOM_DATA);
        return component == null ? new CompoundTag() : component.copyTag();
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
