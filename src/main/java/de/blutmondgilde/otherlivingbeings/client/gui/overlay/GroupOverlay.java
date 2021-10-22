package de.blutmondgilde.otherlivingbeings.client.gui.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import de.blutmondgilde.otherlivingbeings.client.ClientGroupHolder;
import de.blutmondgilde.otherlivingbeings.client.gui.widget.GroupMemberWidget;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.fmlclient.gui.GuiUtils;

import java.awt.*;
import java.util.List;

public class GroupOverlay implements IIngameOverlay {
    private static final Color backgroundColor = new Color(19, 19, 19, 190);

    public GroupOverlay() {

    }

    @Override
    public void render(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height) {
        if (!ClientGroupHolder.isInGroup()) return;

        List<GroupMemberWidget> widgets = ClientGroupHolder.getMemberData()
                .stream()
                .map(groupMemberData -> new GroupMemberWidget(groupMemberData.getDisplayName(), groupMemberData.getHealth(), groupMemberData.getMaxHealth()))
                .toList();
        if (widgets.isEmpty()) return;
        renderBackground(mStack, width, height, widgets.get(0).getWidth(), widgets.stream().map(GroupMemberWidget::getHeight).reduce(1, Integer::sum));

        int i = 0;
        for (GroupMemberWidget widget : widgets) {
            widget.setX(width - widget.getWidth() - 2);
            widget.setY(height / 3 + widget.getHeight() * i++);
            widget.render(mStack, 0, 0, partialTicks);
        }
    }

    private void renderBackground(PoseStack stack, int screenWidth, int screenHeight, int width, int height) {
        GuiUtils.drawGradientRect(stack.last().pose(), 0, screenWidth - width - 4, screenHeight / 3 - 4, screenWidth, screenHeight / 3 + height, backgroundColor.getRGB(), backgroundColor.getRGB());
    }
}
