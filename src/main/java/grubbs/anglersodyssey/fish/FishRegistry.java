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

    public static void register() {
        AnglersOdyssey.LOGGER.info("Registering Angler's Odyssey fish items.");

        for (FishData fish : FishDatabase.FISH) {
            registerFish(fish);
        }

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            for (Item fish : FISH_ITEMS.values()) {
                entries.add(fish);
            }
        });

        AnglersOdyssey.LOGGER.info("Registered {} Angler's Odyssey fish.", FISH_ITEMS.size());
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