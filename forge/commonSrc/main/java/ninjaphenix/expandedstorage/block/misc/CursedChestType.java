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

import java.util.Locale;
import net.minecraft.util.StringRepresentable;

/**
 * Note to self, do not rename, used by chest tracker.
 */
public enum CursedChestType implements StringRepresentable {
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

    @Override
    public String getSerializedName() {
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
