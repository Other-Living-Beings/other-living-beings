package de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.TextComponent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class InventoryTabSwitcherWidget extends GuiComponent implements Widget, GuiEventListener {
    private int page = 1;
    private final int maxPages;
    private final Button prevButton, nextButton;
    private final List<AbstractInventoryTab> tabs = new ArrayList<>();

    public InventoryTabSwitcherWidget(AbstractContainerScreen parent, int maxPages) {
        this.prevButton = new Button(0, 0, 20, 20, new TextComponent("<"), b -> page = Math.max(page - 1, 0));
        this.nextButton = new Button(parent.getGuiLeft() + parent.imageWidth - 20, parent.getGuiTop() - 50, 20, 20, new TextComponent(">"), b -> page = Math.min(page + 1, maxPages));
        this.maxPages = maxPages;
    }


    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (maxPages > 1) {
            this.prevButton.render(poseStack, mouseX, mouseY, partialTicks);
            this.nextButton.render(poseStack, mouseX, mouseY, partialTicks);
        }

        this.tabs.stream().filter(AbstractInventoryTab::isActive).forEach(abstractInventoryTab -> abstractInventoryTab.render(poseStack, mouseX, mouseY, partialTicks));
    }

    public void updateTabs() {
        for (int i = 0; i < this.tabs.size(); i++) {
            AbstractInventoryTab currentTab = tabs.get(i);
            currentTab.setActive(Math.ceil((i + 1) / 12F) == this.page);
            currentTab.setVisible(currentTab.isActive());
        }
    }

    public void addUpdateListener(AbstractInventoryTab widget) {
        tabs.add(widget);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            this.tabs.stream()
                    .filter(abstractInventoryTab -> abstractInventoryTab.isMouseOver(pMouseX, pMouseY))
                    .forEach(abstractInventoryTab -> abstractInventoryTab.mouseClicked(pMouseX, pMouseY, pButton));
            if (this.prevButton.isMouseOver(pMouseX, pMouseY)) this.prevButton.mouseClicked(pMouseX, pMouseY, pButton);
            if (this.nextButton.isMouseOver(pMouseX, pMouseY)) this.nextButton.mouseClicked(pMouseX, pMouseY, pButton);

            return true;
        } else {
            return false;
        }
    }
}
