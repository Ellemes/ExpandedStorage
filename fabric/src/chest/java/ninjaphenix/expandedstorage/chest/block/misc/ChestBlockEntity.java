package ninjaphenix.expandedstorage.chest.block.misc;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.AbstractOpenableStorageBlockEntity;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.FabricChestProperties;
import ninjaphenix.expandedstorage.chest.block.ChestBlock;
import org.jetbrains.annotations.Nullable;

public final class ChestBlockEntity extends AbstractOpenableStorageBlockEntity {
    private final ChestLidController lidController;

    public ChestBlockEntity(BlockEntityType<ChestBlockEntity> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state, ((ChestBlock) state.getBlock()).getBlockId());
        lidController = new ChestLidController();
    }

    public static void progressLidAnimation(Level level, BlockPos pos, BlockState state, ChestBlockEntity blockEntity) {
        blockEntity.lidController.tickLid();
    }

    private static void playSound(Level level, BlockPos pos, BlockState state, SoundEvent sound) {
        DoubleBlockCombiner.BlockType mergeType = ChestBlock.getBlockType(state);
        Vec3 soundPos;
        if (mergeType == DoubleBlockCombiner.BlockType.SINGLE) {
            soundPos = Vec3.atCenterOf(pos);
        } else if (mergeType == DoubleBlockCombiner.BlockType.FIRST) {
            soundPos = Vec3.atCenterOf(pos).add(Vec3.atLowerCornerOf(ChestBlock.getDirectionToAttached(state).getNormal()).scale(0.5D));
        } else {
            return;
        }
        level.playSound(null, soundPos.x(), soundPos.y(), soundPos.z(), sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    protected void onOpen(Level level, BlockPos pos, BlockState state) {
        ChestBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_OPEN);
    }

    @Override
    protected void onClose(Level level, BlockPos pos, BlockState state) {
        ChestBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_CLOSE);
    }

    @Override
    protected void onObserverCountChanged(Level level, BlockPos pos, BlockState state, int oldCount, int newCount) {
        level.blockEvent(pos, state.getBlock(), ChestBlock.SET_OBSERVER_COUNT_EVENT, newCount);
    }

    @Override
    protected boolean isThis(Container container) {
        return super.isThis(container) || container instanceof CompoundContainer compoundContainer && compoundContainer.contains(this.getContainerWrapper());
    }

    @Override
    public boolean triggerEvent(int event, int value) {
        if (event == ChestBlock.SET_OBSERVER_COUNT_EVENT) {
            lidController.shouldBeOpen(value > 0);
            return true;
        }
        return super.triggerEvent(event, value);
    }

    // Client only
    public float getLidOpenness(float f) {
        return lidController.getOpenness(f);
    }

    @Override
    protected Storage<ItemVariant> createItemStorage(Level level, BlockState state, BlockPos pos, @Nullable Direction side) {
        return FabricChestProperties.createItemStorage(level, state, pos).orElse(AbstractOpenableStorageBlockEntity.createGenericItemStorage(this));
    }
}
