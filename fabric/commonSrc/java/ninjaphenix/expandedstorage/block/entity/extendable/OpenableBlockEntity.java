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
package ninjaphenix.expandedstorage.block.entity.extendable;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import ninjaphenix.container_library.api.v2.OpenableBlockEntityV2;
import ninjaphenix.expandedstorage.Utils;
import ninjaphenix.expandedstorage.block.strategies.ItemAccess;
import ninjaphenix.expandedstorage.block.strategies.Lockable;

public abstract class OpenableBlockEntity extends BlockEntity implements OpenableBlockEntityV2 {
    private ResourceLocation blockId;
    private Component defaultName;
    private ItemAccess itemAccess;
    private Lockable lockable;
    private Component customName;

    public OpenableBlockEntity(BlockEntityType<?> type) {
        super(type);
        //this.blockId = blockId;
        //this.defaultName = defaultName;
    }

    @Override
    public boolean canBeUsedBy(ServerPlayer player) {
        //noinspection ConstantConditions
        return this.getLevel().getBlockEntity(this.getBlockPos()) == this &&
                player.distanceToSqr(Vec3.atCenterOf(this.getBlockPos())) <= 64.0D &&
                this.getLockable().canPlayerOpenLock(player);
    }

    @Override
    public Component getInventoryTitle() {
        return this.getName();
    }

    public abstract NonNullList<ItemStack> getItems();

    @Override
    public final void load(BlockState state, CompoundTag tag) {
        super.load(state, tag);
        lockable.readLock(tag);
        if (tag.contains("CustomName", Utils.NBT_STRING_TYPE)) {
            customName = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
        this.loadAdditional(state, tag);
        this.initialize(state, tag);
    }

    protected void loadAdditional(BlockState state, CompoundTag tag) {

    }

    protected void initialize(BlockState state, CompoundTag tag) {

    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        lockable.writeLock(tag);
        if (this.hasCustomName()) {
            tag.putString("CustomName", Component.Serializer.toJson(customName));
        }
        return tag;
    }

    public final ResourceLocation getBlockId() {
        return blockId;
    }

    public ItemAccess getItemAccess() {
        return itemAccess;
    }

    protected void setItemAccess(ItemAccess itemAccess) {
        if (this.itemAccess == null) this.itemAccess = itemAccess;
    }

    public Lockable getLockable() {
        return lockable;
    }

    protected void setLockable(Lockable lockable) {
        if (this.lockable == null) this.lockable = lockable;
    }

    public final boolean hasCustomName() {
        return customName != null;
    }

    public final void setCustomName(Component name) {
        customName = name;
    }

    public final Component getName() {
        return this.hasCustomName() ? customName : defaultName;
    }
}
