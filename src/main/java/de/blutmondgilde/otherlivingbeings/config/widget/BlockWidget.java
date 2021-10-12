package de.blutmondgilde.otherlivingbeings.config.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmllegacy.common.registry.GameRegistry;

import java.util.List;
import java.util.Optional;

public class BlockWidget extends AbstractConfigListEntry<String> {
    private final StringListEntry parent;

    public BlockWidget(StringListEntry parent) {
        super(new TextComponent(""), false);
        this.parent = parent;
        this.parent.setParent(getParent());
    }

    @Override
    public void render(PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        parent.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        Optional<Item> optionalItem = GameRegistry.findRegistry(Item.class)
                .getValues()
                .stream()
                .filter(item -> item instanceof BlockItem)
                .filter(item -> item.getRegistryName().toString().equals(parent.getValue()))
                .findFirst();
        optionalItem.ifPresent(item -> Minecraft.getInstance().getItemRenderer().renderGuiItem(new ItemStack(item), x + entryWidth - 148 - 18, y));
    }

    @Override
    public String getValue() {
        return parent.getValue();
    }

    @Override
    public Optional<String> getDefaultValue() {
        return parent.getDefaultValue();
    }

    @Override
    public void save() {
        parent.save();
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return parent.narratables();
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return parent.children();
    }

    @Override
    public boolean isEditable() {
        return parent.isEditable();
    }
}
