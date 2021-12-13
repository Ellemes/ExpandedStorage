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

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import ninjaphenix.container_library.api.helpers.VariableInventory;
import ninjaphenix.container_library.api.inventory.AbstractHandler;
import ninjaphenix.expandedstorage.block.misc.ChestLidAnimator;
import ninjaphenix.expandedstorage.block.misc.ObserverCountManager;
import ninjaphenix.expandedstorage.block.AbstractChestBlock;
import ninjaphenix.expandedstorage.block.ChestBlock;
import ninjaphenix.expandedstorage.block.entity.extendable.OpenableBlockEntity;
import ninjaphenix.expandedstorage.block.strategies.ItemAccess;
import ninjaphenix.expandedstorage.block.strategies.Lockable;
import ninjaphenix.expandedstorage.block.strategies.Observable;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ChestBlockEntity extends OldChestBlockEntity implements TickableBlockEntity {
    private final ObserverCountManager manager = new ObserverCountManager() {
        @Override
        protected void onOpen(Level world, BlockPos pos, BlockState state) {
            ChestBlockEntity.playSound(world, pos, state, SoundEvents.CHEST_OPEN);
        }

        @Override
        protected void onClose(Level world, BlockPos pos, BlockState state) {
            ChestBlockEntity.playSound(world, pos, state, SoundEvents.CHEST_CLOSE);
        }

        @Override
        protected void onObserverCountUpdated(Level world, BlockPos pos, BlockState state, int oldCount, int newCount) {
            world.blockEvent(pos, state.getBlock(), ChestBlock.SET_OBSERVER_COUNT_EVENT, newCount);
        }

        @Override
        protected boolean isPlayerViewing(Player player) {
            Container inventory = ChestBlockEntity.this.getInventory();
            return player.containerMenu instanceof AbstractHandler handler &&
                    (handler.getInventory() == inventory ||
                            handler.getInventory() instanceof VariableInventory variableInventory && variableInventory.containsPart(inventory));
        }
    };
    private final ChestLidAnimator lidController;

    public ChestBlockEntity(BlockEntityType<?> type,
                            Function<OpenableBlockEntity, ItemAccess> access, Supplier<Lockable> lockable) {
        super(type, access, lockable);
        this.setObservable(new Observable() {
            @Override
            public void playerStartViewing(Player player) {
                BlockEntity self = ChestBlockEntity.this;
                manager.open(player, self.getLevel(), self.getBlockPos(), self.getBlockState());
            }

            @Override
            public void playerStopViewing(Player player) {
                BlockEntity self = ChestBlockEntity.this;
                manager.close(player, self.getLevel(), self.getBlockPos(), self.getBlockState());
            }
        });
        lidController = new ChestLidAnimator();
    }

    private static void playSound(Level world, BlockPos pos, BlockState state, SoundEvent sound) {
        DoubleBlockCombiner.BlockType mergeType = AbstractChestBlock.getBlockType(state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE));
        Vec3 soundPos;
        if (mergeType == DoubleBlockCombiner.BlockType.SINGLE) {
            soundPos = Vec3.atCenterOf(pos);
        } else if (mergeType == DoubleBlockCombiner.BlockType.FIRST) {
            soundPos = Vec3.atCenterOf(pos).add(Vec3.atLowerCornerOf(AbstractChestBlock.getDirectionToAttached(state).getNormal()).scale(0.5D));
        } else {
            return;
        }
        world.playSound(null, soundPos.x(), soundPos.y(), soundPos.z(), sound, SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public boolean triggerEvent(int event, int value) {
        if (event == ChestBlock.SET_OBSERVER_COUNT_EVENT) {
            lidController.setOpen(value > 0);
            return true;
        }
        return super.triggerEvent(event, value);
    }

    public float getLidOpenness(float delta) {
        return lidController.getProgress(delta);
    }

    public void updateViewerCount(ServerLevel world, BlockPos pos, BlockState state) {
        manager.updateObserverCount(world, pos, state);
    }

    @Override
    public void tick() {
        lidController.tick();
    }
}
