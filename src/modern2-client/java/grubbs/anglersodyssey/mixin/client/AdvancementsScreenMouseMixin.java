package grubbs.anglersodyssey.mixin.client;

import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/** Adds predictable mouse-wheel panning to the large Angler's Odyssey tree. */
@Mixin(AdvancementsScreen.class)
public abstract class AdvancementsScreenMouseMixin {

    @Shadow
    private AdvancementTab selectedTab;

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double amount) {
        if (selectedTab != null) {
            selectedTab.move(horizontalAmount * 24.0D, amount * 24.0D);
            return true;
        }
        return false;
    }
}
