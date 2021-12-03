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
package ninjaphenix.expandedstorage.item;

import java.util.Locale;

public enum MutationMode {
    MERGE,
    SPLIT,
    ROTATE,
    SWAP_THEME;

    private static final MutationMode[] VALUES = MutationMode.values();

    public static MutationMode from(byte index) {
        if (index >= 0 && index < MutationMode.VALUES.length) {
            return MutationMode.VALUES[index];
        }
        return null;
    }

    public byte toByte() {
        return (byte) this.ordinal();
    }

    @Override
    public String toString() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public MutationMode next() {
        return MutationMode.VALUES[(ordinal() + 1) % MutationMode.VALUES.length];
    }
}
