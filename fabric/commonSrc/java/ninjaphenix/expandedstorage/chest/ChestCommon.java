package ninjaphenix.expandedstorage.chest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.LockCode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import ninjaphenix.expandedstorage.base.internal_api.BaseApi;
import ninjaphenix.expandedstorage.base.internal_api.Utils;
import ninjaphenix.expandedstorage.base.internal_api.block.AbstractOpenableStorageBlock;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.AbstractOpenableStorageBlockEntity;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.CursedChestType;
import ninjaphenix.expandedstorage.chest.block.ChestBlock;
import ninjaphenix.expandedstorage.chest.block.misc.ChestBlockEntity;
import ninjaphenix.expandedstorage.chest.internal_api.ChestApi;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public final class ChestCommon {
    public static final ResourceLocation BLOCK_TYPE = Utils.resloc("cursed_chest");
    private static final int ICON_SUITABILITY = 1000;
    private static BlockEntityType<ChestBlockEntity> blockEntityType;

    private ChestCommon() {

    }

    static void registerTabIcon(Item item) {
        BaseApi.getInstance().offerTabIcon(item, ChestCommon.ICON_SUITABILITY);
    }

    public static BlockEntityType<ChestBlockEntity> getBlockEntityType() {
        return blockEntityType;
    }

    static void setBlockEntityType(BlockEntityType<ChestBlockEntity> type) {
        if (ChestCommon.blockEntityType == null) {
            ChestCommon.blockEntityType = type;
        }
    }

    static Set<ResourceLocation> getChestTextures(Set<ChestBlock> blocks) {
        Set<ResourceLocation> textures = new HashSet<>();
        for (ChestBlock block : blocks) {
            ResourceLocation blockId = block.getBlockId();
            for (CursedChestType type : CursedChestType.values()) {
                textures.add(ChestApi.INSTANCE.getChestTexture(blockId, type));
            }
        }
        return textures;
    }

    static void registerChestTextures(Set<ChestBlock> blocks) {
        for (ChestBlock block : blocks) {
            ResourceLocation blockId = block.getBlockId();
            ChestApi.INSTANCE.declareChestTextures(
                    blockId, Utils.resloc("entity/" + blockId.getPath() + "/single"),
                    Utils.resloc("entity/" + blockId.getPath() + "/left"),
                    Utils.resloc("entity/" + blockId.getPath() + "/right"),
                    Utils.resloc("entity/" + blockId.getPath() + "/top"),
                    Utils.resloc("entity/" + blockId.getPath() + "/bottom"),
                    Utils.resloc("entity/" + blockId.getPath() + "/front"),
                    Utils.resloc("entity/" + blockId.getPath() + "/back"));
        }
    }

    private static boolean tryUpgradeBlock(UseOnContext context, ResourceLocation from, ResourceLocation to) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();
        ItemStack handStack = context.getItemInHand();
        if (state.getBlock() instanceof ChestBlock) {
            if (ChestBlock.getBlockType(state) == DoubleBlockCombiner.BlockType.SINGLE) {
                ChestCommon.upgradeSingleBlock(level, state, pos, from, to);
                handStack.shrink(1);
                return true;
            } else if (handStack.getCount() > 1 || (player != null && player.isCreative())) {
                BlockPos otherPos = pos.relative(ChestBlock.getDirectionToAttached(state));
                BlockState otherState = level.getBlockState(otherPos);
                ChestCommon.upgradeSingleBlock(level, state, pos, from, to);
                ChestCommon.upgradeSingleBlock(level, otherState, otherPos, from, to);
                handStack.shrink(2);
                return true;
            }
        } else {
            if (net.minecraft.world.level.block.ChestBlock.getBlockType(state) == DoubleBlockCombiner.BlockType.SINGLE) {
                ChestCommon.upgradeSingleBlock(level, state, pos, from, to);
                handStack.shrink(1);
                return true;
            } else if (handStack.getCount() > 1 || (player != null && player.isCreative())) {
                BlockPos otherPos = pos.relative(net.minecraft.world.level.block.ChestBlock.getConnectedDirection(state));
                BlockState otherState = level.getBlockState(otherPos);
                ChestCommon.upgradeSingleBlock(level, state, pos, from, to);
                ChestCommon.upgradeSingleBlock(level, otherState, otherPos, from, to);
                handStack.shrink(2);
                return true;
            }
        }

        return false;
    }

    private static void upgradeSingleBlock(Level level, BlockState state, BlockPos pos, ResourceLocation from, ResourceLocation to) {
        Block block = state.getBlock();
        boolean isExpandedStorageChest = block instanceof ChestBlock;
        int containerSize = !isExpandedStorageChest ? Utils.WOOD_STACK_COUNT : ((ChestBlock) BaseApi.getInstance().getTieredBlock(ChestCommon.BLOCK_TYPE, ((ChestBlock) block).getBlockTier())).getSlotCount();
        if (isExpandedStorageChest && ((ChestBlock) block).getBlockTier() == from || !isExpandedStorageChest && from == Utils.WOOD_TIER.getId()) {
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
                AbstractOpenableStorageBlock toBlock = (AbstractOpenableStorageBlock) BaseApi.getInstance().getTieredBlock(ChestCommon.BLOCK_TYPE, to);
                NonNullList<ItemStack> inventory = NonNullList.withSize(toBlock.getSlotCount(), ItemStack.EMPTY);
                LockCode code = LockCode.fromTag(tag);
                ContainerHelper.loadAllItems(tag, inventory);
                level.removeBlockEntity(pos);
                // Needs fixing up to check for vanilla states.
                BlockState newState = toBlock.defaultBlockState()
                                             .setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
                                             .setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED));
                if (state.hasProperty(ChestBlock.CURSED_CHEST_TYPE)) {
                    newState = newState.setValue(ChestBlock.CURSED_CHEST_TYPE, state.getValue(ChestBlock.CURSED_CHEST_TYPE));
                } else if (state.hasProperty(BlockStateProperties.CHEST_TYPE)) {
                    ChestType type = state.getValue(BlockStateProperties.CHEST_TYPE);
                    newState = newState.setValue(ChestBlock.CURSED_CHEST_TYPE, type == ChestType.LEFT ? CursedChestType.RIGHT : type == ChestType.RIGHT ? CursedChestType.LEFT : CursedChestType.SINGLE);
                }
                if (level.setBlockAndUpdate(pos, newState)) {
                    AbstractOpenableStorageBlockEntity newEntity = (AbstractOpenableStorageBlockEntity) level.getBlockEntity(pos);
                    //noinspection ConstantConditions
                    CompoundTag newTag = newEntity.save(new CompoundTag());
                    ContainerHelper.saveAllItems(newTag, inventory);
                    code.addToTag(newTag);
                    newEntity.load(newState, newTag);
                }
            }
        }
    }

    public static void registerUpgradeBehaviours(Tag<Block> tag) {
        Predicate<Block> isUpgradableChestBlock = (block) -> block instanceof ChestBlock || block instanceof net.minecraft.world.level.block.ChestBlock || tag.contains(block);
        BaseApi.getInstance().defineBlockUpgradeBehaviour(isUpgradableChestBlock, ChestCommon::tryUpgradeBlock);
    }
}
