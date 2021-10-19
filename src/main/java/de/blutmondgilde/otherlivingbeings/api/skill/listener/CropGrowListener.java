package de.blutmondgilde.otherlivingbeings.api.skill.listener;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Event;

public interface CropGrowListener {
    Event.Result onGrowTick(Player player, BlockPos blockPos, BlockState blockState, LevelAccessor world);
}
