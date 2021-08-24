package ninjaphenix.expandedstorage.base.internal_api.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.LockCode;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

@Internal
@Experimental
public abstract class AbstractStorageBlockEntity extends BlockEntity implements Nameable {
    private LockCode lockKey;
    private Component menuTitle;

    public AbstractStorageBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
        lockKey = LockCode.NO_LOCK;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        lockKey = LockCode.fromTag(tag);
        if (tag.contains("CustomName", Tag.TAG_STRING)) {
            menuTitle = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        lockKey.addToTag(tag);
        if (menuTitle != null) {
            tag.putString("CustomName", Component.Serializer.toJson(menuTitle));
        }
        return tag;
    }

    public boolean canPlayerInteractWith(ServerPlayer player) {
        return lockKey == LockCode.NO_LOCK || !player.isSpectator() && lockKey.unlocksWith(player.getMainHandItem());
    }

    @Override
    public final Component getName() {
        return this.hasCustomName() ? menuTitle : this.getDefaultTitle();
    }

    public abstract Component getDefaultTitle();

    @Override
    public final boolean hasCustomName() {
        return menuTitle != null;
    }

    @Nullable
    @Override
    public final Component getCustomName() {
        return menuTitle;
    }

    public final void setMenuTitle(Component title) {
        menuTitle = title;
    }
}
