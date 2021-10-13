package de.blutmondgilde.otherlivingbeings.config.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.clothconfig2.gui.entries.AbstractTextFieldListListEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmllegacy.common.registry.GameRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings({"UnstableApiUsage"})
public class BlockListWidget extends AbstractTextFieldListListEntry<String, BlockListWidget.BlockListWidgetEntry, BlockListWidget> {
    public BlockListWidget(Component fieldName, List<String> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<String>> saveConsumer, Supplier<List<String>> defaultValue, Component resetButtonKey) {
        this(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, false);
    }

    public BlockListWidget(Component fieldName, List<String> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<String>> saveConsumer, Supplier<List<String>> defaultValue, Component resetButtonKey, boolean requiresRestart) {
        this(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, requiresRestart, true, true);
    }

    public BlockListWidget(Component fieldName, List<String> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<String>> saveConsumer, Supplier<List<String>> defaultValue, Component resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront) {
        super(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, requiresRestart, deleteButtonEnabled, insertInFront, BlockListWidgetEntry::new);
    }

    @Override
    public BlockListWidget self() {
        return this;
    }

    public static class BlockListWidgetEntry extends AbstractTextFieldListCell<String, BlockListWidgetEntry, BlockListWidget> {

        public BlockListWidgetEntry(@Nullable String value, BlockListWidget listListEntry) {
            super(value, listListEntry);
        }

        @Override
        protected @Nullable String substituteDefault(@Nullable String value) {
            return value == null ? "" : value;
        }

        @Override
        protected boolean isValidText(@NotNull String s) {
            return true;
        }

        @Override
        public String getValue() {
            return this.widget.getValue();
        }

        @Override
        public Optional<Component> getError() {
            return Optional.empty();
        }

        @Override
        public void render(PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isSelected, delta);
            Optional<Item> optionalItem = GameRegistry.findRegistry(Item.class)
                    .getValues()
                    .stream()
                    .filter(item -> item instanceof BlockItem)
                    .filter(item -> item.getRegistryName().toString().equals(widget.getValue()))
                    .findFirst();
            optionalItem.ifPresent(item -> Minecraft.getInstance().getItemRenderer().renderGuiItem(new ItemStack(item), widget.x - 18, y + 1));
        }
    }
}
