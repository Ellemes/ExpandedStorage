package ninjaphenix.expandedstorage.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.DoubleBlockCombiner.NeighborCombineResult;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.items.IItemHandlerModifiable;
import ninjaphenix.expandedstorage.block.misc.AbstractOpenableStorageBlockEntity;
import ninjaphenix.expandedstorage.block.misc.CursedChestType;
import ninjaphenix.expandedstorage.inventory.CombinedContainer;
import ninjaphenix.expandedstorage.inventory.CombinedIItemHandlerModifiable;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiPredicate;

@Internal
@Experimental
public abstract class AbstractChestBlock<T extends AbstractOpenableStorageBlockEntity> extends AbstractOpenableStorageBlock {
    public static final EnumProperty<CursedChestType> CURSED_CHEST_TYPE = EnumProperty.create("type", CursedChestType.class);

    private final DoubleBlockCombiner.Combiner<T, Optional<WorldlyContainer>> containerGetter = new DoubleBlockCombiner.Combiner<>() {
        @Override
        public Optional<WorldlyContainer> acceptDouble(T first, T second) {
            return Optional.of(new CombinedContainer(first, second));
        }

        @Override
        public Optional<WorldlyContainer> acceptSingle(T single) {
            return Optional.of(single);
        }

        @Override
        public Optional<WorldlyContainer> acceptNone() {
            return Optional.empty();
        }
    };

