package ellemes.expandedstorage.misc;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Predicate;

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

    @Override
    default ItemStack getItem(int slot) {
        return getInventory().getItem(slot);
    }

    @Override
    default ItemStack removeItem(int slot, int count) {
        return getInventory().removeItem(slot, count);
    }

    @Override
    default ItemStack removeItemNoUpdate(int slot) {
        return getInventory().removeItemNoUpdate(slot);
    }

    @Override
    default void setItem(int slot, ItemStack stack) {
        getInventory().setItem(slot, stack);
    }

    @Override
    default int getMaxStackSize() {
        return getInventory().getMaxStackSize();
    }

    @Override
    default void setChanged() {
        getInventory().setChanged();
    }

    @Override
    default boolean stillValid(Player player) {
        return getInventory().stillValid(player);
    }

    @Override
    default void startOpen(Player player) {
        getInventory().startOpen(player);
    }

    @Override
    default void stopOpen(Player player) {
        getInventory().stopOpen(player);
    }

    @Override
    default boolean canPlaceItem(int slot, ItemStack stack) {
        return getInventory().canPlaceItem(slot, stack);
    }

    @Override
    default int countItem(Item item) {
        return getInventory().countItem(item);
    }

    @Override
    default boolean hasAnyOf(Set<Item> set) {
        return getInventory().hasAnyOf(set);
    }

    @Override
    default boolean hasAnyMatching(Predicate<ItemStack> predicate) {
        return getInventory().hasAnyMatching(predicate);
    }

    @Override
    default void clearContent() {
        getInventory().clearContent();
    }

    @Override
    default int[] getSlotsForFace(Direction face) {
        return getInventory().getSlotsForFace(face);
    }

    @Override
    default boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction face) {
        return getInventory().canPlaceItemThroughFace(slot, stack, face);
    }

    @Override
    default boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction face) {
        return getInventory().canTakeItemThroughFace(slot, stack, face);
    }
}
