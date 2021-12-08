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
package ninjaphenix.expandedstorage.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import ninjaphenix.container_library.api.inventory.AbstractHandler;
import ninjaphenix.expandedstorage.block.OpenableBlock;
import ninjaphenix.expandedstorage.block.entity.extendable.ExposedInventoryBlockEntity;
import ninjaphenix.expandedstorage.block.entity.extendable.OpenableBlockEntity;
import ninjaphenix.expandedstorage.block.strategies.ItemAccess;
import ninjaphenix.expandedstorage.block.strategies.Lockable;

import java.util.function.Function;
import java.util.function.Supplier;

public final class BarrelBlockEntity extends ExposedInventoryBlockEntity {
    private final ViewerCountManager manager = new ViewerCountManager() {
        @Override
        protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
            BarrelBlockEntity.playSound(world, state, pos, SoundEvents.BLOCK_BARREL_OPEN);
            BarrelBlockEntity.updateBlockState(world, state, pos, true);
        }

        @Override
        protected void onContainerClose(World world, BlockPos pos, BlockState state) {
            BarrelBlockEntity.playSound(world, state, pos, SoundEvents.BLOCK_BARREL_CLOSE);
            BarrelBlockEntity.updateBlockState(world, state, pos, false);
        }

        @Override
        protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldCount, int newCount) {

        }

        @Override
        protected boolean isPlayerViewing(PlayerEntity player) {
            return player.currentScreenHandler instanceof AbstractHandler handler && handler.getInventory() == BarrelBlockEntity.this;
        }
    };

    public BarrelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, Identifier blockId,
                             Function<OpenableBlockEntity, ItemAccess> access, Supplier<Lockable> lockable) {
        super(type, pos, state, blockId, ((OpenableBlock) state.getBlock()).getInventoryTitle(), ((OpenableBlock) state.getBlock()).getSlotCount());
        this.setItemAccess(access.apply(this));
        this.setLockable(lockable.get());
    }

    private static void playSound(World world, BlockState state, BlockPos pos, SoundEvent sound) {
        Vec3i facingVector = state.get(Properties.FACING).getVector();
        double X = pos.getX() + 0.5D + facingVector.getX() / 2.0D;
        double Y = pos.getY() + 0.5D + facingVector.getY() / 2.0D;
        double Z = pos.getZ() + 0.5D + facingVector.getZ() / 2.0D;
        world.playSound(null, X, Y, Z, sound, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
    }

    private static void updateBlockState(World world, BlockState state, BlockPos pos, boolean open) {
        world.setBlockState(pos, state.with(Properties.OPEN, open), Block.NOTIFY_ALL);
    }

    @Override
    public void onOpen(PlayerEntity player) {
        if (player.isSpectator()) return;
        manager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (player.isSpectator()) return;
        manager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
    }

    public void updateViewerCount(ServerWorld world, BlockPos pos, BlockState state) {
        manager.updateViewerCount(world, pos, state);
    }
}
