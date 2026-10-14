package grubbs.anglersodyssey.journal;

import grubbs.anglersodyssey.AnglersOdyssey;
import net.minecraft.item.Item;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class FishJournalRegistry {

    public static Item ANGLERS_JOURNAL;

    public static void register() {
        ANGLERS_JOURNAL = Registry.register(
                Registries.ITEM,
                Identifier.of(AnglersOdyssey.MOD_ID, "anglers_journal"),
                new FishJournalItem(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AnglersOdyssey.MOD_ID, "anglers_journal"))).maxCount(1))
        );

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(ANGLERS_JOURNAL);
        });

        AnglersOdyssey.LOGGER.info("Registered Angler's Journal.");
    }
}
