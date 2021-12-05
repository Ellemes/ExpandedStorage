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

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
public abstract class BlockEntityIdFix {
    @Inject(
            method = "createFromNbt(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/block/entity/BlockEntity;",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private static void expandedstorage_fixChestIds(BlockPos pos, BlockState state, NbtCompound nbt, CallbackInfoReturnable<BlockEntity> cir) {
        String id = nbt.getString("id");
        if (id.equals("expandedstorage:cursed_chest")) {
            nbt.putString("id", "expandedstorage:chest");
            cir.setReturnValue(BlockEntity.createFromNbt(pos, state, nbt));
        } else if (id.equals("expandedstorage:old_cursed_chest")) {
            nbt.putString("id", "expandedstorage:old_chest");
            cir.setReturnValue(BlockEntity.createFromNbt(pos, state, nbt));
        }
    }
}
