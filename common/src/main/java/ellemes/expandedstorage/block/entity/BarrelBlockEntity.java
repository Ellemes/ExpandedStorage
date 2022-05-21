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
package ellemes.expandedstorage.block.entity;

import ellemes.container_library.api.inventory.AbstractHandler;
import ellemes.expandedstorage.block.OpenableBlock;
import ellemes.expandedstorage.block.entity.extendable.ExposedInventoryBlockEntity;
import ellemes.expandedstorage.block.entity.extendable.OpenableBlockEntity;
import ellemes.expandedstorage.block.strategies.ItemAccess;
import ellemes.expandedstorage.block.strategies.Lockable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.Function;
import java.util.function.Supplier;

public final class BarrelBlockEntity extends ExposedInventoryBlockEntity {
    private final ContainerOpenersCounter manager = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level world, BlockPos pos, BlockState state) {
            BarrelBlockEntity.playSound(world, state, pos, SoundEvents.BARREL_OPEN);
            BarrelBlockEntity.updateBlockState(world, state, pos, true);
        }

        @Override
        protected void onClose(Level world, BlockPos pos, BlockState state) {
            BarrelBlockEntity.playSound(world, state, pos, SoundEvents.BARREL_CLOSE);
            BarrelBlockEntity.updateBlockState(world, state, pos, false);
        }

        @Override
        protected void openerCountChanged(Level world, BlockPos pos, BlockState state, int oldCount, int newCount) {

        }

        @Override
        protected boolean isOwnContainer(Player player) {
            return player.containerMenu instanceof AbstractHandler handler && handler.getInventory() == BarrelBlockEntity.this;
        }
    };

    public BarrelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, ResourceLocation blockId,
                             Function<OpenableBlockEntity, ItemAccess> access, Supplier<Lockable> lockable) {
        super(type, pos, state, blockId, ((OpenableBlock) state.getBlock()).getInventoryTitle(), ((OpenableBlock) state.getBlock()).getSlotCount());
        this.setItemAccess(access.apply(this));
        this.setLockable(lockable.get());
    }

    private static void playSound(Level world, BlockState state, BlockPos pos, SoundEvent sound) {
        Vec3i facingVector = state.getValue(BlockStateProperties.FACING).getNormal();
        double X = pos.getX() + 0.5D + facingVector.getX() / 2.0D;
        double Y = pos.getY() + 0.5D + facingVector.getY() / 2.0D;
        double Z = pos.getZ() + 0.5D + facingVector.getZ() / 2.0D;
        world.playSound(null, X, Y, Z, sound, SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
    }

    private static void updateBlockState(Level world, BlockState state, BlockPos pos, boolean open) {
        world.setBlock(pos, state.setValue(BlockStateProperties.OPEN, open), Block.UPDATE_ALL);
    }

    @Override
    public void startOpen(Player player) {
        if (player.isSpectator()) return;
        manager.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }

    @Override
    public void stopOpen(Player player) {
        if (player.isSpectator()) return;
        manager.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }

    public void updateViewerCount(ServerLevel world, BlockPos pos, BlockState state) {
        manager.recheckOpeners(world, pos, state);
    }
}
