package de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs;

import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Predicate;

public interface IInventoryTab {
    void sendOpenContainerPacket();

    @OnlyIn(Dist.CLIENT)
    Predicate<Screen> isCurrentScreen();
}
