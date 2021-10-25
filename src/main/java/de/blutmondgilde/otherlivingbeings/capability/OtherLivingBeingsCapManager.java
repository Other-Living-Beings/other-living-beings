package de.blutmondgilde.otherlivingbeings.capability;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.capability.block.FurnacePlayerImpl;
import de.blutmondgilde.otherlivingbeings.capability.block.IFurnacePlacer;
import de.blutmondgilde.otherlivingbeings.capability.skill.IPlayerSkills;
import de.blutmondgilde.otherlivingbeings.capability.skill.PlayerSkillsImpl;
import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncSkillsPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

public class OtherLivingBeingsCapManager {
    public static void init(final IEventBus modBus, final IEventBus forgeBus) {
        modBus.addListener(OtherLivingBeingsCapManager::registerCapabilities);
        forgeBus.addGenericListener(Entity.class, OtherLivingBeingsCapManager::attachEntityCap);
        forgeBus.addGenericListener(BlockEntity.class, OtherLivingBeingsCapManager::attachBlockEntityCap);
        forgeBus.addListener(OtherLivingBeingsCapManager::onPlayerLoggedIn);
        forgeBus.addListener(OtherLivingBeingsCapManager::onPlayerRespawn);
        forgeBus.addListener(OtherLivingBeingsCapManager::onPlayerDimChange);
        forgeBus.addListener(OtherLivingBeingsCapManager::onStartTracking);
        forgeBus.addListener(OtherLivingBeingsCapManager::onPlaceFurnace);
    }

    private static void registerCapabilities(final RegisterCapabilitiesEvent e) {
        e.register(IPlayerSkills.class);
        e.register(IFurnacePlacer.class);
    }

    private static void attachEntityCap(final AttachCapabilitiesEvent<Entity> e) {
        if (PlayerSkillsImpl.canAttachTo(e.getObject())) e.addCapability(new ResourceLocation(OtherLivingBeings.MOD_ID, "player_skills"), new PlayerSkillsImpl());
    }

    private static void attachBlockEntityCap(final AttachCapabilitiesEvent<BlockEntity> e) {
        if (FurnacePlayerImpl.canAttachTo(e.getObject())) e.addCapability(new ResourceLocation(OtherLivingBeings.MOD_ID, "furnace_placer"), new FurnacePlayerImpl());
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

    public static void onPlaceFurnace(final BlockEvent.EntityPlaceEvent e) {
        if (!(e.getPlacedBlock().getBlock() instanceof AbstractFurnaceBlock)) return;
        if (e.getEntity() == null) return;
        if (!(e.getEntity() instanceof Player player)) return;
        if (!(e.getWorld().getBlockEntity(e.getPos()) instanceof AbstractFurnaceBlockEntity blockEntity)) return;

        IFurnacePlacer cap = blockEntity.getCapability(OtherLivingBeingsCapability.FURNACE_PLACER).orElse(new FurnacePlayerImpl());
        cap.setOwner(player.getUUID());
        ChunkAccess chunkAccess = e.getWorld().getChunk(e.getPos());
        cap.sync(e.getWorld().getChunkSource().getChunkNow(chunkAccess.getPos().x, chunkAccess.getPos().z), e.getPos());
    }
}
