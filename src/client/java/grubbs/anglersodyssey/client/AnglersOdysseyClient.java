package grubbs.anglersodyssey.client;

import grubbs.anglersodyssey.journal.FishJournalClient;
import grubbs.anglersodyssey.fish.RodRegistry;
import grubbs.anglersodyssey.fish.AnglersFishingBobberAccess;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.fabricmc.api.ClientModInitializer;

public class AnglersOdysseyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FishJournalClient.registerClientPackets();
        registerRodCastPredicates();
    }

    private static void registerRodCastPredicates() {
        Identifier cast = new Identifier("minecraft", "cast");
        registerRodCastPredicate(RodRegistry.REED_ROD, cast);
        registerRodCastPredicate(RodRegistry.OAK_ROD, cast);
        registerRodCastPredicate(RodRegistry.IRONWOOD_ROD, cast);
        registerRodCastPredicate(RodRegistry.ANGLERS_ROD, cast);
    }

    private static void registerRodCastPredicate(Item rod, Identifier cast) {
        ModelPredicateProviderRegistry.register(rod, cast,
                (stack, world, entity, seed) -> entity instanceof PlayerEntity player
                        && player.fishHook instanceof AnglersFishingBobberAccess bobber
                        && bobber.anglers_odyssey$getCastRod() == stack.getItem()
                        && (player.getMainHandStack().getItem() == stack.getItem()
                        || player.getOffHandStack().getItem() == stack.getItem())
                        ? 1.0F : 0.0F);
    }
}
