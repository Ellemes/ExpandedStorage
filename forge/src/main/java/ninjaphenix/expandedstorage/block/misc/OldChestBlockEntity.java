package ninjaphenix.expandedstorage.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandlerModifiable;
import ninjaphenix.expandedstorage.block.AbstractChestBlock;
import org.jetbrains.annotations.Nullable;

public final class OldChestBlockEntity extends AbstractOpenableStorageBlockEntity {
    public OldChestBlockEntity(BlockEntityType<OldChestBlockEntity> blockEntityType, ResourceLocation blockId) {
        super(blockEntityType, blockId);
    }

    @Override
    protected IItemHandlerModifiable createItemHandler(Level level, BlockState state, BlockPos pos, @Nullable Direction side) {
        return AbstractChestBlock.createItemHandler(level, state, pos).orElse(AbstractOpenableStorageBlockEntity.createGenericItemHandler(this));
    }
}
