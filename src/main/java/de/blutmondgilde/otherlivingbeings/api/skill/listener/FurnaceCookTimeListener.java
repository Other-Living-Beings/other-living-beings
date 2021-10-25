package de.blutmondgilde.otherlivingbeings.api.skill.listener;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public interface FurnaceCookTimeListener {
    /**
     * Called whenever a new Item in a {@link AbstractFurnaceBlockEntity} will be smelt.
     *
     * @param blockEntity     {@link AbstractFurnaceBlockEntity} instance
     * @param player          Placer of the {@link AbstractFurnaceBlockEntity}
     * @param currentCookTime total cooking time in Ticks
     *
     * @return new cooking time in ticks
     */
    int calculateCookTime(AbstractFurnaceBlockEntity blockEntity, ServerPlayer player, int currentCookTime);
}
