package de.blutmondgilde.otherlivingbeings.config;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = OtherLivingBeings.MOD_ID)
public class OtherLivingBeingsConfig implements ConfigData {
    boolean test = false;

    @Override
    public void validatePostLoad() throws ValidationException {

    }
}
