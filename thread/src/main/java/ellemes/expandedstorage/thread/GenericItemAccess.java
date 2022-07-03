package ellemes.expandedstorage.thread;

import ellemes.expandedstorage.block.entity.extendable.OpenableBlockEntity;
import ellemes.expandedstorage.block.strategies.ItemAccess;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class GenericItemAccess implements ItemAccess {
    private final OpenableBlockEntity entity;
    @SuppressWarnings("UnstableApiUsage")
    private InventoryStorage storage = null;

    public GenericItemAccess(OpenableBlockEntity entity) {
        this.entity = entity;
    }

    @Override
    public Object get() {
        if (storage == null) {
            NonNullList<ItemStack> items = entity.getItems();
            Container wrapped = entity.getInventory();
            Container transferApiInventory = new Container() {
                @Override
                public int getContainerSize() {
                    return wrapped.getContainerSize();
                }

                @Override
                public boolean isEmpty() {
                    return wrapped.isEmpty();
                }

                @Override
                public ItemStack getItem(int slot) {
                    return wrapped.getItem(slot);
                }

                @Override
                public ItemStack removeItem(int slot, int amount) {
                    return ContainerHelper.removeItem(items, slot, amount);
                }

                @Override
                public ItemStack removeItemNoUpdate(int slot) {
                    return wrapped.removeItemNoUpdate(slot);
                }

                @Override
                public void setItem(int slot, ItemStack stack) {
                    items.set(slot, stack);
                    if (stack.getCount() > this.getMaxStackSize()) {
                        stack.setCount(this.getMaxStackSize());
                    }
                }

                @Override
                public void setChanged() {
                    wrapped.setChanged();
                }

                @Override
                public boolean stillValid(Player player) {
                    return wrapped.stillValid(player);
                }

                @Override
                public void clearContent() {
                    wrapped.clearContent();
                }

                @Override
                public void startOpen(Player player) {
                    wrapped.startOpen(player);
                }

                @Override
                public void stopOpen(Player player) {
                    wrapped.stopOpen(player);
                }
            };
            //noinspection UnstableApiUsage
            storage = InventoryStorage.of(transferApiInventory, null);
        }
        return storage;
    }
}
