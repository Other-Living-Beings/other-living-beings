package de.blutmondgilde.otherlivingbeings.handler;

import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.AbstractInventoryTab;
import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.DefaultTabContainerScreen;
import de.blutmondgilde.otherlivingbeings.client.gui.widget.InventoryTabSwitcherWidget;
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

            tabSwitcher.addUpdateListener(entry.getKey(), widget);
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
