package ellemes.expandedstorage.quilt.compat.htm;

import com.github.fabricservertools.htm.HTMContainerLock;
import ellemes.expandedstorage.block.misc.BasicLockable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;

public final class HTMLockable extends BasicLockable {
    public static final String LOCK_TAG_KEY = "HTM_Lock";
    private HTMContainerLock lock = new HTMContainerLock();

    @Override
    public void writeLock(CompoundTag tag) {
        super.writeLock(tag);
        CompoundTag subTag = new CompoundTag();
        lock.toTag(subTag);
        tag.put(HTMLockable.LOCK_TAG_KEY, subTag);
    }

    @Override
    public void readLock(CompoundTag tag) {
        super.readLock(tag);
        if (tag.contains(HTMLockable.LOCK_TAG_KEY, Tag.TAG_COMPOUND))
            lock.fromTag(tag.getCompound(HTMLockable.LOCK_TAG_KEY));
    }

    @Override
    public boolean canPlayerOpenLock(ServerPlayer player) {
        return !lock.isLocked() && super.canPlayerOpenLock(player) || lock.isLocked() && lock.canOpen(player);
    }

    public HTMContainerLock getLock() {
        return lock;
    }

    public void setLock(HTMContainerLock lock) {
        this.lock = lock;
    }
}
