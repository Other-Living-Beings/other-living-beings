package de.blutmondgilde.otherlivingbeings.capability.block;

import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FurnacePlayerImpl implements IFurnacePlacer {
    @Setter
    @Getter
    private UUID owner = Util.NIL_UUID;
    private final LazyOptional<IFurnacePlacer> holder = LazyOptional.of(() -> this);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return OtherLivingBeingsCapability.FURNACE_PLACER.orEmpty(cap, holder);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("owner", this.owner);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.owner = nbt.getUUID("owner");
    }

    public static boolean canAttachTo(ICapabilityProvider provider) {
        if (!(provider instanceof AbstractFurnaceBlockEntity block)) return false;
        return !block.getCapability(OtherLivingBeingsCapability.FURNACE_PLACER).isPresent();
    }
}
