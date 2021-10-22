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
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class FarmerData {
    private static final String name = "farmer";
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
            if (data.type.equals(type)) {
                expMap.put(data.block, data);
            }
        }

        @Override
        public void sync() {
            OtherLivingBeingNetwork.getInstance().send(PacketDistributor.ALL.noArg(), new SyncDataPackPacket(expMap, SyncDataPackPacket.Type.Farmer));
        }

        @Override
        public void sync(ServerPlayer player) {
            OtherLivingBeingNetwork.getInstance().send(PacketDistributor.PLAYER.with(() -> player), new SyncDataPackPacket(expMap, SyncDataPackPacket.Type.Farmer));
        }
    }

    public static class FarmerDataGenerator extends ExpEntryDataGenerator<BlockStateExpEntry> {
        public FarmerDataGenerator(String modId, Path outputFolder) {
            super(modId, name, outputFolder);
        }

        @Override
        public void run(HashCache cache) throws IOException {
            for (BlockStateExpEntry entry : BlockStateExpEntry.fromBlockState(type, 1.0F,
                    Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, CropBlock.MAX_AGE),
                    Blocks.BEETROOTS.defaultBlockState().setValue(BeetrootBlock.AGE, BeetrootBlock.MAX_AGE),
                    Blocks.CARROTS.defaultBlockState().setValue(CropBlock.AGE, CropBlock.MAX_AGE),
                    Blocks.POTATOES.defaultBlockState().setValue(CropBlock.AGE, CropBlock.MAX_AGE),
                    Blocks.MELON.defaultBlockState(),
                    Blocks.PUMPKIN.defaultBlockState(),
                    Blocks.BAMBOO.defaultBlockState(),
                    Blocks.COCOA.defaultBlockState().setValue(CocoaBlock.AGE, CocoaBlock.MAX_AGE),
                    Blocks.SUGAR_CANE.defaultBlockState(),
                    Blocks.MUSHROOM_STEM.defaultBlockState(),
                    Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState(),
                    Blocks.BROWN_MUSHROOM.defaultBlockState(),
                    Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(),
                    Blocks.POTTED_BROWN_MUSHROOM.defaultBlockState(),
                    Blocks.RED_MUSHROOM.defaultBlockState(),
                    Blocks.POTTED_RED_MUSHROOM.defaultBlockState(),
                    Blocks.CACTUS.defaultBlockState(),
                    Blocks.KELP.defaultBlockState(),
                    Blocks.SEA_PICKLE.defaultBlockState(),
                    Blocks.CRIMSON_FUNGUS.defaultBlockState(),
                    Blocks.POTTED_CRIMSON_FUNGUS.defaultBlockState(),
                    Blocks.POTTED_WARPED_FUNGUS.defaultBlockState(),
                    Blocks.WARPED_FUNGUS.defaultBlockState(),
                    Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(SweetBerryBushBlock.AGE, SweetBerryBushBlock.MAX_AGE))) {
                generateWithKey(cache, entry.getBlock().getRegistryName().getPath() + "_exp", entry);
            }
        }
    }
}
