package de.blutmondgilde.otherlivingbeings.capability.block;

import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncFurnacePlacerCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.util.UUID;

public interface IFurnacePlacer extends ICapabilitySerializable<CompoundTag> {
    void setOwner(UUID uuid);

    UUID getOwner();

    default void sync(LevelChunk chunk, BlockPos pos) {
        CompoundTag tag = serializeNBT();
        tag.putInt("x",pos.getX());
        tag.putInt("y",pos.getY());
        tag.putInt("z",pos.getZ());

        OtherLivingBeingNetwork.getInstance().send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new SyncFurnacePlacerCapability(tag));
    }
}
