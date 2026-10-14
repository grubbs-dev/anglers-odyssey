package grubbs.anglersodyssey.journal;

import grubbs.anglersodyssey.fish.FishDatabase;
import grubbs.anglersodyssey.fish.FishData;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import io.netty.buffer.Unpooled;
import grubbs.anglersodyssey.fish.FishRecordsState;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class FishJournalItem extends Item {

    public FishJournalItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (!(world instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.SUCCESS;
        }

        UUID playerUuid = player.getUUID();
        FishJournalState journal = FishJournalState.get(serverLevel);
        FishRecordsState records = FishRecordsState.get(serverLevel);

        int totalFish = FishDatabase.FISH.length;
        int discovered = journal.getDiscoveredCount(playerUuid);
        int shiny = journal.getShinyCount(playerUuid);
        int totalCatches = journal.getTotalCatches(playerUuid);
        int trophies = journal.getTrophyCatches(playerUuid);

        int completion = totalFish == 0
                ? 0
                : (int) Math.floor((discovered * 100.0D) / totalFish);

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        buf.writeInt(totalFish);
        buf.writeInt(discovered);
        buf.writeInt(completion);
        buf.writeInt(shiny);
        buf.writeInt(totalCatches);
        buf.writeInt(trophies);
        buf.writeInt(journal.getRodTier(playerUuid));

        buf.writeInt(FishDatabase.FISH.length);

for (FishData fish : FishDatabase.FISH) {
    Identifier fishId = Identifier.fromNamespaceAndPath("anglers_odyssey", fish.id());

    buf.writeIdentifier(fishId);

    buf.writeUtf(fish.displayName());
    buf.writeUtf(fish.rarity().name());
    buf.writeUtf(fish.biome());

    buf.writeBoolean(journal.hasDiscovered(playerUuid, fishId));
    buf.writeBoolean(journal.hasShiny(playerUuid, fishId));

    buf.writeDouble(records.getRecordWeight(playerUuid, fishId));
    buf.writeInt(records.getRecordLength(playerUuid, fishId));
    buf.writeInt(journal.getCatchCount(playerUuid, fishId));
}

        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        ServerPlayNetworking.send(serverPlayer, new FishJournalNetworking.OpenJournalPayload(data));

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            net.minecraft.world.item.component.TooltipDisplay display,
            Consumer<Component> tooltip,
            TooltipFlag type
    ) {
        tooltip.accept(
                Component.translatable("tooltip.anglers_odyssey.journal")
                        .withStyle(ChatFormatting.GRAY)
        );

        super.appendHoverText(stack, context, display, tooltip, type);
    }
}
