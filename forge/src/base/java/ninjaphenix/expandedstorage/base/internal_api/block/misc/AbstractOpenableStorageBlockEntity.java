package ninjaphenix.expandedstorage.base.internal_api.block.misc;

import com.google.common.base.Suppliers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import ninjaphenix.expandedstorage.base.internal_api.block.AbstractOpenableStorageBlock;
import ninjaphenix.expandedstorage.base.internal_api.inventory.AbstractMenu;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@Internal
@Experimental
public abstract class AbstractOpenableStorageBlockEntity extends AbstractStorageBlockEntity implements ICapabilityProvider {
    private final ResourceLocation blockId;
    private final ContainerOpenersCounter observerCounter;
    protected Component menuTitle;
    private int slots;
    private NonNullList<ItemStack> inventory;
    private LazyOptional<IItemHandlerModifiable> itemHandler;
    private final Supplier<Container> container = Suppliers.memoize(() -> new Container() {
        @Override
        public int getContainerSize() {
            return slots;
        }

        @Override
        public boolean isEmpty() {
            for (ItemStack stack : inventory) {
                if (!stack.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public ItemStack getItem(int slot) {
            return inventory.get(slot);
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            ItemStack stack = ContainerHelper.removeItem(inventory, slot, amount);
            if (!stack.isEmpty()) {
                this.setChanged();
            }
            return stack;
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            return ContainerHelper.takeItem(inventory, slot);
        }

        @Override
        public void setItem(int slot, ItemStack stack) {
            inventory.set(slot, stack);
            if (stack.getCount() > this.getMaxStackSize()) {
                stack.setCount(this.getMaxStackSize());
            }
            this.setChanged();
        }

        @Override
        public void setChanged() {
            AbstractOpenableStorageBlockEntity.this.setChanged();
        }

        @Override
        public boolean stillValid(Player player) {
            return AbstractOpenableStorageBlockEntity.this.canContinueUse(player);
        }

        @Override
        public void clearContent() {
            inventory.clear();
        }

        @Override
        public void startOpen(Player player) {
            AbstractOpenableStorageBlockEntity.this.playerStartUsing(player);
        }

        @Override
        public void stopOpen(Player player) {
            AbstractOpenableStorageBlockEntity.this.playerStopUsing(player);
        }
    });

    public AbstractOpenableStorageBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, ResourceLocation blockId) {
        super(blockEntityType, pos, state);
        this.observerCounter = new ContainerOpenersCounter() {
            @Override
            protected void onOpen(Level level, BlockPos pos, BlockState state) {
                AbstractOpenableStorageBlockEntity.this.onOpen(level, pos, state);
            }

            @Override
            protected void onClose(Level level, BlockPos pos, BlockState state) {
                AbstractOpenableStorageBlockEntity.this.onClose(level, pos, state);
            }

            @Override
            protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int oldCount, int newCount) {
                AbstractOpenableStorageBlockEntity.this.onObserverCountChanged(level, pos, state, oldCount, newCount);
            }

            @Override
            protected boolean isOwnContainer(Player player) {
                if (player.containerMenu instanceof AbstractMenu<?>) {
                    return AbstractOpenableStorageBlockEntity.this.isThis(((AbstractMenu<?>) player.containerMenu).getContainer());
                } else {
                    return false;
                }
            }
        };
        this.blockId = blockId;
        this.initialise(blockId);
    }

    public static IItemHandlerModifiable createGenericItemHandler(AbstractOpenableStorageBlockEntity entity) {
        return new IItemHandlerModifiable() {
            @Override
            public void setStackInSlot(int slot, ItemStack stack) {
                entity.inventory.set(slot, stack);
                if (stack.getCount() > this.getSlotLimit(slot)) {
                    stack.setCount(this.getSlotLimit(slot));
                }
                entity.setChanged();
            }

            @Override
            public int getSlots() {
                return entity.slots;
            }

            @Override
            public ItemStack getStackInSlot(int slot) {
                return entity.inventory.get(slot);
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if (stack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                var stackInSlot = entity.inventory.get(slot);
                if (stackInSlot.isEmpty()) {
                    var limit = this.getSlotLimit(slot);
                    if (stack.getCount() > limit) {
                        if (!simulate) {
                            stackInSlot.setCount(limit);
                            entity.setChanged();
                        }
                        return simulate ? stack.copy().split(stack.getCount() - limit) : stack.split(stack.getCount() - limit);
                    } else {
                        if (!simulate) {
                            entity.inventory.set(slot, stack.copy());
                            entity.setChanged();
                        }
                    }
                } else if (ItemHandlerHelper.canItemStacksStack(stackInSlot, stack)) {
                    var limit = Math.min(stackInSlot.getMaxStackSize(), this.getSlotLimit(slot));
                    var diff = limit - stackInSlot.getCount();
                    if (diff != 0) {
                        if (stack.getCount() > diff) {
                            if (!simulate) {
                                stackInSlot.setCount(limit);
                                entity.setChanged();
                            }
                            return simulate ? stack.copy().split(stack.getCount() - diff) : stack.split(stack.getCount() - diff);
                        } else {
                            if (!simulate) {
                                stackInSlot.setCount(stackInSlot.getCount() + stack.getCount());
                                entity.setChanged();
                            }
                        }
                    } else {
                        return stack;
                    }
                } else {
                    return stack;
                }
                return ItemStack.EMPTY;
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                var stackInSlot = entity.inventory.get(slot);
                if (stackInSlot.isEmpty()) {
                    return ItemStack.EMPTY;
                } else if (amount >= stackInSlot.getCount()) {
                    if (!simulate) {
                        entity.inventory.set(slot, ItemStack.EMPTY);
                    }
                    return stackInSlot;
                } else {
                    var copy = simulate ? stackInSlot.copy() : stackInSlot;
                    return copy.split(amount);
                }
            }

            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return true;
            }
        };
    }

    private void playerStartUsing(Player player) {
        if (!player.isSpectator()) {
            //noinspection ConstantConditions
            observerCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    private void playerStopUsing(Player player) {
        if (!player.isSpectator()) {
            //noinspection ConstantConditions
            observerCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    protected boolean isThis(Container container) {
        return container == this.container.get();
    }

    protected void onObserverCountChanged(Level level, BlockPos pos, BlockState state, int i, int j) {

    }

    protected void onOpen(Level level, BlockPos pos, BlockState state) {

    }

    protected void onClose(Level level, BlockPos pos, BlockState state) {

    }

    public final void recountObservers() {
        //noinspection ConstantConditions
        observerCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
    }

    public Container getContainerWrapper() {
        return container.get();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (itemHandler == null) {
                itemHandler = LazyOptional.of(() -> this.createItemHandler(this.getLevel(), this.getBlockState(), this.getBlockPos(), side));
            }
            return itemHandler.cast();
        }
        return super.getCapability(capability, side);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setBlockState(BlockState state) {
        super.setBlockState(state);
        this.itemHandler = null;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.itemHandler = null;
    }

    protected IItemHandlerModifiable createItemHandler(Level level, BlockState state, BlockPos pos, @Nullable Direction side) {
        return AbstractOpenableStorageBlockEntity.createGenericItemHandler(this);
    }

    private void initialise(ResourceLocation blockId) {
        if (ForgeRegistries.BLOCKS.getValue(blockId) instanceof AbstractOpenableStorageBlock block) {
            slots = block.getSlotCount();
            inventory = NonNullList.withSize(slots, ItemStack.EMPTY);
            menuTitle = block.getMenuTitle();
        }
    }

    @Override
    public Component getDefaultTitle() {
        return menuTitle;
    }

    public final ResourceLocation getBlockId() {
        return blockId;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (this.getBlockState().getBlock() instanceof AbstractOpenableStorageBlock block) {
            this.initialise(block.getBlockId());
            ContainerHelper.loadAllItems(tag, inventory);
        } else {
            throw new IllegalStateException("Block Entity attached to wrong block.");
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        ContainerHelper.saveAllItems(tag, inventory);
        return tag;
    }

    public NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    public int getSlotCount() {
        return slots;
    }

    public boolean canContinueUse(Player player) {
        //noinspection ConstantConditions
        return level.getBlockEntity(worldPosition) == this && player.distanceToSqr(Vec3.atCenterOf(worldPosition)) <= 64;
    }
}
