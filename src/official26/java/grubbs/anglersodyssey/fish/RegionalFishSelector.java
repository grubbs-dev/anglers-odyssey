package grubbs.anglersodyssey.fish;

import grubbs.anglersodyssey.AnglersOdyssey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class RegionalFishSelector {

    private RegionalFishSelector() {
    }

    public static FishData getRandomFishForBiome(Level world, BlockPos pos, FishRarity rarity, RandomSource random) {
        List<FishData> validFish = new ArrayList<>();

        for (FishData fish : FishDatabase.FISH) {
            if (fish.rarity() != rarity) continue;

            if (canCatchFishHere(world, pos, fish)) {
                validFish.add(fish);
            }
        }

        if (validFish.isEmpty()) {
            return null;
        }

        return validFish.get(random.nextInt(validFish.size()));
    }

    public static boolean canCatchFishHere(Level world, BlockPos pos, FishData fish) {
        Holder<Biome> biomeEntry = world.getBiome(pos);
        String biomePath = normalizeBiomeName(fish.biome());

        ResourceKey<Biome> exactVanillaBiome = ResourceKey.create(
                Registries.BIOME,
                Identifier.fromNamespaceAndPath("minecraft", biomePath)
        );

        if (biomeEntry.is(exactVanillaBiome)) {
            return true;
        }

        TagKey<Biome> regionTag = TagKey.create(
                Registries.BIOME,
                Identifier.fromNamespaceAndPath(AnglersOdyssey.MOD_ID, "regions/" + biomePath)
        );

        return biomeEntry.is(regionTag);
    }

    private static String normalizeBiomeName(String biomeName) {
        return biomeName
                .toLowerCase(Locale.ROOT)
                .replace("minecraft:", "")
                .replace(" ", "_")
                .replace("-", "_")
                .replace("'", "")
                .replace("(", "")
                .replace(")", "");
    }
}
