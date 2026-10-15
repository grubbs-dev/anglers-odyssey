package grubbs.anglersodyssey.advancement;

import grubbs.anglersodyssey.AnglersOdyssey;
import net.minecraft.advancement.Advancement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class AnglersAdvancementHelper {

    public static void grant(ServerPlayerEntity player, String advancementPath) {
        Advancement advancement = player.server
                .getAdvancementLoader()
                .get(new Identifier(AnglersOdyssey.MOD_ID, advancementPath));

        if (advancement == null) {
            AnglersOdyssey.LOGGER.warn("Could not find advancement: {}", advancementPath);
            return;
        }

        player.getAdvancementTracker().grantCriterion(advancement, "granted");
    }
}
