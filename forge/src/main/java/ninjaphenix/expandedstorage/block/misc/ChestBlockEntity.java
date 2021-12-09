package ninjaphenix.expandedstorage.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandlerModifiable;
import ninjaphenix.expandedstorage.block.AbstractChestBlock;
import ninjaphenix.expandedstorage.block.ChestBlock;
import org.jetbrains.annotations.Nullable;

public final class ChestBlockEntity extends AbstractOpenableStorageBlockEntity implements TickableBlockEntity {
    private int observerCount;
    private float lastAnimationAngle;
    private float animationAngle;
    private int ticksOpen;

    public ChestBlockEntity(BlockEntityType<ChestBlockEntity> blockEntityType, ResourceLocation blockId) {
        super(blockEntityType, blockId);
    }

    private static int maybeUpdateObserverCount(Level level, ChestBlockEntity entity, int ticksOpen, int x, int y, int z, int observerCount) {
        if (!level.isClientSide() && observerCount != 0 && (ticksOpen + x + y + z) % 200 == 0) {
            return AbstractOpenableStorageBlockEntity.countObservers(level, entity.getContainerWrapper(), x, y, z);
        }
        return observerCount;
    }

    @Override
    public boolean triggerEvent(int event, int value) {
        if (event == ChestBlock.SET_OBSERVER_COUNT_EVENT) {
            observerCount = value;
            return true;
        }
        return super.triggerEvent(event, value);
    }

    // Client only
    public float getLidOpenness(float f) {
        return Mth.lerp(f, lastAnimationAngle, animationAngle);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void tick() {
        observerCount = ChestBlockEntity.maybeUpdateObserverCount(level, this, ++ticksOpen, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), observerCount);
        lastAnimationAngle = animationAngle;
        if (observerCount > 0 && animationAngle == 0.0F) {
            this.playSound(SoundEvents.CHEST_OPEN);
        }
        if (observerCount == 0 && animationAngle > 0.0F || observerCount > 0 && animationAngle < 1.0F) {
            animationAngle = Mth.clamp(animationAngle + (observerCount > 0 ? 0.1F : -0.1F), 0, 1);
            if (animationAngle < 0.5F && lastAnimationAngle >= 0.5F) {
                this.playSound(SoundEvents.CHEST_CLOSE);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void playSound(SoundEvent sound) {
        BlockState state = this.getBlockState();
        DoubleBlockCombiner.BlockType mergeType = ChestBlock.getBlockType(state);
        Vec3 soundPos;
        if (mergeType == DoubleBlockCombiner.BlockType.SINGLE) {
            soundPos = Vec3.atCenterOf(worldPosition);
        } else if (mergeType == DoubleBlockCombiner.BlockType.FIRST) {
            soundPos = Vec3.atCenterOf(worldPosition).add(Vec3.atLowerCornerOf(ChestBlock.getDirectionToAttached(state).getNormal()).scale(0.5D));
        } else {
            return;
        }
        level.playSound(null, soundPos.x(), soundPos.y(), soundPos.z(), sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void startOpen(Player player) {
        if (player.isSpectator()) {
            return;
        }
        if (observerCount < 0) {
            observerCount = 0;
        }
        observerCount++;
        this.onMenuOpenOrClosed();
    }

    @Override
    public void stopOpen(final Player player) {
        if (player.isSpectator()) {
            return;
        }
        observerCount--;
        this.onMenuOpenOrClosed();
    }

    @SuppressWarnings("ConstantConditions")
    private void onMenuOpenOrClosed() {
        if (this.getBlockState().getBlock() instanceof ChestBlock block) {
            level.blockEvent(worldPosition, block, ChestBlock.SET_OBSERVER_COUNT_EVENT, observerCount);
            level.updateNeighborsAt(worldPosition, block);
        }
    }

    @Override
    protected IItemHandlerModifiable createItemHandler(Level level, BlockState state, BlockPos pos, @Nullable Direction side) {
        return AbstractChestBlock.createItemHandler(level, state, pos).orElse(AbstractOpenableStorageBlockEntity.createGenericItemHandler(this));
    }
}
