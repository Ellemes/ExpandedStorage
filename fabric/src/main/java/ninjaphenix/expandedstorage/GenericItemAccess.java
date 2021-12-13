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
package ninjaphenix.expandedstorage;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import ninjaphenix.expandedstorage.block.entity.extendable.OpenableBlockEntity;
import ninjaphenix.expandedstorage.block.strategies.ItemAccess;

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
            DefaultedList<ItemStack> items = entity.getItems();
            Inventory wrapped = entity.getInventory();
            Inventory transferApiInventory = new Inventory() {
                @Override
                public int size() {
                    return wrapped.size();
                }

                @Override
                public boolean isEmpty() {
                    return wrapped.isEmpty();
                }

                @Override
                public ItemStack getStack(int slot) {
                    return wrapped.getStack(slot);
                }

                @Override
                public ItemStack removeStack(int slot, int amount) {
                    return Inventories.splitStack(items, slot, amount);
                }

                @Override
                public ItemStack removeStack(int slot) {
                    return wrapped.removeStack(slot);
                }

                @Override
                public void setStack(int slot, ItemStack stack) {
                    items.set(slot, stack);
                    if (stack.getCount() > this.getMaxCountPerStack()) {
                        stack.setCount(this.getMaxCountPerStack());
                    }
                }

                @Override
                public void markDirty() {
                    wrapped.markDirty();
                }

                @Override
                public boolean canPlayerUse(PlayerEntity player) {
                    return wrapped.canPlayerUse(player);
                }

                @Override
                public void clear() {
                    wrapped.clear();
                }

                @Override
                public void onOpen(PlayerEntity player) {
                    wrapped.onOpen(player);
                }

                @Override
                public void onClose(PlayerEntity player) {
                    wrapped.onClose(player);
                }
            };
            //noinspection UnstableApiUsage
            storage = InventoryStorage.of(transferApiInventory, null);
        }
        return storage;
    }
}
