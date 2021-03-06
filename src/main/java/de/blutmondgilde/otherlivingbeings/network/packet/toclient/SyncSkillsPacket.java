package de.blutmondgilde.otherlivingbeings.network.packet.toclient;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.capability.skill.IPlayerSkills;
import lombok.AllArgsConstructor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

@AllArgsConstructor
public class SyncSkillsPacket {
    private final CompoundTag tag;
    private int targetId;

    public SyncSkillsPacket(IPlayerSkills playerSkills, Entity target) {
        this(playerSkills.serializeNBT(), target.getId());
    }

    public static void encode(SyncSkillsPacket packet, FriendlyByteBuf buffer) {
        buffer.writeNbt(packet.tag);
        buffer.writeInt(packet.targetId);
    }

    public static SyncSkillsPacket decode(FriendlyByteBuf buffer) {
        return new SyncSkillsPacket(buffer.readNbt(), buffer.readInt());
    }

    public static void handle(final SyncSkillsPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> OtherLivingBeings.getInstance().getProxy().syncPlayerSkills(packet.tag, packet.targetId));
        context.get().setPacketHandled(true);
    }
}
