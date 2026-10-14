package grubbs.anglersodyssey.fish;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.registry.RegistryWrapper;

import java.util.UUID;

public class FishRecordsState extends PersistentState {

    private static final PersistentStateType<FishRecordsState> TYPE = new PersistentStateType<>(
            "anglers_odyssey_fish_records",
            FishRecordsState::new,
            NbtCompound.CODEC.xmap(FishRecordsState::fromNbt, FishRecordsState::toNbt),
            null
    );

    private final NbtCompound records = new NbtCompound();

    public static FishRecordsState get(ServerWorld world) {
        return world.getServer()
                .getOverworld()
                .getPersistentStateManager()
                .getOrCreate(TYPE);
    }

    public static FishRecordsState fromNbt(NbtCompound nbt) {
        FishRecordsState state = new FishRecordsState();

        if (nbt.contains("records")) {
            state.records.copyFrom(nbt.getCompoundOrEmpty("records"));
        }

        return state;
    }

    private NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.put("records", records);
        return nbt;
    }

    public boolean updateRecord(UUID playerUuid, Identifier fishId, double weight, int length) {
        String playerKey = playerUuid.toString();
        String fishKey = fishId.toString();

        NbtCompound playerRecords = records.getCompoundOrEmpty(playerKey);
        NbtCompound fishRecord = playerRecords.getCompoundOrEmpty(fishKey);

        double oldWeight = fishRecord.getDouble("weight", 0.0D);

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
    NbtCompound playerRecords = records.getCompoundOrEmpty(playerUuid.toString());
    NbtCompound fishRecord = playerRecords.getCompoundOrEmpty(fishId.toString());
    return fishRecord.getDouble("weight", 0.0D);
}

public int getRecordLength(UUID playerUuid, Identifier fishId) {
    NbtCompound playerRecords = records.getCompoundOrEmpty(playerUuid.toString());
    NbtCompound fishRecord = playerRecords.getCompoundOrEmpty(fishId.toString());
    return fishRecord.getInt("length", 0);
}
}
