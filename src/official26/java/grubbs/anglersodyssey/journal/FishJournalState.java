package grubbs.anglersodyssey.journal;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.core.HolderLookup;
import com.mojang.serialization.Codec;

import java.util.UUID;

public class FishJournalState extends SavedData {

    private static final SavedDataType<FishJournalState> TYPE = new SavedDataType<>(
            Identifier.withDefaultNamespace("anglers_odyssey_fish_journal"),
            FishJournalState::new,
            CompoundTag.CODEC.xmap(FishJournalState::fromNbt, FishJournalState::toNbt),
            null
    );

    private final CompoundTag journals = new CompoundTag();

    public static FishJournalState get(ServerLevel world) {
        return world.getServer()
                .overworld()
                .getDataStorage()
                .computeIfAbsent(TYPE);
    }

    public static FishJournalState fromNbt(CompoundTag nbt) {
        FishJournalState state = new FishJournalState();

        if (nbt.contains("journals")) {
            state.journals.merge(nbt.getCompoundOrEmpty("journals"));
        }

        return state;
    }

    private CompoundTag toNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("journals", journals);
        return nbt;
    }

    public boolean registerCatch(UUID playerUuid, Identifier fishId, boolean shiny) {
        String playerKey = playerUuid.toString();
        String fishKey = fishId.toString();

        CompoundTag playerJournal = journals.getCompoundOrEmpty(playerKey);
        CompoundTag discovered = playerJournal.getCompoundOrEmpty("discovered");
        CompoundTag shinyDiscovered = playerJournal.getCompoundOrEmpty("shiny");
        CompoundTag catchCounts = playerJournal.getCompoundOrEmpty("catch_counts");

        playerJournal.putInt("total_catches", playerJournal.getInt("total_catches").orElse(0) + 1);
        catchCounts.putInt(fishKey, catchCounts.getInt(fishKey).orElse(0) + 1);

        boolean newSpecies = false;

        if (!discovered.getBoolean(fishKey).orElse(false)) {
            discovered.putBoolean(fishKey, true);
            newSpecies = true;
        }

        if (shiny && !shinyDiscovered.getBoolean(fishKey).orElse(false)) {
            shinyDiscovered.putBoolean(fishKey, true);
        }

        playerJournal.put("discovered", discovered);
        playerJournal.put("shiny", shinyDiscovered);
        playerJournal.put("catch_counts", catchCounts);
        journals.put(playerKey, playerJournal);
        setDirty();

        return newSpecies;
    }

    public int incrementTrophyCatches(UUID playerUuid) {
        String playerKey = playerUuid.toString();

        CompoundTag playerJournal = journals.getCompoundOrEmpty(playerKey);

        int trophies = playerJournal.getInt("trophy_catches").orElse(0) + 1;
        playerJournal.putInt("trophy_catches", trophies);

        journals.put(playerKey, playerJournal);
        setDirty();

        return trophies;
    }

    public int getTrophyCatches(UUID playerUuid) {
        CompoundTag playerJournal = journals.getCompoundOrEmpty(playerUuid.toString());
        return playerJournal.getInt("trophy_catches").orElse(0);
    }

    public int getDiscoveredCount(UUID playerUuid) {
        CompoundTag playerJournal = journals.getCompoundOrEmpty(playerUuid.toString());
        CompoundTag discovered = playerJournal.getCompoundOrEmpty("discovered");
        return discovered.keySet().size();
    }

    public int getShinyCount(UUID playerUuid) {
        CompoundTag playerJournal = journals.getCompoundOrEmpty(playerUuid.toString());
        CompoundTag shiny = playerJournal.getCompoundOrEmpty("shiny");
        return shiny.keySet().size();
    }

    public int getTotalCatches(UUID playerUuid) {
        CompoundTag playerJournal = journals.getCompoundOrEmpty(playerUuid.toString());
        return playerJournal.getInt("total_catches").orElse(0);
    }

    public int getRodTier(UUID playerUuid) {
        return journals.getCompoundOrEmpty(playerUuid.toString()).getInt("rod_tier").orElse(0);
    }

    public void setRodTier(UUID playerUuid, int tier) {
        String playerKey = playerUuid.toString();
        CompoundTag playerJournal = journals.getCompoundOrEmpty(playerKey);
        playerJournal.putInt("rod_tier", tier);
        journals.put(playerKey, playerJournal);
        setDirty();
    }

    public boolean hasDiscovered(UUID playerUuid, Identifier fishId) {
        CompoundTag playerJournal = journals.getCompoundOrEmpty(playerUuid.toString());
        CompoundTag discovered = playerJournal.getCompoundOrEmpty("discovered");
        return discovered.getBoolean(fishId.toString()).orElse(false);
    }

    public boolean hasShiny(UUID playerUuid, Identifier fishId) {
        CompoundTag playerJournal = journals.getCompoundOrEmpty(playerUuid.toString());
        CompoundTag shiny = playerJournal.getCompoundOrEmpty("shiny");
        return shiny.getBoolean(fishId.toString()).orElse(false);
    }
    public int getCatchCount(UUID playerUuid, Identifier fishId) {
    CompoundTag playerJournal = journals.getCompoundOrEmpty(playerUuid.toString());
    CompoundTag catchCounts = playerJournal.getCompoundOrEmpty("catch_counts");
    return catchCounts.getInt(fishId.toString()).orElse(0);
}
}
