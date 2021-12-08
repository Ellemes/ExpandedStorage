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

import net.minecraft.block.BlockState;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestLidAnimator;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ninjaphenix.container_library.api.helpers.VariableInventory;
import ninjaphenix.container_library.api.inventory.AbstractHandler;
import ninjaphenix.expandedstorage.block.AbstractChestBlock;
import ninjaphenix.expandedstorage.block.ChestBlock;
import ninjaphenix.expandedstorage.block.entity.extendable.OpenableBlockEntity;
import ninjaphenix.expandedstorage.block.strategies.ItemAccess;
import ninjaphenix.expandedstorage.block.strategies.Lockable;
import ninjaphenix.expandedstorage.block.strategies.Observable;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ChestBlockEntity extends OldChestBlockEntity {
    private final ViewerCountManager manager = new ViewerCountManager() {
        @Override
        protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
            ChestBlockEntity.playSound(world, pos, state, SoundEvents.BLOCK_CHEST_OPEN);
        }

        @Override
        protected void onContainerClose(World world, BlockPos pos, BlockState state) {
            ChestBlockEntity.playSound(world, pos, state, SoundEvents.BLOCK_CHEST_CLOSE);
        }

        @Override
        protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldCount, int newCount) {
            world.addSyncedBlockEvent(pos, state.getBlock(), ChestBlock.SET_OBSERVER_COUNT_EVENT, newCount);
        }

        @Override
        protected boolean isPlayerViewing(PlayerEntity player) {
            Inventory inventory = ChestBlockEntity.this.getInventory();
            return player.currentScreenHandler instanceof AbstractHandler handler &&
                    (handler.getInventory() == inventory ||
                            handler.getInventory() instanceof VariableInventory variableInventory && variableInventory.containsPart(inventory));
        }
    };
    private final ChestLidAnimator lidController;

    public ChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, Identifier blockId,
                            Function<OpenableBlockEntity, ItemAccess> access, Supplier<Lockable> lockable) {
        super(type, pos, state, blockId, access, lockable);
        this.setObservable(new Observable() {
            @Override
            public void playerStartViewing(PlayerEntity player) {
                BlockEntity self = ChestBlockEntity.this;
                manager.openContainer(player, self.getWorld(), self.getPos(), self.getCachedState());
            }

            @Override
            public void playerStopViewing(PlayerEntity player) {
                BlockEntity self = ChestBlockEntity.this;
                manager.closeContainer(player, self.getWorld(), self.getPos(), self.getCachedState());
            }
        });
        lidController = new ChestLidAnimator();
    }

    @SuppressWarnings("unused")
    public static void progressLidAnimation(World world, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        ((ChestBlockEntity) blockEntity).lidController.step();
    }

    private static void playSound(World world, BlockPos pos, BlockState state, SoundEvent sound) {
        DoubleBlockProperties.Type mergeType = AbstractChestBlock.getBlockType(state.get(AbstractChestBlock.CURSED_CHEST_TYPE));
        Vec3d soundPos;
        if (mergeType == DoubleBlockProperties.Type.SINGLE) {
            soundPos = Vec3d.ofCenter(pos);
        } else if (mergeType == DoubleBlockProperties.Type.FIRST) {
            soundPos = Vec3d.ofCenter(pos).add(Vec3d.of(AbstractChestBlock.getDirectionToAttached(state).getVector()).multiply(0.5D));
        } else {
            return;
        }
        world.playSound(null, soundPos.getX(), soundPos.getY(), soundPos.getZ(), sound, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public boolean onSyncedBlockEvent(int event, int value) {
        if (event == ChestBlock.SET_OBSERVER_COUNT_EVENT) {
            lidController.setOpen(value > 0);
            return true;
        }
        return super.onSyncedBlockEvent(event, value);
    }

    public float getLidOpenness(float delta) {
        return lidController.getProgress(delta);
    }

    public void updateViewerCount(ServerWorld world, BlockPos pos, BlockState state) {
        manager.updateViewerCount(world, pos, state);
    }
}
