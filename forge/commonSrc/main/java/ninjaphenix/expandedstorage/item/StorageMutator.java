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

import ninjaphenix.expandedstorage.Common;
import ninjaphenix.expandedstorage.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class StorageMutator extends Item {
    public StorageMutator(Item.Properties settings) {
        super(settings);
    }

    private static MutationMode getMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("mode", Tag.TAG_BYTE))
            tag.putByte("mode", (byte) 0);

        return MutationMode.from(tag.getByte("mode"));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        MutatorBehaviour behaviour = Common.getMutatorBehaviour(state.getBlock(), StorageMutator.getMode(stack));
        if (behaviour != null) {
            InteractionResult returnValue = behaviour.attempt(context, world, state, pos, stack);
            if (returnValue.shouldSwing()) {
                //noinspection ConstantConditions
                context.getPlayer().getCooldowns().addCooldown(this, Utils.QUARTER_SECOND);
            }
            return returnValue;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            ItemStack stack = player.getItemInHand(hand);
            CompoundTag tag = stack.getOrCreateTag();
            MutationMode nextMode = StorageMutator.getMode(stack).next();
            tag.putByte("mode", nextMode.toByte());
            if (tag.contains("pos"))
                tag.remove("pos");

            if (!world.isClientSide())
                player.displayClientMessage(new TranslatableComponent("tooltip.expandedstorage.storage_mutator.description_" + nextMode, Utils.ALT_USE), true);

            player.getCooldowns().addCooldown(this, Utils.QUARTER_SECOND);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level world, Player player) {
        super.onCraftedBy(stack, world, player);
        StorageMutator.getMode(stack);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        StorageMutator.getMode(stack);
        return stack;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(group)) {
            stacks.add(this.getDefaultInstance());
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag context) {
        MutationMode mode = StorageMutator.getMode(stack);
        list.add(new TranslatableComponent("tooltip.expandedstorage.storage_mutator.tool_mode", new TranslatableComponent("tooltip.expandedstorage.storage_mutator." + mode)).withStyle(ChatFormatting.GRAY));
        list.add(Utils.translation("tooltip.expandedstorage.storage_mutator.description_" + mode, Utils.ALT_USE).withStyle(ChatFormatting.GRAY));
    }
}
