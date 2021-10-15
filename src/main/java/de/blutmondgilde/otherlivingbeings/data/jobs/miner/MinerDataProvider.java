package de.blutmondgilde.otherlivingbeings.data.jobs.miner;

import com.google.gson.JsonElement;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.data.jobs.ReloadableJobDataProvider;
import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.network.packet.SyncDataPack;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.util.HashMap;

public class MinerDataProvider extends ReloadableJobDataProvider {
    @Setter
    @Getter
    private static HashMap<Block, Float> expMap = new HashMap<>();

    public MinerDataProvider() {
        super("miner");
    }

    @Override
    protected void apply(ResourceLocation fileLocation, JsonElement jsonElement) {
        MinerPojo data = new MinerPojo().fromJson(jsonElement.toString());
        OtherLivingBeings.getLogger().debug("Applying {} with {} blocks to Miner", fileLocation, data.getBlocks().size());
        data.getBlocks().forEach(block -> expMap.put(block, data.exp));
    }

    @Override
    public void sync() {
        OtherLivingBeingNetwork.getInstance().send(PacketDistributor.ALL.noArg(), new SyncDataPack(expMap));
    }

    @Override
    public void sync(ServerPlayer player) {
        OtherLivingBeingNetwork.getInstance().send(PacketDistributor.PLAYER.with(() -> player), new SyncDataPack(expMap));
    }
}
