package de.blutmondgilde.otherlivingbeings.config;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.config.elements.TreeListEntry;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

@Config(name = OtherLivingBeings.MOD_ID)
public class OtherLivingBeingsConfig implements ConfigData {
    @ConfigEntry.Gui.RequiresRestart
    @ConfigEntry.Gui.CollapsibleObject
    public JobConfig jobConfig = new JobConfig();

    public static class JobConfig {
        @ConfigEntry.Gui.CollapsibleObject
        public Lumberjack lumberjack = new Lumberjack();

        public static class Lumberjack {
            @ConfigEntry.Gui.Tooltip
            public float breakSpeedPerLevel = 0.05F;
            @ConfigEntry.Gui.Tooltip
            public float unbreakingChance = 0.05F;
            /*
            public List<TreeListEntry> treeBlocks = new ArrayList<>(List.of(
                    new TreeListEntry(1F, new ArrayList<>(List.of(
                            Blocks.ACACIA_LOG,
                            Blocks.BIRCH_LOG,
                            Blocks.DARK_OAK_LOG,
                            Blocks.JUNGLE_LOG,
                            Blocks.OAK_LOG,
                            Blocks.SPRUCE_LOG)))
            ));
             */

            public TreeListEntry treeListEntry = new TreeListEntry(1F, new ArrayList<>(List.of(
                    Blocks.ACACIA_LOG,
                    Blocks.BIRCH_LOG,
                    Blocks.DARK_OAK_LOG,
                    Blocks.JUNGLE_LOG,
                    Blocks.OAK_LOG,
                    Blocks.SPRUCE_LOG)));
        }
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        //TODO validation
    }
}
