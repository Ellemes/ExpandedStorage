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
package ninjaphenix.expandedstorage.mixin;

import com.github.fabricservertools.htm.HTMContainerLock;
import com.github.fabricservertools.htm.api.LockableChestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ninjaphenix.expandedstorage.block.AbstractChestBlock;
import ninjaphenix.expandedstorage.quilt.compat.htm.HTMChestProperties;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;

@Mixin(AbstractChestBlock.class)
public abstract class HTMChestCompat implements LockableChestBlock {
    @Override
    public HTMContainerLock getLockAt(BlockState state, Level world, BlockPos pos) {
        //noinspection ConstantConditions
        return AbstractChestBlock.createPropertyRetriever((AbstractChestBlock) (Object) this, state, world, pos, true).get(HTMChestProperties.LOCK_PROPERTY).orElse(null);
    }

    @Override
    public Optional<BlockEntity> getUnlockedPart(BlockState state, Level world, BlockPos pos) {
        //noinspection ConstantConditions
        return AbstractChestBlock.createPropertyRetriever((AbstractChestBlock) (Object) this, state, world, pos, true).get(HTMChestProperties.UNLOCKED_BE_PROPERTY);
    }

}
