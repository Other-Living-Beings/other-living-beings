package de.blutmondgilde.otherlivingbeings.skills;

import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.api.skill.AbstractLevelSkill;
import de.blutmondgilde.otherlivingbeings.api.skill.listener.BlockBrokenListener;
import de.blutmondgilde.otherlivingbeings.api.skill.listener.CropGrowListener;
import de.blutmondgilde.otherlivingbeings.capability.skill.IPlayerSkills;
import de.blutmondgilde.otherlivingbeings.capability.skill.PlayerSkillsImpl;
import de.blutmondgilde.otherlivingbeings.data.jobs.farmer.FarmerDataProvider;
import de.blutmondgilde.otherlivingbeings.data.jobs.lumberjack.LumberjackDataProvider;
import de.blutmondgilde.otherlivingbeings.util.TranslationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Event;

import java.awt.*;

public class Farmer extends AbstractLevelSkill implements CropGrowListener, BlockBrokenListener {
    @Override
    public MutableComponent getDisplayName() {
        return TranslationUtils.createSkillComponent("farmer").withStyle(Style.EMPTY.withColor(new Color(0, 148, 4).getRGB()));
    }

    @Override
    public Event.Result onGrowTick(Player player, BlockPos blockPos, BlockState blockState, LevelAccessor world) {
        //TODO change to grow a Crop
        return Event.Result.DEFAULT;
    }

    @Override
    public boolean onBlockBroken(Player player, BlockState state, BlockPos pos, LevelAccessor world) {
        //TODO increase Drops
        if (FarmerDataProvider.getExpMap().containsKey(state.getBlock())) {
            IPlayerSkills playerSkills = player.getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).orElse(new PlayerSkillsImpl());
            //Increase EXP
            playerSkills.getSkills()
                    .stream()
                    .filter(iSkill -> iSkill instanceof Lumberjack)
                    .forEach(iSkill -> iSkill.increaseExp(LumberjackDataProvider.getExpMap().get(state.getBlock())));
            //Sync Client
            playerSkills.sync(player);
        }
        return false;
    }
}
