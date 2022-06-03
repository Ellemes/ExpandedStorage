package ellemes.expandedstorage.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public interface PropertyRetriever<A> {
    static <A extends BlockEntity> PropertyRetriever<A> create(
            BlockEntityType<A> blockEntityType,
            Function<BlockState, DoubleBlockCombiner.BlockType> typeGetter,
            BiFunction<BlockState, Direction, Direction> attachedDirectionGetter,
            Function<BlockState, Direction> directionGetter,
            BlockState state,
            LevelAccessor world,
            BlockPos pos,
            BiPredicate<LevelAccessor, BlockPos> blockInaccessible) {
        A entity = blockEntityType.getBlockEntity(world, pos);
        if (entity == null || blockInaccessible.test(world, pos))
            return new EmptyPropertyRetriever<>();

        DoubleBlockCombiner.BlockType type = typeGetter.apply(state);
        if (type != DoubleBlockCombiner.BlockType.SINGLE) {
            Direction facing = directionGetter.apply(state);
            BlockPos attachedPos = pos.relative(attachedDirectionGetter.apply(state, facing));
            BlockState attachedState = world.getBlockState(attachedPos);
            if (attachedState.is(state.getBlock())) {
                if (PropertyRetriever.areTypesOpposite(type, typeGetter.apply(attachedState)) && facing == directionGetter.apply(attachedState)) {
                    if (blockInaccessible.test(world, attachedPos))
                        return new EmptyPropertyRetriever<>();

                    A attachedEntity = blockEntityType.getBlockEntity(world, attachedPos);
                    if (attachedEntity != null) {
                        if (type == DoubleBlockCombiner.BlockType.FIRST)
                            return new DoublePropertyRetriever<>(entity, attachedEntity);

                        return new DoublePropertyRetriever<>(attachedEntity, entity);
                    }
                }
            }
        }
        return new SinglePropertyRetriever<>(entity);
    }

    static boolean areTypesOpposite(DoubleBlockCombiner.BlockType type, DoubleBlockCombiner.BlockType otherType) {
        return type == DoubleBlockCombiner.BlockType.FIRST && otherType == DoubleBlockCombiner.BlockType.SECOND ||
                type == DoubleBlockCombiner.BlockType.SECOND && otherType == DoubleBlockCombiner.BlockType.FIRST;
    }

    static <A> PropertyRetriever<A> createDirect(A single) {
        return new SinglePropertyRetriever<>(single);
    }

    <B> Optional<B> get(Property<A, B> property);
}
