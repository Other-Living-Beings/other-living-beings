package de.blutmondgilde.otherlivingbeings.client.gui;

import de.blutmondgilde.otherlivingbeings.api.capability.OtherLivingBeingsCapability;
import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.DefaultTabContainerScreen;
import de.blutmondgilde.otherlivingbeings.capability.skill.PlayerSkillsImpl;
import de.blutmondgilde.otherlivingbeings.client.gui.widget.SkillWidget;
import de.blutmondgilde.otherlivingbeings.container.SkillContainer;
import de.blutmondgilde.otherlivingbeings.util.TickCondition;
import de.blutmondgilde.otherlivingbeings.util.TickCountDown;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SkillContainerScreen extends DefaultTabContainerScreen<SkillContainer> {
    private List<SkillWidget> skillWidgets;
    private final TickCountDown tickCountDown;

    public SkillContainerScreen(SkillContainer menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.tickCountDown = new TickCountDown();
        this.tickCountDown.addListener(TickCondition.SKIP_TENTH_TICK, this::update);

        AtomicInteger currentX = new AtomicInteger(this.getGuiLeft() + 6);
        AtomicInteger currentY = new AtomicInteger(this.getGuiTop() + 6 + Minecraft.getInstance().font.lineHeight);
        this.skillWidgets = menu.getPlayer().getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).orElse(new PlayerSkillsImpl()).getSkills()
                .stream().map(iSkill -> {

                    if (currentY.get() + 20 + 2 > getYSize()) {
                        currentY.set(this.getGuiTop() + 2 + Minecraft.getInstance().font.lineHeight);
                        currentX.addAndGet(100);
                    }

                    int y = currentY.getAndAdd(20 + 2);
                    return new SkillWidget(iSkill, currentX.get(), y);
                })
                .collect(Collectors.toList());

        this.skillWidgets.forEach(this::addRenderableWidget);
    }

    public void update() {
        this.skillWidgets.forEach(this::removeWidget);
        AtomicInteger currentX = new AtomicInteger(this.getGuiLeft() + 6);
        AtomicInteger currentY = new AtomicInteger(this.getGuiTop() + 6 + Minecraft.getInstance().font.lineHeight);
        this.skillWidgets = getMenu().getPlayer().getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).orElse(new PlayerSkillsImpl()).getSkills()
                .stream().map(iSkill -> {

                    if (currentY.get() + 20 + 2 > getYSize()) {
                        currentY.set(this.getGuiTop() + 2 + Minecraft.getInstance().font.lineHeight);
                        currentX.addAndGet(100);
                    }

                    int y = currentY.getAndAdd(20 + 2);
                    return new SkillWidget(iSkill, currentX.get(), y);
                })
                .collect(Collectors.toList());

        this.skillWidgets.forEach(this::addRenderableWidget);
    }

    @Override
    protected void containerTick() {
        this.tickCountDown.tick();
    }
}
