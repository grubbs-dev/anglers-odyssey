package grubbs.anglersodyssey.fish;

import net.minecraft.util.Formatting;

public enum FishRarity {
    COMMON("Common", Formatting.GRAY),
    UNCOMMON("Uncommon", Formatting.GREEN),
    RARE("Rare", Formatting.BLUE),
    EPIC("Epic", Formatting.LIGHT_PURPLE),
    LEGENDARY("Legendary", Formatting.GOLD);

    private final String displayName;
    private final Formatting color;

    FishRarity(String displayName, Formatting color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Formatting getColor() {
        return color;
    }
}