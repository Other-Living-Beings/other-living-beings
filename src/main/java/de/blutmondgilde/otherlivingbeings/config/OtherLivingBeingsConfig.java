package de.blutmondgilde.otherlivingbeings.config;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = OtherLivingBeings.MOD_ID)
public class OtherLivingBeingsConfig implements ConfigData {
    @ConfigEntry.Gui.RequiresRestart
    @ConfigEntry.Gui.CollapsibleObject
    public SkillConfig skillConfig = new SkillConfig();

    public static class SkillConfig {
        @ConfigEntry.Gui.CollapsibleObject
        public Lumberjack lumberjack = new Lumberjack();
        @ConfigEntry.Gui.CollapsibleObject
        public Miner miner = new Miner();
        @ConfigEntry.Gui.CollapsibleObject
        public Farmer farmer = new Farmer();

        public static class Lumberjack {
            @ConfigEntry.Gui.Tooltip
            public float breakSpeedPerLevel = 0.05F;
            @ConfigEntry.Gui.Tooltip
            public float unbreakingChance = 0.05F;
        }

        public static class Miner {
            @ConfigEntry.Gui.Tooltip
            public float breakSpeedPerLevel = 0.05F;
            @ConfigEntry.Gui.Tooltip
            public float unbreakingChance = 0.05F;
        }

        public static class Farmer {
            @ConfigEntry.Gui.Tooltip
            public float doubleLootChance = 0.05F;
        }
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        //TODO validation
    }
}
