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
package ellemes.expandedstorage.forge;

import ellemes.expandedstorage.block.entity.extendable.OpenableBlockEntity;
import ellemes.expandedstorage.block.misc.DoubleItemAccess;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public final class ChestItemAccess extends GenericItemAccess implements DoubleItemAccess {
    private IItemHandlerModifiable cache;

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
    public void setOther(DoubleItemAccess other) {
        cache = other == null ? null : new CombinedInvWrapper((IItemHandlerModifiable) this.getSingle(), (IItemHandlerModifiable) other.getSingle());
    }

    @Override
    public boolean hasCachedAccess() {
        return cache != null;
    }
}
