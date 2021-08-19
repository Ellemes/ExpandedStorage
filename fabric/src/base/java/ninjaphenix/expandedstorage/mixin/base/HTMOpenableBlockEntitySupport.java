package ninjaphenix.expandedstorage.mixin.base;

import com.github.fabricservertools.htm.HTMContainerLock;
import com.github.fabricservertools.htm.api.LockableObject;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.FabricChestProperties;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.AbstractOpenableStorageBlockEntity;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.AbstractStorageBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractOpenableStorageBlockEntity.class)
public abstract class HTMOpenableBlockEntitySupport extends AbstractStorageBlockEntity implements LockableObject {
    private HTMContainerLock htmLock = new HTMContainerLock();

    public HTMOpenableBlockEntitySupport(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

    @Inject(method = "load(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ContainerHelper;loadAllItems(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/NonNullList;)V"))
    private void loadHTMLock(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains(FabricChestProperties.LOCK_TAG_KEY, Tag.TAG_COMPOUND)) {
            htmLock.fromTag(tag.getCompound(FabricChestProperties.LOCK_TAG_KEY));
        }
    }

    @Inject(method = "save(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;", at = @At("RETURN"), cancellable = true)
    private void saveHTMLock(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag subTag = new CompoundTag();
        htmLock.toTag(subTag);
        tag.put(FabricChestProperties.LOCK_TAG_KEY, subTag);
        cir.setReturnValue(tag);
    }

    @Override
    public boolean canPlayerInteractWith(ServerPlayer player) {
        return htmLock.canOpen(player);
    }

    @Override
    public HTMContainerLock getLock() {
        return htmLock;
    }

    @Override
    public void setLock(HTMContainerLock lock) {
        htmLock = lock;
    }
}
