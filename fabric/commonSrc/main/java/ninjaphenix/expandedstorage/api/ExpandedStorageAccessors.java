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
package ninjaphenix.expandedstorage.api;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import ninjaphenix.expandedstorage.block.AbstractChestBlock;
import ninjaphenix.expandedstorage.block.misc.CursedChestType;

import java.util.Optional;

@SuppressWarnings("unused")
public final class ExpandedStorageAccessors {
    private ExpandedStorageAccessors() {
        throw new IllegalStateException("Should not be instantiated.");
    }

    /**
     * @return The chest type or empty if the state passed is not a chest block.
     */
    public static Optional<EsChestType> getChestType(BlockState state) {
        if (state.contains(AbstractChestBlock.CURSED_CHEST_TYPE)) {
            return Optional.of(EsChestType.of(state.get(AbstractChestBlock.CURSED_CHEST_TYPE)));
        }
        return Optional.empty();
    }

    /**
     * @return The direction to attached chest block or empty if the state passed is not a chest block or is a single chest.
     */
    public static Optional<Direction> getAttachedChestDirection(BlockState state) {
        if (state.contains(AbstractChestBlock.CURSED_CHEST_TYPE) && state.contains(Properties.HORIZONTAL_FACING)) {
            CursedChestType type = state.get(AbstractChestBlock.CURSED_CHEST_TYPE);
            if (type != CursedChestType.SINGLE) {
                Direction facing = state.get(Properties.HORIZONTAL_FACING);
                return Optional.of(AbstractChestBlock.getDirectionToAttached(type, facing));
            }
        }
        return Optional.empty();
    }

    /**
     * @return A chest block of the specified type or empty if the passed in state is not a chest block.
     */
    public static Optional<BlockState> chestWithType(BlockState original, EsChestType type) {
        if (original.contains(AbstractChestBlock.CURSED_CHEST_TYPE)) {
            return Optional.of(original.with(AbstractChestBlock.CURSED_CHEST_TYPE, CursedChestType.of(type)));
        }
        return Optional.empty();
    }
}
