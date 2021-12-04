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
package ninjaphenix.expandedstorage.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ninjaphenix.expandedstorage.Common;
import ninjaphenix.expandedstorage.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class StorageMutator extends Item {
    public StorageMutator(Item.Settings settings) {
        super(settings);
    }

    private static MutationMode getMode(ItemStack stack) {
        NbtCompound tag = stack.getOrCreateNbt();
        if (!tag.contains("mode", NbtElement.BYTE_TYPE))
            tag.putByte("mode", (byte) 0);

        return MutationMode.from(tag.getByte("mode"));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        MutatorBehaviour behaviour = Common.getMutatorBehaviour(state.getBlock(), StorageMutator.getMode(stack));
        if (behaviour != null) {
            ActionResult returnValue = behaviour.attempt(context, world, state, pos, stack);
            if (returnValue.shouldSwingHand()) {
                //noinspection ConstantConditions
                context.getPlayer().getItemCooldownManager().set(this, Utils.QUARTER_SECOND);
            }
            return returnValue;
        }
        return ActionResult.FAIL;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (player.isSneaking()) {
            ItemStack stack = player.getStackInHand(hand);
            NbtCompound tag = stack.getOrCreateNbt();
            MutationMode nextMode = StorageMutator.getMode(stack).next();
            tag.putByte("mode", nextMode.toByte());
            if (tag.contains("pos"))
                tag.remove("pos");

            if (!world.isClient())
                player.sendMessage(new TranslatableText("tooltip.expandedstorage.storage_mutator.description_" + nextMode, Utils.ALT_USE), true);

            player.getItemCooldownManager().set(this, Utils.QUARTER_SECOND);
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.pass(player.getStackInHand(hand));
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        super.onCraft(stack, world, player);
        StorageMutator.getMode(stack);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = super.getDefaultStack();
        StorageMutator.getMode(stack);
        return stack;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            stacks.add(this.getDefaultStack());
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> list, TooltipContext context) {
        MutationMode mode = StorageMutator.getMode(stack);
        list.add(new TranslatableText("tooltip.expandedstorage.storage_mutator.tool_mode", new TranslatableText("tooltip.expandedstorage.storage_mutator." + mode)).formatted(Formatting.GRAY));
        list.add(Utils.translation("tooltip.expandedstorage.storage_mutator.description_" + mode, Utils.ALT_USE).formatted(Formatting.GRAY));
    }
}
