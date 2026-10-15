package grubbs.anglersodyssey.fish;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.World;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.Registry;

import java.util.List;

/** A vanilla fishing rod with Angler's Odyssey progression details. */
public class AnglersRodItem extends FishingRodItem {

    private final int tier;
    private final int lureLevel;
    private final int luckLevel;
    private final int unlockCount;
    private final String specialAbility;

    public AnglersRodItem(Settings settings, int tier, int lureLevel, int luckLevel, int unlockCount, String specialAbility) {
        super(settings);
        this.tier = tier;
        this.lureLevel = lureLevel;
        this.luckLevel = luckLevel;
        this.unlockCount = unlockCount;
        this.specialAbility = specialAbility;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (user.fishHook != null) {
            if (!world.isClient()) {
                int damage = user.fishHook.use(stack);
                stack.damage(damage, user, hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE,
                    SoundCategory.NEUTRAL, 1.0F, 0.8F + world.getRandom().nextFloat() * 0.4F);
            user.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW,
                    SoundCategory.NEUTRAL, 0.5F, 0.8F + world.getRandom().nextFloat() * 0.4F);

            if (!world.isClient()) {
                ItemEnchantmentsComponent enchantments = stack.get(DataComponentTypes.ENCHANTMENTS);
                Registry<Enchantment> enchantmentRegistry = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
                RegistryEntry<Enchantment> luckEntry = enchantmentRegistry.getEntry(Enchantments.LUCK_OF_THE_SEA).orElseThrow();
                RegistryEntry<Enchantment> lureEntry = enchantmentRegistry.getEntry(Enchantments.LURE).orElseThrow();
                int effectiveLuck = luckLevel + (enchantments == null ? 0
                        : enchantments.getLevel(luckEntry));
                int effectiveLure = lureLevel + (enchantments == null ? 0
                        : enchantments.getLevel(lureEntry));
                FishingBobberEntity bobber = new FishingBobberEntity(user, world, effectiveLuck, effectiveLure);
                ((AnglersFishingBobberAccess) bobber).anglers_odyssey$setCastRod(this);
                world.spawnEntity(bobber);
            }

            user.incrementStat(Stats.USED.getOrCreateStat(this));
            user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public int getEnchantability() {
        return 1;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Tier " + tier + " Fishing Rod").formatted(Formatting.AQUA));
        tooltip.add(Text.literal("Durability: " + (stack.getMaxDamage() - stack.getDamage()) + " / " + stack.getMaxDamage())
                .formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Bite Speed Bonus: +" + lureLevel)
                .formatted(Formatting.GREEN));
        tooltip.add(Text.literal("Treasure Bonus: +" + luckLevel)
                .formatted(Formatting.GOLD));
        tooltip.add(Text.literal("Special: " + specialAbility).formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.literal("Unlocks at " + unlockCount + " discovered fish")
                .formatted(Formatting.DARK_AQUA));
        super.appendTooltip(stack, context, tooltip, type);
    }

}
