package de.blutmondgilde.otherlivingbeings.container;

import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.DefaultTabContainer;
import de.blutmondgilde.otherlivingbeings.registry.OtherLivingBeingsContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;

public class SkillContainer extends DefaultTabContainer {
    public SkillContainer(int windowId, Player player, Container inv) {
        super(OtherLivingBeingsContainer.SKILL_CONTAINER, windowId, player, inv);
    }
}
