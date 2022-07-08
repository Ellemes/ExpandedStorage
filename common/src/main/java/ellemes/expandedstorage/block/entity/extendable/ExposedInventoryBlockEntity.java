package ellemes.expandedstorage.block.entity.extendable;

import ellemes.expandedstorage.misc.ExposedInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ExposedInventoryBlockEntity extends OpenableBlockEntity implements ExposedInventory {
    private final NonNullList<ItemStack> items;

    public ExposedInventoryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, ResourceLocation blockId, Component defaultName, int inventorySize) {
        super(type, pos, state, blockId, defaultName);
        items = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.isValidAndPlayerInRange(player);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.loadInventoryFromTag(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.saveInventoryToTag(tag);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public Container getInventory() {
        return this;
    }
}
