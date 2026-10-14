package grubbs.anglersodyssey.mixin;

import grubbs.anglersodyssey.AnglersOdyssey;
import grubbs.anglersodyssey.fish.FishData;
import grubbs.anglersodyssey.fish.FishRegistry;
import grubbs.anglersodyssey.fish.RegionalFishSelector;
import grubbs.anglersodyssey.fish.AnglersRodItem;
import grubbs.anglersodyssey.fish.AnglersFishingBobberAccess;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin implements AnglersFishingBobberAccess {

    @org.spongepowered.asm.mixin.Unique
    private Item anglers_odyssey$castRod;

    @Override
    public void anglers_odyssey$setCastRod(Item rod) {
        this.anglers_odyssey$castRod = rod;
    }

    @Override
    public Item anglers_odyssey$getCastRod() {
        return anglers_odyssey$castRod;
    }

    @Inject(method = "removeIfInvalid", at = @At("HEAD"), cancellable = true)
    private void anglers_odyssey$acceptCustomRods(
            net.minecraft.entity.player.PlayerEntity player,
            CallbackInfoReturnable<Boolean> cir
    ) {
        boolean holdingCustomRod = player.getMainHandStack().getItem() instanceof AnglersRodItem
                || player.getOffHandStack().getItem() instanceof AnglersRodItem;
        Item castRod = anglers_odyssey$getCastRod();

        if (!holdingCustomRod && castRod == null) {
            return;
        }

        FishingBobberEntity bobber = (FishingBobberEntity) (Object) this;
        boolean stillHoldingCastRod = castRod != null
                && (player.getMainHandStack().getItem() == castRod
                || player.getOffHandStack().getItem() == castRod);
        boolean invalid = player.isRemoved()
                || !player.isAlive()
                || bobber.squaredDistanceTo(player) > 1024.0D
                || !stillHoldingCastRod;

        if (invalid) {
            bobber.discard();
        }

        cir.setReturnValue(invalid);
    }

    @Redirect(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/loot/LootTable;generateLoot(Lnet/minecraft/loot/context/LootContextParameterSet;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;",
                    remap = false
            )
    )
    private ObjectArrayList<ItemStack> anglers_odyssey$replaceWithRegionalFish(
            LootTable lootTable,
            LootContextParameterSet parameters
    ) {
        ObjectArrayList<ItemStack> originalLoot = lootTable.generateLoot(parameters);
        ObjectArrayList<ItemStack> replacedLoot = new ObjectArrayList<>();

        FishingBobberEntity bobber = (FishingBobberEntity) (Object) this;

        for (ItemStack originalStack : originalLoot) {
            FishData originalFishData = getFishData(originalStack);

            if (originalFishData == null) {
                replacedLoot.add(originalStack);
                continue;
            }

            FishData regionalFishData = RegionalFishSelector.getRandomFishForBiome(
                    bobber.getWorld(),
                    bobber.getBlockPos(),
                    originalFishData.rarity(),
                    bobber.getWorld().getRandom()
            );

            if (regionalFishData == null) {
                replacedLoot.add(new ItemStack(Items.COD, originalStack.getCount()));
                continue;
            }

            Item regionalItem = FishRegistry.FISH_ITEMS.get(regionalFishData.id());

            if (regionalItem == null) {
                AnglersOdyssey.LOGGER.warn(
                        "Missing registered fish item for regional fish: {}",
                        regionalFishData.id()
                );

                replacedLoot.add(originalStack);
                continue;
            }

            ItemStack regionalStack = new ItemStack(regionalItem, originalStack.getCount());

            if (originalStack.hasNbt() && originalStack.getNbt() != null) {
                regionalStack.setNbt(originalStack.getNbt().copy());
            }

            replacedLoot.add(regionalStack);
        }

        return replacedLoot;
    }

    private FishData getFishData(ItemStack stack) {
        Identifier itemId = Registries.ITEM.getId(stack.getItem());

        if (!itemId.getNamespace().equals(AnglersOdyssey.MOD_ID)) {
            return null;
        }

        return FishRegistry.FISH_DATA.get(itemId.getPath());
    }
}
