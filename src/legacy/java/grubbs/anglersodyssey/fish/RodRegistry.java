package grubbs.anglersodyssey.fish;

import grubbs.anglersodyssey.AnglersOdyssey;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

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

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(REED_ROD);
            entries.add(OAK_ROD);
            entries.add(IRONWOOD_ROD);
            entries.add(ANGLERS_ROD);
        });

        AnglersOdyssey.LOGGER.info("Registered four Angler's Odyssey fishing rods.");
    }

    private static Item registerRod(String id, int tier, int durability, int lureLevel, int luckLevel, int unlockCount, String specialAbility) {
        return Registry.register(
                Registries.ITEM,
                new Identifier(AnglersOdyssey.MOD_ID, id),
                new AnglersRodItem(new FabricItemSettings().maxCount(1).maxDamage(durability),
                        tier, lureLevel, luckLevel, unlockCount, specialAbility)
        );
    }
}
