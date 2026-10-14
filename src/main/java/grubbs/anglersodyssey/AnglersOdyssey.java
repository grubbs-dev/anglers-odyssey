package grubbs.anglersodyssey;

import grubbs.anglersodyssey.fish.FishRegistry;
import grubbs.anglersodyssey.fish.RodRegistry;
import grubbs.anglersodyssey.journal.FishJournalRegistry;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnglersOdyssey implements ModInitializer {

	public static final String MOD_ID = "anglers_odyssey";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Angler's Odyssey is starting registration...");

		FishRegistry.register();
		RodRegistry.register();
		FishJournalRegistry.register();

		LOGGER.info("Angler's Odyssey has loaded.");
	}
}
