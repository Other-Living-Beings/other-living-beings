package de.blutmondgilde.otherlivingbeings.capability;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.capability.skill.IPlayerSkills;
import de.blutmondgilde.otherlivingbeings.capability.skill.PlayerSkillsImpl;
import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.network.packet.SyncSkillsPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

public class OtherLivingBeingsCapManager {
    public static void init(final IEventBus modBus, final IEventBus forgeBus) {
        modBus.addListener(OtherLivingBeingsCapManager::registerCapabilities);
        forgeBus.addGenericListener(Entity.class, OtherLivingBeingsCapManager::attachEntityCap);
        forgeBus.addListener(OtherLivingBeingsCapManager::onPlayerLoggedIn);
        forgeBus.addListener(OtherLivingBeingsCapManager::onPlayerRespawn);
        forgeBus.addListener(OtherLivingBeingsCapManager::onPlayerDimChange);
        forgeBus.addListener(OtherLivingBeingsCapManager::onStartTracking);
    }

    private static void registerCapabilities(final RegisterCapabilitiesEvent e) {
        e.register(IPlayerSkills.class);
    }

    private static void attachEntityCap(final AttachCapabilitiesEvent<Entity> e) {
        if (PlayerSkillsImpl.canAttachTo(e.getObject())) e.addCapability(new ResourceLocation(OtherLivingBeings.MOD_ID, "player_skills"), new PlayerSkillsImpl());
    }

    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e) {
        OtherLivingBeingNetwork.getInstance()
                .send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(e::getPlayer), new SyncSkillsPacket(e.getPlayer()
                        .getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS)
                        .orElse(new PlayerSkillsImpl()), e.getPlayer()));
    }

    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent e) {
        OtherLivingBeingNetwork.getInstance()
                .send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(e::getPlayer), new SyncSkillsPacket(e.getPlayer()
                        .getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS)
                        .orElse(new PlayerSkillsImpl()), e.getPlayer()));
    }

    public static void onPlayerDimChange(PlayerEvent.PlayerChangedDimensionEvent e) {
        OtherLivingBeingNetwork.getInstance()
                .send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(e::getPlayer), new SyncSkillsPacket(e.getPlayer()
                        .getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS)
                        .orElse(new PlayerSkillsImpl()), e.getPlayer()));
    }

    public static void onStartTracking(PlayerEvent.StartTracking e) {
        OtherLivingBeingNetwork.getInstance()
                .send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(e::getPlayer), new SyncSkillsPacket(e.getPlayer()
                        .getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS)
                        .orElse(new PlayerSkillsImpl()), e.getPlayer()));
    }
}
