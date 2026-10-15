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

    /** Supplies the concrete override missing from the 1.20.1 screen class. */
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (selectedTab != null) {
            selectedTab.move(0.0D, amount * 24.0D);
            return true;
        }
        return false;
    }
}
