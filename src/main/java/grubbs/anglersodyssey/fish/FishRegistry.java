package grubbs.anglersodyssey.fish;

import grubbs.anglersodyssey.AnglersOdyssey;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class FishRegistry {

    public static final Map<String, Item> FISH_ITEMS = new LinkedHashMap<>();
    public static final Map<String, FishData> FISH_DATA = new LinkedHashMap<>();

    private static final FoodComponent RAW_FISH_FOOD = new FoodComponent.Builder()
            .hunger(2)
            .saturationModifier(0.1f)
            .build();

    private static final FishData[] FISH = {

            new FishData("bluegill", "Bluegill", FishRarity.COMMON),
            new FishData("pumpkinseed", "Pumpkinseed", FishRarity.COMMON),
            new FishData("green_sunfish", "Green Sunfish", FishRarity.COMMON),
            new FishData("redear_sunfish", "Redear Sunfish", FishRarity.COMMON),
            new FishData("warmouth", "Warmouth", FishRarity.COMMON),
            new FishData("black_crappie", "Black Crappie", FishRarity.COMMON),
            new FishData("white_crappie", "White Crappie", FishRarity.COMMON),
            new FishData("yellow_perch", "Yellow Perch", FishRarity.COMMON),
            new FishData("white_perch", "White Perch", FishRarity.COMMON),
            new FishData("rock_bass", "Rock Bass", FishRarity.COMMON),
            new FishData("largemouth_bass", "Largemouth Bass", FishRarity.COMMON),
            new FishData("smallmouth_bass", "Smallmouth Bass", FishRarity.COMMON),
            new FishData("white_bass", "White Bass", FishRarity.COMMON),
            new FishData("channel_catfish", "Channel Catfish", FishRarity.COMMON),
            new FishData("bullhead_catfish", "Bullhead Catfish", FishRarity.COMMON),
            new FishData("common_carp", "Common Carp", FishRarity.COMMON),
            new FishData("grass_carp", "Grass Carp", FishRarity.COMMON),
            new FishData("koi", "Koi", FishRarity.COMMON),
            new FishData("tilapia", "Tilapia", FishRarity.COMMON),
            new FishData("golden_shiner", "Golden Shiner", FishRarity.COMMON),
            new FishData("fathead_minnow", "Fathead Minnow", FishRarity.COMMON),
            new FishData("creek_chub", "Creek Chub", FishRarity.COMMON),
            new FishData("mosquitofish", "Mosquitofish", FishRarity.COMMON),
            new FishData("emerald_shiner", "Emerald Shiner", FishRarity.COMMON),
            new FishData("sardine", "Sardine", FishRarity.COMMON),
            new FishData("anchovy", "Anchovy", FishRarity.COMMON),
            new FishData("herring", "Herring", FishRarity.COMMON),
            new FishData("smelt", "Smelt", FishRarity.COMMON),
            new FishData("mackerel", "Mackerel", FishRarity.COMMON),
            new FishData("whiting", "Whiting", FishRarity.COMMON)

    };

    public static void register() {
        AnglersOdyssey.LOGGER.info("Registering Angler's Odyssey fish items.");

        for (FishData fish : FISH) {
            registerFish(fish);
        }

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            for (Item fish : FISH_ITEMS.values()) {
                entries.add(fish);
            }
        });

        AnglersOdyssey.LOGGER.info("Registered {} fish.", FISH_ITEMS.size());
    }

    private static void registerFish(FishData fish) {

        Item item = Registry.register(
                Registries.ITEM,
                new Identifier(AnglersOdyssey.MOD_ID, fish.id()),
                new Item(new FabricItemSettings().food(RAW_FISH_FOOD))
        );

        FISH_ITEMS.put(fish.id(), item);
        FISH_DATA.put(fish.id(), fish);
    }
}