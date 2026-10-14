package grubbs.anglersodyssey.advancement;

import grubbs.anglersodyssey.AnglersOdyssey;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class AnglersAdvancementHelper {

    public static void grant(ServerPlayerEntity player, String advancementPath) {
        AdvancementEntry advancement = player.getEntityWorld().getServer()
                .getAdvancementLoader()
                .get(Identifier.of(AnglersOdyssey.MOD_ID, advancementPath));

        if (advancement == null) {
            AnglersOdyssey.LOGGER.warn("Could not find advancement: {}", advancementPath);
            return;
        }

        player.getAdvancementTracker().grantCriterion(advancement, "granted");
    }
}
