package de.blutmondgilde.otherlivingbeings.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs.AbstractInventoryTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.TextComponent;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryTabSwitcherWidget extends GuiComponent implements Widget, GuiEventListener {
    private int page = 0;
    private final int maxPages;
    private final Button prevButton, nextButton;
    private final List<AbstractInventoryTab> tabs = new ArrayList<>();

    public InventoryTabSwitcherWidget(AbstractContainerScreen parent, int maxPages) {
        this.maxPages = maxPages;

        this.prevButton = new Button(0, 0, 20, 20, new TextComponent("<"), b -> {
            page = Math.max(page - 1, 0);
            updateTabs();
        });

        this.nextButton = new Button(parent.getGuiLeft() + parent.imageWidth - 20, parent.getGuiTop() - 50, 20, 20, new TextComponent(">"), b -> {
            page = Math.min(page + 1, this.maxPages);
            updateTabs();
        });
    }


    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (maxPages > 1) {
            this.prevButton.render(poseStack, mouseX, mouseY, partialTicks);
            this.nextButton.render(poseStack, mouseX, mouseY, partialTicks);
            drawString(poseStack, Minecraft.getInstance().font, this.page + " / " + this.maxPages, 0, 20,
                    new Color(255, 255, 255).getRGB());
        }

        this.tabs.stream()
                .filter(AbstractInventoryTab::isVisible)
                .forEach(abstractInventoryTab -> abstractInventoryTab.render(poseStack, mouseX, mouseY, partialTicks));
    }

    public void updateTabs() {
        for (int i = 0; i < this.tabs.size(); i++) {
            AbstractInventoryTab currentTab = tabs.get(this.tabs.size() - 1 - i);
            currentTab.setVisible(Math.ceil(i / 12F) == this.page);
        }
    }

    public void addUpdateListener(AbstractInventoryTab widget) {
        tabs.add(widget);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            this.tabs.stream()
                    .filter(AbstractInventoryTab::isVisible)
                    .forEach(abstractInventoryTab -> abstractInventoryTab.mouseClicked(pMouseX, pMouseY, pButton));
            if (this.prevButton.isMouseOver(pMouseX, pMouseY)) this.prevButton.mouseClicked(pMouseX, pMouseY, pButton);
            if (this.nextButton.isMouseOver(pMouseX, pMouseY)) this.nextButton.mouseClicked(pMouseX, pMouseY, pButton);

            return true;
        } else {
            return false;
        }
    }
}
