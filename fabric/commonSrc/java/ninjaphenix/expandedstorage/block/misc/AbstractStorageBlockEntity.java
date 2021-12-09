package ninjaphenix.expandedstorage.block.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.LockCode;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import ninjaphenix.expandedstorage.Utils;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

@Internal
@Experimental
public abstract class AbstractStorageBlockEntity extends BlockEntity implements Nameable {
    private LockCode lockKey;
    private Component customTitle;

    public AbstractStorageBlockEntity(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
        lockKey = LockCode.NO_LOCK;
    }

    public static void notifyBlockLocked(Player player, Component displayName) {
        player.displayClientMessage(new TranslatableComponent("container.isLocked", displayName), true);
        player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public void load(BlockState state, CompoundTag tag) {
        super.load(state, tag);
        lockKey = LockCode.fromTag(tag);
        if (tag.contains("CustomName", Utils.NBT_STRING_TYPE)) {
            customTitle = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        lockKey.addToTag(tag);
        if (customTitle != null) {
            tag.putString("CustomName", Component.Serializer.toJson(customTitle));
        }
        return tag;
    }

    public boolean canPlayerInteractWith(ServerPlayer player) {
        return lockKey == LockCode.NO_LOCK || !player.isSpectator() && lockKey.unlocksWith(player.getMainHandItem());
    }

    @Override
    public final Component getName() {
        return this.hasCustomName() ? customTitle : this.getDefaultTitle();
    }

    public abstract Component getDefaultTitle();

    @Override
    public final boolean hasCustomName() {
        return customTitle != null;
    }

    @Nullable
    @Override
    public final Component getCustomName() {
        return customTitle;
    }

    public final void setCustomTitle(Component title) {
        customTitle = title;
    }
}
