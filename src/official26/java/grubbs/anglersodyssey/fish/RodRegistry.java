package grubbs.anglersodyssey.fish;

import grubbs.anglersodyssey.AnglersOdyssey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

public final class RodRegistry {

    public static Item REED_ROD;
    public static Item OAK_ROD;
    public static Item IRONWOOD_ROD;
    public static Item ANGLERS_ROD;

    private RodRegistry() {
    }

    public static void register() {
        REED_ROD = registerRod("reed_rod", 1, 64, 0, 0, 20, "Steady Grip — easier early progression");
        OAK_ROD = registerRod("oak_rod", 2, 128, 1, 0, 50, "Quick Set — bites arrive sooner");
        IRONWOOD_ROD = registerRod("ironwood_rod", 3, 192, 2, 1, 100, "Treasure Sense — improved treasure odds");
        ANGLERS_ROD = registerRod("anglers_rod", 4, 256, 3, 2, 150, "Master Angler — faster bites and better treasure");

        CreativeModeTabEvents.modifyOutputEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("tools_and_utilities"))).register(entries -> {
            entries.accept(new ItemStack(REED_ROD));
            entries.accept(new ItemStack(OAK_ROD));
            entries.accept(new ItemStack(IRONWOOD_ROD));
            entries.accept(new ItemStack(ANGLERS_ROD));
        });

        AnglersOdyssey.LOGGER.info("Registered four Angler's Odyssey fishing rods.");
    }

    private static Item registerRod(String id, int tier, int durability, int lureLevel, int luckLevel, int unlockCount, String specialAbility) {
        return Registry.register(
                BuiltInRegistries.ITEM,
                Identifier.fromNamespaceAndPath(AnglersOdyssey.MOD_ID, id),
                new AnglersRodItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(AnglersOdyssey.MOD_ID, id))).stacksTo(1).durability(durability),
                        tier, lureLevel, luckLevel, unlockCount, specialAbility)
        );
    }
}
