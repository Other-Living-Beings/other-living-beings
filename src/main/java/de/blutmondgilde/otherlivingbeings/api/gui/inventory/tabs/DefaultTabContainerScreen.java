package de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

public class DefaultTabContainerScreen<T extends DefaultTabContainer> extends EffectRenderingInventoryScreen<T> {
    public static final ResourceLocation INVENTORY_LOCATION = new ResourceLocation(OtherLivingBeings.MOD_ID, "textures/gui/inventory.png");

    public DefaultTabContainerScreen(T menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float pPartialTicks, int pMouseX, int pMouseY) {
        fill(poseStack, 0, 0, this.width, this.height, new Color(14, 15, 17, 204).getRGB());
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, INVENTORY_LOCATION);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}
