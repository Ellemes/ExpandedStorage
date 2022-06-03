package ellemes.expandedstorage.block.misc;

import ellemes.expandedstorage.block.strategies.Lockable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.LockCode;

public class BasicLockable implements Lockable {
    LockCode lock = LockCode.NO_LOCK;

    @Override
    public void writeLock(CompoundTag tag) {
        lock.addToTag(tag);
    }

    @Override
    public void readLock(CompoundTag tag) {
        lock = LockCode.fromTag(tag);
    }

    @Override
    public boolean canPlayerOpenLock(ServerPlayer player) {
        return lock == LockCode.NO_LOCK || !player.isSpectator() && lock.unlocksWith(player.getMainHandItem());
    }
}
