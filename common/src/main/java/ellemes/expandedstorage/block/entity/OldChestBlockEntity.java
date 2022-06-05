package ellemes.expandedstorage.block.entity;

import ellemes.container_library.api.helpers.VariableSidedInventory;
import ellemes.expandedstorage.block.OpenableBlock;
import ellemes.expandedstorage.block.entity.extendable.InventoryBlockEntity;
import ellemes.expandedstorage.block.entity.extendable.OpenableBlockEntity;
import ellemes.expandedstorage.block.misc.DoubleItemAccess;
import ellemes.expandedstorage.block.strategies.ItemAccess;
import ellemes.expandedstorage.block.strategies.Lockable;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;
import java.util.function.Supplier;

public class OldChestBlockEntity extends InventoryBlockEntity {
    WorldlyContainer cachedDoubleInventory = null;

    public OldChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, ResourceLocation blockId,
                               Function<OpenableBlockEntity, ItemAccess> access, Supplier<Lockable> lockable) {
        super(type, pos, state, blockId, ((OpenableBlock) state.getBlock()).getInventoryTitle(), ((OpenableBlock) state.getBlock()).getSlotCount());
        this.setItemAccess(access.apply(this));
        this.setLockable(lockable.get());
    }

    public void invalidateDoubleBlockCache() {
        cachedDoubleInventory = null;
        this.getItemAccess().setOther(null);
    }

    public WorldlyContainer getCachedDoubleInventory() {
        return cachedDoubleInventory;
    }

    public void setCachedDoubleInventory(OldChestBlockEntity other) {
        this.cachedDoubleInventory = VariableSidedInventory.of(this.getInventory(), other.getInventory());
    }

    @Override
    public DoubleItemAccess getItemAccess() {
        return (DoubleItemAccess) super.getItemAccess();
    }

}
