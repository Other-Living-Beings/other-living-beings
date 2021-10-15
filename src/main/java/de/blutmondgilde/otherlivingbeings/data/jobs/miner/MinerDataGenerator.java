package de.blutmondgilde.otherlivingbeings.data.jobs.miner;

import de.blutmondgilde.otherlivingbeings.data.jobs.JobDataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.world.level.block.Blocks;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class MinerDataGenerator extends JobDataGenerator<MinerPojo> {
    public MinerDataGenerator(String modId, Path outputFolder) {
        super(modId, "miner", outputFolder);
    }

    @Override
    public void run(HashCache cache) throws IOException {

        generateWithKey(cache, "default", new MinerPojo(1.0F, List.of(
                Blocks.COAL_ORE,
                Blocks.COPPER_ORE,
                Blocks.DIAMOND_ORE,
                Blocks.EMERALD_ORE,
                Blocks.GOLD_ORE,
                Blocks.IRON_ORE,
                Blocks.LAPIS_ORE,
                Blocks.DEEPSLATE_COAL_ORE,
                Blocks.DEEPSLATE_COPPER_ORE,
                Blocks.DEEPSLATE_DIAMOND_ORE,
                Blocks.DEEPSLATE_EMERALD_ORE,
                Blocks.DEEPSLATE_GOLD_ORE,
                Blocks.DEEPSLATE_IRON_ORE,
                Blocks.DEEPSLATE_LAPIS_ORE,
                Blocks.DEEPSLATE_REDSTONE_ORE,
                Blocks.NETHER_GOLD_ORE,
                Blocks.NETHER_QUARTZ_ORE
        )));
    }
}
