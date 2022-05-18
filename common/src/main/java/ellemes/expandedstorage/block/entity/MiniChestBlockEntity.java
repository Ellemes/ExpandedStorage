/*
 * Copyright 2021-2022 Ellemes
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
package ellemes.expandedstorage.block.entity;

import ellemes.expandedstorage.block.OpenableBlock;
import ellemes.expandedstorage.block.entity.extendable.ExposedInventoryBlockEntity;
import ellemes.expandedstorage.block.entity.extendable.OpenableBlockEntity;
import ellemes.expandedstorage.block.strategies.ItemAccess;
import ellemes.expandedstorage.block.strategies.Lockable;

import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public final class MiniChestBlockEntity extends ExposedInventoryBlockEntity {
    public MiniChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, ResourceLocation blockId,
                                Function<OpenableBlockEntity, ItemAccess> access, Supplier<Lockable> lockable) {
        super(type, pos, state, blockId, ((OpenableBlock) state.getBlock()).getInventoryTitle(), 1);
        this.setItemAccess(access.apply(this));
        this.setLockable(lockable.get());
    }
}
