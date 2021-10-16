package de.blutmondgilde.otherlivingbeings.network.packet.toserver;

import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.OpenInventoryPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.util.function.Supplier;

public class RequestInventoryOpening {
    public RequestInventoryOpening() {}

    public RequestInventoryOpening(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            OtherLivingBeingNetwork.getInstance().send(PacketDistributor.PLAYER.with(() -> player), new OpenInventoryPacket());
        });
        ctx.get().setPacketHandled(true);
    }
}