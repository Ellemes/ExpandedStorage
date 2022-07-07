package ellemes.expandedstorage.entity;

import ellemes.expandedstorage.block.ChestBlock;
import ellemes.expandedstorage.misc.ExposedInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ChestMinecart extends AbstractMinecart implements ExposedInventory {
    private final NonNullList<ItemStack> inventory;
    private final Item dropItem;
    private final BlockState renderBlockState;

    public ChestMinecart(EntityType<?> entityType, Level level, Item dropItem, ChestBlock block) {
        super(entityType, level);
        this.dropItem = dropItem;
        this.renderBlockState = block.defaultBlockState();
        inventory = NonNullList.withSize(block.getSlotCount(), ItemStack.EMPTY);
    }

    @Override
    protected Item getDropItem() {
        return dropItem;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(dropItem);
    }

    @Override
    public BlockState getDisplayBlockState() {
        return renderBlockState;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand interactionHand) {
        return super.interact(player, interactionHand);
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        if (!this.level.isClientSide() && reason.shouldDestroy()) {
            Containers.dropContents(this.level, this, this);
        }
        super.remove(reason);
    }

    @Override
    public Type getMinecartType() {
        return Type.CHEST;
    }

    public static ChestMinecart createMinecart(Level level, Vec3 pos, ResourceLocation cartItemId) {
        ChestMinecart cart = (ChestMinecart) Registry.ENTITY_TYPE.get(cartItemId).create(level);
        cart.setPos(pos);
        return cart;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return this.isAlive() && player.distanceToSqr(this) <= 36.0D;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.saveInventoryToTag(tag);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.loadInventoryFromTag(tag);
    }
}
