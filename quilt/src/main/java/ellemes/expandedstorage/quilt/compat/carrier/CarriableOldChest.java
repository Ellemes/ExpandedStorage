/*
 * Copyright 2021-2022 Ellemes
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
package ellemes.expandedstorage.quilt.compat.carrier;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import ellemes.expandedstorage.block.entity.OldChestBlockEntity;
import me.steven.carrier.api.Carriable;
import me.steven.carrier.api.CarriablePlacementContext;
import me.steven.carrier.api.CarrierComponent;
import me.steven.carrier.api.CarryingData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CarriableOldChest implements Carriable<Block> {
    private final ResourceLocation id;
    private final Block parent;

    public CarriableOldChest(ResourceLocation id, Block parent) {
        this.id = id;
        this.parent = parent;
    }

    @NotNull
    @Override
    public final Block getParent() {
        return parent;
    }

    @NotNull
    @Override
    public final InteractionResult tryPickup(@NotNull CarrierComponent component, @NotNull Level world, @NotNull BlockPos pos, @Nullable Entity entity) {
        if (world.isClientSide()) return InteractionResult.PASS;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof OldChestBlockEntity && !blockEntity.isRemoved()) {
            CompoundTag tag = new CompoundTag();
            tag.put("blockEntity", blockEntity.saveWithoutMetadata());
            CarryingData carrying = new CarryingData(id, tag);
            component.setCarryingData(carrying);
            world.removeBlockEntity(pos);
            world.removeBlock(pos, false); // todo: may return false if failed to remove block?
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @NotNull
    @Override
    public final InteractionResult tryPlace(@NotNull CarrierComponent component, @NotNull Level world, @NotNull CarriablePlacementContext context) {
        if (world.isClientSide()) return InteractionResult.PASS;
        CarryingData carrying = component.getCarryingData();
        if (carrying == null) return InteractionResult.PASS; // Should never be null, but if it is just ignore.
        BlockPos pos = context.getBlockPos();
        BlockState state = this.parent.getStateForPlacement(new BlockPlaceContext(component.getOwner(), InteractionHand.MAIN_HAND, ItemStack.EMPTY, new BlockHitResult(new Vec3(pos.getX(), pos.getY(), pos.getZ()), context.getSide(), context.getBlockPos(), false)));
        world.setBlockAndUpdate(pos, state);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity == null) { // Should be very rare if not impossible to be null.
            world.removeBlock(pos, false);
            return InteractionResult.FAIL;
        }
        blockEntity.load(carrying.getBlockEntityTag());
        component.setCarryingData(null);
        return InteractionResult.SUCCESS;
    }

    @Override
    public final void render(@NotNull Player player, @NotNull CarrierComponent component, @NotNull PoseStack stack, @NotNull MultiBufferSource consumer, float delta, int light) {
        stack.pushPose();
        stack.scale(0.6F, 0.6F, 0.6F);
        float yaw = Mth.approachDegrees(delta, player.yBodyRotO, player.yBodyRot);
        stack.mulPose(Vector3f.YP.rotationDegrees(180 - yaw));
        stack.translate(-0.5D, 0.8D, -1.3D);
        this.preRenderBlock(player, component, stack, consumer, delta, light);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(this.getParent().defaultBlockState(), stack, consumer, light, OverlayTexture.NO_OVERLAY);
        stack.popPose();
    }

    protected void preRenderBlock(Player player, CarrierComponent component, PoseStack stack, MultiBufferSource consumer, float delta, int light) {

    }
}
