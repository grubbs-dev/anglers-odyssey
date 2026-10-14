package grubbs.anglersodyssey.journal;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.registry.RegistryWrapper;
import com.mojang.serialization.Codec;

import java.util.UUID;

public class FishJournalState extends PersistentState {

    private static final PersistentStateType<FishJournalState> TYPE = new PersistentStateType<>(
            "anglers_odyssey_fish_journal",
            FishJournalState::new,
            NbtCompound.CODEC.xmap(FishJournalState::fromNbt, FishJournalState::toNbt),
            null
    );

    private final NbtCompound journals = new NbtCompound();

    public static FishJournalState get(ServerWorld world) {
        return world.getServer()
                .getOverworld()
                .getPersistentStateManager()
                .getOrCreate(TYPE);
    }

    public static FishJournalState fromNbt(NbtCompound nbt) {
        FishJournalState state = new FishJournalState();

        if (nbt.contains("journals")) {
            state.journals.copyFrom(nbt.getCompoundOrEmpty("journals"));
        }

        return state;
    }

    private NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.put("journals", journals);
        return nbt;
    }

    public boolean registerCatch(UUID playerUuid, Identifier fishId, boolean shiny) {
        String playerKey = playerUuid.toString();
        String fishKey = fishId.toString();

        NbtCompound playerJournal = journals.getCompoundOrEmpty(playerKey);
        NbtCompound discovered = playerJournal.getCompoundOrEmpty("discovered");
        NbtCompound shinyDiscovered = playerJournal.getCompoundOrEmpty("shiny");
        NbtCompound catchCounts = playerJournal.getCompoundOrEmpty("catch_counts");

        playerJournal.putInt("total_catches", playerJournal.getInt("total_catches", 0) + 1);
        catchCounts.putInt(fishKey, catchCounts.getInt(fishKey, 0) + 1);

        boolean newSpecies = false;

        if (!discovered.getBoolean(fishKey, false)) {
            discovered.putBoolean(fishKey, true);
            newSpecies = true;
        }

        if (shiny && !shinyDiscovered.getBoolean(fishKey, false)) {
            shinyDiscovered.putBoolean(fishKey, true);
        }

        playerJournal.put("discovered", discovered);
        playerJournal.put("shiny", shinyDiscovered);
        playerJournal.put("catch_counts", catchCounts);
        journals.put(playerKey, playerJournal);
        markDirty();

        return newSpecies;
    }

    public int incrementTrophyCatches(UUID playerUuid) {
        String playerKey = playerUuid.toString();

        NbtCompound playerJournal = journals.getCompoundOrEmpty(playerKey);

        int trophies = playerJournal.getInt("trophy_catches", 0) + 1;
        playerJournal.putInt("trophy_catches", trophies);

        journals.put(playerKey, playerJournal);
        markDirty();

        return trophies;
    }

    public int getTrophyCatches(UUID playerUuid) {
        NbtCompound playerJournal = journals.getCompoundOrEmpty(playerUuid.toString());
        return playerJournal.getInt("trophy_catches", 0);
    }

    public int getDiscoveredCount(UUID playerUuid) {
        NbtCompound playerJournal = journals.getCompoundOrEmpty(playerUuid.toString());
        NbtCompound discovered = playerJournal.getCompoundOrEmpty("discovered");
        return discovered.getKeys().size();
    }

    public int getShinyCount(UUID playerUuid) {
        NbtCompound playerJournal = journals.getCompoundOrEmpty(playerUuid.toString());
        NbtCompound shiny = playerJournal.getCompoundOrEmpty("shiny");
        return shiny.getKeys().size();
    }

    public int getTotalCatches(UUID playerUuid) {
        NbtCompound playerJournal = journals.getCompoundOrEmpty(playerUuid.toString());
        return playerJournal.getInt("total_catches", 0);
    }

    public int getRodTier(UUID playerUuid) {
        return journals.getCompoundOrEmpty(playerUuid.toString()).getInt("rod_tier", 0);
    }

    public void setRodTier(UUID playerUuid, int tier) {
        String playerKey = playerUuid.toString();
        NbtCompound playerJournal = journals.getCompoundOrEmpty(playerKey);
        playerJournal.putInt("rod_tier", tier);
        journals.put(playerKey, playerJournal);
        markDirty();
    }

    public boolean hasDiscovered(UUID playerUuid, Identifier fishId) {
        NbtCompound playerJournal = journals.getCompoundOrEmpty(playerUuid.toString());
        NbtCompound discovered = playerJournal.getCompoundOrEmpty("discovered");
        return discovered.getBoolean(fishId.toString(), false);
    }

    public boolean hasShiny(UUID playerUuid, Identifier fishId) {
        NbtCompound playerJournal = journals.getCompoundOrEmpty(playerUuid.toString());
        NbtCompound shiny = playerJournal.getCompoundOrEmpty("shiny");
        return shiny.getBoolean(fishId.toString(), false);
    }
    public int getCatchCount(UUID playerUuid, Identifier fishId) {
    NbtCompound playerJournal = journals.getCompoundOrEmpty(playerUuid.toString());
    NbtCompound catchCounts = playerJournal.getCompoundOrEmpty("catch_counts");
    return catchCounts.getInt(fishId.toString(), 0);
}
}
