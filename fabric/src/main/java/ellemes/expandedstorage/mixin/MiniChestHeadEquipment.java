package ellemes.expandedstorage.mixin;

import ellemes.expandedstorage.block.MiniChestBlock;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
@SuppressWarnings("unused")
public abstract class MiniChestHeadEquipment {

    @SuppressWarnings({"SpellCheckingInspection", "unused"})
    @Inject(
            method = "getEquipmentSlotForItem(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/EquipmentSlot;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void expandedstorage_makeMiniChestEquipable(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir) {
        if (stack.getItem() instanceof BlockItem item && item.getBlock() instanceof MiniChestBlock)
            cir.setReturnValue(EquipmentSlot.HEAD);
    }
}
