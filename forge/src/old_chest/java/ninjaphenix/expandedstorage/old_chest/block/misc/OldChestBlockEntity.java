package ninjaphenix.expandedstorage.old_chest.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandlerModifiable;
import ninjaphenix.expandedstorage.base.internal_api.block.AbstractChestBlock;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.AbstractOpenableStorageBlockEntity;
import ninjaphenix.expandedstorage.old_chest.block.OldChestBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OldChestBlockEntity extends AbstractOpenableStorageBlockEntity {
    public OldChestBlockEntity(BlockEntityType<OldChestBlockEntity> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state, ((OldChestBlock) state.getBlock()).blockId());
    }

    @NotNull
    @Override
    protected IItemHandlerModifiable createItemHandler(Level level, BlockState state, BlockPos pos, @Nullable Direction side) {
        return AbstractChestBlock.createItemHandler(level, state, pos).orElse(AbstractOpenableStorageBlockEntity.createGenericItemHandler(this));
    }
}
