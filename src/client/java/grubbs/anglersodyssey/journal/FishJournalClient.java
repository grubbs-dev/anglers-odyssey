package grubbs.anglersodyssey.journal;

import grubbs.anglersodyssey.fish.FishRarity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class FishJournalClient {

    public static void registerClientPackets() {
        ClientPlayNetworking.registerGlobalReceiver(FishJournalNetworking.OPEN_JOURNAL,
                (client, handler, buf, responseSender) -> {
                    int totalFish = buf.readInt();
                    int discovered = buf.readInt();
                    int completion = buf.readInt();
                    int shiny = buf.readInt();
                    int totalCatches = buf.readInt();
                    int trophies = buf.readInt();
                    int rodTier = buf.readInt();

                    int fishCount = buf.readInt();
                    List<FishJournalScreen.FishEntry> entries = new ArrayList<>();

for (int i = 0; i < fishCount; i++) {

    Identifier fishId = buf.readIdentifier();

    String displayName = buf.readString();
    FishRarity rarity = FishRarity.valueOf(buf.readString());
    String biome = buf.readString();

    boolean discoveredFish = buf.readBoolean();
    boolean shinyFish = buf.readBoolean();

    double recordWeight = buf.readDouble();
    int recordLength = buf.readInt();
    int catchCount = buf.readInt();

    entries.add(new FishJournalScreen.FishEntry(
            fishId,
            displayName,
            rarity,
            biome,
            discoveredFish,
            shinyFish,
            recordWeight,
            recordLength,
            catchCount
    ));
}

                    client.execute(() -> client.setScreen(new FishJournalScreen(
                            totalFish,
                            discovered,
                            completion,
                            shiny,
                            totalCatches,
                            trophies,
                            rodTier,
                            entries
                    )));
                });
    }
}
