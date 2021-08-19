package ninjaphenix.expandedstorage.base.internal_api.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandlerModifiable;
import ninjaphenix.expandedstorage.base.internal_api.block.AbstractChestBlock;
import ninjaphenix.expandedstorage.base.internal_api.inventory.CombinedIItemHandlerModifiable;

import java.util.Optional;

public final class ForgeChestProperties {
    public static final DoubleBlockCombiner.Combiner<AbstractOpenableStorageBlockEntity, Optional<IItemHandlerModifiable>> INVENTORY_GETTER = new DoubleBlockCombiner.Combiner<>() {
        @Override
        public Optional<IItemHandlerModifiable> acceptDouble(AbstractOpenableStorageBlockEntity first, AbstractOpenableStorageBlockEntity second) {
            return Optional.of(new CombinedIItemHandlerModifiable(
                    AbstractOpenableStorageBlockEntity.createGenericItemHandler(first),
                    AbstractOpenableStorageBlockEntity.createGenericItemHandler(second)
            ));
        }

        @Override
        public Optional<IItemHandlerModifiable> acceptSingle(AbstractOpenableStorageBlockEntity single) {
            return Optional.of(AbstractOpenableStorageBlockEntity.createGenericItemHandler(single));
        }

        @Override
        public Optional<IItemHandlerModifiable> acceptNone() {
            return Optional.empty();
        }
    };

    public static Optional<IItemHandlerModifiable> createItemHandler(Level level, BlockState state, BlockPos pos) {
        if (state.getBlock() instanceof AbstractChestBlock<?> block) {
            return block.createCombinedPropertyGetter(state, level, pos, false).apply(ForgeChestProperties.INVENTORY_GETTER);
        }
        return Optional.empty();
    }
}
