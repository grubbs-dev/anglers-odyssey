package grubbs.anglersodyssey.fish;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.minecraft.util.Formatting;
import java.util.List;

public class FishItem extends Item {

    private final FishData fishData;

    public FishItem(Settings settings, FishData fishData) {
        super(settings);
        this.fishData = fishData;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable(this.getTranslationKey(stack))
                .formatted(fishData.rarity().getColor());
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal(fishData.rarity().getDisplayName())
                .formatted(fishData.rarity().getColor()));

        tooltip.add(Text.literal("Found in: " + fishData.biome())
        .formatted(Formatting.DARK_AQUA));
    }
}