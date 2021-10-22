package de.blutmondgilde.otherlivingbeings.data.group;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncGroupDataPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@ToString
public class GroupData implements INBTSerializable<CompoundTag> {
    @Getter
    private UUID partyId;
    @Setter
    @Getter
    private UUID partyOwner;
    @Getter
    private List<UUID> members;

    public GroupData() {
        this.partyId = UUID.randomUUID();
        this.partyOwner = Util.NIL_UUID;
        this.members = new ArrayList<>();
    }

    public boolean isOwner(UUID uuid) {
        return partyOwner.equals(uuid);
    }

    public boolean isOwner(Player player) {
        return isOwner(player.getUUID());
    }

    public boolean isMember(UUID uuid) {
        return members.contains(uuid);
    }

    public boolean isMember(Player player) {
        return isMember(player.getUUID());
    }

    public void removeMember(Player player) {
        members.remove(player.getUUID());
    }

    public void addMember(Player player) {
        members.add(player.getUUID());
    }

    public void sync() {
        this.members.forEach(uuid -> {
            try {
                OtherLivingBeingNetwork.getInstance().send(PacketDistributor.PLAYER.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid)),
                        new SyncGroupDataPacket(this));
            } catch (NullPointerException ex) {
                OtherLivingBeings.getLogger().error("Exception while trying to sync group {} to player {}", this, uuid);
            }
        });
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("id", this.partyId);
        tag.putUUID("owner", this.partyOwner);

        CompoundTag member = new CompoundTag();
        for (int i = 0; i < this.members.size(); i++) {
            member.putUUID(String.valueOf(i), this.members.get(i));
        }
        tag.put("member", member);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.partyId = nbt.getUUID("id");
        this.partyOwner = nbt.getUUID("owner");
        this.members.clear();
        CompoundTag memberTag = nbt.getCompound("member");
        for (String key : memberTag.getAllKeys()) {
            this.members.add(memberTag.getUUID(key));
        }
    }
}
