package de.blutmondgilde.otherlivingbeings.data.group;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.util.ChatMessageUtils;
import de.blutmondgilde.otherlivingbeings.util.TranslationUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class GroupInvite {
    @Getter
    private final UUID inviteId = UUID.randomUUID();
    @Getter
    private final UUID partyOwner, target, partyId;
    @Getter
    private long remainingTicks = 15 * 20;

    public GroupInvite(GroupData groupData, UUID target) {
        this.partyId = groupData.getPartyId();
        this.partyOwner = groupData.getPartyOwner();
        this.target = target;
    }

    public void tick() {
        remainingTicks--;
    }

    public void accept() {
        GroupProvider.groupInvites.remove(this);
        Optional<GroupData> group = GroupProvider.getGroup(partyOwner);
        if (group.isPresent()) {
            Optional<ServerPlayer> target = Optional.ofNullable(ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(getTarget()));
            if (target.isPresent()) {
                GroupProvider.addPlayerToGroup(target.get(), group.get());
                group.get().getMembers().forEach(uuid -> {
                    Optional<ServerPlayer> messageTarget = Optional.ofNullable(ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid));
                    MutableComponent joinMessage = new TextComponent("");
                    joinMessage.append(target.get().getDisplayName());
                    joinMessage.append(" ");
                    joinMessage.append(TranslationUtils.createGroupMessage("invite.success").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
                    messageTarget.ifPresent(player -> player.sendMessage(joinMessage, Util.NIL_UUID));
                });
            } else {
                Optional<ServerPlayer> groupOwner = Optional.ofNullable(ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(getPartyOwner()));
                groupOwner.ifPresent(player -> {
                    MutableComponent failureMessage = ChatMessageUtils.createGroupSystemMessage();
                    failureMessage.append(TranslationUtils.createGroupMessage("invite.failed.notarget").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                    player.sendMessage(failureMessage, Util.NIL_UUID);
                });
            }
        } else {
            OtherLivingBeings.getLogger().error("Exception while trying to add player {} to group {} with owner {}", getTarget(), getPartyId(), getPartyOwner());
        }
    }

    public void deny() {
        GroupProvider.groupInvites.remove(this);
        Optional<GroupData> group = GroupProvider.getGroup(partyOwner);
        if (group.isPresent()) {
            group.get().getMembers().forEach(uuid -> {
                Optional<ServerPlayer> owner = Optional.ofNullable(ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid));
                owner.ifPresent(player -> {
                    MutableComponent deniedMessage = ChatMessageUtils.createGroupSystemMessage();
                    deniedMessage.append(player.getDisplayName());
                    deniedMessage.append(" ");
                    deniedMessage.append(TranslationUtils.createGroupMessage("invite.denied").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                    player.sendMessage(deniedMessage, Util.NIL_UUID);
                });
            });
        } else {
            OtherLivingBeings.getLogger().error("Exception while trying to add player {} to group {} with owner {}", getTarget(), getPartyId(), getPartyOwner());
        }
    }
}
