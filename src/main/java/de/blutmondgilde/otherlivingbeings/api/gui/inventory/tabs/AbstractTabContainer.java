package de.blutmondgilde.otherlivingbeings.api.gui.inventory.tabs;

import de.blutmondgilde.otherlivingbeings.registry.OtherLivingBeingsContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

public class AbstractTabContainer extends AbstractContainerMenu {
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    public static final int PLAYER_INVENTORY_YPOS = 84;
    private static final int HOTBAR_YPOS = PLAYER_INVENTORY_YPOS + 64 - 6;
    private final Player player;
    private final IItemHandler playerInventory;

    public AbstractTabContainer(int windowId, Player player, Container inv) {
        super(OtherLivingBeingsContainer.BASIC_TAB_CONTAINER, windowId);
        this.player = player;
        this.playerInventory = new InvWrapper(inv);

        addInventorySlots();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.player == player;
    }

    private void addInventorySlots() {
        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;
        final int HOTBAR_XPOS = 8;

        // Add the players hotbar to the gui - the [xpos, ypos] location of each item
        for (int slotNumber = 0; slotNumber < HOTBAR_SLOT_COUNT; slotNumber++) {
            addSlot(new SlotItemHandler(this.playerInventory, slotNumber, HOTBAR_XPOS + SLOT_X_SPACING * slotNumber, HOTBAR_YPOS));
        }

        final int PLAYER_INVENTORY_XPOS = 8;
        // Add the rest of the player's inventory to the gui
        for (int row = 0; row < PLAYER_INVENTORY_ROW_COUNT; row++) {
            for (int column = 0; column < PLAYER_INVENTORY_COLUMN_COUNT; column++) {
                int slotNumber = HOTBAR_SLOT_COUNT + row * PLAYER_INVENTORY_COLUMN_COUNT + column;
                int xpos = PLAYER_INVENTORY_XPOS + column * SLOT_X_SPACING;
                int ypos = PLAYER_INVENTORY_YPOS + row * SLOT_Y_SPACING;
                addSlot(new SlotItemHandler(this.playerInventory, slotNumber, xpos, ypos));
            }
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (slotIndex == VANILLA_FIRST_SLOT_INDEX) {
                if (!this.moveItemStackTo(itemstack1, 9, VANILLA_SLOT_COUNT, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (slotIndex >= 1 && slotIndex < 5) {
                if (!this.moveItemStackTo(itemstack1, 9, VANILLA_SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= 5 && slotIndex < 9) {
                if (!this.moveItemStackTo(itemstack1, 9, VANILLA_SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= 9 && slotIndex < VANILLA_SLOT_COUNT) {
                if (!this.moveItemStackTo(itemstack1, 36, VANILLA_SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 9, VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
            if (slotIndex == 0) {
                player.drop(itemstack1, false);
            }
        }

        return itemstack;
    }
}
