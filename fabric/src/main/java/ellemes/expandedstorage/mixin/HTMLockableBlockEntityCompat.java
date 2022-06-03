package ellemes.expandedstorage.mixin;

import com.github.fabricservertools.htm.HTMContainerLock;
import com.github.fabricservertools.htm.api.LockableObject;
import ellemes.expandedstorage.block.entity.extendable.OpenableBlockEntity;
import ellemes.expandedstorage.fabric.compat.htm.HTMLockable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("unused")
@Mixin(OpenableBlockEntity.class)
public abstract class HTMLockableBlockEntityCompat extends BlockEntity implements LockableObject {
    public HTMLockableBlockEntityCompat(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public HTMContainerLock getLock() {
        return ((HTMLockable) self().getLockable()).getLock();
    }

    @Override
    public void setLock(HTMContainerLock lock) {
        ((HTMLockable) self().getLockable()).setLock(lock);
    }

    private OpenableBlockEntity self() {
        //noinspection ConstantConditions
        return (OpenableBlockEntity) (Object) this;
    }
}
