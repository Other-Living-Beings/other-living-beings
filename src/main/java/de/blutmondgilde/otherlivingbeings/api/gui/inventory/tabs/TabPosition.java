package de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs;

import com.mojang.blaze3d.systems.RenderSystem;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import lombok.AllArgsConstructor;
import net.minecraft.resources.ResourceLocation;

@AllArgsConstructor
public enum TabPosition {
    LEFT_TOP(new ResourceLocation(OtherLivingBeings.MOD_ID, "textures/gui/tabs/top-left.png")),
    TOP(new ResourceLocation(OtherLivingBeings.MOD_ID, "textures/gui/tabs/top.png")),
    RIGHT_TOP(new ResourceLocation(OtherLivingBeings.MOD_ID, "textures/gui/tabs/top-right.png")),
    LEFT_BOT(new ResourceLocation(OtherLivingBeings.MOD_ID, "textures/gui/tabs/bot-left.png")),
    BOT(new ResourceLocation(OtherLivingBeings.MOD_ID, "textures/gui/tabs/bot.png")),
    RIGHT_BOT(new ResourceLocation(OtherLivingBeings.MOD_ID, "textures/gui/tabs/bot-right.png"));

    private final ResourceLocation tabLocation;

    public void bindTexture() {
        RenderSystem.setShaderTexture(0, this.tabLocation);
    }
}
