package de.blutmondgilde.otherlivingbeings.client.gui.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import de.blutmondgilde.otherlivingbeings.client.ClientGroupHolder;
import de.blutmondgilde.otherlivingbeings.client.gui.widget.GroupMemberWidget;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class GroupOverlay implements IIngameOverlay {

    public GroupOverlay() {

    }

    @Override
    public void render(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height) {
        if (!ClientGroupHolder.isInGroup()) return;
        //TODO render background

        int i = 0;
        for (GroupMemberWidget widget : ClientGroupHolder.getMemberData()
                .stream()
                .map(groupMemberData -> new GroupMemberWidget(groupMemberData.getDisplayName(), groupMemberData.getHealth(), groupMemberData.getMaxHealth()))
                .toList()) {

            //TODO set position
            widget.setX(width - widget.getWidth() - 2);
            widget.setY(height / 3 + widget.getHeight() * i++);
            widget.render(mStack, 0, 0, partialTicks);
        }
    }
}
