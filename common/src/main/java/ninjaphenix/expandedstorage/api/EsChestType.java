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
package ninjaphenix.expandedstorage.api;

import ninjaphenix.expandedstorage.block.misc.CursedChestType;
import org.jetbrains.annotations.ApiStatus;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;

public enum EsChestType implements StringRepresentable {
    TOP,
    BOTTOM,
    FRONT,
    BACK,
    LEFT,
    RIGHT,
    SINGLE;

    private final String name;

    EsChestType() {
        name = this.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    /**
     * @deprecated Temporary method, will be removed in the future.
     */
    @Deprecated
    @ApiStatus.Internal
    public static EsChestType of(CursedChestType type) {
        if (type == CursedChestType.TOP) return EsChestType.TOP;
        else if (type == CursedChestType.BOTTOM) return EsChestType.BOTTOM;
        else if (type == CursedChestType.FRONT) return EsChestType.FRONT;
        else if (type == CursedChestType.BACK) return EsChestType.BACK;
        else if (type == CursedChestType.LEFT) return EsChestType.LEFT;
        else if (type == CursedChestType.RIGHT) return EsChestType.RIGHT;
        else if (type == CursedChestType.SINGLE) return EsChestType.SINGLE;
        throw new IllegalStateException("Invalid type passed");
    }
}
