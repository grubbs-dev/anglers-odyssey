package grubbs.anglersodyssey.journal;

import grubbs.anglersodyssey.fish.FishDatabase;
import grubbs.anglersodyssey.fish.FishData;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import io.netty.buffer.Unpooled;
import grubbs.anglersodyssey.fish.FishRecordsState;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class FishJournalItem extends Item {

    public FishJournalItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        if (!(world instanceof ServerWorld serverWorld)) {
            return ActionResult.SUCCESS;
        }

        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return ActionResult.SUCCESS;
        }

        UUID playerUuid = player.getUuid();
        FishJournalState journal = FishJournalState.get(serverWorld);
        FishRecordsState records = FishRecordsState.get(serverWorld);

        int totalFish = FishDatabase.FISH.length;
        int discovered = journal.getDiscoveredCount(playerUuid);
        int shiny = journal.getShinyCount(playerUuid);
        int totalCatches = journal.getTotalCatches(playerUuid);
        int trophies = journal.getTrophyCatches(playerUuid);

        int completion = totalFish == 0
                ? 0
                : (int) Math.floor((discovered * 100.0D) / totalFish);

        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeInt(totalFish);
        buf.writeInt(discovered);
        buf.writeInt(completion);
        buf.writeInt(shiny);
        buf.writeInt(totalCatches);
        buf.writeInt(trophies);
        buf.writeInt(journal.getRodTier(playerUuid));

        buf.writeInt(FishDatabase.FISH.length);

for (FishData fish : FishDatabase.FISH) {
    Identifier fishId = Identifier.of("anglers_odyssey", fish.id());

    buf.writeIdentifier(fishId);

    buf.writeString(fish.displayName());
    buf.writeString(fish.rarity().name());
    buf.writeString(fish.biome());

    buf.writeBoolean(journal.hasDiscovered(playerUuid, fishId));
    buf.writeBoolean(journal.hasShiny(playerUuid, fishId));

    buf.writeDouble(records.getRecordWeight(playerUuid, fishId));
    buf.writeInt(records.getRecordLength(playerUuid, fishId));
    buf.writeInt(journal.getCatchCount(playerUuid, fishId));
}

        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        ServerPlayNetworking.send(serverPlayer, new FishJournalNetworking.OpenJournalPayload(data));

        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(
            ItemStack stack,
            TooltipContext context,
            net.minecraft.component.type.TooltipDisplayComponent display,
            Consumer<Text> tooltip,
            TooltipType type
    ) {
        tooltip.accept(
                Text.translatable("tooltip.anglers_odyssey.journal")
                        .formatted(Formatting.GRAY)
        );

        super.appendTooltip(stack, context, display, tooltip, type);
    }
}
