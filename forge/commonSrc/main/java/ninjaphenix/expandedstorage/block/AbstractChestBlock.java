/*
 * Copyright 2021 NinjaPhenix
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ninjaphenix.expandedstorage.block;

import ninjaphenix.container_library.api.v2.OpenableBlockEntityV2;
import ninjaphenix.container_library.api.v2.helpers.OpenableBlockEntitiesV2;
import ninjaphenix.expandedstorage.Common;
import ninjaphenix.expandedstorage.Utils;
import ninjaphenix.expandedstorage.api.ExpandedStorageAccessors;
import ninjaphenix.expandedstorage.block.entity.OldChestBlockEntity;
import ninjaphenix.expandedstorage.block.misc.CursedChestType;
import ninjaphenix.expandedstorage.block.misc.Property;
import ninjaphenix.expandedstorage.block.misc.PropertyRetriever;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;
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

/**
 * Note to self, do not rename, used by chest tracker.
 * @deprecated Use {@link ExpandedStorageAccessors} instead.
 */
@Deprecated
@ApiStatus.Internal
public class AbstractChestBlock extends OpenableBlock implements WorldlyContainerHolder {
    /**
     * Note to self, do not rename, used by chest tracker.
     * @deprecated Use {@link ExpandedStorageAccessors} instead.
     */
    @Deprecated
    @ApiStatus.Internal
    public static final EnumProperty<CursedChestType> CURSED_CHEST_TYPE = EnumProperty.create("type", CursedChestType.class);
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
                                 .setValue(AbstractChestBlock.CURSED_CHEST_TYPE, CursedChestType.SINGLE)
                                 .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    public static <T extends OldChestBlockEntity> PropertyRetriever<T> createPropertyRetriever(AbstractChestBlock block, BlockState state, LevelAccessor world, BlockPos pos, boolean retrieveBlockedChests) {
        BiPredicate<LevelAccessor, BlockPos> isChestBlocked = retrieveBlockedChests ? (_world, _pos) -> false : block::isAccessBlocked;
        return PropertyRetriever.create(block.getBlockEntityType(), AbstractChestBlock::getBlockType, AbstractChestBlock::getDirectionToAttached,
                (s) -> s.getValue(BlockStateProperties.HORIZONTAL_FACING), state, world, pos, isChestBlocked);
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

    /**
     * Note to self, do not rename, used by chest tracker.
     * @deprecated Use {@link ExpandedStorageAccessors} instead.
     */
    @Deprecated
    @ApiStatus.Internal
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
        CursedChestType chestType = CursedChestType.SINGLE;
        Direction chestForwardDir = context.getHorizontalDirection().getOpposite();
        Direction clickedFace = context.getClickedFace();
        if (context.isSecondaryUseActive()) {
            Direction offsetDir = clickedFace.getOpposite();
            BlockState offsetState = world.getBlockState(pos.relative(offsetDir));
            if (offsetState.is(this) && offsetState.getValue(BlockStateProperties.HORIZONTAL_FACING) == chestForwardDir && offsetState.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) == CursedChestType.SINGLE) {
                chestType = AbstractChestBlock.getChestType(chestForwardDir, offsetDir);
            }
        } else {
            for (Direction dir : Direction.values()) {
                BlockState offsetState = world.getBlockState(pos.relative(dir));
                if (offsetState.is(this) && offsetState.getValue(BlockStateProperties.HORIZONTAL_FACING) == chestForwardDir && offsetState.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) == CursedChestType.SINGLE) {
                    CursedChestType type = AbstractChestBlock.getChestType(chestForwardDir, dir);
                    if (type != CursedChestType.SINGLE) {
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
        DoubleBlockCombiner.BlockType mergeType = AbstractChestBlock.getBlockType(state);
        if (mergeType == DoubleBlockCombiner.BlockType.SINGLE) {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (!offsetState.is(this)) {
                return state.setValue(AbstractChestBlock.CURSED_CHEST_TYPE, CursedChestType.SINGLE);
            }
            CursedChestType newType = AbstractChestBlock.getChestType(facing, offset);
            if (offsetState.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) == newType.getOpposite() && facing == offsetState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
                return state.setValue(AbstractChestBlock.CURSED_CHEST_TYPE, newType);
            }
        } else {
            BlockState otherState = world.getBlockState(pos.relative(AbstractChestBlock.getDirectionToAttached(state)));
            if (!otherState.is(this) ||
                    otherState.getValue(CURSED_CHEST_TYPE) != state.getValue(CURSED_CHEST_TYPE).getOpposite() ||
                    state.getValue(BlockStateProperties.HORIZONTAL_FACING) != state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
                return state.setValue(AbstractChestBlock.CURSED_CHEST_TYPE, CursedChestType.SINGLE);
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
        if (state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) == CursedChestType.SINGLE) {
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
    public OpenableBlockEntityV2 getOpenableBlockEntity(Level world, BlockState state, BlockPos pos) {
        return AbstractChestBlock.createPropertyRetriever(this, state, world, pos, false).get(new Property<OldChestBlockEntity, OpenableBlockEntityV2>() {
            @Override
            public OpenableBlockEntityV2 get(OldChestBlockEntity first, OldChestBlockEntity second) {
                Component name = first.hasCustomName() ? first.getName() : second.hasCustomName() ? second.getName() : Utils.translation("container.expandedstorage.generic_double", first.getName());
                return new OpenableBlockEntitiesV2(name, first, second);
            }

            @Override
            public OpenableBlockEntityV2 get(OldChestBlockEntity single) {
                return single;
            }
        }).orElse(null);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean bl) {
        if (state.hasProperty(AbstractChestBlock.CURSED_CHEST_TYPE) && newState.hasProperty(AbstractChestBlock.CURSED_CHEST_TYPE)) {
            CursedChestType oldChestType = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
            CursedChestType newChestType = newState.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
            if (oldChestType != CursedChestType.SINGLE && newChestType == CursedChestType.SINGLE) {
                if (AbstractChestBlock.getBlockType(state) == DoubleBlockCombiner.BlockType.FIRST) {
                    if (world.getBlockEntity(pos) instanceof OldChestBlockEntity entity) {
                        entity.invalidateDoubleBlockCache();
                    }
                }
                world.updateNeighbourForOutputSignal(pos, newState.getBlock());
            } else if (oldChestType == CursedChestType.SINGLE && newChestType != CursedChestType.SINGLE) {
                BlockPos otherPos = pos.relative(AbstractChestBlock.getDirectionToAttached(newState));
                world.updateNeighbourForOutputSignal(otherPos, world.getBlockState(otherPos).getBlock());
            }
        }
        super.onRemove(state, world, pos, newState, bl);
    }
}
