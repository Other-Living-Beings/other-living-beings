package de.blutmondgilde.otherlivingbeings.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SkillContainerScreen extends DefaultTabContainerScreen<SkillContainer> {
    private List<SkillWidget> skillWidgets;
    private final TickCountDown tickCountDown;
    private int yOffset = 0;

    public SkillContainerScreen(SkillContainer menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.tickCountDown = new TickCountDown();
        this.tickCountDown.addListener(TickCondition.SKIP_TENTH_TICK, this::update);

        this.skillWidgets = new ArrayList<>();
        update();
    }

    @Override
    public void render(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
        int y = this.getGuiTop() + 6 + Minecraft.getInstance().font.lineHeight;

        for (SkillWidget widget : skillWidgets) {
            //Set Scissor frame
            double scale = getMinecraft().getWindow().getGuiScale();
            int scissorStartX = (int) Math.round(getGuiLeft() * scale);
            int scissorStartY = (int) Math.round((this.height - (getGuiTop() + imageHeight - 95)) * scale);
            widget.setScissor(scissorStartX, scissorStartY, (int) Math.round(imageWidth * scale), (int) Math.round(56 * scale));

            //adjust position to scroll
            widget.setY(y + yOffset);
            y += widget.getHeight() + 2;
            //check is visible
            widget.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);

        }
    }

    public void update() {
        this.skillWidgets.forEach(this::removeWidget);
        this.skillWidgets = getMenu().getPlayer().getCapability(OtherLivingBeingsCapability.PLAYER_SKILLS).orElse(new PlayerSkillsImpl()).getSkills()
                .stream().map(iSkill -> new SkillWidget(iSkill, this.getGuiLeft() + 20, 0))
                .collect(Collectors.toList());

        this.skillWidgets.forEach(this::addWidget);
    }

    @Override
    protected void containerTick() {
        this.tickCountDown.tick();
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        this.yOffset += pDelta * 2;
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        OtherLivingBeings.getLogger().info("MouseX: {}, MouseY: {}", pMouseY, pMouseY);
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
}
