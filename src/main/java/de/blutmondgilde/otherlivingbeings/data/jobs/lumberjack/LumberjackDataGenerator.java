package de.blutmondgilde.otherlivingbeings.data.jobs.lumberjack;

import de.blutmondgilde.otherlivingbeings.data.jobs.JobDataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.world.level.block.Blocks;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class LumberjackDataGenerator extends JobDataGenerator<LumberjackPojo> {
    public LumberjackDataGenerator(String modId, Path outputFolder) {
        super(modId, "lumberjack", outputFolder);
    }

    @Override
    public void run(HashCache cache) throws IOException {

        generateWithKey(cache, "default", new LumberjackPojo(1.0F, List.of(
                Blocks.ACACIA_LOG,
                Blocks.BIRCH_LOG,
                Blocks.DARK_OAK_LOG,
                Blocks.JUNGLE_LOG,
                Blocks.OAK_LOG,
                Blocks.SPRUCE_LOG)));
    }
}
