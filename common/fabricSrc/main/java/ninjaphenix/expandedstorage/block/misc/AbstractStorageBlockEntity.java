/**
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
package ninjaphenix.expandedstorage.block.misc;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.ContainerLock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import ninjaphenix.expandedstorage.block.AbstractStorageBlock;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
@Experimental
public abstract class AbstractStorageBlockEntity<T extends AbstractStorageBlock> extends BlockEntity {
    private final Identifier blockId;
    private ContainerLock lockKey;

    public AbstractStorageBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, Identifier blockId) {
        super(blockEntityType, pos, state);
        lockKey = ContainerLock.EMPTY;
        this.blockId = blockId;
        this.initialise(blockId, (T) state.getBlock());
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        lockKey = ContainerLock.fromNbt(tag);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        lockKey.writeNbt(tag);
        return tag;
    }

    public boolean usableBy(ServerPlayerEntity player) {
        return lockKey == ContainerLock.EMPTY || !player.isSpectator() && lockKey.canOpen(player.getMainHandStack());
    }

    public final Identifier getBlockId() {
        return blockId;
    }

    protected abstract void initialise(Identifier blockId, T block);
}
