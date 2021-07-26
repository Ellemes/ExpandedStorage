package ninjaphenix.expandedstorage.base.internal_api.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public final class CombinedIItemHandlerModifiable implements IItemHandlerModifiable {
    private final IItemHandlerModifiable first;
    private final IItemHandlerModifiable second;
    private final int firstSize;
    private final int totalSize;

    public CombinedIItemHandlerModifiable(IItemHandlerModifiable first, IItemHandlerModifiable second) {
        this.first = first;
        this.second = second;
        this.firstSize = first.getSlots();
        this.totalSize = firstSize + second.getSlots();
    }

    @Override
    public int getSlots() {
        return totalSize;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot >= firstSize) {
            return second.getStackInSlot(slot - firstSize);
        }
        return first.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (slot >= firstSize) {
            return second.insertItem(slot - firstSize, stack, simulate);
        }
        return first.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot >= firstSize) {
            return second.extractItem(slot - firstSize, amount, simulate);
        }
        return first.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        if (slot >= firstSize) {
            return second.getSlotLimit(slot - firstSize);
        }
        return first.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot >= firstSize) {
            return second.isItemValid(slot - firstSize, stack);
        }
        return first.isItemValid(slot, stack);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (slot >= firstSize) {
            second.setStackInSlot(slot - firstSize, stack);
        } else {
            first.setStackInSlot(slot, stack);
        }
    }
}
