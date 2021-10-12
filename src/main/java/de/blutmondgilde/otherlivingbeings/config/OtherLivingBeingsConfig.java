package de.blutmondgilde.otherlivingbeings.config;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

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
            public Block woodBlock = Blocks.ACACIA_LOG;
        }
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        //TODO validation
    }
}
