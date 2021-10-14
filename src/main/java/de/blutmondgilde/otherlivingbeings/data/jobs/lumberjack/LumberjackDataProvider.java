package de.blutmondgilde.otherlivingbeings.data.jobs.lumberjack;

import com.google.gson.JsonElement;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.data.jobs.ReloadableJobDataProvider;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;

public class LumberjackDataProvider extends ReloadableJobDataProvider {
    @Getter
    private static final HashMap<Block, Float> expMap = new HashMap<>();

    public LumberjackDataProvider() {
        super("lumberjack");
    }

    @Override
    protected void apply(ResourceLocation fileLocation, JsonElement jsonElement) {
        LumberjackPojo data = GSON.fromJson(jsonElement, LumberjackPojo.class);
        OtherLivingBeings.getLogger().debug("Applying {} with {} blocks to Lumberjack", fileLocation, data.getBlocks().size());
        //data.getBlocks().forEach(block -> expMap.put(block, exp));
    }
}
