package de.blutmondgilde.otherlivingbeings.network.packet.toserver;


import de.blutmondgilde.otherlivingbeings.data.group.GroupMemberData;
import de.blutmondgilde.otherlivingbeings.data.group.GroupProvider;
import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.UpdateMemberDataPacket;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RequestMemberDataPacket {
    private final UUID groupId;

    public RequestMemberDataPacket(FriendlyByteBuf buf) {
        this.groupId = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.groupId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;
            GroupProvider.getGroup(sender).ifPresent(data -> {
                if (!data.getPartyId().equals(this.groupId)) return;
                if (!data.isMember(sender)) return;
                List<ServerPlayer> members = data.getMembers()
                        .stream()
                        .map(uuid -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                members.forEach(player -> OtherLivingBeingNetwork.getInstance()
                        .send(PacketDistributor.PLAYER.with(() -> sender), new UpdateMemberDataPacket(new GroupMemberData(player.getUUID(), player.getDisplayName(), player.getHealth(), player.getMaxHealth()))));
            });
        });
        ctx.get().setPacketHandled(true);
    }
}