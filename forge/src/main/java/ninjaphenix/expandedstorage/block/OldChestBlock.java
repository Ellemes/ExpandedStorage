package ninjaphenix.expandedstorage.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import ninjaphenix.expandedstorage.OldChestCommon;
import ninjaphenix.expandedstorage.block.misc.OldChestBlockEntity;
import org.jetbrains.annotations.NotNull;

public final class OldChestBlock extends AbstractChestBlock<OldChestBlockEntity> {
    public OldChestBlock(Properties properties, ResourceLocation blockId, ResourceLocation blockTier,
                         ResourceLocation openingStat, int slots) {
        super(properties, blockId, blockTier, openingStat, slots);
    }

    @Override
    protected BlockEntityType<OldChestBlockEntity> getBlockEntityType() {
        return OldChestCommon.getBlockEntityType();
    }

    @Override
    public ResourceLocation getBlockType() {
        return OldChestCommon.BLOCK_TYPE;
    }

    @NotNull
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return new OldChestBlockEntity(OldChestCommon.getBlockEntityType(), this.getBlockId());
    }
}
