package de.blutmondgilde.otherlivingbeings.skills;

import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.api.skill.AbstractLevelSkill;
import de.blutmondgilde.otherlivingbeings.api.skill.listener.BlockBrokenListener;
import de.blutmondgilde.otherlivingbeings.api.skill.listener.CropGrowListener;
import de.blutmondgilde.otherlivingbeings.capability.skill.IPlayerSkills;
import de.blutmondgilde.otherlivingbeings.capability.skill.PlayerSkillsImpl;
import de.blutmondgilde.otherlivingbeings.data.skills.BlockStateExpEntry;
import de.blutmondgilde.otherlivingbeings.data.skills.provider.FarmerData;
import de.blutmondgilde.otherlivingbeings.util.TranslationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Event;

import java.awt.*;
import java.util.HashMap;

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
        HashMap<Block, BlockStateExpEntry> expMap = FarmerData.Provider.getExpMap();
        if (expMap.containsKey(state.getBlock())) {
            if (expMap.get(state.getBlock()).isValid(state)) {
                IPlayerSkills playerSkills = player.getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).orElse(new PlayerSkillsImpl());
                //Increase EXP
                playerSkills.getSkills()
                        .stream()
                        .filter(iSkill -> iSkill instanceof Farmer)
                        .forEach(iSkill -> iSkill.increaseExp(expMap.get(state.getBlock()).getExp()));
                //Sync Client
                playerSkills.sync(player);
            }
        }
        return false;
    }
}
