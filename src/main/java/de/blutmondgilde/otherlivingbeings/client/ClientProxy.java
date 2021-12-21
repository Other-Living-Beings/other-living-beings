package de.blutmondgilde.otherlivingbeings.client;

import de.blutmondgilde.otherlivingbeings.CommonProxy;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.data.group.GroupData;
import de.blutmondgilde.otherlivingbeings.data.group.GroupMemberData;
import de.blutmondgilde.otherlivingbeings.data.skills.pojo.BlockStateExpEntry;
import de.blutmondgilde.otherlivingbeings.data.skills.pojo.EntityExpEntry;
import de.blutmondgilde.otherlivingbeings.data.skills.pojo.ItemExpEntry;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncBlockDataPackPacket;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncEntityDataPackPacket;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncItemDataPackPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.Map;
import java.util.Optional;

public class ClientProxy extends CommonProxy {
    private final Minecraft minecraft = Minecraft.getInstance();

    @Override
    public void preInit(IEventBus modBus, IEventBus forgeBus) {
        super.preInit(modBus, forgeBus);
        modBus.addListener(OtherLivingBeingsClient::clientSetup);
        OtherLivingBeingsClient.init();
    }

    @Override
    public void updateGroupMemberInformation(GroupMemberData memberData) {
        ClientGroupHolder.updateMemberData(memberData);
    }

    @Override
    public void resetGroupInformation() {
        ClientGroupHolder.reset();
    }

    @Override
    public void updateGroupInformation(GroupData groupData) {
        ClientGroupHolder.updateGroupData(groupData);
    }

    @Override
    public void syncPlayerSkills(final CompoundTag tag, final int targetId) {
        OtherLivingBeings.getLogger().debug("Received Player Skills Sync Packet with {} values. Start Handling...", tag.size());

        final long startTime = System.currentTimeMillis();
        final ClientLevel level = minecraft.level;

        if (level == null) {
            OtherLivingBeings.getLogger().fatal("Exception while syncing Skills from Entity {}. ClientWorld is NULL!", targetId);
            return;
        }
        Optional<Entity> target = Optional.ofNullable(level.getEntity(targetId));
        target.ifPresent(entity -> entity.getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).ifPresent(playerSkills -> playerSkills.deserializeNBT(tag)));

        OtherLivingBeings.getLogger().debug("Applied Player Skill Sync Packet in {} ms", System.currentTimeMillis() - startTime);
    }

    @Override
    public void updateDataProvider(Map<Item, ItemExpEntry> expMap, SyncItemDataPackPacket.Type type) {
        type.apply.accept(expMap);
    }

    @Override
    public void updateDataProvider(Map<EntityType<?>, EntityExpEntry> expMap, SyncEntityDataPackPacket.Type type) {
        type.apply.accept(expMap);
    }

    @Override
    public void updateDataProvider(Map<Block, BlockStateExpEntry> expMap, SyncBlockDataPackPacket.Type type) {
        type.apply.accept(expMap);
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public void syncFurnaceOwner(BlockPos pos, CompoundTag tag) {
        minecraft.level.getBlockEntity(pos).getCapability(OtherLivingBeingsCapability.FURNACE_PLACER).ifPresent(iFurnacePlacer -> iFurnacePlacer.deserializeNBT(tag));
    }

    @Override
    public void openPlayerInventory() {
        minecraft.setScreen(new InventoryScreen(minecraft.player));
    }
}
