package grubbs.anglersodyssey.journal;

import grubbs.anglersodyssey.fish.FishDatabase;
import grubbs.anglersodyssey.fish.FishData;
import grubbs.anglersodyssey.fish.FishRarity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class FishJournalItem extends Item {

    public FishJournalItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (world.isClient()) {
            return TypedActionResult.success(stack);
        }

        if (!(world instanceof ServerWorld serverWorld)) {
            return TypedActionResult.success(stack);
        }

        UUID playerUuid = player.getUuid();
        FishJournalState journal = FishJournalState.get(serverWorld);

        int totalFish = FishDatabase.FISH.length;
        int discovered = journal.getDiscoveredCount(playerUuid);
        int shiny = journal.getShinyCount(playerUuid);
        int totalCatches = journal.getTotalCatches(playerUuid);

        int completion = totalFish == 0
                ? 0
                : (int) Math.floor((discovered * 100.0D) / totalFish);

        player.sendMessage(
                Text.literal("=== Angler's Journal ===")
                        .formatted(Formatting.AQUA, Formatting.BOLD),
                false
        );

        player.sendMessage(
                Text.literal("Species Found: ")
                        .formatted(Formatting.GRAY)
                        .append(Text.literal(discovered + " / " + totalFish).formatted(Formatting.GREEN)),
                false
        );

        player.sendMessage(
                Text.literal("Completion: ")
                        .formatted(Formatting.GRAY)
                        .append(Text.literal(completion + "%").formatted(Formatting.YELLOW)),
                false
        );

        player.sendMessage(
                Text.literal("Shiny Species: ")
                        .formatted(Formatting.GRAY)
                        .append(Text.literal(String.valueOf(shiny)).formatted(Formatting.AQUA)),
                false
        );

        player.sendMessage(
        Text.literal("Total Fish Caught: ")
                .formatted(Formatting.GRAY)
                .append(Text.literal(String.valueOf(totalCatches)).formatted(Formatting.GOLD)),
        false
        );

        player.sendMessage(
                Text.literal("-------------------------").formatted(Formatting.DARK_GRAY),
                false
        );

        sendRarityLine(player, "Common", FishRarity.COMMON, playerUuid, journal, Formatting.WHITE);
        sendRarityLine(player, "Uncommon", FishRarity.UNCOMMON, playerUuid, journal, Formatting.GREEN);
        sendRarityLine(player, "Rare", FishRarity.RARE, playerUuid, journal, Formatting.BLUE);
        sendRarityLine(player, "Epic", FishRarity.EPIC, playerUuid, journal, Formatting.LIGHT_PURPLE);
        sendRarityLine(player, "Legendary", FishRarity.LEGENDARY, playerUuid, journal, Formatting.GOLD);

        return TypedActionResult.success(stack);
    }

    @Override
    public void appendTooltip(
            ItemStack stack,
            World world,
            List<Text> tooltip,
            TooltipContext context
    ) {
        tooltip.add(
                Text.translatable("tooltip.anglers_odyssey.journal")
                        .formatted(Formatting.GRAY)
        );

        super.appendTooltip(stack, world, tooltip, context);
    }

    private void sendRarityLine(
            PlayerEntity player,
            String label,
            FishRarity rarity,
            UUID playerUuid,
            FishJournalState journal,
            Formatting color
    ) {
        int total = 0;
        int found = 0;

        for (FishData fish : FishDatabase.FISH) {
            if (fish.rarity() == rarity) {
                total++;

                if (journal.hasDiscovered(
                        playerUuid,
                        new Identifier("anglers_odyssey", fish.id())
                )) {
                    found++;
                }
            }
        }

        player.sendMessage(
                Text.literal(label + ": ")
                        .formatted(color)
                        .append(Text.literal(found + " / " + total).formatted(Formatting.GRAY)),
                false
        );
    }
}