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
