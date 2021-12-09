package ninjaphenix.expandedstorage.barrel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.LockCode;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import ninjaphenix.expandedstorage.barrel.block.BarrelBlock;
import ninjaphenix.expandedstorage.barrel.block.misc.BarrelBlockEntity;
import ninjaphenix.expandedstorage.base.internal_api.BaseApi;
import ninjaphenix.expandedstorage.base.internal_api.Utils;
import ninjaphenix.expandedstorage.base.internal_api.block.AbstractOpenableStorageBlock;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.AbstractOpenableStorageBlockEntity;

import java.util.function.Predicate;

public final class BarrelCommon {
    public static final ResourceLocation BLOCK_TYPE = Utils.resloc("barrel");
    private static final int ICON_SUITABILITY = 998;
    private static BlockEntityType<BarrelBlockEntity> blockEntityType;

    private BarrelCommon() {

    }

    public static BlockEntityType<BarrelBlockEntity> getBlockEntityType() {
        return blockEntityType;
    }

    static void setBlockEntityType(BlockEntityType<BarrelBlockEntity> blockEntityType) {
        if (BarrelCommon.blockEntityType == null) {
            BarrelCommon.blockEntityType = blockEntityType;
        }
    }

    public static boolean tryUpgradeBlock(UseOnContext context, ResourceLocation from, ResourceLocation to) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        boolean isExpandedStorageBarrel = block instanceof BarrelBlock;
        int containerSize = !isExpandedStorageBarrel ? Utils.WOOD_STACK_COUNT : ((BarrelBlock) BaseApi.getInstance().getTieredBlock(BarrelCommon.BLOCK_TYPE, ((BarrelBlock) block).getBlockTier())).getSlotCount();
        if (isExpandedStorageBarrel && ((BarrelBlock) block).getBlockTier() == from || !isExpandedStorageBarrel && from == Utils.WOOD_TIER.getId()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            //noinspection ConstantConditions
            CompoundTag tag = blockEntity.save(new CompoundTag());
            boolean verifiedSize = blockEntity instanceof Container container && container.getContainerSize() == containerSize;
            if (!verifiedSize) { // Cannot verify container size, we'll let it upgrade if it has or has less than 27 items
                if (tag.contains("Items", Utils.NBT_LIST_TYPE)) {
                    ListTag items = tag.getList("Items", Utils.NBT_COMPOUND_TYPE);
                    if (items.size() <= containerSize) {
                        verifiedSize = true;
                    }
                }
            }
            if (verifiedSize) {
                AbstractOpenableStorageBlock toBlock = (AbstractOpenableStorageBlock) BaseApi.getInstance().getTieredBlock(BarrelCommon.BLOCK_TYPE, to);
                NonNullList<ItemStack> inventory = NonNullList.withSize(toBlock.getSlotCount(), ItemStack.EMPTY);
                LockCode code = LockCode.fromTag(tag);
                ContainerHelper.loadAllItems(tag, inventory);
                level.removeBlockEntity(pos);
                BlockState newState = toBlock.defaultBlockState().setValue(BlockStateProperties.FACING, state.getValue(BlockStateProperties.FACING));
                if (level.setBlockAndUpdate(pos, newState)) {
                    AbstractOpenableStorageBlockEntity newEntity = (AbstractOpenableStorageBlockEntity) level.getBlockEntity(pos);
                    //noinspection ConstantConditions
                    CompoundTag newTag = newEntity.save(new CompoundTag());
                    ContainerHelper.saveAllItems(newTag, inventory);
                    code.addToTag(newTag);
                    newEntity.load(newState, newTag);
                    context.getItemInHand().shrink(1);
                    return true;
                }
            }
        }
        return false;
    }

    public static void registerTabIcon(BlockItem item) {
        BaseApi.getInstance().offerTabIcon(item, BarrelCommon.ICON_SUITABILITY);
    }

    public static void registerUpgradeBehaviours(net.minecraft.tags.Tag<Block> tag) {
        Predicate<Block> isUpgradableChestBlock = (block) -> block instanceof BarrelBlock || block instanceof net.minecraft.world.level.block.BarrelBlock || tag.contains(block);
        BaseApi.getInstance().defineBlockUpgradeBehaviour(isUpgradableChestBlock, BarrelCommon::tryUpgradeBlock);
    }
}
