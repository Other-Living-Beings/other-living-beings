package de.blutmondgilde.otherlivingbeings.skills;

import de.blutmondgilde.otherlivingbeings.api.skill.AbstractLevelSkill;
import de.blutmondgilde.otherlivingbeings.util.TranslationUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.awt.*;

public class Lumberjack extends AbstractLevelSkill {
    @Override
    public MutableComponent getName() {
        return TranslationUtils.createSkillComponent("lumberjack")
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(new Color(129, 74, 0, 197).getRGB())));
    }
}
