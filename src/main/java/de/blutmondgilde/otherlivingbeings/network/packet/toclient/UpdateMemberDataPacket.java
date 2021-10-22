package de.blutmondgilde.otherlivingbeings.network.packet.toclient;

import de.blutmondgilde.otherlivingbeings.client.ClientGroupHolder;
import de.blutmondgilde.otherlivingbeings.data.group.GroupMemberData;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class UpdateMemberDataPacket {
    private final GroupMemberData memberData;

    public UpdateMemberDataPacket(FriendlyByteBuf buf) {
        memberData = GroupMemberData.deserialize(buf.readNbt());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(this.memberData.serializeNBT());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientGroupHolder.updateMemberData(this.memberData));
        ctx.get().setPacketHandled(true);
    }
}