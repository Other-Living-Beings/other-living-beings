package de.blutmondgilde.otherlivingbeings.network.packet.toclient;

import de.blutmondgilde.otherlivingbeings.CommonProxy;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.data.group.GroupData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SyncGroupDataPacket {
    @Nullable
    private final GroupData data;

    public SyncGroupDataPacket(@Nullable GroupData data) {
        this.data = data;
    }

    public SyncGroupDataPacket(FriendlyByteBuf buf) {
        this.data = new GroupData();
        CompoundTag tag = buf.readNbt();
        if (tag != null) {
            this.data.deserializeNBT(tag);
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        if (this.data != null) {
            buf.writeNbt(this.data.serializeNBT());
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            CommonProxy proxy = OtherLivingBeings.getInstance().getProxy();
            if (this.data == null) {
                proxy.resetGroupInformation();
            } else {
                proxy.updateGroupInformation(data);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}