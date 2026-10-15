package grubbs.anglersodyssey.mixin.client;

import grubbs.anglersodyssey.client.CastRodClientState;
import grubbs.anglersodyssey.fish.AnglersRodItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnglersRodItem.class)
public abstract class AnglersRodItemClientMixin {
    @Inject(method = "use", at = @At("HEAD"))
    private void anglers_odyssey$trackClientCast(
            World world,
            PlayerEntity player,
            Hand hand,
            CallbackInfoReturnable<TypedActionResult<ItemStack>> callback
    ) {
        if (!world.isClient()) {
            return;
        }

        if (player.fishHook != null) {
            CastRodClientState.clear(player);
        } else {
            CastRodClientState.markCast(player, (AnglersRodItem) (Object) this);
        }
    }
}
