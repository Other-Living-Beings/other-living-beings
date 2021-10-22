package de.blutmondgilde.otherlivingbeings.client;

import de.blutmondgilde.otherlivingbeings.data.group.GroupData;
import de.blutmondgilde.otherlivingbeings.data.group.GroupMemberData;
import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.network.packet.toserver.RequestMemberDataPacket;
import net.minecraft.Util;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class ClientGroupHolder {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static Optional<GroupData> currentGroup = Optional.empty();
    private static final HashMap<UUID, GroupMemberData> dataMap = new HashMap<>();


    public static void updateGroupData(@Nullable GroupData data) {
        currentGroup = Optional.ofNullable(data);
        if (currentGroup.isPresent()) {
            OtherLivingBeingNetwork.getInstance().send(PacketDistributor.SERVER.noArg(), new RequestMemberDataPacket(currentGroup.get().getPartyId()));
        } else {
            dataMap.clear();
        }
    }

    public static void reset() {
        currentGroup = Optional.empty();
        dataMap.clear();
    }

    public static boolean isInGroup() {
        return currentGroup.isPresent();
    }

    public static Optional<GroupData> getGroupData() {
        return currentGroup;
    }

    public static void updateMemberData(GroupMemberData data) {
        if (data.getPlayerUUID().equals(Util.NIL_UUID)) {
            reset();
        } else {
            dataMap.put(data.getPlayerUUID(), data);
        }
    }

    public static Collection<GroupMemberData> getMemberData() {
        return dataMap.values();
    }
}
