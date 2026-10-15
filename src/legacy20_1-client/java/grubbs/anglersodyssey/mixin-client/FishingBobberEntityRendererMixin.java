package grubbs.anglersodyssey.mixin.client;

import grubbs.anglersodyssey.fish.AnglersRodItem;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FishingBobberEntityRenderer.class)
public class FishingBobberEntityRendererMixin {

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
            )
    )
    private boolean anglers_odyssey$renderCustomRodFromMainHand(ItemStack stack, Item item) {
        return stack.isOf(item)
                || (item == Items.FISHING_ROD && stack.getItem() instanceof AnglersRodItem);
    }
}
