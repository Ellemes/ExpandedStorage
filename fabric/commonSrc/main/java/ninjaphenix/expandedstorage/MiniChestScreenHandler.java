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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public final class MiniChestScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    public MiniChestScreenHandler(int syncId, Inventory inventory, PlayerInventory playerInventory) {
        super(Common.getMiniChestScreenHandlerType(), syncId);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
        this.addSlot(new Slot(inventory, 0, 80, 35));
        for(int y = 0; y < 3; ++y) for (int x = 0; x < 9; ++x)
            this.addSlot(new Slot(playerInventory, 9 + x + y * 9, 8 + x * 18, 84 + y * 18));

        for(int i = 0; i < 9; ++i) this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
    }

    public static MiniChestScreenHandler createClientMenu(int syncId, PlayerInventory playerInventory) {
        return new MiniChestScreenHandler(syncId, new SimpleInventory(1), playerInventory);
    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public void close(PlayerEntity player) {
        super.close(player);
        this.inventory.onClose(player);
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack originalStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack newStack = slot.getStack();
            originalStack = newStack.copy();
            if (index < this.inventory.size()) {
                if (!this.insertItem(newStack, this.inventory.size(), this.inventory.size() + 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(newStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (newStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return originalStack;
    }
}
