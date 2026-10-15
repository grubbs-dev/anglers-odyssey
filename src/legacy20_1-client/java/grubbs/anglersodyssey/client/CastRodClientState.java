package grubbs.anglersodyssey.client;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

/**
 * Client-side fallback for the 1.20.1 cast model predicate.
 *
 * The server-owned fishing bobber is not consistently attached to the local
 * player quickly enough for the item model and hotbar render passes. The
 * client item-use hook supplies the immediate state, while the bobber removal
 * hook clears it when the cast ends.
 */
public final class CastRodClientState {
    private static PlayerEntity player;
    private static Item rod;

    private CastRodClientState() {
    }

    public static void markCast(PlayerEntity castPlayer, Item castRod) {
        player = castPlayer;
        rod = castRod;
    }

    public static void clear(PlayerEntity castPlayer) {
        if (player == castPlayer) {
            player = null;
            rod = null;
        }
    }

    public static boolean isCast(PlayerEntity queryPlayer, Item queryRod) {
        return player == queryPlayer && rod == queryRod;
    }
}
