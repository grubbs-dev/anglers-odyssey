package grubbs.anglersodyssey.client;

import grubbs.anglersodyssey.journal.FishJournalClient;
import grubbs.anglersodyssey.fish.RodRegistry;
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
        Identifier cast = Identifier.of("minecraft", "cast");
        registerRodCastPredicate(RodRegistry.REED_ROD, cast);
        registerRodCastPredicate(RodRegistry.OAK_ROD, cast);
        registerRodCastPredicate(RodRegistry.IRONWOOD_ROD, cast);
        registerRodCastPredicate(RodRegistry.ANGLERS_ROD, cast);
    }

    private static void registerRodCastPredicate(Item rod, Identifier cast) {
        ModelPredicateProviderRegistry.register(rod, cast,
                (stack, world, entity, seed) -> entity instanceof PlayerEntity player
                        // The bobber's custom cast-rod field is server-side state and is not
                        // synchronized to the client. The active hook plus the held rod is
                        // the reliable client-side equivalent for the cast model predicate.
                        && (player.fishHook != null
                        || CastRodClientState.isCast(player, stack.getItem()))
                        && (player.getMainHandStack().getItem() == stack.getItem()
                        || player.getOffHandStack().getItem() == stack.getItem())
                        ? 1.0F : 0.0F);
    }
}
