package grubbs.anglersodyssey.fish;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.core.HolderLookup;

import java.util.UUID;

public class FishRecordsState extends SavedData {

    private static final SavedDataType<FishRecordsState> TYPE = new SavedDataType<>(
            Identifier.withDefaultNamespace("anglers_odyssey_fish_records"),
            FishRecordsState::new,
            CompoundTag.CODEC.xmap(FishRecordsState::fromNbt, FishRecordsState::toNbt),
            null
    );

    private final CompoundTag records = new CompoundTag();

    public static FishRecordsState get(ServerLevel world) {
        return world.getServer()
                .overworld()
                .getDataStorage()
                .computeIfAbsent(TYPE);
    }

    public static FishRecordsState fromNbt(CompoundTag nbt) {
        FishRecordsState state = new FishRecordsState();

        if (nbt.contains("records")) {
            state.records.merge(nbt.getCompoundOrEmpty("records"));
        }

        return state;
    }

    private CompoundTag toNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("records", records);
        return nbt;
    }

    public boolean updateRecord(UUID playerUuid, Identifier fishId, double weight, int length) {
        String playerKey = playerUuid.toString();
        String fishKey = fishId.toString();

        CompoundTag playerRecords = records.getCompoundOrEmpty(playerKey);
        CompoundTag fishRecord = playerRecords.getCompoundOrEmpty(fishKey);

        double oldWeight = fishRecord.getDouble("weight").orElse(0.0D);

        if (!fishRecord.contains("weight") || weight > oldWeight) {
            fishRecord.putDouble("weight", weight);
            fishRecord.putInt("length", length);

            playerRecords.put(fishKey, fishRecord);
            records.put(playerKey, playerRecords);

            setDirty();
            return true;
        }

        return false;
    }
    public double getRecordWeight(UUID playerUuid, Identifier fishId) {
    CompoundTag playerRecords = records.getCompoundOrEmpty(playerUuid.toString());
    CompoundTag fishRecord = playerRecords.getCompoundOrEmpty(fishId.toString());
    return fishRecord.getDouble("weight").orElse(0.0D);
}

public int getRecordLength(UUID playerUuid, Identifier fishId) {
    CompoundTag playerRecords = records.getCompoundOrEmpty(playerUuid.toString());
    CompoundTag fishRecord = playerRecords.getCompoundOrEmpty(fishId.toString());
    return fishRecord.getInt("length").orElse(0);
}
}
