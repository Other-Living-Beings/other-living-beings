package de.blutmondgilde.otherlivingbeings.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class GroupMemberWidget implements Widget {
    private static final ResourceLocation BAR_LOCATION = new ResourceLocation(OtherLivingBeings.MOD_ID, "textures/gui/bar.png");
    private final Component displayName;
    private final double health, maxHealth;
    private final Minecraft minecraft;
    private final Font font;
    @Getter
    @Setter
    private int x, y, width, height;
    private final TextColor color;

    public GroupMemberWidget(Component displayName, double health, double maxHealth) {
        this.displayName = displayName;
        this.health = Math.ceil(health);
        this.maxHealth = Math.ceil(maxHealth);
        this.minecraft = Minecraft.getInstance();
        this.font = minecraft.font;
        this.x = 0;
        this.y = 0;
        this.width = 91;
        this.height = font.lineHeight + 5 + 2;
        this.color = displayName.getStyle().getColor() != null ? displayName.getStyle().getColor() : TextColor.fromRgb(new Color(255, 255, 255).getRGB());
    }


    @Override
    public void render(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        //Render display name
        font.draw(poseStack, displayName, this.x + 1, this.y, color.getValue());
        renderHpBarBackground(poseStack);
        renderHpBar(poseStack);
    }

    private void renderHpBarBackground(PoseStack poseStack) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, BAR_LOCATION);


        float scale = 0.5F;
        poseStack.scale(scale, 1F, 1F);
        GuiComponent.blit(poseStack, Math.round(this.x / scale), this.y + this.font.lineHeight, 0F, 0F, 182, 5, 182, 10);
        poseStack.scale(1F / scale, 1F, 1F);
    }

    private void renderHpBar(PoseStack poseStack) {
        double healthPercentage = 1.0 / this.maxHealth * this.health;
        Color barColor = getBarColor(healthPercentage);

        RenderSystem.setShaderColor(1F / 255 * barColor.getRed(), 1F / 255 * barColor.getGreen(), 1F / 255 * barColor.getBlue(), 1F);
        float scale = 0.5F;
        poseStack.scale(scale, 1F, 1F);
        int width = Math.toIntExact(Math.round(182 * healthPercentage));
        GuiComponent.blit(poseStack, Math.round(this.x / scale), this.y + this.font.lineHeight, 0F, 5F, width, 5, 182, 10);
        poseStack.scale(1F / scale, 1F, 1F);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
    }

    private Color getBarColor(double healthPercentage) {
        int red = (int) Math.round(255 * (1 - healthPercentage));
        int green = (int) Math.round(255 * healthPercentage);
        return new Color(red, green, 0);
    }
}
