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
package ninjaphenix.expandedstorage.block.misc;

import net.minecraft.util.StringIdentifiable;
import ninjaphenix.expandedstorage.api.EsChestType;
import ninjaphenix.expandedstorage.api.ExpandedStorageAccessors;
import org.jetbrains.annotations.ApiStatus;

import java.util.Locale;

/**
 * Note to self, do not rename, used by chest tracker.
 * @deprecated Use {@link EsChestType} and {@link ExpandedStorageAccessors} instead.
 */
@Deprecated
@ApiStatus.Internal
public enum CursedChestType implements StringIdentifiable {
    TOP(-1),
    BOTTOM(-1),
    FRONT(0),
    BACK(2),
    LEFT(1),
    RIGHT(3),
    SINGLE(-1);

    private final String name;
    private final int offset;

    CursedChestType(int offset) {
        this.name = name().toLowerCase(Locale.ROOT);
        this.offset = offset;
    }

    public static CursedChestType of(EsChestType type) {
        if (type == EsChestType.TOP) return CursedChestType.TOP;
        else if (type == EsChestType.BOTTOM) return CursedChestType.BOTTOM;
        else if (type == EsChestType.FRONT) return CursedChestType.FRONT;
        else if (type == EsChestType.BACK) return CursedChestType.BACK;
        else if (type == EsChestType.LEFT) return CursedChestType.LEFT;
        else if (type == EsChestType.RIGHT) return CursedChestType.RIGHT;
        else if (type == EsChestType.SINGLE) return CursedChestType.SINGLE;
        throw new IllegalStateException("Invalid type passed");
    }

    @Override
    public String asString() {
        return name;
    }

    public int getOffset() {
        return offset;
    }

    public CursedChestType getOpposite() {
        if (this == CursedChestType.TOP) {
            return CursedChestType.BOTTOM;
        } else if (this == CursedChestType.BOTTOM) {
            return CursedChestType.TOP;
        } else if (this == CursedChestType.FRONT) {
            return CursedChestType.BACK;
        } else if (this == CursedChestType.BACK) {
            return CursedChestType.FRONT;
        } else if (this == CursedChestType.LEFT) {
            return CursedChestType.RIGHT;
        } else if (this == CursedChestType.RIGHT) {
            return CursedChestType.LEFT;
        }
        throw new IllegalStateException("CursedChestType.SINGLE has no opposite type.");
    }
}
