/*
 * Copyright 2021-2022 Ellemes
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
package ellemes.expandedstorage.block;

import ellemes.expandedstorage.Common;
import ellemes.expandedstorage.block.entity.ChestBlockEntity;
import ellemes.expandedstorage.block.entity.OldChestBlockEntity;
import ninjaphenix.expandedstorage.block.AbstractChestBlock;
import ninjaphenix.expandedstorage.block.misc.CursedChestType;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class ChestBlock extends AbstractChestBlock implements SimpleWaterloggedBlock {
    public static final int SET_OBSERVER_COUNT_EVENT = 1;
    private static final VoxelShape[] SHAPES = {
            Block.box(1, 0, 0, 15, 14, 15), // Horizontal shapes, depends on orientation and chest type.
            Block.box(1, 0, 1, 16, 14, 15),
            Block.box(1, 0, 1, 15, 14, 16),
            Block.box(0, 0, 1, 15, 14, 15),
            Block.box(1, 0, 1, 15, 14, 15), // Top shape.
            Block.box(1, 0, 1, 15, 16, 15), // Bottom shape.
            Block.box(1, 0, 1, 15, 14, 15)  // Single shape.
    };

    public ChestBlock(Properties settings, ResourceLocation blockId, ResourceLocation blockTier, ResourceLocation openingStat, int slotCount) {
        super(settings, blockId, blockTier, openingStat, slotCount);
        this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        CursedChestType type = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
        if (type == CursedChestType.TOP) {
            return ChestBlock.SHAPES[4];
        } else if (type == CursedChestType.BOTTOM) {
            return ChestBlock.SHAPES[5];
        } else if (type == CursedChestType.SINGLE) {
            return ChestBlock.SHAPES[6];
        } else {
            int index = (state.getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue() + type.getOffset()) % 4;
            return ChestBlock.SHAPES[index];
        }
    }

    @Override
    protected void appendAdditionalStateDefinitions(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return super.getStateForPlacement(context).setValue(BlockStateProperties.WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor world, BlockPos pos, BlockPos otherPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, direction, otherState, world, pos, otherPos);
    }


    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected <T extends OldChestBlockEntity> BlockEntityType<T> getBlockEntityType() {
        //noinspection unchecked
        return (BlockEntityType<T>) Common.getChestBlockEntityType();
    }

    @Override
    public ResourceLocation getBlockType() {
        return Common.CHEST_BLOCK_TYPE;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> blockEntityType) {
        return world.isClientSide() && blockEntityType == this.getBlockEntityType() ? ChestBlockEntity::progressLidAnimation : null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int event, int value) {
        super.triggerEvent(state, world, pos, event, value);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(event, value);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        if (world.getBlockEntity(pos) instanceof ChestBlockEntity entity) {
            entity.updateViewerCount(world, pos, state);
        }
    }

    @Override
    protected boolean isAccessBlocked(LevelAccessor world, BlockPos pos) {
        return net.minecraft.world.level.block.ChestBlock.isChestBlockedAt(world, pos);
    }
}
