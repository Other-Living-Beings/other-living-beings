package de.blutmondgilde.otherlivingbeings.skills;

import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.api.skill.AbstractLevelSkill;
import de.blutmondgilde.otherlivingbeings.api.skill.listener.BlockBreakListener;
import de.blutmondgilde.otherlivingbeings.api.skill.listener.BlockBrokenListener;
import de.blutmondgilde.otherlivingbeings.capability.skill.IPlayerSkills;
import de.blutmondgilde.otherlivingbeings.capability.skill.PlayerSkillsImpl;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.LumberjackData;
import de.blutmondgilde.otherlivingbeings.util.TranslationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;

public class Lumberjack extends AbstractLevelSkill implements BlockBreakListener, BlockBrokenListener {
    @Override
    public float onBlockBreak(Player player, BlockState state, BlockPos pos, float originalSpeed, float newSpeed) {
        //increases break speed
        return newSpeed + OtherLivingBeings.getConfig().get().skillConfig.lumberjack.breakSpeedPerLevel * getLevel();
    }

    @Override
    public boolean onBlockBroken(Player player, BlockState state, BlockPos pos, LevelAccessor world) {
        final ItemStack item = player.getMainHandItem();
        if (item.getItem() instanceof AxeItem) {
            double random = Math.random();
            //apply chance to not use durability of item in Hand
            double increment = Math.ceil(getLevel() / 10.0);
            if (random < OtherLivingBeings.getConfig().get().skillConfig.lumberjack.unbreakingChance * increment) {
                if (item.getDamageValue() > 0) {
                    item.setDamageValue(item.getDamageValue() - 1);
                }
            }

            if (LumberjackData.Provider.getExpMap().containsKey(state.getBlock())) {
                if (LumberjackData.Provider.getExpMap().get(state.getBlock()).isValid(state)) {
                    IPlayerSkills playerSkills = player.getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).orElse(new PlayerSkillsImpl());
                    //Increase EXP
                    playerSkills.getSkills()
                            .stream()
                            .filter(iSkill -> iSkill instanceof Lumberjack)
                            .forEach(iSkill -> {
                                iSkill.increaseExp(LumberjackData.Provider.getExpMap().get(state.getBlock()).getExp());
                            });
                    //Sync Client
                    playerSkills.sync(player);
                }
            }
        }
        return false;
    }

    @Override
    public MutableComponent getDisplayName() {
        return TranslationUtils.createSkillComponent("lumberjack")
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(new Color(129, 74, 0, 197).getRGB())));
    }
}
