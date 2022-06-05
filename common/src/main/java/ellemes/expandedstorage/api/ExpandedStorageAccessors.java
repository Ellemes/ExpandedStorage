package ellemes.expandedstorage.api;

import ellemes.expandedstorage.block.AbstractChestBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Optional;

@SuppressWarnings("unused")
public final class ExpandedStorageAccessors {
    private ExpandedStorageAccessors() {
        throw new IllegalStateException("Should not be instantiated.");
    }

    /**
     * @return The chest type or empty if the state passed is not a chest block.
     */
    public static Optional<EsChestType> getChestType(BlockState state) {
        if (state.hasProperty(AbstractChestBlock.CURSED_CHEST_TYPE)) {
            return Optional.of(state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE));
        }
        return Optional.empty();
    }

    /**
     * @return The direction to attached chest block or empty if the state passed is not a chest block or is a single chest.
     */
    public static Optional<Direction> getAttachedChestDirection(BlockState state) {
        if (state.hasProperty(AbstractChestBlock.CURSED_CHEST_TYPE) && state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            EsChestType type = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
            if (type != EsChestType.SINGLE) {
                Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                return Optional.of(AbstractChestBlock.getDirectionToAttached(type, facing));
            }
        }
        return Optional.empty();
    }

    /**
     * @return A chest block of the specified type or empty if the passed in state is not a chest block.
     */
    public static Optional<BlockState> chestWithType(BlockState original, EsChestType type) {
        if (original.hasProperty(AbstractChestBlock.CURSED_CHEST_TYPE)) {
            return Optional.of(original.setValue(AbstractChestBlock.CURSED_CHEST_TYPE, type));
        }
        return Optional.empty();
    }
}
