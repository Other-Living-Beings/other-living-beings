package de.blutmondgilde.otherlivingbeings.data.group;

import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GroupMasterList {
    private final Map<UUID, GroupData> playerGroup = new HashMap<>();

    public void addPlayerToGroup(Player player, GroupData groupData) {
        if (!playerGroup.containsKey(player.getUUID())) {
            playerGroup.put(player.getUUID(), groupData);
        } else {
            //TODO enable duplicate prevention
            //throw new IllegalArgumentException("Cannot add Player to a group while he's already in another group.");
        }
        groupData.sync();
    }

    public void removePlayerFromGroup(Player player) {
        if (!playerGroup.containsKey(player.getUUID())) return;
        GroupData groupData = playerGroup.get(player.getUUID());

        //update player group data
        groupData.removeMember(player);
        if (groupData.isOwner(player)) {
            if (!groupData.getMembers().isEmpty()) {
                groupData.setPartyOwner(groupData.getMembers().get(0));
            }
        }

        //update member group date
        for (UUID memberId : groupData.getMembers()) {
            playerGroup.put(memberId, groupData);
        }

        //update clients
        groupData.sync();
    }

    public GroupData getPlayerGroup(Player player) {
        return playerGroup.get(player.getUUID());
    }

    public GroupData getPlayerGroup(UUID player) {
        return playerGroup.get(player);
    }

    public void createGroup(GroupData data) {
        playerGroup.put(data.getPartyOwner(), data);
        data.sync();
    }
}
