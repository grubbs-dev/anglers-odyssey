package grubbs.anglersodyssey.client;

import grubbs.anglersodyssey.journal.FishJournalClient;
import net.fabricmc.api.ClientModInitializer;

public class AnglersOdysseyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FishJournalClient.registerClientPackets();
    }
}

