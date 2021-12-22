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

import net.minecraft.util.Mth;

public final class ChestLidAnimator {
    private static final float STEP = 0.1f;
    private static final float CLOSED = 0.0f;
    private static final float OPEN = 1.0f;
    private boolean open;
    private float previousLidAngle;
    private float lidAngle;

    public void setOpen(boolean open) {
        this.open = open;
    }

    public float getProgress(float delta) {
        return Mth.lerp(delta, previousLidAngle, lidAngle);
    }

    public void tick() {
        previousLidAngle = lidAngle;
        if (!open && lidAngle > CLOSED) {
            lidAngle = Math.max(lidAngle - STEP, CLOSED);
        } else if (open && lidAngle < OPEN) {
            lidAngle = Math.min(lidAngle + STEP, OPEN);
        }
    }
}
