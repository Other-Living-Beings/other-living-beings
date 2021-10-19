package de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.fmlclient.gui.GuiUtils;

import java.util.List;

public abstract class AbstractInventoryTab extends Button implements IInventoryTab {
    private static final int TAB_WIDTH = 28;
    private static final int TAB_HEIGHT = 32;
    @Setter
    @Getter
    private boolean isVisible = false;
    @Setter
    private TabPosition position;

    public AbstractInventoryTab() {
        this((pButton, pPoseStack, pMouseX, pMouseY) -> {
            Screen currentScreen = Minecraft.getInstance().screen;
            AbstractInventoryTab button = (AbstractInventoryTab) pButton;
            //noinspection removal
            GuiUtils.drawHoveringText(pPoseStack, button.getToolTip(), pMouseX, pMouseY, currentScreen.width, currentScreen.height, 150, Minecraft.getInstance().font);
        });
    }

    public AbstractInventoryTab(OnTooltip pOnTooltip) {
        super(0, 0, TAB_WIDTH, TAB_HEIGHT, new TextComponent(""), button -> {
            AbstractInventoryTab tab = (AbstractInventoryTab) button;
            tab.sendOpenContainerPacket();
        }, pOnTooltip);
    }

    @Override
    public void renderButton(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        if (!isVisible) return;

        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.renderBg(pMatrixStack, minecraft, pMouseX, pMouseY);

        this.renderIcon(pMatrixStack, pMouseX, pMouseY, pPartialTicks);

        if (this.isHovered()) {
            this.renderToolTip(pMatrixStack, pMouseX, pMouseY);
        }
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, Minecraft pMinecraft, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        position.bindTexture();
        float yOffset = this.isActive() ? TAB_HEIGHT : 0F;
        blit(pPoseStack, this.x, this.y, TAB_WIDTH, TAB_HEIGHT, 0F, yOffset, TAB_WIDTH, TAB_HEIGHT - 1, TAB_WIDTH, TAB_HEIGHT * 2);
    }

    public boolean isActive() {
        return isCurrentScreen().test(Minecraft.getInstance().screen);
    }

    protected abstract void renderIcon(PoseStack poseStack, int mouseX, int mouseY, float partialTicks);

    public abstract List<? extends FormattedText> getToolTip();
}
