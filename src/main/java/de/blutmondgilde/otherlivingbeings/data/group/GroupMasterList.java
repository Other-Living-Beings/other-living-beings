package de.blutmondgilde.otherlivingbeings.data.group;

import de.blutmondgilde.otherlivingbeings.util.ChatMessageUtils;
import de.blutmondgilde.otherlivingbeings.util.TranslationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
            Optional.ofNullable(ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(memberId)).ifPresent(player1 -> {
                MutableComponent leftMessage = ChatMessageUtils.createGroupSystemMessage();
                leftMessage.append(player.getDisplayName());
                leftMessage.append(" ");
                leftMessage.append(new TranslatableComponent("leave").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                player1.sendMessage(leftMessage, Util.NIL_UUID);
            });
        }

        MutableComponent leftMessage = ChatMessageUtils.createGroupSystemMessage();
        leftMessage.append(player.getDisplayName());
        leftMessage.append(" ");
        leftMessage.append(TranslationUtils.createGroupMessage("leave").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
        player.sendMessage(leftMessage, Util.NIL_UUID);

        //update clients
        playerGroup.remove(player.getUUID());
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
