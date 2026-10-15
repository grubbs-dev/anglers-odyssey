package grubbs.anglersodyssey.fish;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;

import java.util.UUID;

public class FishRecordsState extends PersistentState {

    private final NbtCompound records = new NbtCompound();

    public static FishRecordsState get(ServerWorld world) {
        return world.getServer()
                .getOverworld()
                .getPersistentStateManager()
                .getOrCreate(FishRecordsState::fromNbt, FishRecordsState::new,
                        "anglers_odyssey_fish_records");
    }

    public static FishRecordsState fromNbt(NbtCompound nbt) {
        FishRecordsState state = new FishRecordsState();

        if (nbt.contains("records")) {
            state.records.copyFrom(nbt.getCompound("records"));
        }

        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.put("records", records);
        return nbt;
    }

    public boolean updateRecord(UUID playerUuid, Identifier fishId, double weight, int length) {
        String playerKey = playerUuid.toString();
        String fishKey = fishId.toString();

        NbtCompound playerRecords = records.getCompound(playerKey);
        NbtCompound fishRecord = playerRecords.getCompound(fishKey);

        double oldWeight = fishRecord.getDouble("weight");

        if (!fishRecord.contains("weight") || weight > oldWeight) {
            fishRecord.putDouble("weight", weight);
            fishRecord.putInt("length", length);

            playerRecords.put(fishKey, fishRecord);
            records.put(playerKey, playerRecords);

            markDirty();
            return true;
        }

        return false;
    }
    public double getRecordWeight(UUID playerUuid, Identifier fishId) {
    NbtCompound playerRecords = records.getCompound(playerUuid.toString());
    NbtCompound fishRecord = playerRecords.getCompound(fishId.toString());
    return fishRecord.getDouble("weight");
}

public int getRecordLength(UUID playerUuid, Identifier fishId) {
    NbtCompound playerRecords = records.getCompound(playerUuid.toString());
    NbtCompound fishRecord = playerRecords.getCompound(fishId.toString());
    return fishRecord.getInt("length");
}
}
