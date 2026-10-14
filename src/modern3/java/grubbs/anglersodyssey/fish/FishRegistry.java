package grubbs.anglersodyssey.fish;

import grubbs.anglersodyssey.AnglersOdyssey;
import net.minecraft.item.Item;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class FishRegistry {

    public static final Map<String, Item> FISH_ITEMS = new LinkedHashMap<>();
    public static final Map<String, FishData> FISH_DATA = new LinkedHashMap<>();

    public static Item COOKED_FISH;

    private static final FoodComponent RAW_FISH_FOOD = new FoodComponent.Builder()
            .nutrition(2)
            .saturationModifier(0.1f)
            .build();

    private static final FoodComponent COOKED_FISH_FOOD = new FoodComponent.Builder()
            .nutrition(5)
            .saturationModifier(0.6f)
            .build();

    public static void register() {
        AnglersOdyssey.LOGGER.info("Registering Angler's Odyssey fish items.");

        COOKED_FISH = Registry.register(
                Registries.ITEM,
                Identifier.of(AnglersOdyssey.MOD_ID, "cooked_fish"),
                new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AnglersOdyssey.MOD_ID, "cooked_fish"))).food(COOKED_FISH_FOOD))
        );

        for (FishData fish : FishDatabase.FISH) {
            registerFish(fish);
        }

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(COOKED_FISH);

            for (Item fish : FISH_ITEMS.values()) {
                entries.add(fish);
            }
        });

        AnglersOdyssey.LOGGER.info("Registered {} Angler's Odyssey fish.", FISH_ITEMS.size());
    }

    private static void registerFish(FishData fish) {
        Item item = Registry.register(
                Registries.ITEM,
                Identifier.of(AnglersOdyssey.MOD_ID, fish.id()),
                new FishItem(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AnglersOdyssey.MOD_ID, fish.id()))).food(RAW_FISH_FOOD), fish)
        );

        FISH_ITEMS.put(fish.id(), item);
        FISH_DATA.put(fish.id(), fish);
    }
}
