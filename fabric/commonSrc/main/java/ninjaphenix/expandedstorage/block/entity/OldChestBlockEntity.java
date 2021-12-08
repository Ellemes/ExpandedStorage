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
package ninjaphenix.expandedstorage.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import ninjaphenix.container_library.api.helpers.VariableSidedInventory;
import ninjaphenix.expandedstorage.block.AbstractChestBlock;
import ninjaphenix.expandedstorage.block.OpenableBlock;
import ninjaphenix.expandedstorage.block.entity.extendable.InventoryBlockEntity;
import ninjaphenix.expandedstorage.block.entity.extendable.OpenableBlockEntity;
import ninjaphenix.expandedstorage.block.misc.DoubleItemAccess;
import ninjaphenix.expandedstorage.block.strategies.ItemAccess;
import ninjaphenix.expandedstorage.block.strategies.Lockable;

import java.util.function.Function;
import java.util.function.Supplier;

public class OldChestBlockEntity extends InventoryBlockEntity {
    SidedInventory cachedDoubleInventory = null;
    public OldChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, Identifier blockId,
                               Function<OpenableBlockEntity, ItemAccess> access, Supplier<Lockable> lockable) {
        super(type, pos, state, blockId, ((OpenableBlock) state.getBlock()).getInventoryTitle(), ((OpenableBlock) state.getBlock()).getSlotCount());
        this.setItemAccess(access.apply(this));
        this.setLockable(lockable.get());
    }

    public void invalidateDoubleBlockCache() {
        cachedDoubleInventory = null;
        this.getItemAccess().setOther(null);
    }

    public void setCachedDoubleInventory(OldChestBlockEntity other) {
        this.cachedDoubleInventory = VariableSidedInventory.of(this.getInventory(), other.getInventory());
    }

    public SidedInventory getCachedDoubleInventory() {
        return cachedDoubleInventory;
    }

    @Override
    public DoubleItemAccess getItemAccess() {
        return (DoubleItemAccess) super.getItemAccess();
    }

}
