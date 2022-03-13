/*
 * Copyright 2021 NinjaPhenix
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ninjaphenix.expandedstorage.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import ninjaphenix.expandedstorage.block.MiniChestBlock;
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
