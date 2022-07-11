package ellemes.expandedstorage.misc;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface WrappedInventory extends WorldlyContainer {
    WorldlyContainer getInventory();

    @Override
    default int getContainerSize() {
        return getInventory().getContainerSize();
    }

    @Override
    default boolean isEmpty() {
        return getInventory().isEmpty();
    }

    default ItemStack getItem(int slot) {
        return getInventory().getItem(slot);
    }

    default ItemStack removeItem(int slot, int count) {
        return getInventory().removeItem(slot, count);
    }

    default ItemStack removeItemNoUpdate(int slot) {
        return getInventory().removeItemNoUpdate(slot);
    }

    default void setItem(int slot, ItemStack stack) {
        getInventory().setItem(slot, stack);
    }

    default int getMaxStackSize() {
        return getInventory().getMaxStackSize();
    }

    default void setChanged() {
        getInventory().setChanged();
    }

    default boolean stillValid(Player player) {
        return getInventory().stillValid(player);
    }

    default void startOpen(Player player) {
        getInventory().startOpen(player);
    }

    default void stopOpen(Player player) {
        getInventory().stopOpen(player);
    }

    default boolean canPlaceItem(int slot, ItemStack stack) {
        return getInventory().canPlaceItem(slot, stack);
    }

    default int[] getSlotsForFace(Direction face) {
        return getInventory().getSlotsForFace(face);
    }

    default boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction face) {
        return getInventory().canPlaceItemThroughFace(slot, stack, face);
    }

    default boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction face) {
        return getInventory().canTakeItemThroughFace(slot, stack, face);
    }
}
