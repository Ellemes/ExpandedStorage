package ellemes.expandedstorage.mixin;

import com.github.fabricservertools.htm.HTMContainerLock;
import com.github.fabricservertools.htm.api.LockableChestBlock;
import ellemes.expandedstorage.quilt.compat.htm.HTMChestProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ninjaphenix.expandedstorage.block.AbstractChestBlock;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;

@Mixin(AbstractChestBlock.class)
public abstract class HTMChestCompat implements LockableChestBlock {
    @Override
    public HTMContainerLock getLockAt(BlockState state, Level world, BlockPos pos) {
        //noinspection ConstantConditions
        return AbstractChestBlock.createPropertyRetriever((AbstractChestBlock) (Object) this, state, world, pos, true).get(HTMChestProperties.LOCK_PROPERTY).orElse(null);
    }

    @Override
    public Optional<BlockEntity> getUnlockedPart(BlockState state, Level world, BlockPos pos) {
        //noinspection ConstantConditions
        return AbstractChestBlock.createPropertyRetriever((AbstractChestBlock) (Object) this, state, world, pos, true).get(HTMChestProperties.UNLOCKED_BE_PROPERTY);
    }

}
