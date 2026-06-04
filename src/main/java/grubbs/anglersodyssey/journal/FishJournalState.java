package grubbs.anglersodyssey.journal;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;

import java.util.UUID;

public class FishJournalState extends PersistentState {

    private final NbtCompound journals = new NbtCompound();

    public static FishJournalState get(ServerWorld world) {
        return world.getServer()
                .getOverworld()
                .getPersistentStateManager()
                .getOrCreate(FishJournalState::fromNbt, FishJournalState::new, "anglers_odyssey_fish_journal");
    }

    public static FishJournalState fromNbt(NbtCompound nbt) {
        FishJournalState state = new FishJournalState();

        if (nbt.contains("journals")) {
            state.journals.copyFrom(nbt.getCompound("journals"));
        }

        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.put("journals", journals);
        return nbt;
    }

    public boolean registerCatch(UUID playerUuid, Identifier fishId, boolean shiny) {
        String playerKey = playerUuid.toString();
        String fishKey = fishId.toString();

        NbtCompound playerJournal = journals.getCompound(playerKey);
        NbtCompound discovered = playerJournal.getCompound("discovered");
        NbtCompound shinyDiscovered = playerJournal.getCompound("shiny");

        playerJournal.putInt("total_catches", playerJournal.getInt("total_catches") + 1);

        boolean newSpecies = false;

        if (!discovered.getBoolean(fishKey)) {
            discovered.putBoolean(fishKey, true);
            newSpecies = true;
        }

        if (shiny && !shinyDiscovered.getBoolean(fishKey)) {
            shinyDiscovered.putBoolean(fishKey, true);
        }

        playerJournal.put("discovered", discovered);
        playerJournal.put("shiny", shinyDiscovered);
        journals.put(playerKey, playerJournal);
        markDirty();

        return newSpecies;
    }

    public int incrementTrophyCatches(UUID playerUuid) {
        String playerKey = playerUuid.toString();

        NbtCompound playerJournal = journals.getCompound(playerKey);

        int trophies = playerJournal.getInt("trophy_catches") + 1;
        playerJournal.putInt("trophy_catches", trophies);

        journals.put(playerKey, playerJournal);
        markDirty();

        return trophies;
    }

    public int getTrophyCatches(UUID playerUuid) {
        NbtCompound playerJournal = journals.getCompound(playerUuid.toString());
        return playerJournal.getInt("trophy_catches");
    }

    public int getDiscoveredCount(UUID playerUuid) {
        NbtCompound playerJournal = journals.getCompound(playerUuid.toString());
        NbtCompound discovered = playerJournal.getCompound("discovered");
        return discovered.getKeys().size();
    }

    public int getShinyCount(UUID playerUuid) {
        NbtCompound playerJournal = journals.getCompound(playerUuid.toString());
        NbtCompound shiny = playerJournal.getCompound("shiny");
        return shiny.getKeys().size();
    }

    public int getTotalCatches(UUID playerUuid) {
        NbtCompound playerJournal = journals.getCompound(playerUuid.toString());
        return playerJournal.getInt("total_catches");
    }

    public boolean hasDiscovered(UUID playerUuid, Identifier fishId) {
        NbtCompound playerJournal = journals.getCompound(playerUuid.toString());
        NbtCompound discovered = playerJournal.getCompound("discovered");
        return discovered.getBoolean(fishId.toString());
    }

    public boolean hasShiny(UUID playerUuid, Identifier fishId) {
        NbtCompound playerJournal = journals.getCompound(playerUuid.toString());
        NbtCompound shiny = playerJournal.getCompound("shiny");
        return shiny.getBoolean(fishId.toString());
    }
}