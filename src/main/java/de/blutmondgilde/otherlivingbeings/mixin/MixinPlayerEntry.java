package de.blutmondgilde.otherlivingbeings.mixin;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.social.PlayerEntry;
import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Mixin(PlayerEntry.class)
public abstract class MixinPlayerEntry {
    @Shadow
    @Final
    private List<AbstractWidget> children;
    @Shadow
    @Nullable
    private Button hideButton;

    @Shadow
    public abstract String getPlayerName();

    @Shadow
    float tooltipHoverTime;
    private static final ResourceLocation groupButtonTextures = new ResourceLocation(OtherLivingBeings.MOD_ID, "textures/gui/group_buttons.png");
    private static final Component emptyText = new TextComponent("");
    private List<FormattedCharSequence> inviteButtonToolTip;

    private SocialInteractionsScreen parent;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void constructorEnd(Minecraft minecraft, SocialInteractionsScreen socialInteractionsScreen, UUID p_100554_, String playerName, Supplier p_100556_, CallbackInfo ci) {
        parent = socialInteractionsScreen;
        inviteButtonToolTip = minecraft.font.split(new TranslatableComponent("otherlivingbeings.gui.socialInteractions.tooltip.invite", playerName), 150);
    }

    private final ImageButton inviteButton = new ImageButton(0, 0, 20, 20, 0, 0, 20, groupButtonTextures, 256, 256, pButton -> {
        Minecraft.getInstance().player.chat("/group invite " + getPlayerName());
    }, (pButton, stack, mouseX, mouseY) -> {
        tooltipHoverTime += Minecraft.getInstance().getDeltaFrameTime();
        if (tooltipHoverTime >= 10.0F) {
            parent.setPostRenderRunnable(() -> PlayerEntry.postRenderTooltip(parent, stack, inviteButtonToolTip, mouseX, mouseY));
        }
    }, emptyText);

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIIIIZF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button;render" +
        "(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V", ordinal = 1))
    public void onRender(PoseStack pPoseStack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTick, CallbackInfo ci) {
        inviteButton.x = hideButton.x - inviteButton.getWidth() - 4;
        inviteButton.y = hideButton.y;
        inviteButton.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Inject(method = "children", at = @At("RETURN"), cancellable = true)
    public void getChildren(CallbackInfoReturnable<List<? extends GuiEventListener>> cir) {
        if (!cir.getReturnValue().isEmpty()) {
            List<AbstractWidget> children = Lists.newArrayList(this.children);
            children.add(inviteButton);
            cir.setReturnValue(children);
        }
    }

    @Inject(method = "narratables", at = @At("RETURN"), cancellable = true)
    public void getNarratables(CallbackInfoReturnable<List<? extends NarratableEntry>> cir) {
        if (!cir.getReturnValue().isEmpty()) {
            List<AbstractWidget> narratableEntries = Lists.newArrayList(this.children);
            narratableEntries.add(inviteButton);
            cir.setReturnValue(narratableEntries);
        }
    }
}
