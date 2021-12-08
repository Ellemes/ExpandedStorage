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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
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

/**
 * Note to self, do not rename, used by chest tracker.
 * @deprecated Use {@link ExpandedStorageAccessors} instead.
 */
@Deprecated
@ApiStatus.Internal
public class AbstractChestBlock extends OpenableBlock implements InventoryProvider {
    /**
     * Note to self, do not rename, used by chest tracker.
     * @deprecated Use {@link ExpandedStorageAccessors} instead.
     */
    @Deprecated
    @ApiStatus.Internal
    public static final EnumProperty<CursedChestType> CURSED_CHEST_TYPE = EnumProperty.of("type", CursedChestType.class);
    private static final Property<OldChestBlockEntity, SidedInventory> INVENTORY_GETTER = new Property<>() {
        @Override
        public SidedInventory get(OldChestBlockEntity first, OldChestBlockEntity second) {
            SidedInventory cachedInventory = first.getCachedDoubleInventory();
            if (cachedInventory == null) {
                first.setCachedDoubleInventory(second);
                return first.getCachedDoubleInventory();
            }
            return cachedInventory;
        }

        @Override
        public SidedInventory get(OldChestBlockEntity single) {
            return single.getInventory();
        }
    };

    public AbstractChestBlock(Settings settings, Identifier blockId, Identifier blockTier, Identifier openingStat, int slotCount) {
        super(settings, blockId, blockTier, openingStat, slotCount);
        this.setDefaultState(this.getDefaultState()
                                 .with(AbstractChestBlock.CURSED_CHEST_TYPE, CursedChestType.SINGLE)
                                 .with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    public static <T extends OldChestBlockEntity> PropertyRetriever<T> createPropertyRetriever(AbstractChestBlock block, BlockState state, WorldAccess world, BlockPos pos, boolean retrieveBlockedChests) {
        BiPredicate<WorldAccess, BlockPos> isChestBlocked = retrieveBlockedChests ? (_world, _pos) -> false : block::isAccessBlocked;
        return PropertyRetriever.create(block.getBlockEntityType(),
                (s) -> AbstractChestBlock.getBlockType(s.get(AbstractChestBlock.CURSED_CHEST_TYPE)),
                (s, facing) -> AbstractChestBlock.getDirectionToAttached(s.get(AbstractChestBlock.CURSED_CHEST_TYPE), facing),
                (s) -> s.get(Properties.HORIZONTAL_FACING), state, world, pos, isChestBlocked);
    }

    protected boolean isAccessBlocked(WorldAccess world, BlockPos pos) {
        return false;
    }

    protected <T extends OldChestBlockEntity> BlockEntityType<T> getBlockEntityType() {
        //noinspection unchecked
        return (BlockEntityType<T>) Common.getOldChestBlockEntityType();
    }

    @Override
    public Identifier getBlockType() {
        return Common.OLD_CHEST_BLOCK_TYPE;
    }

    public static Direction getDirectionToAttached(CursedChestType type, Direction facing) {
        if (type == CursedChestType.TOP) {
            return Direction.DOWN;
        } else if (type == CursedChestType.BACK) {
            return facing;
        } else if (type == CursedChestType.RIGHT) {
            return facing.rotateYClockwise();
        } else if (type == CursedChestType.BOTTOM) {
            return Direction.UP;
        } else if (type == CursedChestType.FRONT) {
            return facing.getOpposite();
        } else if (type == CursedChestType.LEFT) {
            return facing.rotateYCounterclockwise();
        } else if (type == CursedChestType.SINGLE) {
            throw new IllegalArgumentException("AbstractChestBlock#getDirectionToAttached received an unexpected chest type.");
        }
        throw new IllegalArgumentException("AbstractChestBlock#getDirectionToAttached received an unknown chest type.");
    }

    /**
     * Note to self, do not rename, used by chest tracker.
     * @deprecated Use {@link ExpandedStorageAccessors} instead.
     */
    @Deprecated
    @ApiStatus.Internal
    public static Direction getDirectionToAttached(BlockState state) {
        return AbstractChestBlock.getDirectionToAttached(state.get(AbstractChestBlock.CURSED_CHEST_TYPE), state.get(Properties.HORIZONTAL_FACING));
    }

    public static DoubleBlockProperties.Type getBlockType(CursedChestType type) {
        if (type == CursedChestType.TOP || type == CursedChestType.LEFT || type == CursedChestType.FRONT) {
            return DoubleBlockProperties.Type.FIRST;
        } else if (type == CursedChestType.BACK || type == CursedChestType.RIGHT || type == CursedChestType.BOTTOM) {
            return DoubleBlockProperties.Type.SECOND;
        } else if (type == CursedChestType.SINGLE) {
            return DoubleBlockProperties.Type.SINGLE;
        }
        throw new IllegalArgumentException("Invalid CursedChestType passed.");
    }

    public static CursedChestType getChestType(Direction facing, Direction offset) {
        if (facing.rotateYClockwise() == offset) {
            return CursedChestType.RIGHT;
        } else if (facing.rotateYCounterclockwise() == offset) {
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
    protected final void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AbstractChestBlock.CURSED_CHEST_TYPE);
        builder.add(Properties.HORIZONTAL_FACING);
        this.appendAdditionalStateDefinitions(builder);
    }

    protected void appendAdditionalStateDefinitions(StateManager.Builder<Block, BlockState> builder) {

    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return this.getBlockEntityType().instantiate(pos, state);
    }

    @NotNull
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        CursedChestType chestType = CursedChestType.SINGLE;
        Direction chestForwardDir = context.getPlayerFacing().getOpposite();
        Direction clickedFace = context.getSide();
        if (context.shouldCancelInteraction()) {
            Direction offsetDir = clickedFace.getOpposite();
            BlockState offsetState = world.getBlockState(pos.offset(offsetDir));
            if (offsetState.isOf(this) && offsetState.get(Properties.HORIZONTAL_FACING) == chestForwardDir && offsetState.get(AbstractChestBlock.CURSED_CHEST_TYPE) == CursedChestType.SINGLE) {
                chestType = AbstractChestBlock.getChestType(chestForwardDir, offsetDir);
            }
        } else {
            for (Direction dir : Direction.values()) {
                BlockState offsetState = world.getBlockState(pos.offset(dir));
                if (offsetState.isOf(this) && offsetState.get(Properties.HORIZONTAL_FACING) == chestForwardDir && offsetState.get(AbstractChestBlock.CURSED_CHEST_TYPE) == CursedChestType.SINGLE) {
                    CursedChestType type = AbstractChestBlock.getChestType(chestForwardDir, dir);
                    if (type != CursedChestType.SINGLE) {
                        chestType = type;
                        break;
                    }
                }
            }
        }
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, chestForwardDir).with(AbstractChestBlock.CURSED_CHEST_TYPE, chestType);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState getStateForNeighborUpdate(BlockState state, Direction offset, BlockState offsetState, WorldAccess world,
                                                BlockPos pos, BlockPos offsetPos) {
        DoubleBlockProperties.Type mergeType = AbstractChestBlock.getBlockType(state.get(AbstractChestBlock.CURSED_CHEST_TYPE));
        if (mergeType == DoubleBlockProperties.Type.SINGLE) {
            Direction facing = state.get(Properties.HORIZONTAL_FACING);
            if (!offsetState.isOf(this)) {
                return state.with(AbstractChestBlock.CURSED_CHEST_TYPE, CursedChestType.SINGLE);
            }
            CursedChestType newType = AbstractChestBlock.getChestType(facing, offset);
            if (offsetState.get(AbstractChestBlock.CURSED_CHEST_TYPE) == newType.getOpposite() && facing == offsetState.get(Properties.HORIZONTAL_FACING)) {
                return state.with(AbstractChestBlock.CURSED_CHEST_TYPE, newType);
            }
        } else {
            BlockState otherState = world.getBlockState(pos.offset(AbstractChestBlock.getDirectionToAttached(state)));
            if (!otherState.isOf(this) ||
                    otherState.get(CURSED_CHEST_TYPE) != state.get(CURSED_CHEST_TYPE).getOpposite() ||
                    state.get(Properties.HORIZONTAL_FACING) != state.get(Properties.HORIZONTAL_FACING)) {
                return state.with(AbstractChestBlock.CURSED_CHEST_TYPE, CursedChestType.SINGLE);
            }
        }
        return super.getStateForNeighborUpdate(state, offset, offsetState, world, pos, offsetPos);
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        return AbstractChestBlock.createPropertyRetriever(this, state, world, pos, true).get(AbstractChestBlock.INVENTORY_GETTER).orElse(null);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(Properties.HORIZONTAL_FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        if (state.get(AbstractChestBlock.CURSED_CHEST_TYPE) == CursedChestType.SINGLE) {
            return state.with(Properties.HORIZONTAL_FACING, rotation.rotate(state.get(Properties.HORIZONTAL_FACING)));
        }
        return super.rotate(state, rotation);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        SidedInventory inventory = this.getInventory(state, world, pos);
        if (inventory != null) return ScreenHandler.calculateComparatorOutput(inventory);
        return super.getComparatorOutput(state, world, pos);
    }

    @Override
    public OpenableBlockEntityV2 getOpenableBlockEntity(World world, BlockState state, BlockPos pos) {
        return AbstractChestBlock.createPropertyRetriever(this, state, world, pos, false).get(new Property<OldChestBlockEntity, OpenableBlockEntityV2>() {
            @Override
            public OpenableBlockEntityV2 get(OldChestBlockEntity first, OldChestBlockEntity second) {
                Text name = first.hasCustomName() ? first.getName() : second.hasCustomName() ? second.getName() : Utils.translation("container.expandedstorage.generic_double", first.getName());
                return new OpenableBlockEntitiesV2(name, first, second);
            }

            @Override
            public OpenableBlockEntityV2 get(OldChestBlockEntity single) {
                return single;
            }
        }).orElse(null);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean bl) {
        if (state.contains(AbstractChestBlock.CURSED_CHEST_TYPE) && newState.contains(AbstractChestBlock.CURSED_CHEST_TYPE)) {
            CursedChestType oldChestType = state.get(AbstractChestBlock.CURSED_CHEST_TYPE);
            CursedChestType newChestType = newState.get(AbstractChestBlock.CURSED_CHEST_TYPE);
            if (oldChestType != CursedChestType.SINGLE && newChestType == CursedChestType.SINGLE) {
                if (AbstractChestBlock.getBlockType(oldChestType) == DoubleBlockProperties.Type.FIRST) {
                    if (world.getBlockEntity(pos) instanceof OldChestBlockEntity entity) {
                        entity.invalidateDoubleBlockCache();
                    }
                }
                world.updateComparators(pos, newState.getBlock());
            } else if (oldChestType == CursedChestType.SINGLE && newChestType != CursedChestType.SINGLE) {
                BlockPos otherPos = pos.offset(AbstractChestBlock.getDirectionToAttached(newState));
                world.updateComparators(otherPos, world.getBlockState(otherPos).getBlock());
            }
        }
        super.onStateReplaced(state, world, pos, newState, bl);
    }
}
