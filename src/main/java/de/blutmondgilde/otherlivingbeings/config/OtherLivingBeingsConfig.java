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
        @ConfigEntry.Gui.CollapsibleObject
        public Slaughterer slaughterer = new Slaughterer();
        @ConfigEntry.Gui.CollapsibleObject
        public Smelter smelter = new Smelter();

        public static class Lumberjack {
            @ConfigEntry.Gui.Tooltip
            public float breakSpeedPerLevel = 0.005F;
            @ConfigEntry.Gui.Tooltip
            public float unbreakingChance = 0.005F;
        }

        public static class Miner {
            @ConfigEntry.Gui.Tooltip
            public float breakSpeedPerLevel = 0.005F;
            @ConfigEntry.Gui.Tooltip(count = 2)
            public float unbreakingChance = 0.005F;
            @ConfigEntry.Gui.Tooltip(count = 2)
            public float doubleLootChance = 0.005F;
            @ConfigEntry.Gui.Tooltip(count = 2)
            public float tripleLootChance = 0.005F;
        }

        public static class Farmer {
            @ConfigEntry.Gui.Tooltip(count = 2)
            public float doubleLootChance = 0.005F;
            @ConfigEntry.Gui.Tooltip(count = 2)
            public float growthTickChance = 0.005F;
        }

        public static class Slaughterer {
            @ConfigEntry.Gui.Tooltip
            public float additionalDamage = 1.0F;
        }

        public static class Smelter {
            @ConfigEntry.Gui.Tooltip(count = 2)
            public int smeltBoost = 1;
            @ConfigEntry.Gui.Tooltip(count = 2)
            public float doubleLootChance = 0.005F;
        }
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        //TODO validation
    }
}
