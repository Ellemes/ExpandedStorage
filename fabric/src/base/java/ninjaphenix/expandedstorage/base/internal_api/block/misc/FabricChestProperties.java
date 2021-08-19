package ninjaphenix.expandedstorage.base.internal_api.block.misc;

import com.github.fabricservertools.htm.HTMContainerLock;
import com.github.fabricservertools.htm.api.LockableObject;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ninjaphenix.expandedstorage.base.internal_api.block.AbstractChestBlock;

import java.util.List;
import java.util.Optional;

public final class FabricChestProperties {
    public static final String LOCK_TAG_KEY = "HTM_Lock";

    public static final DoubleBlockCombiner.Combiner<AbstractOpenableStorageBlockEntity, HTMContainerLock> LOCK_GETTER = new DoubleBlockCombiner.Combiner<>() {
        @Override
        public HTMContainerLock acceptDouble(AbstractOpenableStorageBlockEntity first, AbstractOpenableStorageBlockEntity second) {
            LockableObject firstLockable = (LockableObject) first;
            LockableObject secondLockable = (LockableObject) second;
            if (firstLockable.getLock().isLocked() || !secondLockable.getLock().isLocked()) {
                return firstLockable.getLock();
            }
            return secondLockable.getLock();
        }

        @Override
        public HTMContainerLock acceptSingle(AbstractOpenableStorageBlockEntity single) {
            return ((LockableObject) single).getLock();
        }

        @Override
        public HTMContainerLock acceptNone() {
            return null;
        }
    };

    public static final DoubleBlockCombiner.Combiner<AbstractOpenableStorageBlockEntity, Optional<BlockEntity>> UNLOCKED_BE_GETTER = new DoubleBlockCombiner.Combiner<>() {
        @Override
        public Optional<BlockEntity> acceptDouble(AbstractOpenableStorageBlockEntity first, AbstractOpenableStorageBlockEntity second) {
            LockableObject firstLockable = (LockableObject) first;
            if (!firstLockable.getLock().isLocked()) {
                return Optional.of(first);
            }
            LockableObject secondLockable = (LockableObject) second;
            if (!secondLockable.getLock().isLocked()) {
                return Optional.of(second);
            }
            return Optional.empty();
        }

        @Override
        public Optional<BlockEntity> acceptSingle(AbstractOpenableStorageBlockEntity single) {
            return Optional.empty();
        }

        @Override
        public Optional<BlockEntity> acceptNone() {
            return Optional.empty();
        }
    };

    public static final DoubleBlockCombiner.Combiner<AbstractOpenableStorageBlockEntity, Optional<Storage<ItemVariant>>> INVENTORY_GETTER = new DoubleBlockCombiner.Combiner<>() {
        @Override
        public Optional<Storage<ItemVariant>> acceptDouble(AbstractOpenableStorageBlockEntity first, AbstractOpenableStorageBlockEntity second) {
            return Optional.of(new CombinedStorage<>(List.of(AbstractOpenableStorageBlockEntity.createGenericItemStorage(first),
                    AbstractOpenableStorageBlockEntity.createGenericItemStorage(second))));
        }

        @Override
        public Optional<Storage<ItemVariant>> acceptSingle(AbstractOpenableStorageBlockEntity single) {
            return Optional.of(AbstractOpenableStorageBlockEntity.createGenericItemStorage(single));
        }

        @Override
        public Optional<Storage<ItemVariant>> acceptNone() {
            return Optional.empty();
        }
    };


    public static Optional<Storage<ItemVariant>> createItemStorage(Level level, BlockState state, BlockPos pos) {
        if (state.getBlock() instanceof AbstractChestBlock<?> block) {
            return block.createCombinedPropertyGetter(state, level, pos, false).apply(FabricChestProperties.INVENTORY_GETTER);
        }
        return Optional.empty();
    }
}
