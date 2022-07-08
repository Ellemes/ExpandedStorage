package ellemes.expandedstorage.block;

import ellemes.container_library.api.v3.OpenableInventory;
import ellemes.container_library.api.v3.context.BlockContext;
import ellemes.container_library.api.v3.helpers.OpenableInventories;
import ellemes.expandedstorage.Common;
import ellemes.expandedstorage.Utils;
import ellemes.expandedstorage.api.EsChestType;
import ellemes.expandedstorage.block.entity.OldChestBlockEntity;
import ellemes.expandedstorage.block.misc.Property;
import ellemes.expandedstorage.block.misc.PropertyRetriever;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;

public class AbstractChestBlock extends OpenableBlock implements WorldlyContainerHolder {
    public static final EnumProperty<EsChestType> CURSED_CHEST_TYPE = EnumProperty.create("type", EsChestType.class);
    private static final Property<OldChestBlockEntity, WorldlyContainer> INVENTORY_GETTER = new Property<>() {
        @Override
        public WorldlyContainer get(OldChestBlockEntity first, OldChestBlockEntity second) {
            WorldlyContainer cachedInventory = first.getCachedDoubleInventory();
            if (cachedInventory == null) {
                first.setCachedDoubleInventory(second);
                return first.getCachedDoubleInventory();
            }
            return cachedInventory;
        }

        @Override
        public WorldlyContainer get(OldChestBlockEntity single) {
            return single.getInventory();
        }
    };

