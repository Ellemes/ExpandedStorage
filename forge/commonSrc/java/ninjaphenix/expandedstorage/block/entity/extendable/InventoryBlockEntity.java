/*
 * Copyright 2021 NinjaPhenix
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ninjaphenix.expandedstorage.block.entity.extendable;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import ninjaphenix.expandedstorage.block.OpenableBlock;
import ninjaphenix.expandedstorage.block.strategies.Observable;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public abstract class InventoryBlockEntity extends OpenableBlockEntity {
    private NonNullList<ItemStack> items;
    private final WorldlyContainer inventory = new WorldlyContainer() {
        private int[] availableSlots;
        @Override
        public void clearContent() {
            items.clear();
        }

        @Override
        public int getContainerSize() {
            return items.size();
        }

        @Override
        public boolean isEmpty() {
            for (ItemStack stack : items) {
                if (stack.isEmpty()) continue;
                return false;
            }
            return true;
        }

        @Override
        public ItemStack getItem(int slot) {
            return items.get(slot);
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            ItemStack stack = ContainerHelper.removeItem(items, slot, amount);
            if (!stack.isEmpty()) this.setChanged();
            return stack;
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            return ContainerHelper.takeItem(items, slot);
        }

        @Override
        public void setItem(int slot, ItemStack stack) {
            if (stack.getCount() > this.getMaxStackSize()) stack.setCount(this.getMaxStackSize());
            items.set(slot, stack);
            this.setChanged();
        }

        @Override
        public void setChanged() {
            InventoryBlockEntity.this.setChanged();
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }

        @Override
        public void startOpen(Player player) {
            if (player.isSpectator()) return;
            observable.playerStartViewing(player);
        }

        @Override
        public void stopOpen(Player player) {
            if (player.isSpectator()) return;
            observable.playerStopViewing(player);
        }

        @Override
        public int[] getSlotsForFace(Direction direction) {
            if (availableSlots == null) {
                availableSlots = IntStream.range(0, this.getContainerSize()).toArray();
            }
            return availableSlots;
        }

        @Override
        public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
            return true;
        }

        @Override
        public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
            return true;
        }
    };
    private Observable observable = Observable.NOT;

    public InventoryBlockEntity(BlockEntityType<?> type) {
        super(type);
    }

    public final WorldlyContainer getInventory() {
        return inventory;
    }

    @Override
    public final NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void loadAdditional(BlockState state, CompoundTag tag) {
        items = NonNullList.withSize(((OpenableBlock) state.getBlock()).getSlotCount(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        ContainerHelper.saveAllItems(tag, items);
        return tag;
    }

    protected void setObservable(Observable observable) {
        if (this.observable == Observable.NOT) this.observable = observable;
    }
}

