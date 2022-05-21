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
package ellemes.expandedstorage.api;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum EsChestType implements StringRepresentable {
    TOP(-1),
    BOTTOM(-1),
    FRONT(0),
    BACK(2),
    LEFT(1),
    RIGHT(3),
    SINGLE(-1);

    private final String name;
    private final int offset;

    EsChestType(int offset) {
        this.name = this.name().toLowerCase(Locale.ROOT);
        this.offset = offset;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public EsChestType getOpposite() {
        if (this == EsChestType.TOP) {
            return EsChestType.BOTTOM;
        } else if (this == EsChestType.BOTTOM) {
            return EsChestType.TOP;
        } else if (this == EsChestType.FRONT) {
            return EsChestType.BACK;
        } else if (this == EsChestType.BACK) {
            return EsChestType.FRONT;
        } else if (this == EsChestType.LEFT) {
            return EsChestType.RIGHT;
        } else if (this == EsChestType.RIGHT) {
            return EsChestType.LEFT;
        }
        throw new IllegalStateException("EsChestType.SINGLE has no opposite type.");
    }

    public int getOffset() {
        return offset;
    }
}
