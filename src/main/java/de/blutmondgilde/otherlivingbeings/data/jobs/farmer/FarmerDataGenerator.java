package de.blutmondgilde.otherlivingbeings.data.jobs.farmer;

import de.blutmondgilde.otherlivingbeings.data.jobs.BlockExpPojo;
import de.blutmondgilde.otherlivingbeings.data.jobs.JobDataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.world.level.block.Blocks;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class FarmerDataGenerator extends JobDataGenerator<BlockExpPojo> {
    public FarmerDataGenerator(String modId, Path outputFolder) {
        super(modId, "farmer", outputFolder);
    }

    @Override
    public void run(HashCache cache) throws IOException {

        generateWithKey(cache, "default", new BlockExpPojo(1.0F, List.of(
                Blocks.WHEAT,
                Blocks.BEETROOTS,
                Blocks.CARROTS,
                Blocks.POTATOES,
                Blocks.MELON,
                Blocks.PUMPKIN,
                Blocks.BAMBOO,
                Blocks.COCOA,
                Blocks.SUGAR_CANE,
                Blocks.MUSHROOM_STEM,
                Blocks.BROWN_MUSHROOM_BLOCK,
                Blocks.BROWN_MUSHROOM,
                Blocks.RED_MUSHROOM_BLOCK,
                Blocks.POTTED_BROWN_MUSHROOM,
                Blocks.RED_MUSHROOM,
                Blocks.POTTED_RED_MUSHROOM,
                Blocks.CACTUS,
                Blocks.KELP,
                Blocks.SEA_PICKLE,
                Blocks.CRIMSON_FUNGUS,
                Blocks.POTTED_CRIMSON_FUNGUS,
                Blocks.POTTED_WARPED_FUNGUS,
                Blocks.WARPED_FUNGUS,
                Blocks.SWEET_BERRY_BUSH)));
    }
}
