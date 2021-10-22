package de.blutmondgilde.otherlivingbeings.data.skills.provider;

import com.google.gson.JsonElement;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.data.skills.BlockStateExpEntry;
import de.blutmondgilde.otherlivingbeings.data.skills.ExpEntryDataGenerator;
import de.blutmondgilde.otherlivingbeings.data.skills.ReloadableJobDataProvider;
import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncDataPackPacket;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class MinerData {
    private static final String name = "miner";
    private static final String type = OtherLivingBeings.MOD_ID + ":" + name + "_blocks";

    public static class Provider extends ReloadableJobDataProvider {
        @Setter
        @Getter
        private static HashMap<Block, BlockStateExpEntry> expMap = new HashMap<>();

        public Provider() {
            super(name);
        }

        @Override
        protected void apply(ResourceLocation fileLocation, JsonElement jsonElement) {
            BlockStateExpEntry data = new BlockStateExpEntry().fromJson(jsonElement.toString());
            if (data.type.equals(type)) expMap.put(data.block, data);
        }

        @Override
        public void sync() {
            OtherLivingBeingNetwork.getInstance().send(PacketDistributor.ALL.noArg(), new SyncDataPackPacket(expMap, SyncDataPackPacket.Type.Miner));
        }

        @Override
        public void sync(ServerPlayer player) {
            OtherLivingBeingNetwork.getInstance().send(PacketDistributor.PLAYER.with(() -> player), new SyncDataPackPacket(expMap, SyncDataPackPacket.Type.Miner));
        }
    }

    public static class MinerDataGenerator extends ExpEntryDataGenerator<BlockStateExpEntry> {
        public MinerDataGenerator(String modId, Path outputFolder) {
            super(modId, "miner", outputFolder);
        }

        @Override
        public void run(HashCache cache) throws IOException {
            //EXP values by niceore_101
            for (BlockStateExpEntry entry : BlockStateExpEntry.fromBlockState(type, 1.0F,
                    Blocks.COAL_ORE.defaultBlockState(),
                    Blocks.DEEPSLATE_COAL_ORE.defaultBlockState(),
                    Blocks.COPPER_ORE.defaultBlockState(),
                    Blocks.DEEPSLATE_COPPER_ORE.defaultBlockState(),
                    Blocks.NETHER_QUARTZ_ORE.defaultBlockState())) {
                generateWithKey(cache, entry.getBlock().getRegistryName().getPath() + "_exp", entry);
            }

            for (BlockStateExpEntry entry : BlockStateExpEntry.fromBlockState(type, 2.0F,
                    Blocks.IRON_ORE.defaultBlockState(),
                    Blocks.DEEPSLATE_IRON_ORE.defaultBlockState(),
                    Blocks.REDSTONE_ORE.defaultBlockState(),
                    Blocks.DEEPSLATE_REDSTONE_ORE.defaultBlockState())) {
                generateWithKey(cache, entry.getBlock().getRegistryName().getPath() + "_exp", entry);
            }

            for (BlockStateExpEntry entry : BlockStateExpEntry.fromBlockState(type, 3.0F,
                    Blocks.GOLD_ORE.defaultBlockState(),
                    Blocks.NETHER_GOLD_ORE.defaultBlockState(),
                    Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState(),
                    Blocks.LAPIS_ORE.defaultBlockState(),
                    Blocks.DEEPSLATE_LAPIS_ORE.defaultBlockState())) {
                generateWithKey(cache, entry.getBlock().getRegistryName().getPath() + "_exp", entry);
            }

            for (BlockStateExpEntry entry : BlockStateExpEntry.fromBlockState(type, 5.0F,
                    Blocks.DIAMOND_ORE.defaultBlockState(),
                    Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState())) {
                generateWithKey(cache, entry.getBlock().getRegistryName().getPath() + "_exp", entry);
            }

            for (BlockStateExpEntry entry : BlockStateExpEntry.fromBlockState(type, 7.0F,
                    Blocks.EMERALD_ORE.defaultBlockState(),
                    Blocks.DEEPSLATE_EMERALD_ORE.defaultBlockState())) {
                generateWithKey(cache, entry.getBlock().getRegistryName().getPath() + "_exp", entry);
            }

        }
    }
}
