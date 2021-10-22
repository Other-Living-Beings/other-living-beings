package de.blutmondgilde.otherlivingbeings.handler;

import de.blutmondgilde.otherlivingbeings.data.group.GroupData;
import de.blutmondgilde.otherlivingbeings.data.group.GroupMemberData;
import de.blutmondgilde.otherlivingbeings.data.group.GroupProvider;
import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.UpdateMemberDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

public class GroupHandler {
    public static void init(IEventBus forgeBus) {
        forgeBus.addListener(GroupHandler::onPlayerGetDamage);
        forgeBus.addListener(GroupHandler::onPlayerHeal);
        forgeBus.addListener(GroupHandler::onPlayerLogIn);
    }

    public static void onPlayerGetDamage(LivingDamageEvent e) {
        if (!(e.getEntityLiving() instanceof Player player)) return;
        GroupProvider.getGroup(player).ifPresent(data -> data.getMembers().forEach(uuid -> OtherLivingBeingNetwork.getInstance()
                .send(PacketDistributor.PLAYER.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid)),
                        new UpdateMemberDataPacket(new GroupMemberData(player.getUUID(), player.getDisplayName(), player.getHealth() - e.getAmount(), player.getMaxHealth())))));
    }

    public static void onPlayerHeal(LivingHealEvent e) {
        if (!(e.getEntityLiving() instanceof Player player)) return;
        GroupProvider.getGroup(player).ifPresent(data -> data.getMembers().forEach(uuid -> OtherLivingBeingNetwork.getInstance()
                .send(PacketDistributor.PLAYER.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid)),
                        new UpdateMemberDataPacket(new GroupMemberData(player.getUUID(), player.getDisplayName(), player.getHealth() + e.getAmount(), player.getMaxHealth())))));
    }

    public static void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent e) {
        GroupProvider.getGroup(e.getPlayer()).ifPresent(GroupData::sync);
    }

}
