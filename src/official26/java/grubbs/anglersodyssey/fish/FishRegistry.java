package grubbs.anglersodyssey.fish;

import grubbs.anglersodyssey.AnglersOdyssey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class FishRegistry {

    public static final Map<String, Item> FISH_ITEMS = new LinkedHashMap<>();
    public static final Map<String, FishData> FISH_DATA = new LinkedHashMap<>();

    public static Item COOKED_FISH;

    private static final FoodProperties RAW_FISH_FOOD = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.1f)
            .build();

    private static final FoodProperties COOKED_FISH_FOOD = new FoodProperties.Builder()
            .nutrition(5)
            .saturationModifier(0.6f)
            .build();

    public static void register() {
        AnglersOdyssey.LOGGER.info("Registering Angler's Odyssey fish items.");

        COOKED_FISH = Registry.register(
                BuiltInRegistries.ITEM,
                Identifier.fromNamespaceAndPath(AnglersOdyssey.MOD_ID, "cooked_fish"),
                new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(AnglersOdyssey.MOD_ID, "cooked_fish"))).food(COOKED_FISH_FOOD))
        );

        for (FishData fish : FishDatabase.FISH) {
            registerFish(fish);
        }

        CreativeModeTabEvents.modifyOutputEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("food_and_drinks"))).register(entries -> {
            entries.accept(new ItemStack(COOKED_FISH));

            for (Item fish : FISH_ITEMS.values()) {
                entries.accept(new ItemStack(fish));
            }
        });

        AnglersOdyssey.LOGGER.info("Registered {} Angler's Odyssey fish.", FISH_ITEMS.size());
    }

    private static void registerFish(FishData fish) {
        Item item = Registry.register(
                BuiltInRegistries.ITEM,
                Identifier.fromNamespaceAndPath(AnglersOdyssey.MOD_ID, fish.id()),
                new FishItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(AnglersOdyssey.MOD_ID, fish.id()))).food(RAW_FISH_FOOD), fish)
        );

        FISH_ITEMS.put(fish.id(), item);
        FISH_DATA.put(fish.id(), fish);
    }
}
