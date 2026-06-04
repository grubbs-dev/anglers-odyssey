package grubbs.anglersodyssey.fish;

public record FishData(
        String id,
        String displayName,
        FishRarity rarity,
        String biome
) {
}