package grubbs.anglersodyssey.fish;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class FishItem extends Item {

    public static final String SHINY_NBT_KEY = "anglers_odyssey_shiny";

    private final FishData fishData;

    public FishItem(Settings settings, FishData fishData) {
        super(settings);
        this.fishData = fishData;
    }

    @Override
    public Text getName(ItemStack stack) {
        Text name = Text.translatable(this.getTranslationKey(stack))
                .formatted(fishData.rarity().getColor());

        // Legendary decoration
        if (fishData.rarity() == FishRarity.LEGENDARY) {
            name = Text.literal("≋ ")
                    .formatted(Formatting.GOLD)
                    .append(name)
                    .append(Text.literal(" ≋").formatted(Formatting.GOLD));
        }

        // Shiny decoration
        if (isShiny(stack)) {
            name = Text.literal("✦ ")
                    .append(name)
                    .append(Text.literal(" ✦").formatted(Formatting.AQUA));
        }

        return name;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        // Only Legendary fish get enchantment glint
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

    public static boolean isShiny(ItemStack stack) {
        return stack.hasNbt()
                && stack.getNbt() != null
                && stack.getNbt().getBoolean(SHINY_NBT_KEY);
    }
}