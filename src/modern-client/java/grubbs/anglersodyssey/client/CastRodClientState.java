package grubbs.anglersodyssey.client;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

/** Immediate client-side cast state for 1.21 rod model predicates. */
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
