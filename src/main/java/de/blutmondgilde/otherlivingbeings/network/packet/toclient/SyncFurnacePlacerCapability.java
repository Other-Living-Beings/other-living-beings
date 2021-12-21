package de.blutmondgilde.otherlivingbeings.network.packet.toclient;


import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import lombok.AllArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

@AllArgsConstructor
public class SyncFurnacePlacerCapability {
    private final CompoundTag serializedCap;

    public SyncFurnacePlacerCapability(FriendlyByteBuf buf) {
        this.serializedCap = buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(this.serializedCap);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            BlockPos pos = new BlockPos(serializedCap.getInt("x"), serializedCap.getInt("y"), serializedCap.getInt("z"));
            OtherLivingBeings.getInstance().getProxy().syncFurnaceOwner(pos, serializedCap);
        });
        ctx.get().setPacketHandled(true);
    }
}