    private static final DoubleBlockCombiner.Combiner<AbstractOpenableStorageBlockEntity, Optional<IItemHandlerModifiable>> inventoryGetter = new DoubleBlockCombiner.Combiner<>() {
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

    public AbstractChestBlock(BlockBehaviour.Properties properties, ResourceLocation blockId, ResourceLocation blockTier,
                              ResourceLocation openingStat, int slots) {
        super(properties, blockId, blockTier, openingStat, slots);
        this.registerDefaultState(this.getStateDefinition().any().setValue(AbstractChestBlock.CURSED_CHEST_TYPE, CursedChestType.SINGLE)
                                      .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    public static Direction getDirectionToAttached(BlockState state) {
        CursedChestType value = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
        if (value == CursedChestType.TOP) {
            return Direction.DOWN;
        } else if (value == CursedChestType.BACK) {
            return state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        } else if (value == CursedChestType.RIGHT) {
            return state.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise();
        } else if (value == CursedChestType.BOTTOM) {
            return Direction.UP;
        } else if (value == CursedChestType.FRONT) {
            return state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
        } else if (value == CursedChestType.LEFT) {
            return state.getValue(BlockStateProperties.HORIZONTAL_FACING).getCounterClockWise();
        } else if (value == CursedChestType.SINGLE) {
            throw new IllegalArgumentException("BaseChestBlock#getDirectionToAttached received an unexpected state.");
        }
        throw new IllegalArgumentException("Invalid CursedChestType passed.");
    }

    public static DoubleBlockCombiner.BlockType getBlockType(BlockState state) {
        CursedChestType value = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
        if (value == CursedChestType.TOP || value == CursedChestType.LEFT || value == CursedChestType.FRONT) {
            return DoubleBlockCombiner.BlockType.FIRST;
        } else if (value == CursedChestType.BACK || value == CursedChestType.RIGHT || value == CursedChestType.BOTTOM) {
            return DoubleBlockCombiner.BlockType.SECOND;
        } else if (value == CursedChestType.SINGLE) {
            return DoubleBlockCombiner.BlockType.SINGLE;
        }
        throw new IllegalArgumentException("Invalid CursedChestType passed.");
    }

    public static CursedChestType getChestType(Direction facing, Direction offset) {
        if (facing.getClockWise() == offset) {
            return CursedChestType.RIGHT;
        } else if (facing.getCounterClockWise() == offset) {
            return CursedChestType.LEFT;
        } else if (facing == offset) {
            return CursedChestType.BACK;
        } else if (facing == offset.getOpposite()) {
            return CursedChestType.FRONT;
        } else if (offset == Direction.DOWN) {
            return CursedChestType.TOP;
        } else if (offset == Direction.UP) {
            return CursedChestType.BOTTOM;
        }
        return CursedChestType.SINGLE;
    }

    public static Optional<IItemHandlerModifiable> createItemHandler(Level level, BlockState state, BlockPos pos) {
        if (state.getBlock() instanceof AbstractChestBlock<?> block) {
            return block.createCombinedPropertyGetter(state, level, pos, false).apply(AbstractChestBlock.inventoryGetter);
        }
        return Optional.empty();
    }

    @Override
    protected final void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AbstractChestBlock.CURSED_CHEST_TYPE);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        appendAdditionalStateDefinitions(builder);
    }

    @NotNull
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        CursedChestType chestType = CursedChestType.SINGLE;
        Direction direction_1 = context.getHorizontalDirection().getOpposite();
        Direction direction_2 = context.getClickedFace();
        if (context.isSecondaryUseActive()) {
            BlockState state;
            if (direction_2.getAxis().isVertical()) {
                state = level.getBlockState(pos.relative(direction_2.getOpposite()));
                if (state.getBlock() == this && state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) == CursedChestType.SINGLE) {
                    Direction direction_3 = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    if (direction_3.getAxis() != direction_2.getAxis() && direction_3 == direction_1) {
                        chestType = direction_2 == Direction.UP ? CursedChestType.TOP : CursedChestType.BOTTOM;
                    }
                }
            } else {
                Direction offsetDir = direction_2.getOpposite();
                BlockState clickedBlock = level.getBlockState(pos.relative(offsetDir));
                if (clickedBlock.getBlock() == this && clickedBlock.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) == CursedChestType.SINGLE) {
                    if (clickedBlock.getValue(BlockStateProperties.HORIZONTAL_FACING) == direction_2 && clickedBlock.getValue(BlockStateProperties.HORIZONTAL_FACING) == direction_1) {
                        chestType = CursedChestType.FRONT;
                    } else {
                        state = level.getBlockState(pos.relative(direction_2.getOpposite()));
                        if (state.getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue() < 2) {
                            offsetDir = offsetDir.getOpposite();
                        }
                        if (direction_1 == state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
                            chestType = (offsetDir == Direction.WEST || offsetDir == Direction.NORTH) ? CursedChestType.LEFT : CursedChestType.RIGHT;
                        }
                    }
                }
            }
        } else {
            for (Direction dir : Direction.values()) {
                BlockState state = level.getBlockState(pos.relative(dir));
                if (state.getBlock() != this || state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) != CursedChestType.SINGLE || state.getValue(BlockStateProperties.HORIZONTAL_FACING) != direction_1) {
                    continue;
                }
                CursedChestType type = getChestType(direction_1, dir);
                if (type != CursedChestType.SINGLE) {
                    chestType = type;
                    break;
                }
            }
        }
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction_1).setValue(AbstractChestBlock.CURSED_CHEST_TYPE, chestType);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState state, Direction offset, BlockState offsetState, LevelAccessor level,
                                  BlockPos pos, BlockPos offsetPos) {
        DoubleBlockCombiner.BlockType mergeType = getBlockType(state);
        if (mergeType == DoubleBlockCombiner.BlockType.SINGLE) {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (!offsetState.hasProperty(AbstractChestBlock.CURSED_CHEST_TYPE)) {
                return state.setValue(AbstractChestBlock.CURSED_CHEST_TYPE, CursedChestType.SINGLE);
            }
            CursedChestType newType = getChestType(facing, offset);
            if (offsetState.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) == newType.getOpposite() && facing == offsetState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
                return state.setValue(AbstractChestBlock.CURSED_CHEST_TYPE, newType);
            }
        } else if (level.getBlockState(pos.relative(getDirectionToAttached(state))).getBlock() != this) {
            return state.setValue(AbstractChestBlock.CURSED_CHEST_TYPE, CursedChestType.SINGLE);
        }
        return super.updateShape(state, offset, offsetState, level, pos, offsetPos);
    }

    protected void appendAdditionalStateDefinitions(StateDefinition.Builder<Block, BlockState> builder) {

    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    public final NeighborCombineResult<? extends T> createCombinedPropertyGetter(BlockState state, LevelAccessor level, BlockPos pos, boolean alwaysOpen) {
        BiPredicate<LevelAccessor, BlockPos> isChestBlocked = alwaysOpen ? (_level, _pos) -> false : this::isAccessBlocked;
        return DoubleBlockCombiner.combineWithNeigbour(this.getBlockEntityType(), AbstractChestBlock::getBlockType,
                AbstractChestBlock::getDirectionToAttached, BlockStateProperties.HORIZONTAL_FACING, state, level, pos,
                isChestBlocked);
    }

    protected abstract BlockEntityType<T> getBlockEntityType();

    protected boolean isAccessBlocked(LevelAccessor level, BlockPos pos) {
        return false;
    }

    @Override // Keep for hoppers.
    public WorldlyContainer getContainer(BlockState state, LevelAccessor level, BlockPos pos) {
        return this.createCombinedPropertyGetter(state, level, pos, true).apply(containerGetter).orElse(null);
    }
}
