package de.blutmondgilde.otherlivingbeings.client.gui.widget;

import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.DefaultTabContainerScreen;
import de.blutmondgilde.otherlivingbeings.container.SkillContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SkillContainerScreen extends DefaultTabContainerScreen<SkillContainer> {
    public SkillContainerScreen(SkillContainer menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }
}
