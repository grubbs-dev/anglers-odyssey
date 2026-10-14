package grubbs.anglersodyssey.journal;

import grubbs.anglersodyssey.AnglersOdyssey;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public class FishJournalNetworking {
    public static final Identifier OPEN_JOURNAL =
            Identifier.of(AnglersOdyssey.MOD_ID, "open_fish_journal");

    public record OpenJournalPayload(byte[] data) implements CustomPayload {
        public static final CustomPayload.Id<OpenJournalPayload> ID = new CustomPayload.Id<>(OPEN_JOURNAL);
        public static final PacketCodec<RegistryByteBuf, OpenJournalPayload> CODEC = PacketCodec.of(
                (payload, buf) -> buf.writeByteArray(payload.data),
                buf -> new OpenJournalPayload(buf.readByteArray())
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public static void register() {
        PayloadTypeRegistry.playS2C().register(OpenJournalPayload.ID, OpenJournalPayload.CODEC);
    }
}
