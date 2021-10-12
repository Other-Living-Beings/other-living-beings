package de.blutmondgilde.otherlivingbeings.api.skill.listener;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerEvent;

public interface BlockBreakListener {
    /**
     * Event listener for {@link PlayerEvent.BreakSpeed}.
     *
     * @param player        Player breaking the Block
     * @param state         Blockstate of block which is about to break
     * @param pos           Position of block which is about to break
     * @param originalSpeed Original break speed (without modifiers)
     * @param newSpeed      Modified break speed
     *
     * @return new break speed. If the value is less or equal 0 the event will be canceled.
     */
    float onBlockBreak(final Player player, BlockState state, BlockPos pos, float originalSpeed, float newSpeed);
}
