package de.blutmondgilde.otherlivingbeings.config.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmllegacy.common.registry.GameRegistry;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockTextField extends StringListEntry {
    @SuppressWarnings({"deprecation", "UnstableApiUsage"})
    public BlockTextField(Component fieldName, String value, Component resetButtonKey, Supplier<String> defaultValue, Consumer<String> saveConsumer, Supplier<Optional<Component[]>> tooltipSupplier, boolean requiresRestart) {
        super(fieldName, value, resetButtonKey, defaultValue, saveConsumer, tooltipSupplier, requiresRestart);
    }

    @Override
    public void render(PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        Optional<Item> optionalItem = GameRegistry.findRegistry(Item.class)
                .getValues()
                .stream()
                .filter(item -> item instanceof BlockItem)
                .filter(item -> item.getRegistryName().toString().equals(textFieldWidget.getValue()))
                .findFirst();
        optionalItem.ifPresent(item -> Minecraft.getInstance().getItemRenderer().renderGuiItem(new ItemStack(item), x + entryWidth - 148 - 18, y));
    }
}
