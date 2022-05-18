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
package ellemes.expandedstorage.quilt.compat.htm;

import com.github.fabricservertools.htm.HTMContainerLock;
import com.github.fabricservertools.htm.api.LockableObject;
import net.minecraft.world.level.block.entity.BlockEntity;
import ellemes.expandedstorage.block.entity.OldChestBlockEntity;
import ellemes.expandedstorage.block.misc.Property;

public final class HTMChestProperties {
    public static final Property<OldChestBlockEntity, HTMContainerLock> LOCK_PROPERTY = new Property<>() {
        @Override
        public HTMContainerLock get(OldChestBlockEntity first, OldChestBlockEntity second) {
            LockableObject firstLockable = (LockableObject) first;
            LockableObject secondLockable = (LockableObject) second;
            if (firstLockable.getLock().isLocked() || !secondLockable.getLock().isLocked())
                return firstLockable.getLock();

            return secondLockable.getLock();
        }

        @Override
        public HTMContainerLock get(OldChestBlockEntity single) {
            return ((LockableObject) single).getLock();
        }
    };
    public static final Property<OldChestBlockEntity, BlockEntity> UNLOCKED_BE_PROPERTY = new Property<>() {
        @Override
        public BlockEntity get(OldChestBlockEntity first, OldChestBlockEntity second) {
            LockableObject firstLockable = (LockableObject) first;
            if (!firstLockable.getLock().isLocked()) return first;
            LockableObject secondLockable = (LockableObject) second;
            if (!secondLockable.getLock().isLocked()) return second;
            return null;
        }

        @Override
        public BlockEntity get(OldChestBlockEntity single) {
            return null;
        }
    };

    private HTMChestProperties() {
        throw new IllegalStateException("Should not instantiate this class.");
    }
}
