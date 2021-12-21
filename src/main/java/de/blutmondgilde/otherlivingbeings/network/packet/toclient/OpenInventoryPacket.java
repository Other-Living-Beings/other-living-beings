package de.blutmondgilde.otherlivingbeings.network.packet.toclient;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenInventoryPacket {

    public OpenInventoryPacket() {}

    public OpenInventoryPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> OtherLivingBeings.getInstance().getProxy().openPlayerInventory());
        ctx.get().setPacketHandled(true);
    }
}