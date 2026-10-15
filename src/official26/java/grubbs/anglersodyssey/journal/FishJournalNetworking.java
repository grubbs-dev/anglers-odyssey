package grubbs.anglersodyssey.journal;

import grubbs.anglersodyssey.AnglersOdyssey;
import net.minecraft.resources.Identifier;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class FishJournalNetworking {
    public static final Identifier OPEN_JOURNAL =
            Identifier.fromNamespaceAndPath(AnglersOdyssey.MOD_ID, "open_fish_journal");

    public record OpenJournalPayload(byte[] data) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<OpenJournalPayload> ID = new CustomPacketPayload.Type<>(OPEN_JOURNAL);
        public static final StreamCodec<RegistryFriendlyByteBuf, OpenJournalPayload> CODEC =
                StreamCodec.composite(ByteBufCodecs.BYTE_ARRAY, OpenJournalPayload::data, OpenJournalPayload::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }

    public static void register() {
        PayloadTypeRegistry.clientboundPlay().register(OpenJournalPayload.ID, OpenJournalPayload.CODEC);
    }
}
