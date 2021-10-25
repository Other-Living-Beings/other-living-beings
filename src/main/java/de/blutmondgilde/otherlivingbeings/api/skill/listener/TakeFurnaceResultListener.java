package de.blutmondgilde.otherlivingbeings.api.skill.listener;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface TakeFurnaceResultListener {
    void onTake(Player player, ItemStack itemStack);
}
