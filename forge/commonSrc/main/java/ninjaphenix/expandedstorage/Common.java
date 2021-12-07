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
package ninjaphenix.expandedstorage;

import ninjaphenix.expandedstorage.block.AbstractChestBlock;
import ninjaphenix.expandedstorage.block.BarrelBlock;
import ninjaphenix.expandedstorage.block.ChestBlock;
import ninjaphenix.expandedstorage.block.MiniChestBlock;
import ninjaphenix.expandedstorage.block.OpenableBlock;
import ninjaphenix.expandedstorage.block.entity.BarrelBlockEntity;
import ninjaphenix.expandedstorage.block.entity.ChestBlockEntity;
import ninjaphenix.expandedstorage.block.entity.MiniChestBlockEntity;
import ninjaphenix.expandedstorage.block.entity.OldChestBlockEntity;
import ninjaphenix.expandedstorage.block.entity.extendable.OpenableBlockEntity;
import ninjaphenix.expandedstorage.block.misc.CursedChestType;
import ninjaphenix.expandedstorage.block.strategies.ItemAccess;
import ninjaphenix.expandedstorage.block.strategies.Lockable;
import ninjaphenix.expandedstorage.client.TextureCollection;
import ninjaphenix.expandedstorage.item.BlockUpgradeBehaviour;
import ninjaphenix.expandedstorage.item.MutationMode;
import ninjaphenix.expandedstorage.item.MutatorBehaviour;
import ninjaphenix.expandedstorage.item.StorageConversionKit;
import ninjaphenix.expandedstorage.item.StorageMutator;
import ninjaphenix.expandedstorage.registration.BlockItemCollection;
import ninjaphenix.expandedstorage.registration.BlockItemPair;
import ninjaphenix.expandedstorage.registration.RegistrationConsumer;
import ninjaphenix.expandedstorage.tier.Tier;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import net.minecraft.Util;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.LockCode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public final class Common {
    public static final ResourceLocation BARREL_BLOCK_TYPE = Utils.id("barrel");
    public static final ResourceLocation CHEST_BLOCK_TYPE = Utils.id("chest");
    public static final ResourceLocation OLD_CHEST_BLOCK_TYPE = Utils.id("old_chest");
    public static final ResourceLocation MINI_CHEST_BLOCK_TYPE = Utils.id("mini_chest");

    private static final Map<Predicate<Block>, BlockUpgradeBehaviour> BLOCK_UPGRADE_BEHAVIOURS = new HashMap<>();
    private static final Map<Pair<Predicate<Block>, MutationMode>, MutatorBehaviour> MUTATOR_BEHAVIOURS = new HashMap<>();
    private static final Map<Pair<ResourceLocation, ResourceLocation>, OpenableBlock> BLOCKS = new HashMap<>();
    private static final Map<ResourceLocation, TextureCollection> CHEST_TEXTURES = new HashMap<>();

    private static BlockEntityType<ChestBlockEntity> chestBlockEntityType;
    private static BlockEntityType<OldChestBlockEntity> oldChestBlockEntityType;
    private static BlockEntityType<BarrelBlockEntity> barrelBlockEntityType;
    private static BlockEntityType<MiniChestBlockEntity> miniChestBlockEntityType;

    private static Function<OpenableBlockEntity, ItemAccess> itemAccess;
    private static Function<OpenableBlockEntity, Lockable> lockable;
    private static MenuType<MiniChestScreenHandler> miniChestScreenHandler;

    public static BlockEntityType<ChestBlockEntity> getChestBlockEntityType() {
        return chestBlockEntityType;
    }

    public static BlockEntityType<OldChestBlockEntity> getOldChestBlockEntityType() {
        return oldChestBlockEntityType;
    }

    public static BlockEntityType<BarrelBlockEntity> getBarrelBlockEntityType() {
        return barrelBlockEntityType;
    }

    public static BlockEntityType<MiniChestBlockEntity> getMiniChestBlockEntityType() {
        return miniChestBlockEntityType;
    }

    private static BlockItemPair<ChestBlock, BlockItem> chestBlock(
            ResourceLocation blockId, ResourceLocation stat, Tier tier, Properties settings,
            BiFunction<ChestBlock, Item.Properties, BlockItem> blockItemMaker, CreativeModeTab group
    ) {
        ChestBlock block = new ChestBlock(tier.getBlockSettings().apply(settings), blockId, tier.getId(), stat, tier.getSlotCount());
        Common.registerTieredBlock(block);
        return new BlockItemPair<>(block, blockItemMaker.apply(block, new Item.Properties().tab(group)));
    }

    private static BlockItemPair<AbstractChestBlock, BlockItem> oldChestBlock(
            ResourceLocation blockId, ResourceLocation stat, Tier tier, Properties settings, CreativeModeTab group
    ) {
        AbstractChestBlock block = new AbstractChestBlock(tier.getBlockSettings().apply(settings), blockId, tier.getId(), stat, tier.getSlotCount());
        Common.registerTieredBlock(block);
        BlockItem item = new BlockItem(block, tier.getItemSettings().apply(new Item.Properties().tab(group)));
        return new BlockItemPair<>(block, item);
    }

    private static BlockItemPair<BarrelBlock, BlockItem> barrelBlock(
            ResourceLocation blockId, ResourceLocation stat, Tier tier, Properties settings, CreativeModeTab group
    ) {
        BarrelBlock block = new BarrelBlock(tier.getBlockSettings().apply(settings), blockId, tier.getId(), stat, tier.getSlotCount());
        Common.registerTieredBlock(block);
        BlockItem item = new BlockItem(block, tier.getItemSettings().apply(new Item.Properties().tab(group)));
        return new BlockItemPair<>(block, item);
    }

    private static BlockItemPair<MiniChestBlock, BlockItem> miniChestBlock(
            ResourceLocation blockId, ResourceLocation stat, Tier tier, Properties settings, BiFunction<MiniChestBlock, Item.Properties, BlockItem> blockItemMaker, CreativeModeTab group
    ) {
        MiniChestBlock block = new MiniChestBlock(tier.getBlockSettings().apply(settings), blockId, stat);
        Common.registerTieredBlock(block);
        BlockItem item = blockItemMaker.apply(block, tier.getItemSettings().apply(new Item.Properties().tab(group)));
        return new BlockItemPair<>(block, item);
    }

    static ResourceLocation[] getChestTextures(ChestBlock[] blocks) {
        ResourceLocation[] textures = new ResourceLocation[blocks.length * CursedChestType.values().length];
        int index = 0;
        for (ChestBlock block : blocks) {
            ResourceLocation blockId = block.getBlockId();
            for (CursedChestType type : CursedChestType.values()) {
                textures[index++] = Common.getChestTexture(blockId, type);
            }
        }
        return textures;
    }

    static void registerChestTextures(ChestBlock[] blocks) {
        for (ChestBlock block : blocks) {
            ResourceLocation blockId = block.getBlockId();
            Common.declareChestTextures(
                    blockId, Utils.id("entity/" + blockId.getPath() + "/single"),
                    Utils.id("entity/" + blockId.getPath() + "/left"),
                    Utils.id("entity/" + blockId.getPath() + "/right"),
                    Utils.id("entity/" + blockId.getPath() + "/top"),
                    Utils.id("entity/" + blockId.getPath() + "/bottom"),
                    Utils.id("entity/" + blockId.getPath() + "/front"),
                    Utils.id("entity/" + blockId.getPath() + "/back"));
        }
    }

    private static boolean upgradeSingleBlockToChest(Level world, BlockState state, BlockPos pos, ResourceLocation from, ResourceLocation to) {
        Block block = state.getBlock();
        boolean isExpandedStorageChest = block instanceof ChestBlock;
        int inventorySize = !isExpandedStorageChest ? Utils.WOOD_STACK_COUNT : Common.getTieredBlock(Common.CHEST_BLOCK_TYPE, ((ChestBlock) block).getBlockTier()).getSlotCount();
        if (isExpandedStorageChest && ((ChestBlock) block).getBlockTier() == from || !isExpandedStorageChest && from == Utils.WOOD_TIER_ID) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            //noinspection ConstantConditions
            CompoundTag tag = blockEntity.saveWithoutMetadata();
            boolean verifiedSize = blockEntity instanceof Container inventory && inventory.getContainerSize() == inventorySize;
            if (!verifiedSize) { // Cannot verify inventory size, we'll let it upgrade if it has or has less than 27 items
                if (tag.contains("Items", Tag.TAG_LIST)) {
                    ListTag items = tag.getList("Items", Tag.TAG_COMPOUND);
                    if (items.size() <= inventorySize) {
                        verifiedSize = true;
                    }
                }
            }
            if (verifiedSize) {
                ChestBlock toBlock = (ChestBlock) Common.getTieredBlock(Common.CHEST_BLOCK_TYPE, to);
                NonNullList<ItemStack> inventory = NonNullList.withSize(toBlock.getSlotCount(), ItemStack.EMPTY);
                LockCode code = LockCode.fromTag(tag);
                ContainerHelper.loadAllItems(tag, inventory);
                world.removeBlockEntity(pos);
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
                if (world.setBlockAndUpdate(pos, newState)) {
                    BlockEntity newEntity = world.getBlockEntity(pos);
                    //noinspection ConstantConditions
                    CompoundTag newTag = newEntity.saveWithoutMetadata();
                    ContainerHelper.saveAllItems(newTag, inventory);
                    code.addToTag(newTag);
                    newEntity.load(newTag);
                    return true;
                } else {
                    world.setBlockEntity(blockEntity);
                }
            }
        }
        return false;
    }

    private static boolean upgradeSingleBlockToOldChest(Level world, BlockState state, BlockPos pos, ResourceLocation from, ResourceLocation to) {
        if (((AbstractChestBlock) state.getBlock()).getBlockTier() == from) {
            AbstractChestBlock toBlock = (AbstractChestBlock) Common.getTieredBlock(Common.OLD_CHEST_BLOCK_TYPE, to);
            NonNullList<ItemStack> inventory = NonNullList.withSize(toBlock.getSlotCount(), ItemStack.EMPTY);
            BlockEntity blockEntity = world.getBlockEntity(pos);
            //noinspection ConstantConditions
            CompoundTag tag = blockEntity.saveWithoutMetadata();
            LockCode code = LockCode.fromTag(tag);
            ContainerHelper.loadAllItems(tag, inventory);
            world.removeBlockEntity(pos);
            BlockState newState = toBlock.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING)).setValue(AbstractChestBlock.CURSED_CHEST_TYPE, state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE));
            if (world.setBlockAndUpdate(pos, newState)) {
                BlockEntity newEntity = world.getBlockEntity(pos);
                //noinspection ConstantConditions
                CompoundTag newTag = newEntity.saveWithoutMetadata();
                ContainerHelper.saveAllItems(newTag, inventory);
                code.addToTag(newTag);
                newEntity.load(newTag);
                return true;
            }  else {
                world.setBlockEntity(blockEntity);
            }
        }
        return false;
    }

    public static ResourceLocation stat(String stat) {
        ResourceLocation statId = Utils.id(stat);
        Registry.register(Registry.CUSTOM_STAT, statId, statId); // Forge doesn't provide custom registries for stats;
        Stats.CUSTOM.get(statId);
        return statId;
    }

    private static void defineTierUpgradePath(Pair<ResourceLocation, Item>[] items, boolean wrapTooltipManually, CreativeModeTab group, Tier... tiers) {
        int numTiers = tiers.length;
        int index = 1;
        for (int fromIndex = 0; fromIndex < numTiers - 1; fromIndex++) {
            Tier fromTier = tiers[fromIndex];
            for (int toIndex = fromIndex + 1; toIndex < numTiers; toIndex++) {
                Tier toTier = tiers[toIndex];
                ResourceLocation itemId = Utils.id(fromTier.getId().getPath() + "_to_" + toTier.getId().getPath() + "_conversion_kit");
                Item.Properties settings = fromTier.getItemSettings()
                                                 .andThen(toTier.getItemSettings())
                                                 .apply(new Item.Properties().tab(group).stacksTo(16));
                Item kit = new StorageConversionKit(settings, fromTier.getId(), toTier.getId(), wrapTooltipManually);
                items[index++] = new Pair<>(itemId, kit);
            }
        }
    }

    public static BlockUpgradeBehaviour getBlockUpgradeBehaviour(Block block) {
        for (Map.Entry<Predicate<Block>, BlockUpgradeBehaviour> entry : Common.BLOCK_UPGRADE_BEHAVIOURS.entrySet()) {
            if (entry.getKey().test(block)) return entry.getValue();
        }
        return null;
    }

    private static void defineBlockUpgradeBehaviour(Predicate<Block> target, BlockUpgradeBehaviour behaviour) {
        Common.BLOCK_UPGRADE_BEHAVIOURS.put(target, behaviour);
    }

    public static void setSharedStrategies(Function<OpenableBlockEntity, ItemAccess> itemAccess, Function<OpenableBlockEntity, Lockable> lockable) {
        Common.itemAccess = itemAccess;
        Common.lockable = lockable;
    }

    private static void registerTieredBlock(OpenableBlock block) {
        Common.BLOCKS.putIfAbsent(new Pair<>(block.getBlockType(), block.getBlockTier()), block);
    }

    public static OpenableBlock getTieredBlock(ResourceLocation blockType, ResourceLocation tier) {
        return Common.BLOCKS.get(new Pair<>(blockType, tier));
    }

    public static void declareChestTextures(ResourceLocation block, ResourceLocation singleTexture, ResourceLocation leftTexture, ResourceLocation rightTexture, ResourceLocation topTexture, ResourceLocation bottomTexture, ResourceLocation frontTexture, ResourceLocation backTexture) {
        if (!Common.CHEST_TEXTURES.containsKey(block)) {
            TextureCollection collection = new TextureCollection(singleTexture, leftTexture, rightTexture, topTexture, bottomTexture, frontTexture, backTexture);
            Common.CHEST_TEXTURES.put(block, collection);
        } else {
            throw new IllegalArgumentException("Tried registering chest textures for \"" + block + "\" which already has textures.");
        }
    }

    public static ResourceLocation getChestTexture(ResourceLocation block, CursedChestType chestType) {
        if (Common.CHEST_TEXTURES.containsKey(block)) return Common.CHEST_TEXTURES.get(block).getTexture(chestType);
        return MissingTextureAtlasSprite.getLocation();
    }

    private static void registerMutationBehaviour(Predicate<Block> predicate, MutationMode mode, MutatorBehaviour behaviour) {
        Common.MUTATOR_BEHAVIOURS.put(new Pair<>(predicate, mode), behaviour);
    }

    public static MutatorBehaviour getMutatorBehaviour(Block block, MutationMode mode) {
        for (Map.Entry<Pair<Predicate<Block>, MutationMode>, MutatorBehaviour> entry : Common.MUTATOR_BEHAVIOURS.entrySet()) {
            Pair<Predicate<Block>, MutationMode> pair = entry.getKey();
            if (pair.getSecond() == mode && pair.getFirst().test(block)) return entry.getValue();
        }
        return null;
    }

    public static void registerContent(CreativeModeTab group, boolean isClient,
                                       Consumer<Pair<ResourceLocation, Item>[]> baseRegistration, boolean manuallyWrapTooltips,
                                       RegistrationConsumer<ChestBlock, BlockItem, ChestBlockEntity> chestRegistration, net.minecraft.tags.Tag.Named<Block> chestTag, BiFunction<ChestBlock, Item.Properties, BlockItem> chestItemMaker, Function<OpenableBlockEntity, ItemAccess> chestAccessMaker,
                                       RegistrationConsumer<AbstractChestBlock, BlockItem, OldChestBlockEntity> oldChestRegistration,
                                       RegistrationConsumer<BarrelBlock, BlockItem, BarrelBlockEntity> barrelRegistration, net.minecraft.tags.Tag.Named<Block> barrelTag,
                                       RegistrationConsumer<MiniChestBlock, BlockItem, MiniChestBlockEntity> miniChestRegistration, BiFunction<MiniChestBlock, Item.Properties, BlockItem> miniChestItemMaker, MenuType<MiniChestScreenHandler> miniChestScreenHandlerType,
                                       net.minecraft.tags.Tag.Named<Block> chestCycle, net.minecraft.tags.Tag.Named<Block> miniChestCycle, net.minecraft.tags.Tag.Named<Block> miniChestSecretCycle, net.minecraft.tags.Tag.Named<Block> miniChestSecretCycle2) {
        final Tier woodTier = new Tier(Utils.WOOD_TIER_ID, Utils.WOOD_STACK_COUNT, UnaryOperator.identity(), UnaryOperator.identity());
        final Tier ironTier = new Tier(Utils.id("iron"), 54, Properties::requiresCorrectToolForDrops, UnaryOperator.identity());
        final Tier goldTier = new Tier(Utils.id("gold"), 81, Properties::requiresCorrectToolForDrops, UnaryOperator.identity());
        final Tier diamondTier = new Tier(Utils.id("diamond"), 108, Properties::requiresCorrectToolForDrops, UnaryOperator.identity());
        final Tier obsidianTier = new Tier(Utils.id("obsidian"), 108, Properties::requiresCorrectToolForDrops, UnaryOperator.identity());
        final Tier netheriteTier = new Tier(Utils.id("netherite"), 135, Properties::requiresCorrectToolForDrops, Item.Properties::fireResistant);

        //<editor-fold desc="-- Base Content">
        //noinspection unchecked
        Pair<ResourceLocation, Item>[] baseContent = new Pair[16];
        baseContent[0] = new Pair<>(Utils.id("storage_mutator"), new StorageMutator(new Item.Properties().stacksTo(1).tab(group)));
        Common.defineTierUpgradePath(baseContent, manuallyWrapTooltips, group, woodTier, ironTier, goldTier, diamondTier, obsidianTier, netheriteTier);
        baseRegistration.accept(baseContent);
        //</editor-fold>
        //<editor-fold desc="-- Chest Content">
        // Init block settings
        Properties woodSettings = Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.5f).sound(SoundType.WOOD);
        Properties pumpkinSettings = Properties.of(Material.VEGETABLE, MaterialColor.COLOR_ORANGE).strength(1).sound(SoundType.WOOD);
        Properties presentSettings = Properties.of(Material.WOOD, state -> {
            CursedChestType type = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
            if (type == CursedChestType.SINGLE) return MaterialColor.COLOR_RED;
            else if (type == CursedChestType.FRONT || type == CursedChestType.BACK) return MaterialColor.PLANT;
            return MaterialColor.SNOW;
        }).strength(2.5f).sound(SoundType.WOOD);
        Properties ironSettings = Properties.of(Material.METAL, MaterialColor.METAL).strength(5, 6).sound(SoundType.METAL);
        Properties goldSettings = Properties.of(Material.METAL, MaterialColor.GOLD).strength(3, 6).sound(SoundType.METAL);
        Properties diamondSettings = Properties.of(Material.METAL, MaterialColor.DIAMOND).strength(5, 6).sound(SoundType.METAL);
        Properties obsidianSettings = Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).strength(50, 1200);
        Properties netheriteSettings = Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).strength(50, 1200).sound(SoundType.NETHERITE_BLOCK);
        // Init content
        BlockItemCollection<ChestBlock, BlockItem> chestContent = BlockItemCollection.of(ChestBlock[]::new, BlockItem[]::new,
                Common.chestBlock(Utils.id("wood_chest"), Common.stat("open_wood_chest"), woodTier, woodSettings, chestItemMaker, group),
                Common.chestBlock(Utils.id("pumpkin_chest"), Common.stat("open_pumpkin_chest"), woodTier, pumpkinSettings, chestItemMaker, group),
                Common.chestBlock(Utils.id("present"), Common.stat("open_present"), woodTier, presentSettings, chestItemMaker, group),
                Common.chestBlock(Utils.id("iron_chest"), Common.stat("open_iron_chest"), ironTier, ironSettings, chestItemMaker, group),
                Common.chestBlock(Utils.id("gold_chest"), Common.stat("open_gold_chest"), goldTier, goldSettings, chestItemMaker, group),
                Common.chestBlock(Utils.id("diamond_chest"), Common.stat("open_diamond_chest"), diamondTier, diamondSettings, chestItemMaker, group),
                Common.chestBlock(Utils.id("obsidian_chest"), Common.stat("open_obsidian_chest"), obsidianTier, obsidianSettings, chestItemMaker, group),
                Common.chestBlock(Utils.id("netherite_chest"), Common.stat("open_netherite_chest"), netheriteTier, netheriteSettings, chestItemMaker, group)
        );
        if (isClient) Common.registerChestTextures(chestContent.getBlocks());
        // Init block entity type
        Common.chestBlockEntityType = BlockEntityType.Builder.of((pos, state) -> new ChestBlockEntity(Common.getChestBlockEntityType(), pos, state, ((ChestBlock) state.getBlock()).getBlockId(), chestAccessMaker, Common.lockable), chestContent.getBlocks()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, Common.CHEST_BLOCK_TYPE.toString()));
        chestRegistration.accept(chestContent, Common.chestBlockEntityType);
        // Register chest upgrade behaviours
        Predicate<Block> isUpgradableChestBlock = (block) -> block instanceof ChestBlock || block instanceof net.minecraft.world.level.block.ChestBlock || chestTag.contains(block);
        Common.defineBlockUpgradeBehaviour(isUpgradableChestBlock, (context, from, to) -> {
            Level world = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockState state = world.getBlockState(pos);
            Player player = context.getPlayer();
            ItemStack handStack = context.getItemInHand();
            if (state.getBlock() instanceof ChestBlock) {
                CursedChestType type = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
                Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                if (AbstractChestBlock.getBlockType(type) == DoubleBlockCombiner.BlockType.SINGLE) {
                    boolean upgradeSucceeded = Common.upgradeSingleBlockToChest(world, state, pos, from, to);
                    if (upgradeSucceeded) handStack.shrink(1);
                    return upgradeSucceeded;
                } else if (handStack.getCount() > 1 || (player != null && player.isCreative())) {
                    BlockPos otherPos = pos.relative(AbstractChestBlock.getDirectionToAttached(type, facing));
                    BlockState otherState = world.getBlockState(otherPos);
                    boolean firstSucceeded = Common.upgradeSingleBlockToChest(world, state, pos, from, to);
                    boolean secondSucceeded = Common.upgradeSingleBlockToChest(world, otherState, otherPos, from, to);
                    if (firstSucceeded && secondSucceeded) handStack.shrink(2);
                    else if (firstSucceeded || secondSucceeded) handStack.shrink(1);
                    return firstSucceeded || secondSucceeded;
                }
            } else {
                if (net.minecraft.world.level.block.ChestBlock.getBlockType(state) == DoubleBlockCombiner.BlockType.SINGLE) {
                    boolean upgradeSucceeded = Common.upgradeSingleBlockToChest(world, state, pos, from, to);
                    if (upgradeSucceeded) handStack.shrink(1);
                    return upgradeSucceeded;
                } else if (handStack.getCount() > 1 || (player != null && player.isCreative())) {
                    BlockPos otherPos = pos.relative(net.minecraft.world.level.block.ChestBlock.getConnectedDirection(state));
                    BlockState otherState = world.getBlockState(otherPos);
                    boolean firstSucceeded = Common.upgradeSingleBlockToChest(world, state, pos, from, to);
                    boolean secondSucceeded = Common.upgradeSingleBlockToChest(world, otherState, otherPos, from, to);
                    if (firstSucceeded && secondSucceeded) handStack.shrink(2);
                    else if (firstSucceeded || secondSucceeded) handStack.shrink(1);
                    return firstSucceeded || secondSucceeded;
                }
            }

            return false;
        });
        //</editor-fold>
        //<editor-fold desc="-- Old Chest Content">
        // Init content
        BlockItemCollection<AbstractChestBlock, BlockItem> oldChestContent = BlockItemCollection.of(AbstractChestBlock[]::new, BlockItem[]::new,
                Common.oldChestBlock(Utils.id("old_wood_chest"), Common.stat("open_old_wood_chest"), woodTier, woodSettings, group),
                Common.oldChestBlock(Utils.id("old_iron_chest"), Common.stat("open_old_iron_chest"), ironTier, ironSettings, group),
                Common.oldChestBlock(Utils.id("old_gold_chest"), Common.stat("open_old_gold_chest"), goldTier, goldSettings, group),
                Common.oldChestBlock(Utils.id("old_diamond_chest"), Common.stat("open_old_diamond_chest"), diamondTier, diamondSettings, group),
                Common.oldChestBlock(Utils.id("old_obsidian_chest"), Common.stat("open_old_obsidian_chest"), obsidianTier, obsidianSettings, group),
                Common.oldChestBlock(Utils.id("old_netherite_chest"), Common.stat("open_old_netherite_chest"), netheriteTier, netheriteSettings, group)
        );
        // Init block entity type
        Common.oldChestBlockEntityType = BlockEntityType.Builder.of((pos, state) -> new OldChestBlockEntity(Common.getOldChestBlockEntityType(), pos, state, ((AbstractChestBlock) state.getBlock()).getBlockId(), chestAccessMaker, Common.lockable), oldChestContent.getBlocks()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, Common.OLD_CHEST_BLOCK_TYPE.toString()));
        oldChestRegistration.accept(oldChestContent, Common.oldChestBlockEntityType);
        // Register upgrade behaviours
        Predicate<Block> isUpgradableOldChestBlock = (block) -> block.getClass() == AbstractChestBlock.class;
        Common.defineBlockUpgradeBehaviour(isUpgradableOldChestBlock, (context, from, to) -> {
            Level world = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockState state = world.getBlockState(pos);
            Player player = context.getPlayer();
            ItemStack handStack = context.getItemInHand();
            if (AbstractChestBlock.getBlockType(state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE)) == DoubleBlockCombiner.BlockType.SINGLE) {
                boolean upgradeSucceeded = Common.upgradeSingleBlockToOldChest(world, state, pos, from, to);
                if (upgradeSucceeded) handStack.shrink(1);
                return upgradeSucceeded;
            } else if (handStack.getCount() > 1 || (player != null && player.isCreative())) {
                BlockPos otherPos = pos.relative(AbstractChestBlock.getDirectionToAttached(state));
                BlockState otherState = world.getBlockState(otherPos);
                boolean firstSucceeded = Common.upgradeSingleBlockToOldChest(world, state, pos, from, to);
                boolean secondSucceeded = Common.upgradeSingleBlockToOldChest(world, otherState, otherPos, from, to);
                if (firstSucceeded && secondSucceeded) handStack.shrink(2);
                else if (firstSucceeded || secondSucceeded) handStack.shrink(1);
                return firstSucceeded || secondSucceeded;
            }
            return false;
        });
        //</editor-fold>
        //<editor-fold desc="-- Barrel Content">
        // Init block settings
        Properties ironBarrelSettings = Properties.of(Material.WOOD).strength(5, 6).sound(SoundType.WOOD);
        Properties goldBarrelSettings = Properties.of(Material.WOOD).strength(3, 6).sound(SoundType.WOOD);
        Properties diamondBarrelSettings = Properties.of(Material.WOOD).strength(5, 6).sound(SoundType.WOOD);
        Properties obsidianBarrelSettings = Properties.of(Material.WOOD).strength(50, 1200).sound(SoundType.WOOD);
        Properties netheriteBarrelSettings = Properties.of(Material.WOOD).strength(50, 1200).sound(SoundType.WOOD);
        // Init content
        BlockItemCollection<BarrelBlock, BlockItem> barrelContent = BlockItemCollection.of(BarrelBlock[]::new, BlockItem[]::new,
                Common.barrelBlock(Utils.id("iron_barrel"), Common.stat("open_iron_barrel"), ironTier, ironBarrelSettings, group),
                Common.barrelBlock(Utils.id("gold_barrel"), Common.stat("open_gold_barrel"), goldTier, goldBarrelSettings, group),
                Common.barrelBlock(Utils.id("diamond_barrel"), Common.stat("open_diamond_barrel"), diamondTier, diamondBarrelSettings, group),
                Common.barrelBlock(Utils.id("obsidian_barrel"), Common.stat("open_obsidian_barrel"), obsidianTier, obsidianBarrelSettings, group),
                Common.barrelBlock(Utils.id("netherite_barrel"), Common.stat("open_netherite_barrel"), netheriteTier, netheriteBarrelSettings, group)
        );
        // Init block entity type
        Common.barrelBlockEntityType = BlockEntityType.Builder.of((pos, state) -> new BarrelBlockEntity(Common.getBarrelBlockEntityType(), pos, state, ((BarrelBlock) state.getBlock()).getBlockId(), Common.itemAccess, Common.lockable), barrelContent.getBlocks()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, Common.BARREL_BLOCK_TYPE.toString()));
        barrelRegistration.accept(barrelContent, Common.barrelBlockEntityType);
        // Register upgrade behaviours
        Predicate<Block> isUpgradableBarrelBlock = (block) -> block instanceof BarrelBlock || block instanceof net.minecraft.world.level.block.BarrelBlock || barrelTag.contains(block);
        Common.defineBlockUpgradeBehaviour(isUpgradableBarrelBlock, (context, from, to) -> {
            Level world = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            boolean isExpandedStorageBarrel = block instanceof BarrelBlock;
            int inventorySize = !isExpandedStorageBarrel ? Utils.WOOD_STACK_COUNT : Common.getTieredBlock(Common.BARREL_BLOCK_TYPE, ((BarrelBlock) block).getBlockTier()).getSlotCount();
            if (isExpandedStorageBarrel && ((BarrelBlock) block).getBlockTier() == from || !isExpandedStorageBarrel && from == Utils.WOOD_TIER_ID) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                //noinspection ConstantConditions
                CompoundTag tag = blockEntity.saveWithoutMetadata();
                boolean verifiedSize = blockEntity instanceof Container inventory && inventory.getContainerSize() == inventorySize;
                if (!verifiedSize) { // Cannot verify inventory size, we'll let it upgrade if it has or has less than 27 items
                    if (tag.contains("Items", Tag.TAG_LIST)) {
                        ListTag items = tag.getList("Items", Tag.TAG_COMPOUND);
                        if (items.size() <= inventorySize) {
                            verifiedSize = true;
                        }
                    }
                }
                if (verifiedSize) {
                    OpenableBlock toBlock = Common.getTieredBlock(Common.BARREL_BLOCK_TYPE, to);
                    NonNullList<ItemStack> inventory = NonNullList.withSize(toBlock.getSlotCount(), ItemStack.EMPTY);
                    LockCode code = LockCode.fromTag(tag);
                    ContainerHelper.loadAllItems(tag, inventory);
                    world.removeBlockEntity(pos);
                    BlockState newState = toBlock.defaultBlockState().setValue(BlockStateProperties.FACING, state.getValue(BlockStateProperties.FACING));
                    if (world.setBlockAndUpdate(pos, newState)) {
                        BlockEntity newEntity = world.getBlockEntity(pos);
                        //noinspection ConstantConditions
                        CompoundTag newTag = newEntity.saveWithoutMetadata();
                        ContainerHelper.saveAllItems(newTag, inventory);
                        code.addToTag(newTag);
                        newEntity.load(newTag);
                        context.getItemInHand().shrink(1);
                        return true;
                    } else {
                        world.setBlockEntity(blockEntity);
                    }
                }
            }
            return false;
        });
        //</editor-fold>
        //<editor-fold desc="-- Mini Chest Content">
        // Init and register opening stats
        ResourceLocation woodStat = Common.stat("open_wood_mini_chest");
        ResourceLocation pumpkinStat = Common.stat("open_pumpkin_mini_chest");
        ResourceLocation redPresentStat = Common.stat("open_red_mini_present");
        ResourceLocation whitePresentStat = Common.stat("open_white_mini_present");
        ResourceLocation candyCanePresentStat = Common.stat("open_candy_cane_mini_present");
        ResourceLocation greenPresentStat = Common.stat("open_green_mini_present");
        ResourceLocation lavenderPresentStat = Common.stat("open_lavender_mini_present");
        ResourceLocation pinkAmethystPresentStat = Common.stat("open_pink_amethyst_mini_present");
        // Init block settings
        Properties redPresentSettings = Properties.of(Material.WOOD, MaterialColor.COLOR_RED).strength(2.5f).sound(SoundType.WOOD);
        Properties whitePresentSettings = Properties.of(Material.WOOD, MaterialColor.SNOW).strength(2.5f).sound(SoundType.WOOD);
        Properties candyCanePresentSettings = Properties.of(Material.WOOD, MaterialColor.SNOW).strength(2.5f).sound(SoundType.WOOD);
        Properties greenPresentSettings = Properties.of(Material.WOOD, MaterialColor.PLANT).strength(2.5f).sound(SoundType.WOOD);
        Properties lavenderPresentSettings = Properties.of(Material.WOOD, MaterialColor.COLOR_PURPLE).strength(2.5f).sound(SoundType.WOOD);
        Properties pinkAmethystPresentSettings = Properties.of(Material.WOOD, MaterialColor.COLOR_PURPLE).strength(2.5f).sound(SoundType.WOOD);
        // Init content
        BlockItemCollection<MiniChestBlock, BlockItem> miniChestContent = BlockItemCollection.of(MiniChestBlock[]::new, BlockItem[]::new,
                Common.miniChestBlock(Utils.id("vanilla_wood_mini_chest"), woodStat, woodTier, woodSettings, miniChestItemMaker, group),
                Common.miniChestBlock(Utils.id("wood_mini_chest"), woodStat, woodTier, woodSettings, miniChestItemMaker, group),
                Common.miniChestBlock(Utils.id("pumpkin_mini_chest"), pumpkinStat, woodTier, pumpkinSettings, miniChestItemMaker, group),
                Common.miniChestBlock(Utils.id("red_mini_present"), redPresentStat, woodTier, redPresentSettings, miniChestItemMaker, group),
                Common.miniChestBlock(Utils.id("white_mini_present"), whitePresentStat, woodTier, whitePresentSettings, miniChestItemMaker, group),
                Common.miniChestBlock(Utils.id("candy_cane_mini_present"), candyCanePresentStat, woodTier, candyCanePresentSettings, miniChestItemMaker, group),
                Common.miniChestBlock(Utils.id("green_mini_present"), greenPresentStat, woodTier, greenPresentSettings, miniChestItemMaker, group),
                Common.miniChestBlock(Utils.id("lavender_mini_present"), lavenderPresentStat, woodTier, lavenderPresentSettings, miniChestItemMaker, group),
                Common.miniChestBlock(Utils.id("pink_amethyst_mini_present"), pinkAmethystPresentStat, woodTier, pinkAmethystPresentSettings, miniChestItemMaker, group),
                Common.miniChestBlock(Utils.id("vanilla_wood_mini_chest_with_sparrow"), woodStat, woodTier, woodSettings, miniChestItemMaker, group),
                Common.miniChestBlock(Utils.id("wood_mini_chest_with_sparrow"),woodStat, woodTier, woodSettings, miniChestItemMaker, group),
                Common.miniChestBlock(Utils.id("pumpkin_mini_chest_with_sparrow"), pumpkinStat, woodTier, pumpkinSettings, miniChestItemMaker, group),
                Common.miniChestBlock(Utils.id("red_mini_present_with_sparrow"), redPresentStat, woodTier, redPresentSettings, miniChestItemMaker, group),
                Common.miniChestBlock(Utils.id("white_mini_present_with_sparrow"), whitePresentStat, woodTier, whitePresentSettings, miniChestItemMaker, group),
                Common.miniChestBlock(Utils.id("candy_cane_mini_present_with_sparrow"), candyCanePresentStat, woodTier, candyCanePresentSettings, miniChestItemMaker, group),
                Common.miniChestBlock(Utils.id("green_mini_present_with_sparrow"), greenPresentStat, woodTier, greenPresentSettings, miniChestItemMaker, group),
                Common.miniChestBlock(Utils.id("lavender_mini_present_with_sparrow"), lavenderPresentStat, woodTier, lavenderPresentSettings, miniChestItemMaker, group),
                Common.miniChestBlock(Utils.id("pink_amethyst_mini_present_with_sparrow"), pinkAmethystPresentStat, woodTier, pinkAmethystPresentSettings, miniChestItemMaker, group)
        );
        // Init block entity type
        Common.miniChestBlockEntityType = BlockEntityType.Builder.of((pos, state) -> new MiniChestBlockEntity(Common.getMiniChestBlockEntityType(), pos, state, ((MiniChestBlock) state.getBlock()).getBlockId(), Common.itemAccess, Common.lockable), miniChestContent.getBlocks()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, Common.MINI_CHEST_BLOCK_TYPE.toString()));
        miniChestRegistration.accept(miniChestContent, Common.miniChestBlockEntityType);
        //</editor-fold>
        Common.miniChestScreenHandler = miniChestScreenHandlerType;
        //<editor-fold desc="-- Storage mutator logic">
        Predicate<Block> isChestBlock = b -> b instanceof AbstractChestBlock;
        Common.registerMutationBehaviour(isChestBlock, MutationMode.MERGE, (context, world, state, pos, stack) -> {
            Player player = context.getPlayer();
            if (state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) == CursedChestType.SINGLE) {
                CompoundTag tag = stack.getOrCreateTag();
                if (tag.contains("pos")) {
                    BlockPos otherPos = NbtUtils.readBlockPos(tag.getCompound("pos"));
                    BlockState otherState = world.getBlockState(otherPos);
                    Direction direction = Direction.fromNormal(otherPos.subtract(pos));
                    if (direction != null) {
                        if (state.getBlock() == otherState.getBlock()) {
                            if (otherState.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) == CursedChestType.SINGLE) {
                                if (state.getValue(BlockStateProperties.HORIZONTAL_FACING) == otherState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
                                    if (!world.isClientSide()) {
                                        CursedChestType chestType = AbstractChestBlock.getChestType(state.getValue(BlockStateProperties.HORIZONTAL_FACING), direction);
                                        world.setBlockAndUpdate(pos, state.setValue(AbstractChestBlock.CURSED_CHEST_TYPE, chestType));
                                        // note: other state is updated via neighbour update
                                        tag.remove("pos");
                                        //noinspection ConstantConditions
                                        player.displayClientMessage(new TranslatableComponent("tooltip.expandedstorage.storage_mutator.merge_end"), true);
                                    }
                                    return InteractionResult.SUCCESS;
                                } else {
                                    //noinspection ConstantConditions
                                    player.displayClientMessage(new TranslatableComponent("tooltip.expandedstorage.storage_mutator.merge_wrong_facing"), true);
                                }
                            } else {
                                //noinspection ConstantConditions
                                player.displayClientMessage(new TranslatableComponent("tooltip.expandedstorage.storage_mutator.merge_already_double_chest"), true);
                            }
                        } else {
                            //noinspection ConstantConditions
                            player.displayClientMessage(new TranslatableComponent("tooltip.expandedstorage.storage_mutator.merge_wrong_block", state.getBlock().getName()), true);
                        }
                    } else {
                        //noinspection ConstantConditions
                        player.displayClientMessage(new TranslatableComponent("tooltip.expandedstorage.storage_mutator.merge_not_adjacent"), true);
                    }
                } else {
                    if (!world.isClientSide()) {
                        tag.put("pos", NbtUtils.writeBlockPos(pos));
                        //noinspection ConstantConditions
                        player.displayClientMessage(new TranslatableComponent("tooltip.expandedstorage.storage_mutator.merge_start", Utils.ALT_USE), true);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.FAIL;
        });
        Common.registerMutationBehaviour(isChestBlock, MutationMode.SPLIT, (context, world, state, pos, stack) -> {
            if (state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) != CursedChestType.SINGLE) {
                if (!world.isClientSide()) {
                    world.setBlockAndUpdate(pos, state.setValue(AbstractChestBlock.CURSED_CHEST_TYPE, CursedChestType.SINGLE));
                    // note: other state is updated to single via neighbour update
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        });
        Common.registerMutationBehaviour(isChestBlock, MutationMode.ROTATE, (context, world, state, pos, stack) -> {
            if (!world.isClientSide()) {
                CursedChestType chestType = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
                if (chestType == CursedChestType.SINGLE) {
                    world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise()));
                } else {
                    BlockPos otherPos = pos.relative(AbstractChestBlock.getDirectionToAttached(state));
                    BlockState otherState = world.getBlockState(otherPos);
                    if (chestType == CursedChestType.TOP || chestType == CursedChestType.BOTTOM) {
                        world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise()));
                        world.setBlockAndUpdate(otherPos, otherState.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise()));
                    } else {
                        world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite()).setValue(AbstractChestBlock.CURSED_CHEST_TYPE, state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE).getOpposite()));
                        world.setBlockAndUpdate(otherPos, otherState.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite()).setValue(AbstractChestBlock.CURSED_CHEST_TYPE, otherState.getValue(AbstractChestBlock.CURSED_CHEST_TYPE).getOpposite()));
                    }
                }
            }
            return InteractionResult.SUCCESS;
        });
        Common.registerMutationBehaviour(b -> b instanceof ChestBlock, MutationMode.SWAP_THEME, (context, world, state, pos, stack) -> {
            List<Block> blocks = chestCycle.getValues();
            int index = blocks.indexOf(state.getBlock());
            if (index != -1) { // Cannot change style e.g. iron chest, ect.
                Block next = blocks.get((index + 1) % blocks.size());
                CursedChestType chestType = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
                if (chestType != CursedChestType.SINGLE) {
                    BlockPos otherPos = pos.relative(AbstractChestBlock.getDirectionToAttached(state));
                    BlockState otherState = world.getBlockState(otherPos);
                    world.setBlock(otherPos, next.defaultBlockState()
                                                      .setValue(BlockStateProperties.HORIZONTAL_FACING, otherState.getValue(BlockStateProperties.HORIZONTAL_FACING))
                                                      .setValue(BlockStateProperties.WATERLOGGED, otherState.getValue(BlockStateProperties.WATERLOGGED))
                                                      .setValue(AbstractChestBlock.CURSED_CHEST_TYPE, chestType.getOpposite()), Block.UPDATE_SUPPRESS_LIGHT | Block.UPDATE_CLIENTS);
                }
                world.setBlock(pos, next.defaultBlockState()
                                             .setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
                                             .setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED))
                                             .setValue(AbstractChestBlock.CURSED_CHEST_TYPE, chestType), Block.UPDATE_SUPPRESS_LIGHT | Block.UPDATE_CLIENTS);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        });
        Predicate<Block> isBarrelBlock = b -> b instanceof BarrelBlock || b instanceof net.minecraft.world.level.block.BarrelBlock || barrelTag.contains(b);
        Common.registerMutationBehaviour(isBarrelBlock, MutationMode.ROTATE, (context, world, state, pos, stack) -> {
            if (state.hasProperty(BlockStateProperties.FACING)) {
                if (!world.isClientSide()) {
                    world.setBlockAndUpdate(pos, state.cycle(BlockStateProperties.FACING));
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        });
        Predicate<Block> isMiniChest = b -> b instanceof MiniChestBlock;
        Common.registerMutationBehaviour(isMiniChest, MutationMode.ROTATE, (context, world, state, pos, stack) -> {
            if (!world.isClientSide()) {
                world.setBlockAndUpdate(pos, state.rotate(Rotation.CLOCKWISE_90));
            }
            return InteractionResult.SUCCESS;
        });
        Common.registerMutationBehaviour(isMiniChest, MutationMode.SWAP_THEME, (context, world, state, pos, stack) -> {
            String nameHash = DigestUtils.md5Hex(stack.getHoverName().getContents());
            List<Block> blocks;
            if (nameHash.equals("4c88924788f419b562d50acfddc3a781")) {
                blocks = miniChestSecretCycle.getValues();
            } else if (nameHash.equals("ef5a30521df4c0dc7568844eefe7e7e3")) {
                blocks = miniChestSecretCycle2.getValues();
            } else {
                blocks = miniChestCycle.getValues();
            }
            int index = blocks.indexOf(state.getBlock());
            if (index != -1) { // Illegal state / misconfigured tag
                Block next = blocks.get((index + 1) % blocks.size());
                world.setBlockAndUpdate(pos, next.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING)).setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)));
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        });
        //</editor-fold>
    }

    public static MenuType<MiniChestScreenHandler> getMiniChestScreenHandlerType() {
        return miniChestScreenHandler;
    }
}
