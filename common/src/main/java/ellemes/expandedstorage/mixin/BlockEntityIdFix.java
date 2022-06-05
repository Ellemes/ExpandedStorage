package ellemes.expandedstorage.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
public abstract class BlockEntityIdFix {
    @Inject(
            method = "loadStatic(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/level/block/entity/BlockEntity;",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private static void expandedstorage_fixChestIds(BlockPos pos, BlockState state, CompoundTag nbt, CallbackInfoReturnable<BlockEntity> cir) {
        String id = nbt.getString("id");
        if (id.equals("expandedstorage:cursed_chest")) {
            nbt.putString("id", "expandedstorage:chest");
            cir.setReturnValue(BlockEntity.loadStatic(pos, state, nbt));
        } else if (id.equals("expandedstorage:old_cursed_chest")) {
            nbt.putString("id", "expandedstorage:old_chest");
            cir.setReturnValue(BlockEntity.loadStatic(pos, state, nbt));
        }
    }
}
