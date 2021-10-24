package de.blutmondgilde.otherlivingbeings.data.skills.provider;

import com.google.gson.JsonElement;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.data.skills.pojo.BlockStateExpEntry;
import de.blutmondgilde.otherlivingbeings.data.skills.ExpEntryDataGenerator;
import de.blutmondgilde.otherlivingbeings.data.skills.ReloadableJobDataProvider;
import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncBlockDataPackPacket;
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

public class LumberjackData {
    private static final String name = "lumberjack";
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
            OtherLivingBeingNetwork.getInstance().send(PacketDistributor.ALL.noArg(), new SyncBlockDataPackPacket(expMap, SyncBlockDataPackPacket.Type.Lumberjack));
        }

        @Override
        public void sync(ServerPlayer player) {
            OtherLivingBeingNetwork.getInstance().send(PacketDistributor.PLAYER.with(() -> player), new SyncBlockDataPackPacket(expMap, SyncBlockDataPackPacket.Type.Lumberjack));
        }
    }

    public static class DataGenerator extends ExpEntryDataGenerator<BlockStateExpEntry> {
        public DataGenerator(String modId, Path outputFolder) {
            super(modId, "lumberjack", outputFolder);
        }

        @Override
        public void run(HashCache cache) throws IOException {

            for (BlockStateExpEntry entry : BlockStateExpEntry.fromBlockState(type, 1.0F,
                    Blocks.ACACIA_LOG.defaultBlockState(),
                    Blocks.BIRCH_LOG.defaultBlockState(),
                    Blocks.DARK_OAK_LOG.defaultBlockState(),
                    Blocks.JUNGLE_LOG.defaultBlockState(),
                    Blocks.OAK_LOG.defaultBlockState(),
                    Blocks.SPRUCE_LOG.defaultBlockState())) {
                generateWithKey(cache, entry.getBlock().getRegistryName().getPath() + "_exp", entry);
            }
        }
    }
}
