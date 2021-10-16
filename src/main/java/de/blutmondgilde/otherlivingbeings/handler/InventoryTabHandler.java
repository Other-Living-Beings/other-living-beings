package de.blutmondgilde.otherlivingbeings.handler;

import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.AbstractInventoryTab;
import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.DefaultTabContainerScreen;
import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.InventoryTabSwitcherWidget;
import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.TabPosition;
import de.blutmondgilde.otherlivingbeings.registry.InventoryTabRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.Map;

public class InventoryTabHandler {
    public static void init(IEventBus forgeBus) {
        forgeBus.addListener(InventoryTabHandler::onPlayerInventory);
    }

    public static void onPlayerInventory(GuiScreenEvent.InitGuiEvent e) {
        if (!isTabScreen(e.getGui())) return;
        AbstractContainerScreen screen = (AbstractContainerScreen) e.getGui();

        final Map<Integer, AbstractInventoryTab> tabRegistryValues = InventoryTabRegistry.getIdTabMap();
        InventoryTabSwitcherWidget tabSwitcher = new InventoryTabSwitcherWidget(screen, (int) Math.round(Math.ceil(tabRegistryValues.size() / 12F)));

        for (Map.Entry<Integer, AbstractInventoryTab> entry : tabRegistryValues.entrySet()) {
            AbstractInventoryTab widget = entry.getValue();

            int posIndex = entry.getKey();
            while (posIndex > 12) {
                posIndex -= 12;
            }

            int yOffset = 4;
            int xOffset = 1;

            switch (posIndex) {
                case 1 -> {
                    widget.x = screen.getGuiLeft();
                    widget.y = screen.getGuiTop() - widget.getHeight() + yOffset;
                    widget.setPosition(TabPosition.LEFT_TOP);
                }
                case 2, 3, 4, 5 -> {
                    widget.x = screen.getGuiLeft() + widget.getWidth() * (posIndex - 1) + (xOffset * posIndex - 1);
                    widget.y = screen.getGuiTop() - widget.getHeight() + yOffset;
                    widget.setPosition(TabPosition.TOP);
                }
                case 6 -> {
                    widget.x = screen.getGuiLeft() + widget.getWidth() * (posIndex - 1) + (xOffset * posIndex - 1);
                    widget.y = screen.getGuiTop() - widget.getHeight() + yOffset;
                    widget.setPosition(TabPosition.RIGHT_TOP);
                }
                case 7 -> {
                    widget.x = screen.getGuiLeft();
                    widget.y = screen.getGuiTop() + screen.imageWidth - yOffset;
                    widget.setPosition(TabPosition.LEFT_BOT);
                }
                case 8, 9, 10, 11 -> {
                    widget.x = screen.getGuiLeft() + widget.getWidth() * (posIndex - 7) + (xOffset * posIndex - 1);
                    widget.y = screen.getGuiTop() + screen.imageWidth - yOffset;
                    widget.setPosition(TabPosition.BOT);
                }
                case 12 -> {
                    widget.x = screen.getGuiLeft() + widget.getWidth() * (posIndex - 7) + (xOffset * posIndex - 1);
                    widget.y = screen.getGuiTop() + screen.imageWidth - yOffset;
                    widget.setPosition(TabPosition.RIGHT_BOT);
                }
            }

            tabSwitcher.addUpdateListener(widget);
        }

        tabSwitcher.updateTabs();
        e.addWidget(tabSwitcher);
    }

    private static boolean isTabScreen(Screen screen) {
        if (screen instanceof InventoryScreen) return true;
        if (screen instanceof DefaultTabContainerScreen) return true;
        return false;
    }
}
