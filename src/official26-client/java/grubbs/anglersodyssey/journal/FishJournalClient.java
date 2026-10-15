package grubbs.anglersodyssey.journal;

import grubbs.anglersodyssey.fish.FishRarity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class FishJournalClient {

    public static void registerClientPackets() {
        ClientPlayNetworking.registerGlobalReceiver(FishJournalNetworking.OpenJournalPayload.ID,
                (payload, context) -> {
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(payload.data()));
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

    String displayName = buf.readUtf();
    FishRarity rarity = FishRarity.valueOf(buf.readUtf());
    String biome = buf.readUtf();

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

                    context.client().execute(() -> context.client().setScreenAndShow(new FishJournalScreen(
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

