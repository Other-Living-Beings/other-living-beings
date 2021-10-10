package de.blutmondgilde.otherlivingbeings.util;

import net.minecraft.network.chat.TranslatableComponent;

public class TranslationUtils {
    public static TranslatableComponent createModComponent(String path) {
        return new TranslatableComponent("otherlivingbeings." + path);
    }

    public static TranslatableComponent createSkillComponent(String skillName) {
        return createModComponent("skill." + skillName);
    }
}
