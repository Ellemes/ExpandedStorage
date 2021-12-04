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
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import ninjaphenix.expandedstorage.Common;
import ninjaphenix.expandedstorage.block.entity.ChestBlockEntity;
import ninjaphenix.expandedstorage.block.entity.OldChestBlockEntity;
import ninjaphenix.expandedstorage.block.misc.CursedChestType;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public final class ChestBlock extends AbstractChestBlock implements Waterloggable {
    public static final int SET_OBSERVER_COUNT_EVENT = 1;
    private static final VoxelShape[] SHAPES = {
            Block.createCuboidShape(1, 0, 0, 15, 14, 15), // Horizontal shapes, depends on orientation and chest type.
            Block.createCuboidShape(1, 0, 1, 16, 14, 15),
            Block.createCuboidShape(1, 0, 1, 15, 14, 16),
            Block.createCuboidShape(0, 0, 1, 15, 14, 15),
            Block.createCuboidShape(1, 0, 1, 15, 14, 15), // Top shape.
            Block.createCuboidShape(1, 0, 1, 15, 16, 15), // Bottom shape.
            Block.createCuboidShape(1, 0, 1, 15, 14, 15)  // Single shape.
    };

    public ChestBlock(Settings settings, Identifier blockId, Identifier blockTier, Identifier openingStat, int slotCount) {
        super(settings, blockId, blockTier, openingStat, slotCount);
        this.setDefaultState(this.getDefaultState().with(Properties.WATERLOGGED, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        CursedChestType type = state.get(AbstractChestBlock.CURSED_CHEST_TYPE);
        if (type == CursedChestType.TOP) {
            return ChestBlock.SHAPES[4];
        } else if (type == CursedChestType.BOTTOM) {
            return ChestBlock.SHAPES[5];
        } else if (type == CursedChestType.SINGLE) {
            return ChestBlock.SHAPES[6];
        } else {
            int index = (state.get(Properties.HORIZONTAL_FACING).getHorizontal() + type.getOffset()) % 4;
            return ChestBlock.SHAPES[index];
        }
    }

    @Override
    protected void appendAdditionalStateDefinitions(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        FluidState fluidState = context.getWorld().getFluidState(context.getBlockPos());
        return super.getPlacementState(context).with(Properties.WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState otherState, WorldAccess world, BlockPos pos, BlockPos otherPos) {
        if (state.get(Properties.WATERLOGGED)) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, otherState, world, pos, otherPos);
    }


    @Override
    @SuppressWarnings("deprecation")
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected <T extends OldChestBlockEntity> BlockEntityType<T> getBlockEntityType() {
        //noinspection unchecked
        return (BlockEntityType<T>) Common.getChestBlockEntityType();
    }

    @Override
    public Identifier getBlockType() {
        return Common.CHEST_BLOCK_TYPE;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> blockEntityType) {
        return world.isClient() && blockEntityType == this.getBlockEntityType() ? ChestBlockEntity::progressLidAnimation : null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int event, int value) {
        super.onSyncedBlockEvent(state, world, pos, event, value);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity != null && blockEntity.onSyncedBlockEvent(event, value);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBlockEntity(pos) instanceof ChestBlockEntity entity) {
            entity.updateViewerCount(world, pos, state);
        }
    }

    @Override
    protected boolean isAccessBlocked(WorldAccess world, BlockPos pos) {
        return net.minecraft.block.ChestBlock.isChestBlocked(world, pos);
    }
}