    public AbstractChestBlock(Properties settings, ResourceLocation blockId, ResourceLocation blockTier, ResourceLocation openingStat, int slotCount) {
        super(settings, blockId, blockTier, openingStat, slotCount);
        this.registerDefaultState(this.defaultBlockState()
                                      .setValue(AbstractChestBlock.CURSED_CHEST_TYPE, EsChestType.SINGLE)
                                      .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    public static <T extends OldChestBlockEntity> PropertyRetriever<T> createPropertyRetriever(AbstractChestBlock block, BlockState state, LevelAccessor world, BlockPos pos, boolean retrieveBlockedChests) {
        BiPredicate<LevelAccessor, BlockPos> isChestBlocked = retrieveBlockedChests ? (_world, _pos) -> false : block::isAccessBlocked;
        return PropertyRetriever.create(block.getBlockEntityType(),
                (s) -> AbstractChestBlock.getBlockType(s.getValue(AbstractChestBlock.CURSED_CHEST_TYPE)),
                (s, facing) -> AbstractChestBlock.getDirectionToAttached(s.getValue(AbstractChestBlock.CURSED_CHEST_TYPE), facing),
                (s) -> s.getValue(BlockStateProperties.HORIZONTAL_FACING), state, world, pos, isChestBlocked);
    }

    public static Direction getDirectionToAttached(EsChestType type, Direction facing) {
        if (type == EsChestType.TOP) {
            return Direction.DOWN;
        } else if (type == EsChestType.BACK) {
            return facing;
        } else if (type == EsChestType.RIGHT) {
            return facing.getClockWise();
        } else if (type == EsChestType.BOTTOM) {
            return Direction.UP;
        } else if (type == EsChestType.FRONT) {
            return facing.getOpposite();
        } else if (type == EsChestType.LEFT) {
            return facing.getCounterClockWise();
        } else if (type == EsChestType.SINGLE) {
            throw new IllegalArgumentException("AbstractChestBlock#getDirectionToAttached received an unexpected chest type.");
        }
        throw new IllegalArgumentException("AbstractChestBlock#getDirectionToAttached received an unknown chest type.");
    }

    public static Direction getDirectionToAttached(BlockState state) {
        return AbstractChestBlock.getDirectionToAttached(state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE), state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    public static DoubleBlockCombiner.BlockType getBlockType(EsChestType type) {
        if (type == EsChestType.TOP || type == EsChestType.LEFT || type == EsChestType.FRONT) {
            return DoubleBlockCombiner.BlockType.FIRST;
        } else if (type == EsChestType.BACK || type == EsChestType.RIGHT || type == EsChestType.BOTTOM) {
            return DoubleBlockCombiner.BlockType.SECOND;
        } else if (type == EsChestType.SINGLE) {
            return DoubleBlockCombiner.BlockType.SINGLE;
        }
        throw new IllegalArgumentException("Invalid EsChestType passed.");
    }

    public static EsChestType getChestType(Direction facing, Direction offset) {
        if (facing.getClockWise() == offset) {
            return EsChestType.RIGHT;
        } else if (facing.getCounterClockWise() == offset) {
            return EsChestType.LEFT;
        } else if (facing == offset) {
            return EsChestType.BACK;
        } else if (facing == offset.getOpposite()) {
            return EsChestType.FRONT;
        } else if (offset == Direction.DOWN) {
            return EsChestType.TOP;
        } else if (offset == Direction.UP) {
            return EsChestType.BOTTOM;
        }
        return EsChestType.SINGLE;
    }

    protected boolean isAccessBlocked(LevelAccessor world, BlockPos pos) {
        return false;
    }

    protected <T extends OldChestBlockEntity> BlockEntityType<T> getBlockEntityType() {
        //noinspection unchecked
        return (BlockEntityType<T>) Common.getOldChestBlockEntityType();
    }

    @Override
    public ResourceLocation getBlockType() {
        return Common.OLD_CHEST_BLOCK_TYPE;
    }

    @Override
    protected final void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AbstractChestBlock.CURSED_CHEST_TYPE);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        this.appendAdditionalStateDefinitions(builder);
    }

    protected void appendAdditionalStateDefinitions(StateDefinition.Builder<Block, BlockState> builder) {

    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return this.getBlockEntityType().create(pos, state);
    }

    @NotNull
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        EsChestType chestType = EsChestType.SINGLE;
        Direction chestForwardDir = context.getHorizontalDirection().getOpposite();
        Direction clickedFace = context.getClickedFace();
        if (context.isSecondaryUseActive()) {
            Direction offsetDir = clickedFace.getOpposite();
            BlockState offsetState = world.getBlockState(pos.relative(offsetDir));
            if (offsetState.is(this) && offsetState.getValue(BlockStateProperties.HORIZONTAL_FACING) == chestForwardDir && offsetState.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) == EsChestType.SINGLE) {
                chestType = AbstractChestBlock.getChestType(chestForwardDir, offsetDir);
            }
        } else {
            for (Direction dir : Direction.values()) {
                BlockState offsetState = world.getBlockState(pos.relative(dir));
                if (offsetState.is(this) && offsetState.getValue(BlockStateProperties.HORIZONTAL_FACING) == chestForwardDir && offsetState.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) == EsChestType.SINGLE) {
                    EsChestType type = AbstractChestBlock.getChestType(chestForwardDir, dir);
                    if (type != EsChestType.SINGLE) {
                        chestType = type;
                        break;
                    }
                }
            }
        }
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, chestForwardDir).setValue(AbstractChestBlock.CURSED_CHEST_TYPE, chestType);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState state, Direction offset, BlockState offsetState, LevelAccessor world,
                                  BlockPos pos, BlockPos offsetPos) {
        DoubleBlockCombiner.BlockType mergeType = AbstractChestBlock.getBlockType(state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE));
        if (mergeType == DoubleBlockCombiner.BlockType.SINGLE) {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (!offsetState.is(this)) {
                return state.setValue(AbstractChestBlock.CURSED_CHEST_TYPE, EsChestType.SINGLE);
            }
            EsChestType newType = AbstractChestBlock.getChestType(facing, offset);
            if (offsetState.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) == newType.getOpposite() && facing == offsetState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
                return state.setValue(AbstractChestBlock.CURSED_CHEST_TYPE, newType);
            }
        } else {
            BlockState otherState = world.getBlockState(pos.relative(AbstractChestBlock.getDirectionToAttached(state)));
            if (!otherState.is(this) ||
                    otherState.getValue(CURSED_CHEST_TYPE) != state.getValue(CURSED_CHEST_TYPE).getOpposite() ||
                    state.getValue(BlockStateProperties.HORIZONTAL_FACING) != state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
                return state.setValue(AbstractChestBlock.CURSED_CHEST_TYPE, EsChestType.SINGLE);
            }
        }
        return super.updateShape(state, offset, offsetState, world, pos, offsetPos);
    }

    @Override
    public WorldlyContainer getContainer(BlockState state, LevelAccessor world, BlockPos pos) {
        return AbstractChestBlock.createPropertyRetriever(this, state, world, pos, true).get(AbstractChestBlock.INVENTORY_GETTER).orElse(null);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rotation) {
        if (state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) == EsChestType.SINGLE) {
            return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
        }
        return super.rotate(state, rotation);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        WorldlyContainer inventory = this.getContainer(state, world, pos);
        if (inventory != null) return AbstractContainerMenu.getRedstoneSignalFromContainer(inventory);
        return super.getAnalogOutputSignal(state, world, pos);
    }

    @Override
    public OpenableInventory getOpenableInventory(BlockContext context) {
        Level world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        return AbstractChestBlock.createPropertyRetriever(this, state, world, pos, false).get(new Property<OldChestBlockEntity, OpenableInventory>() {
            @Override
            public OpenableInventory get(OldChestBlockEntity first, OldChestBlockEntity second) {
                Component name = first.hasCustomName() ? first.getName() : second.hasCustomName() ? second.getName() : Utils.translation("container.expandedstorage.generic_double", first.getName());
                return OpenableInventories.of(name, first, second);
            }

            @Override
            public OpenableInventory get(OldChestBlockEntity single) {
                return single;
            }
        }).orElse(null);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean bl) {
        if (state.hasProperty(AbstractChestBlock.CURSED_CHEST_TYPE) && newState.hasProperty(AbstractChestBlock.CURSED_CHEST_TYPE)) {
            EsChestType oldChestType = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
            EsChestType newChestType = newState.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
            if (oldChestType != EsChestType.SINGLE && newChestType == EsChestType.SINGLE) {
                if (AbstractChestBlock.getBlockType(oldChestType) == DoubleBlockCombiner.BlockType.FIRST) {
                    if (world.getBlockEntity(pos) instanceof OldChestBlockEntity entity) {
                        entity.invalidateDoubleBlockCache();
                    }
                }
                world.updateNeighbourForOutputSignal(pos, newState.getBlock());
            } else if (oldChestType == EsChestType.SINGLE && newChestType != EsChestType.SINGLE) {
                BlockPos otherPos = pos.relative(AbstractChestBlock.getDirectionToAttached(newState));
                world.updateNeighbourForOutputSignal(otherPos, world.getBlockState(otherPos).getBlock());
            }
        }
        super.onRemove(state, world, pos, newState, bl);
    }
}
