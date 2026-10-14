package grubbs.anglersodyssey.fish;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Consumer;

/** A vanilla fishing rod with Angler's Odyssey progression details. */
public class AnglersRodItem extends FishingRodItem {

    private final int tier;
    private final int lureLevel;
    private final int luckLevel;
    private final int unlockCount;
    private final String specialAbility;

    public AnglersRodItem(Properties properties, int tier, int lureLevel, int luckLevel, int unlockCount, String specialAbility) {
        super(properties);
        this.tier = tier;
        this.lureLevel = lureLevel;
        this.luckLevel = luckLevel;
        this.unlockCount = unlockCount;
        this.specialAbility = specialAbility;
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        if (user.fishing != null) {
            stack.remove(DataComponents.ITEM_MODEL);
            if (!world.isClientSide()) {
                int damage = user.fishing.retrieve(stack);
                stack.hurtAndBreak(damage, user, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE,
                    SoundSource.NEUTRAL, 1.0F, 0.8F + world.getRandom().nextFloat() * 0.4F);
            user.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            stack.set(DataComponents.ITEM_MODEL, Identifier.fromNamespaceAndPath("anglers_odyssey", "item/" + BuiltInRegistries.ITEM.getKey(this).getPath() + "_cast"));
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.FISHING_BOBBER_THROW,
                    SoundSource.NEUTRAL, 0.5F, 0.8F + world.getRandom().nextFloat() * 0.4F);

            if (!world.isClientSide()) {
                FishingHook bobber = new FishingHook(user, world, luckLevel, lureLevel);
                ((AnglersFishingBobberAccess) bobber).anglers_odyssey$setCastRod(this);
                world.addFreshEntity(bobber);
            }

            user.awardStat(Stats.ITEM_USED.get(this));
            user.gameEvent(GameEvent.ITEM_INTERACT_START);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag type) {
        tooltip.accept(Component.literal("Tier " + tier + " Fishing Rod").withStyle(ChatFormatting.AQUA));
        tooltip.accept(Component.literal("Durability: " + (stack.getMaxDamage() - stack.getDamageValue()) + " / " + stack.getMaxDamage())
                .withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.literal("Bite Speed Bonus: +" + lureLevel)
                .withStyle(ChatFormatting.GREEN));
        tooltip.accept(Component.literal("Treasure Bonus: +" + luckLevel)
                .withStyle(ChatFormatting.GOLD));
        tooltip.accept(Component.literal("Special: " + specialAbility).withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.accept(Component.literal("Unlocks at " + unlockCount + " discovered fish")
                .withStyle(ChatFormatting.DARK_AQUA));
        super.appendHoverText(stack, context, display, tooltip, type);
    }

}
