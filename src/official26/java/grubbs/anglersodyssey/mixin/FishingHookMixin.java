package grubbs.anglersodyssey.mixin;

import grubbs.anglersodyssey.AnglersOdyssey;
import grubbs.anglersodyssey.advancement.AnglersAdvancementHelper;
import grubbs.anglersodyssey.fish.FishData;
import grubbs.anglersodyssey.fish.FishRegistry;
import grubbs.anglersodyssey.fish.RegionalFishSelector;
import grubbs.anglersodyssey.fish.AnglersRodItem;
import grubbs.anglersodyssey.fish.AnglersFishingBobberAccess;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin implements AnglersFishingBobberAccess {

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

    @Inject(method = "shouldStopFishing", at = @At("HEAD"), cancellable = true)
    private void anglers_odyssey$acceptCustomRods(
            net.minecraft.world.entity.player.Player player,
            CallbackInfoReturnable<Boolean> cir
    ) {
        boolean holdingCustomRod = player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof AnglersRodItem
                || player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof AnglersRodItem;
        Item castRod = anglers_odyssey$getCastRod();

        if (!holdingCustomRod && castRod == null) {
            return;
        }

        FishingHook bobber = (FishingHook) (Object) this;
        boolean stillHoldingCastRod = castRod != null
                && (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == castRod
                || player.getItemInHand(InteractionHand.OFF_HAND).getItem() == castRod);
        boolean invalid = player.isRemoved()
                || !player.isAlive()
                || bobber.distanceToSqr(player) > 1024.0D
                || !stillHoldingCastRod;

        if (invalid) {
            bobber.discard();
        }

        cir.setReturnValue(invalid);
    }

    @Redirect(
            method = "retrieve",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;"
            )
    )
    private ObjectArrayList<ItemStack> anglers_odyssey$replaceWithRegionalFish(
            LootTable lootTable,
            LootParams parameters
    ) {
        ObjectArrayList<ItemStack> originalLoot = lootTable.getRandomItems(parameters);
        ObjectArrayList<ItemStack> replacedLoot = new ObjectArrayList<>();

        FishingHook bobber = (FishingHook) (Object) this;

        for (ItemStack originalStack : originalLoot) {
            FishData originalFishData = getFishData(originalStack);

            if (originalFishData == null) {
                replacedLoot.add(originalStack);
                continue;
            }

            FishData regionalFishData = RegionalFishSelector.getRandomFishForBiome(
                    bobber.level(),
                    bobber.blockPosition(),
                    originalFishData.rarity(),
                    bobber.level().getRandom()
            );

            if (regionalFishData != null && bobber.getPlayerOwner() instanceof ServerPlayer serverPlayer) {
                AnglersAdvancementHelper.grant(serverPlayer, "every_cast_begins_an_adventure");
            }

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

            CustomData customData = originalStack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                regionalStack.set(DataComponents.CUSTOM_DATA, CustomData.of(customData.copyTag()));
            }

            replacedLoot.add(regionalStack);
        }

        return replacedLoot;
    }

    private FishData getFishData(ItemStack stack) {
        Identifier itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());

        if (!itemId.getNamespace().equals(AnglersOdyssey.MOD_ID)) {
            return null;
        }

        return FishRegistry.FISH_DATA.get(itemId.getPath());
    }
}
