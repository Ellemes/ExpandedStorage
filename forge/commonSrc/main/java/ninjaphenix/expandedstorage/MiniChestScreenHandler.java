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

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class MiniChestScreenHandler extends AbstractContainerMenu {
    private final Container inventory;

    public MiniChestScreenHandler(int syncId, Container inventory, Inventory playerInventory) {
        super(Common.getMiniChestScreenHandlerType(), syncId);
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);
        this.addSlot(new Slot(inventory, 0, 80, 35));
        for(int y = 0; y < 3; ++y) for (int x = 0; x < 9; ++x)
            this.addSlot(new Slot(playerInventory, 9 + x + y * 9, 8 + x * 18, 84 + y * 18));

        for(int i = 0; i < 9; ++i) this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
    }

    public static MiniChestScreenHandler createClientMenu(int syncId, Inventory playerInventory) {
        return new MiniChestScreenHandler(syncId, new SimpleContainer(1), playerInventory);
    }

    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    public void removed(Player player) {
        super.removed(player);
        this.inventory.stopOpen(player);
    }

    public Container getInventory() {
        return this.inventory;
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack originalStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack newStack = slot.getItem();
            originalStack = newStack.copy();
            if (index < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(newStack, this.inventory.getContainerSize(), this.inventory.getContainerSize() + 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(newStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (newStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return originalStack;
    }
}
