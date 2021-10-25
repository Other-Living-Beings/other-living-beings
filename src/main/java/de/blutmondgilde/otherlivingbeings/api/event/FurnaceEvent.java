package de.blutmondgilde.otherlivingbeings.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.eventbus.api.Event;

public class FurnaceEvent extends Event {

    @AllArgsConstructor
    public static class TakeResult extends FurnaceEvent {
        @Getter
        private final Player player;
        @Getter
        private final ItemStack itemStack;
    }

    @AllArgsConstructor
    public static class CalculateCookTime extends FurnaceEvent {
        @Getter
        private final AbstractFurnaceBlockEntity blockEntity;
        @Getter
        @Setter
        private int cookTime;
    }
}
