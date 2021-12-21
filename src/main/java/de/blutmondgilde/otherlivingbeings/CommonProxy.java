package de.blutmondgilde.otherlivingbeings;

import de.blutmondgilde.otherlivingbeings.capability.OtherLivingBeingsCapManager;
import de.blutmondgilde.otherlivingbeings.data.group.GroupData;
import de.blutmondgilde.otherlivingbeings.data.group.GroupMemberData;
import de.blutmondgilde.otherlivingbeings.data.group.GroupProvider;
import de.blutmondgilde.otherlivingbeings.data.skills.pojo.BlockStateExpEntry;
import de.blutmondgilde.otherlivingbeings.data.skills.pojo.EntityExpEntry;
import de.blutmondgilde.otherlivingbeings.data.skills.pojo.ItemExpEntry;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.FarmerData;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.LumberjackData;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.MinerData;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.SlaughtererData;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.SmelterData;
import de.blutmondgilde.otherlivingbeings.handler.DataPackHandler;
import de.blutmondgilde.otherlivingbeings.handler.GroupHandler;
import de.blutmondgilde.otherlivingbeings.handler.SkillHandler;
import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncBlockDataPackPacket;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncEntityDataPackPacket;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncItemDataPackPacket;
import de.blutmondgilde.otherlivingbeings.registry.CommandRegistry;
import de.blutmondgilde.otherlivingbeings.registry.OtherLivingBeingRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import net.minecraftforge.fmlserverevents.FMLServerStoppingEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.util.Map;

public class CommonProxy {
    public void preInit(IEventBus modBus, IEventBus forgeBus) {
        modBus.addListener(this::init);
        modBus.addListener(this::dataGen);

        forgeBus.addListener(this::serverStart);
        forgeBus.addListener(this::serverStop);

        OtherLivingBeingRegistry.init();

        SkillHandler.init(forgeBus);
        DataPackHandler.init(forgeBus);
        CommandRegistry.init(forgeBus);
        GroupHandler.init(forgeBus);

        OtherLivingBeingsCapManager.init(modBus, forgeBus);
    }

    private void init(final FMLCommonSetupEvent e) {
        e.enqueueWork(OtherLivingBeingNetwork::registerPackets);
    }

    private void dataGen(final GatherDataEvent e) {
        if (e.includeServer()) {
            e.getGenerator().addProvider(new LumberjackData.DataGenerator(OtherLivingBeings.MOD_ID, e.getGenerator().getOutputFolder()));
            e.getGenerator().addProvider(new MinerData.DataGenerator(OtherLivingBeings.MOD_ID, e.getGenerator().getOutputFolder()));
            e.getGenerator().addProvider(new FarmerData.DataGenerator(OtherLivingBeings.MOD_ID, e.getGenerator().getOutputFolder()));
            e.getGenerator().addProvider(new SlaughtererData.DataGenerator(OtherLivingBeings.MOD_ID, e.getGenerator().getOutputFolder()));
            e.getGenerator().addProvider(new SmelterData.DataGenerator(OtherLivingBeings.MOD_ID, e.getGenerator().getOutputFolder()));
        }
    }

    private void serverStart(final FMLServerStartingEvent e) {
        GroupProvider.initialize(e.getServer());
    }

    private void serverStop(final FMLServerStoppingEvent e) {
        GroupProvider.shutdown(e.getServer());
    }

    public void updateGroupMemberInformation(GroupMemberData memberData) {}

    public void updateGroupInformation(GroupData groupData) {}

    public void resetGroupInformation() {}

    public void syncPlayerSkills(final CompoundTag tag, final int targetId) {}

    public void updateDataProvider(Map<Item, ItemExpEntry> expMap, SyncItemDataPackPacket.Type type) {}

    public void updateDataProvider(Map<EntityType<?>, EntityExpEntry> expMap, SyncEntityDataPackPacket.Type type) {}

    public void updateDataProvider(Map<Block, BlockStateExpEntry> expMap, SyncBlockDataPackPacket.Type type) {}

    public boolean isClient() {
        return false;
    }

    public void syncFurnaceOwner(BlockPos pos, CompoundTag tag) {}

    public void openPlayerInventory(){}
}
