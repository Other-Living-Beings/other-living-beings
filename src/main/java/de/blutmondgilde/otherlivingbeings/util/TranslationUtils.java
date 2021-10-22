package de.blutmondgilde.otherlivingbeings.util;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import net.minecraft.network.chat.TranslatableComponent;

public class TranslationUtils {
    public static TranslatableComponent createModComponent(String path) {
        return new TranslatableComponent("otherlivingbeings." + path);
    }

    public static TranslatableComponent createSkillComponent(String skillName) {
        return createModComponent("skill." + skillName);
    }

    public static TranslatableComponent createMessageComponent(String messagePath) {
        return new TranslatableComponent(OtherLivingBeings.MOD_ID + ".messages." + messagePath);
    }

    public static TranslatableComponent createGroupMessage(String messagePath) {
        return createMessageComponent("group." + messagePath);
    }

    public static TranslatableComponent createSkillMessage(String messagePath) {
        return createMessageComponent("skill." + messagePath);
    }
}
