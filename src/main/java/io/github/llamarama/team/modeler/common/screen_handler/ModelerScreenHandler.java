package io.github.llamarama.team.modeler.common.screen_handler;

import io.github.llamarama.team.modeler.Modeler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class ModelerScreenHandler extends ScreenHandler {

    private final Inventory inventory;

    public ModelerScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(1));
    }

    public ModelerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(Modeler.MODELER_HANDLER, syncId);
        ScreenHandler.checkSize(inventory, 1);
        this.inventory = inventory;

        inventory.onOpen(playerInventory.player);

        this.addSlot(new Slot(this.inventory, 0, 45, 30));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        Slot clickedSlot = this.slots.get(index);

        if (clickedSlot.hasStack() && clickedSlot.id == 0) {
            ItemStack slotStack = clickedSlot.getStack();

            if (!this.insertItem(slotStack, 1, 37, false)) {
                return ItemStack.EMPTY;
            }
        } else if (clickedSlot.hasStack()) {
            ItemStack playerSlotStack = clickedSlot.getStack();

            ItemStack slotStack = this.slots.get(0).getStack();

            if (slotStack.isEmpty()) {
                this.slots.get(0).setStack(playerSlotStack.copy());
            } else {
                if (ItemStack.canCombine(slotStack, playerSlotStack)) {
                    short newCount = (short) (slotStack.getCount() + playerSlotStack.getCount());

                    if (newCount > slotStack.getMaxCount()) {
                        slotStack.setCount(slotStack.getMaxCount());
                        playerSlotStack.setCount(newCount - slotStack.getMaxCount());

                    } else {
                        slotStack.setCount(newCount);
                        playerSlotStack.setCount(newCount - slotStack.getCount());

                    }
                }
                return ItemStack.EMPTY;
            }

            clickedSlot.setStack(ItemStack.EMPTY);
            return ItemStack.EMPTY;
        }

        return ItemStack.EMPTY;
    }

    public void setCustomModelData(int customModelDataValue) {
        ItemStack stack = this.slots.get(0).getStack();
        stack.getOrCreateNbt().putInt("CustomModelData", customModelDataValue);
    }

}
