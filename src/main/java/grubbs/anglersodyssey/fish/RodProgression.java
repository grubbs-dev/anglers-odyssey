package grubbs.anglersodyssey.fish;

import grubbs.anglersodyssey.advancement.AnglersAdvancementHelper;
import grubbs.anglersodyssey.journal.FishJournalState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class RodProgression {

    private static final int[] DISCOVERY_THRESHOLDS = {20, 50, 100, 150};

    private RodProgression() {
    }

    public static void updateForDiscovery(ServerPlayerEntity player, int discovered) {
        int targetTier = getTierForDiscovery(discovered);
        FishJournalState journal = FishJournalState.get(player.getServerWorld());
        int currentTier = journal.getRodTier(player.getUuid());

        if (targetTier <= currentTier) {
            return;
        }

        ItemStack rod = createRod(targetTier);
        if (!player.giveItemStack(rod)) {
            player.dropItem(rod, false);
        }

        journal.setRodTier(player.getUuid(), targetTier);

        player.sendMessage(
                Text.literal("Rod upgrade unlocked: ")
                        .formatted(Formatting.GOLD, Formatting.BOLD)
                        .append(rod.getName())
                        .append(Text.literal(" — " + discovered + " species discovered")
                                .formatted(Formatting.GREEN)),
                false
        );
        player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 0.4F, 1.25F);
        AnglersAdvancementHelper.grant(player, "rod_tier_" + targetTier);
    }

    public static int getTierForDiscovery(int discovered) {
        int tier = 0;
        for (int i = 0; i < DISCOVERY_THRESHOLDS.length; i++) {
            if (discovered >= DISCOVERY_THRESHOLDS[i]) {
                tier = i + 1;
            }
        }
        return tier;
    }

    public static ItemStack createRod(int tier) {
        Item rod = switch (tier) {
            case 1 -> RodRegistry.REED_ROD;
            case 2 -> RodRegistry.OAK_ROD;
            case 3 -> RodRegistry.IRONWOOD_ROD;
            case 4 -> RodRegistry.ANGLERS_ROD;
            default -> throw new IllegalArgumentException("Unknown rod tier: " + tier);
        };

        return new ItemStack(rod);
    }
}
