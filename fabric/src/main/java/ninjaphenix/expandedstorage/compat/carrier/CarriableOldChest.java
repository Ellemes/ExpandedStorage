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
package ninjaphenix.expandedstorage.compat.carrier;

import me.steven.carrier.api.Carriable;
import me.steven.carrier.api.CarriablePlacementContext;
import me.steven.carrier.api.CarrierComponent;
import me.steven.carrier.api.CarryingData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import ninjaphenix.expandedstorage.block.entity.OldChestBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CarriableOldChest implements Carriable<Block> {
    private final Identifier id;
    private final Block parent;

    public CarriableOldChest(Identifier id, Block parent) {
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
    public final ActionResult tryPickup(@NotNull CarrierComponent component, @NotNull World world, @NotNull BlockPos pos, @Nullable Entity entity) {
        if (world.isClient()) return ActionResult.PASS;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof OldChestBlockEntity && !blockEntity.isRemoved()) {
            NbtCompound tag = new NbtCompound();
            tag.put("blockEntity", blockEntity.createNbt());
            CarryingData carrying = new CarryingData(id, tag);
            component.setCarryingData(carrying);
            world.removeBlockEntity(pos);
            world.removeBlock(pos, false); // todo: may return false if failed to remove block?
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @NotNull
    @Override
    public final ActionResult tryPlace(@NotNull CarrierComponent component, @NotNull World world, @NotNull CarriablePlacementContext context) {
        if (world.isClient()) return ActionResult.PASS;
        CarryingData carrying = component.getCarryingData();
        if (carrying == null) return ActionResult.PASS; // Should never be null, but if it is just ignore.
        BlockPos pos = context.getBlockPos();
        BlockState state = this.parent.getPlacementState(new ItemPlacementContext(component.getOwner(), Hand.MAIN_HAND, ItemStack.EMPTY, new BlockHitResult(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), context.getSide(), context.getBlockPos(), false)));
        world.setBlockState(pos, state);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity == null) { // Should be very rare if not impossible to be null.
            world.removeBlock(pos, false);
            return ActionResult.FAIL;
        }
        blockEntity.readNbt(carrying.getBlockEntityTag());
        component.setCarryingData(null);
        return ActionResult.SUCCESS;
    }

    @Override
    public final void render(@NotNull PlayerEntity player, @NotNull CarrierComponent component, @NotNull MatrixStack stack, @NotNull VertexConsumerProvider consumer, float delta, int light) {
        stack.push();
        stack.scale(0.6F, 0.6F, 0.6F);
        float yaw = MathHelper.lerpAngleDegrees(delta, player.prevBodyYaw, player.bodyYaw);
        stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180 - yaw));
        stack.translate(-0.5D, 0.8D, -1.3D);
        this.preRenderBlock(player, component, stack, consumer, delta, light);
        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(this.getParent().getDefaultState(), stack, consumer, light, OverlayTexture.DEFAULT_UV);
        stack.pop();
    }

    protected void preRenderBlock(PlayerEntity player, CarrierComponent component, MatrixStack stack, VertexConsumerProvider consumer, float delta, int light) {

    }
}
