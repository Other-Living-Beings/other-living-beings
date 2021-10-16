package de.blutmondgilde.otherlivingbeings.registry;

import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.AbstractInventoryTab;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class InventoryTabRegistry {
    @Getter
    private static int amount = 1;
    private static final TreeMap<Integer, AbstractInventoryTab> registeredTabs = new TreeMap<>();

    public static void register(AbstractInventoryTab inventoryTab) {
        registeredTabs.put(amount++, inventoryTab);
    }

    public static Collection<AbstractInventoryTab> getValues() {
        return registeredTabs.values();
    }

    public static Map<Integer, AbstractInventoryTab> getIdTabMap() {
        return Map.copyOf(registeredTabs);
    }
}
