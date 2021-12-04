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

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ninjaphenix.container_library.api.v2.OpenableBlockEntityV2;
import ninjaphenix.expandedstorage.block.strategies.ItemAccess;
import ninjaphenix.expandedstorage.block.strategies.Lockable;

public abstract class OpenableBlockEntity extends BlockEntity implements OpenableBlockEntityV2 {
    private final Identifier blockId;
    private final Text defaultName;
    private ItemAccess itemAccess;
    private Lockable lockable;
    private Text customName;

    public OpenableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, Identifier blockId, Text defaultName) {
        super(type, pos, state);
        this.blockId = blockId;
        this.defaultName = defaultName;
    }

    @Override
    public boolean canBeUsedBy(ServerPlayerEntity player) {
        //noinspection ConstantConditions
        return this.getWorld().getBlockEntity(this.getPos()) == this &&
                player.squaredDistanceTo(Vec3d.ofCenter(this.getPos())) <= 64.0D &&
                this.getLockable().canPlayerOpenLock(player);
    }

    @Override
    public Text getInventoryTitle() {
        return this.getName();
    }

    public abstract DefaultedList<ItemStack> getItems();

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        lockable.readLock(tag);
        if (tag.contains("CustomName", NbtElement.STRING_TYPE)) {
            customName = Text.Serializer.fromJson(tag.getString("CustomName"));
        }
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        lockable.writeLock(tag);
        if (this.hasCustomName()) {
            tag.putString("CustomName", Text.Serializer.toJson(customName));
        }
    }

    public final Identifier getBlockId() {
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

    public final void setCustomName(Text name) {
        customName = name;
    }

    public final Text getName() {
        return this.hasCustomName() ? customName : defaultName;
    }
}
