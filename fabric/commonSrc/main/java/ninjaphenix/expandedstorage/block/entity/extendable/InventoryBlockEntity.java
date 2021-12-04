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

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import ninjaphenix.expandedstorage.block.strategies.Observable;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public abstract class InventoryBlockEntity extends OpenableBlockEntity {
    private final DefaultedList<ItemStack> items;
    private final SidedInventory inventory = new SidedInventory() {
        private int[] availableSlots;
        @Override
        public int[] getAvailableSlots(Direction side) {
            if (availableSlots == null) {
                availableSlots = IntStream.range(0, this.size()).toArray();
            }
            return availableSlots;
        }

        @Override
        public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
            return true;
        }

        @Override
        public boolean canExtract(int slot, ItemStack stack, Direction dir) {
            return true;
        }

        @Override
        public int size() {
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
        public ItemStack getStack(int slot) {
            return items.get(slot);
        }

        @Override
        public ItemStack removeStack(int slot, int amount) {
            ItemStack stack = Inventories.splitStack(items, slot, amount);
            if (!stack.isEmpty()) this.markDirty();
            return stack;
        }

        @Override
        public ItemStack removeStack(int slot) {
            return Inventories.removeStack(items, slot);
        }

        @Override
        public void setStack(int slot, ItemStack stack) {
            if (stack.getCount() > this.getMaxCountPerStack()) stack.setCount(this.getMaxCountPerStack());
            items.set(slot, stack);
            this.markDirty();
        }

        @Override
        public void markDirty() {
            InventoryBlockEntity.this.markDirty();
        }

        @Override
        public boolean canPlayerUse(PlayerEntity player) {
            return true;
        }

        @Override
        public void clear() {
            items.clear();
        }

        @Override
        public void onOpen(PlayerEntity player) {
            if (player.isSpectator()) return;
            observable.playerStartViewing(player);
        }

        @Override
        public void onClose(PlayerEntity player) {
            if (player.isSpectator()) return;
            observable.playerStopViewing(player);
        }
    };
    private Observable observable = Observable.NOT;

    public InventoryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, Identifier blockId, Text defaultName, int inventorySize) {
        super(type, pos, state, blockId, defaultName);
        items = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
    }

    public final SidedInventory getInventory() {
        return inventory;
    }

    @Override
    public final DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        Inventories.readNbt(tag, items);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        Inventories.writeNbt(tag, items);
    }

    protected void setObservable(Observable observable) {
        if (this.observable == Observable.NOT) this.observable = observable;
    }
}

