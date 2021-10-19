package de.blutmondgilde.otherlivingbeings.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import de.blutmondgilde.otherlivingbeings.api.skill.ISkill;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class SkillWidget implements GuiEventListener, Widget, NarratableEntry {
    private static final ResourceLocation BAR_LOCATION = new ResourceLocation(OtherLivingBeings.MOD_ID, "textures/gui/bar.png");
    private final ISkill skill;
    private final MutableComponent skillName;
    private final int color;
    private final Color barColor = new Color(52, 255, 64);
    @Setter
    @Getter
    private int x, y, width, height;
    private final Font font;
    @Setter
    @Getter
    private boolean isVisible = true;
    private int scissorX = 0, scissorWidth = 9000, scissorY = 0, scissorHeight = 9000;

    public SkillWidget(ISkill skill, int x, int y) {
        this.skill = skill;
        this.skillName = skill.getDisplayName();
        this.font = Minecraft.getInstance().font;
        this.color = skillName.getStyle().getColor() != null ? skillName.getStyle().getColor().getValue() : new Color(255, 254, 254).getRGB();
        this.x = x;
        this.y = y;
        this.width = 100;
        this.height = 20;
    }

    @Override
    public void render(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        if (!isVisible()) return;
        RenderSystem.enableScissor(scissorX, scissorY, scissorWidth, scissorHeight);
        font.draw(poseStack, this.skillName.append(" " + skill.getLevel()), this.x, this.y, this.color);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, BAR_LOCATION);

        poseStack.scale(0.75F, 1F, 1F);
        GuiComponent.blit(poseStack, Math.round(this.x / 0.75F), this.y + this.font.lineHeight, 0F, 0F, 182, 5, 182, 10);
        poseStack.scale(1F / 0.75F, 1F, 1F);


        RenderSystem.setShaderColor(1F / 255 * barColor.getRed(), 1F / 255 * barColor.getGreen(), 1F / 255 * barColor.getBlue(), 1F);
        poseStack.scale(0.75F, 1F, 1F);
        double widthModifier = 1.0 / this.skill.nextLevelAt() * this.skill.getExp();
        long percentWidth = Math.round(182 * widthModifier);
        int width = Math.toIntExact(percentWidth);
        GuiComponent.blit(poseStack, Math.round(this.x / 0.75F), this.y + this.font.lineHeight, 0F, 5F, width, 5, 182, 10);
        poseStack.scale(1F / 0.75F, 1F, 1F);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.disableScissor();
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.HOVERED;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.POSITION, this.skillName);
    }

    public void setScissor(int x, int y, int width, int height) {
        scissorX = x;
        scissorY = y;
        scissorWidth = width;
        scissorHeight = height;
    }
}
