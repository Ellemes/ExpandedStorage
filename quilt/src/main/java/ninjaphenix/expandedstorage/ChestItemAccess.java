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

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import ninjaphenix.expandedstorage.block.entity.extendable.OpenableBlockEntity;
import ninjaphenix.expandedstorage.block.misc.DoubleItemAccess;

import java.util.List;

public final class ChestItemAccess extends GenericItemAccess implements DoubleItemAccess {
    @SuppressWarnings("UnstableApiUsage")
    private Storage<ItemVariant> cache;

    public ChestItemAccess(OpenableBlockEntity entity) {
        super(entity);
    }

    @Override
    public Object get() {
        return this.hasCachedAccess() ? cache : this.getSingle();
    }

    @Override
    public Object getSingle() {
        return super.get();
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void setOther(DoubleItemAccess other) {
        //noinspection unchecked
        cache = other == null ? null : new CombinedStorage<>(List.of((Storage<ItemVariant>) this.getSingle(), (Storage<ItemVariant>) other.getSingle()));
    }

    @Override
    public boolean hasCachedAccess() {
        return cache != null;
    }
}
