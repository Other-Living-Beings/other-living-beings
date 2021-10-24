package de.blutmondgilde.otherlivingbeings.skills;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.api.skill.AbstractLevelSkill;
import de.blutmondgilde.otherlivingbeings.api.skill.listener.LivingHurtListener;
import de.blutmondgilde.otherlivingbeings.capability.skill.IPlayerSkills;
import de.blutmondgilde.otherlivingbeings.capability.skill.PlayerSkillsImpl;
import de.blutmondgilde.otherlivingbeings.data.skills.pojo.EntityExpEntry;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.SlaughtererData;
import de.blutmondgilde.otherlivingbeings.util.TranslationUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.awt.*;
import java.util.List;

public class Slaughterer extends AbstractLevelSkill implements LivingHurtListener {

    @Override
    public MutableComponent getDisplayName() {
        return TranslationUtils.createSkillComponent("slaughterer")
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(new Color(148, 0, 0, 255).getRGB())));
    }

    @Override
    public float onHurt(LivingEntity target, float amount, DamageSource source, Player trueSource) {
        if (!SlaughtererData.Provider.getExpMap().containsKey(target.getType())) return amount;
        EntityExpEntry expValue = SlaughtererData.Provider.getExpMap().get(target.getType());
        IPlayerSkills skills = trueSource.getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).orElse(new PlayerSkillsImpl());
        List<Slaughterer> slaughterers = skills.getSkills()
                .stream()
                .filter(iSkill -> iSkill instanceof Slaughterer)
                .map(iSkill -> (Slaughterer) iSkill)
                .toList();

        for (Slaughterer skill : slaughterers) {
            skill.increaseExp(expValue.getExp());
            double increment = Math.ceil(skill.getLevel() / 10.0) - 1;
            amount += OtherLivingBeings.getConfig().get().skillConfig.slaughterer.additionalDamage * increment;
        }

        skills.sync(trueSource);

        return amount;
    }
}
