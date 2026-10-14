package grubbs.anglersodyssey.advancement;

import grubbs.anglersodyssey.AnglersOdyssey;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.Identifier;

public class AnglersAdvancementHelper {

    public static void grant(ServerPlayer player, String advancementPath) {
        AdvancementHolder advancement = player.level().getServer()
                .getAdvancements()
                .get(Identifier.fromNamespaceAndPath(AnglersOdyssey.MOD_ID, advancementPath));

        if (advancement == null) {
            AnglersOdyssey.LOGGER.warn("Could not find advancement: {}", advancementPath);
            return;
        }

        player.getAdvancements().award(advancement, "granted");
    }
}
