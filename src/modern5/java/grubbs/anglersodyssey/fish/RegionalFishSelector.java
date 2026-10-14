package grubbs.anglersodyssey.fish;

import grubbs.anglersodyssey.AnglersOdyssey;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class RegionalFishSelector {

    private RegionalFishSelector() {
    }

    public static FishData getRandomFishForBiome(World world, BlockPos pos, FishRarity rarity, Random random) {
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

    public static boolean canCatchFishHere(World world, BlockPos pos, FishData fish) {
        RegistryEntry<Biome> biomeEntry = world.getBiome(pos);
        String biomePath = normalizeBiomeName(fish.biome());

        RegistryKey<Biome> exactVanillaBiome = RegistryKey.of(
                RegistryKeys.BIOME,
                Identifier.of("minecraft", biomePath)
        );

        if (biomeEntry.matchesKey(exactVanillaBiome)) {
            return true;
        }

        TagKey<Biome> regionTag = TagKey.of(
                RegistryKeys.BIOME,
                Identifier.of(AnglersOdyssey.MOD_ID, "regions/" + biomePath)
        );

        return biomeEntry.isIn(regionTag);
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
