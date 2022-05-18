/*
 * Copyright 2022 Ellemes
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
package ellemes.expandedstorage.registration;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public final class NamedValue<T> {
    private final ResourceLocation name;
    private final Supplier<T> valueSupplier;
    private T value;

    public NamedValue(ResourceLocation name, Supplier<T> valueSupplier) {
        this.name = name;
        this.valueSupplier = valueSupplier;
    }

    public ResourceLocation getName() {
        return name;
    }

    public T getValue() {
        if (value == null) {
            value = valueSupplier.get();
        }
        return value;
    }
}
