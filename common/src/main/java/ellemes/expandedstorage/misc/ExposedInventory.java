package ellemes.expandedstorage.misc;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ExposedInventory extends Container {
    NonNullList<ItemStack> getItems();

    default void loadInventoryFromTag(CompoundTag tag) {
        ContainerHelper.loadAllItems(tag, getItems());
    }

    default void saveInventoryToTag(CompoundTag tag) {
        ContainerHelper.saveAllItems(tag, getItems());
    }

    @Override
    default int getContainerSize() {
        return getItems().size();
    }

    @Override
    default boolean isEmpty() {
        for (ItemStack stack : getItems()) {
            if (stack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    @Override
    default ItemStack getItem(int slot) {
        return getItems().get(slot);
    }

    @Override
    default ItemStack removeItem(int slot, int amount) {
        ItemStack stack = ContainerHelper.removeItem(getItems(), slot, amount);
        if (!stack.isEmpty()) this.setChanged();
        return stack;
    }

    @Override
    default ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(getItems(), slot);
    }

    @Override
    default void setItem(int slot, ItemStack stack) {
        if (stack.getCount() > this.getMaxStackSize()) stack.setCount(this.getMaxStackSize());
        getItems().set(slot, stack);
        this.setChanged();
    }

    // todo: can we provide a default impl?
    //  e.g. isOwnerValid() && player.distanceToSqr(getCenterPos()) <= 36.0D;?
    @Override
    boolean stillValid(Player player);

    @Override
    default void clearContent() {
        getItems().clear();
    }
}
