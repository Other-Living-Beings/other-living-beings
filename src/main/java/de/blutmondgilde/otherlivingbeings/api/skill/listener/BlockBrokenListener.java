package de.blutmondgilde.otherlivingbeings.api.skill.listener;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.world.BlockEvent;

public interface BlockBrokenListener {
    /**
     * Event listener for {@link BlockEvent.BreakEvent}.
     *
     * @param player Player breaking the Block
     * @param state  Blockstate of the Block
     * @param pos    Position of the Block
     * @param world  World the block is in
     *
     * @return true = cancel event      false = continue event
     */
    boolean onBlockBroken(Player player, BlockState state, BlockPos pos, LevelAccessor world);
}
