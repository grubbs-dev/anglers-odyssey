package grubbs.anglersodyssey.journal;

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

public class FishJournalRegistry {

    public static Item ANGLERS_JOURNAL;

    public static void register() {
        ANGLERS_JOURNAL = Registry.register(
                BuiltInRegistries.ITEM,
                Identifier.fromNamespaceAndPath(AnglersOdyssey.MOD_ID, "anglers_journal"),
                new FishJournalItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(AnglersOdyssey.MOD_ID, "anglers_journal"))).stacksTo(1))
        );

        CreativeModeTabEvents.modifyOutputEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("tools_and_utilities"))).register(entries -> {
            entries.accept(new ItemStack(ANGLERS_JOURNAL));
        });

        AnglersOdyssey.LOGGER.info("Registered Angler's Journal.");
    }
}
