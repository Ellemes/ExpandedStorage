package ninjaphenix.expandedstorage.base.internal_api.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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
import ninjaphenix.expandedstorage.base.internal_api.Utils;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

    public static Component getDisplayName(List<? extends AbstractStorageBlockEntity> inventories) {
        for (AbstractStorageBlockEntity inventory : inventories) {
            if (inventory.hasCustomName()) {
                return inventory.getName();
            }
        }
        if (inventories.size() == 1) {
            return inventories.get(0).getName();
        } else if (inventories.size() == 2) {
            return Utils.translation("container.expandedstorage.generic_double", inventories.get(0).getName());
        }
        throw new IllegalStateException("inventories size is > 2");
    }
}
