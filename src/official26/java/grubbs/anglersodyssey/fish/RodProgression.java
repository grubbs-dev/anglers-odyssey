package grubbs.anglersodyssey.fish;

import grubbs.anglersodyssey.AnglersOdyssey;
import grubbs.anglersodyssey.advancement.AnglersAdvancementHelper;
import grubbs.anglersodyssey.journal.FishJournalState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public final class RodProgression {

    private static final int[] DISCOVERY_THRESHOLDS = {20, 50, 100, 150};

    private RodProgression() {
    }

    public static void updateForDiscovery(ServerPlayer player, int discovered) {
        int targetTier = getTierForDiscovery(discovered);
        FishJournalState journal = FishJournalState.get(player.level());
        int currentTier = journal.getRodTier(player.getUUID());

        if (targetTier <= currentTier) {
            return;
        }

        ItemStack rod = createRod(targetTier);
        if (!player.getInventory().add(rod)) {
            returnRodToPlayer(player, rod);
        }

        journal.setRodTier(player.getUUID(), targetTier);

        player.sendSystemMessage(
                Component.literal("Rod upgrade unlocked: ")
                        .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
                        .append(rod.getHoverName())
                        .append(Component.literal(" — " + discovered + " species discovered")
                                .withStyle(ChatFormatting.GREEN)),
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

    /** Handles the 26.3 prediction parameter while remaining binary-compatible with 26.1/26.2. */
    private static void returnRodToPlayer(ServerPlayer player, ItemStack rod) {
        for (var method : player.getInventory().getClass().getMethods()) {
            if (!method.getName().equals("placeItemBackInInventory")) continue;
            try {
                if (method.getParameterCount() == 1) {
                    method.invoke(player.getInventory(), rod);
                    return;
                }
                if (method.getParameterCount() == 2 && method.getParameterTypes()[1] == boolean.class) {
                    method.invoke(player.getInventory(), rod, false);
                    return;
                }
                if (method.getParameterCount() == 2 && method.getParameterTypes()[1].getName().equals("net.minecraft.util.Prediction")) {
                    Object serverOnly = Enum.valueOf((Class) method.getParameterTypes()[1], "SERVER_ONLY");
                    method.invoke(player.getInventory(), rod, serverOnly);
                    return;
                }
            } catch (ReflectiveOperationException exception) {
                AnglersOdyssey.LOGGER.warn("Could not return rod upgrade to inventory", exception);
            }
        }
        AnglersOdyssey.LOGGER.error("No compatible inventory return method found for rod upgrade");
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
