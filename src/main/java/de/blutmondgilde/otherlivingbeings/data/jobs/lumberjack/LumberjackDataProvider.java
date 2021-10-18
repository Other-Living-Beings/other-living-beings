package de.blutmondgilde.otherlivingbeings.data.jobs.lumberjack;

import com.google.gson.JsonElement;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.data.jobs.ReloadableJobDataProvider;
import de.blutmondgilde.otherlivingbeings.network.OtherLivingBeingNetwork;
import de.blutmondgilde.otherlivingbeings.network.packet.toclient.SyncDataPack;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.util.HashMap;

public class LumberjackDataProvider extends ReloadableJobDataProvider {
    @Setter
    @Getter
    private static HashMap<Block, Float> expMap = new HashMap<>();

    public LumberjackDataProvider() {
        super("lumberjack");
    }

    @Override
    protected void apply(ResourceLocation fileLocation, JsonElement jsonElement) {
        LumberjackPojo data = new LumberjackPojo().fromJson(jsonElement.toString());
        OtherLivingBeings.getLogger().debug("Applying {} with {} blocks to Lumberjack", fileLocation, data.getBlocks().size());
        data.getBlocks().forEach(block -> expMap.put(block, data.exp));
    }

    @Override
    public void sync() {
        OtherLivingBeingNetwork.getInstance().send(PacketDistributor.ALL.noArg(), new SyncDataPack(expMap, SyncDataPack.Type.Lumberjack));
    }

    @Override
    public void sync(ServerPlayer player) {
        OtherLivingBeingNetwork.getInstance().send(PacketDistributor.PLAYER.with(() -> player), new SyncDataPack(expMap, SyncDataPack.Type.Lumberjack));
    }
}
