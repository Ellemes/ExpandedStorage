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

import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ContainerLock;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.Tag;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
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

public final class Common {
    public static final Identifier BARREL_BLOCK_TYPE = Utils.id("barrel");
    public static final Identifier CHEST_BLOCK_TYPE = Utils.id("chest");
    public static final Identifier OLD_CHEST_BLOCK_TYPE = Utils.id("old_chest");
    public static final Identifier MINI_CHEST_BLOCK_TYPE = Utils.id("mini_chest");

    private static final Map<Predicate<Block>, BlockUpgradeBehaviour> BLOCK_UPGRADE_BEHAVIOURS = new HashMap<>();
    private static final Map<Pair<Predicate<Block>, MutationMode>, MutatorBehaviour> MUTATOR_BEHAVIOURS = new HashMap<>();
    private static final Map<Pair<Identifier, Identifier>, OpenableBlock> BLOCKS = new HashMap<>();
    private static final Map<Identifier, TextureCollection> CHEST_TEXTURES = new HashMap<>();

    private static BlockEntityType<ChestBlockEntity> chestBlockEntityType;
    private static BlockEntityType<OldChestBlockEntity> oldChestBlockEntityType;
    private static BlockEntityType<BarrelBlockEntity> barrelBlockEntityType;
    private static BlockEntityType<MiniChestBlockEntity> miniChestBlockEntityType;

    private static Function<OpenableBlockEntity, ItemAccess> itemAccess;
    private static Function<OpenableBlockEntity, Lockable> lockable;
    private static ScreenHandlerType<MiniChestScreenHandler> miniChestScreenHandler;

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
            Identifier blockId, Identifier stat, Tier tier, Settings settings,
            BiFunction<ChestBlock, Item.Settings, BlockItem> blockItemMaker, ItemGroup group
    ) {
        ChestBlock block = new ChestBlock(tier.getBlockSettings().apply(settings), blockId, tier.getId(), stat, tier.getSlotCount());
        Common.registerTieredBlock(block);
        return new BlockItemPair<>(block, blockItemMaker.apply(block, new Item.Settings().group(group)));
    }

    private static BlockItemPair<AbstractChestBlock, BlockItem> oldChestBlock(
            Identifier blockId, Identifier stat, Tier tier, Settings settings, ItemGroup group
    ) {
        AbstractChestBlock block = new AbstractChestBlock(tier.getBlockSettings().apply(settings), blockId, tier.getId(), stat, tier.getSlotCount());
        Common.registerTieredBlock(block);
        BlockItem item = new BlockItem(block, tier.getItemSettings().apply(new Item.Settings().group(group)));
        return new BlockItemPair<>(block, item);
    }

    private static BlockItemPair<BarrelBlock, BlockItem> barrelBlock(
            Identifier blockId, Identifier stat, Tier tier, Settings settings, ItemGroup group
    ) {
        BarrelBlock block = new BarrelBlock(tier.getBlockSettings().apply(settings), blockId, tier.getId(), stat, tier.getSlotCount());
        Common.registerTieredBlock(block);
        BlockItem item = new BlockItem(block, tier.getItemSettings().apply(new Item.Settings().group(group)));
        return new BlockItemPair<>(block, item);
    }

    private static BlockItemPair<MiniChestBlock, BlockItem> miniChestBlock(
            Identifier blockId, Identifier stat, Tier tier, Settings settings, BiFunction<MiniChestBlock, Item.Settings, BlockItem> blockItemMaker, ItemGroup group
    ) {
        MiniChestBlock block = new MiniChestBlock(tier.getBlockSettings().apply(settings), blockId, stat);
        Common.registerTieredBlock(block);
        BlockItem item = blockItemMaker.apply(block, tier.getItemSettings().apply(new Item.Settings().group(group)));
        return new BlockItemPair<>(block, item);
    }

    static Identifier[] getChestTextures(ChestBlock[] blocks) {
        Identifier[] textures = new Identifier[blocks.length * CursedChestType.values().length];
        int index = 0;
        for (ChestBlock block : blocks) {
            Identifier blockId = block.getBlockId();
            for (CursedChestType type : CursedChestType.values()) {
                textures[index++] = Common.getChestTexture(blockId, type);
            }
        }
        return textures;
    }

    static void registerChestTextures(ChestBlock[] blocks) {
        for (ChestBlock block : blocks) {
            Identifier blockId = block.getBlockId();
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

    private static boolean upgradeSingleBlockToChest(World world, BlockState state, BlockPos pos, Identifier from, Identifier to) {
        Block block = state.getBlock();
        boolean isExpandedStorageChest = block instanceof ChestBlock;
        int inventorySize = !isExpandedStorageChest ? Utils.WOOD_STACK_COUNT : Common.getTieredBlock(Common.CHEST_BLOCK_TYPE, ((OpenableBlock) block).getBlockTier()).getSlotCount();
        if (isExpandedStorageChest && ((OpenableBlock) block).getBlockTier() == from || !isExpandedStorageChest && from == Utils.WOOD_TIER_ID) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            //noinspection ConstantConditions
            NbtCompound tag = blockEntity.createNbt();
            boolean verifiedSize = blockEntity instanceof Inventory inventory && inventory.size() == inventorySize;
            if (!verifiedSize) { // Cannot verify inventory size, we'll let it upgrade if it has or has less than 27 items
                if (tag.contains("Items", NbtElement.LIST_TYPE)) {
                    NbtList items = tag.getList("Items", NbtElement.COMPOUND_TYPE);
                    if (items.size() <= inventorySize) {
                        verifiedSize = true;
                    }
                }
            }
            if (verifiedSize) {
                ChestBlock toBlock = (ChestBlock) Common.getTieredBlock(Common.CHEST_BLOCK_TYPE, to);
                DefaultedList<ItemStack> inventory = DefaultedList.ofSize(toBlock.getSlotCount(), ItemStack.EMPTY);
                ContainerLock code = ContainerLock.fromNbt(tag);
                Inventories.readNbt(tag, inventory);
                world.removeBlockEntity(pos);
                // Needs fixing up to check for vanilla states.
                BlockState newState = toBlock.getDefaultState()
                                             .with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING))
                                             .with(Properties.WATERLOGGED, state.get(Properties.WATERLOGGED));
                if (state.contains(ChestBlock.CURSED_CHEST_TYPE)) {
                    newState = newState.with(ChestBlock.CURSED_CHEST_TYPE, state.get(ChestBlock.CURSED_CHEST_TYPE));
                } else if (state.contains(Properties.CHEST_TYPE)) {
                    ChestType type = state.get(Properties.CHEST_TYPE);
                    newState = newState.with(ChestBlock.CURSED_CHEST_TYPE, type == ChestType.LEFT ? CursedChestType.RIGHT : type == ChestType.RIGHT ? CursedChestType.LEFT : CursedChestType.SINGLE);
                }
                if (world.setBlockState(pos, newState)) {
                    BlockEntity newEntity = world.getBlockEntity(pos);
                    //noinspection ConstantConditions
                    NbtCompound newTag = newEntity.createNbt();
                    Inventories.writeNbt(newTag, inventory);
                    code.writeNbt(newTag);
                    newEntity.readNbt(newTag);
                    return true;
                } else {
                    world.addBlockEntity(blockEntity);
                }
            }
        }
        return false;
    }

    private static boolean upgradeSingleBlockToOldChest(World world, BlockState state, BlockPos pos, Identifier from, Identifier to) {
        if (((OpenableBlock) state.getBlock()).getBlockTier() == from) {
            AbstractChestBlock toBlock = (AbstractChestBlock) Common.getTieredBlock(Common.OLD_CHEST_BLOCK_TYPE, to);
            DefaultedList<ItemStack> inventory = DefaultedList.ofSize(toBlock.getSlotCount(), ItemStack.EMPTY);
            BlockEntity blockEntity = world.getBlockEntity(pos);
            //noinspection ConstantConditions
            NbtCompound tag = blockEntity.createNbt();
            ContainerLock code = ContainerLock.fromNbt(tag);
            Inventories.readNbt(tag, inventory);
            world.removeBlockEntity(pos);
            BlockState newState = toBlock.getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING)).with(AbstractChestBlock.CURSED_CHEST_TYPE, state.get(AbstractChestBlock.CURSED_CHEST_TYPE));
            if (world.setBlockState(pos, newState)) {
                BlockEntity newEntity = world.getBlockEntity(pos);
                //noinspection ConstantConditions
                NbtCompound newTag = newEntity.createNbt();
                Inventories.writeNbt(newTag, inventory);
                code.writeNbt(newTag);
                newEntity.readNbt(newTag);
                return true;
            }  else {
                world.addBlockEntity(blockEntity);
            }
        }
        return false;
    }

    public static Identifier stat(String stat) {
        Identifier statId = Utils.id(stat);
        Registry.register(Registry.CUSTOM_STAT, statId, statId); // Forge doesn't provide custom registries for stats;
        Stats.CUSTOM.getOrCreateStat(statId);
        return statId;
    }

    private static void defineTierUpgradePath(Pair<Identifier, Item>[] items, boolean wrapTooltipManually, ItemGroup group, Tier... tiers) {
        int numTiers = tiers.length;
        int index = 1;
        for (int fromIndex = 0; fromIndex < numTiers - 1; fromIndex++) {
            Tier fromTier = tiers[fromIndex];
            for (int toIndex = fromIndex + 1; toIndex < numTiers; toIndex++) {
                Tier toTier = tiers[toIndex];
                Identifier itemId = Utils.id(fromTier.getId().getPath() + "_to_" + toTier.getId().getPath() + "_conversion_kit");
                Item.Settings settings = fromTier.getItemSettings()
                                                 .andThen(toTier.getItemSettings())
                                                 .apply(new Item.Settings().group(group).maxCount(16));
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

    public static OpenableBlock getTieredBlock(Identifier blockType, Identifier tier) {
        return Common.BLOCKS.get(new Pair<>(blockType, tier));
    }

    public static void declareChestTextures(Identifier block, Identifier singleTexture, Identifier leftTexture, Identifier rightTexture, Identifier topTexture, Identifier bottomTexture, Identifier frontTexture, Identifier backTexture) {
        if (!Common.CHEST_TEXTURES.containsKey(block)) {
            TextureCollection collection = new TextureCollection(singleTexture, leftTexture, rightTexture, topTexture, bottomTexture, frontTexture, backTexture);
            Common.CHEST_TEXTURES.put(block, collection);
        } else {
            throw new IllegalArgumentException("Tried registering chest textures for \"" + block + "\" which already has textures.");
        }
    }

    public static Identifier getChestTexture(Identifier block, CursedChestType chestType) {
        if (Common.CHEST_TEXTURES.containsKey(block)) return Common.CHEST_TEXTURES.get(block).getTexture(chestType);
        return MissingSprite.getMissingSpriteId();
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

    public static void registerContent(ItemGroup group, boolean isClient,
                                       Consumer<Pair<Identifier, Item>[]> baseRegistration, boolean manuallyWrapTooltips,
                                       RegistrationConsumer<ChestBlock, BlockItem, ChestBlockEntity> chestRegistration, Tag.Identified<Block> chestTag, BiFunction<ChestBlock, Item.Settings, BlockItem> chestItemMaker, Function<OpenableBlockEntity, ItemAccess> chestAccessMaker,
                                       RegistrationConsumer<AbstractChestBlock, BlockItem, OldChestBlockEntity> oldChestRegistration,
                                       RegistrationConsumer<BarrelBlock, BlockItem, BarrelBlockEntity> barrelRegistration, Tag.Identified<Block> barrelTag,
                                       RegistrationConsumer<MiniChestBlock, BlockItem, MiniChestBlockEntity> miniChestRegistration, BiFunction<MiniChestBlock, Item.Settings, BlockItem> miniChestItemMaker, ScreenHandlerType<MiniChestScreenHandler> miniChestScreenHandlerType,
                                       Tag.Identified<Block> chestCycle, Tag.Identified<Block> miniChestCycle, Tag.Identified<Block> miniChestSecretCycle, Tag.Identified<Block> miniChestSecretCycle2) {
        final Tier woodTier = new Tier(Utils.WOOD_TIER_ID, Utils.WOOD_STACK_COUNT, UnaryOperator.identity(), UnaryOperator.identity());
        final Tier ironTier = new Tier(Utils.id("iron"), 54, Settings::requiresTool, UnaryOperator.identity());
        final Tier goldTier = new Tier(Utils.id("gold"), 81, Settings::requiresTool, UnaryOperator.identity());
        final Tier diamondTier = new Tier(Utils.id("diamond"), 108, Settings::requiresTool, UnaryOperator.identity());
        final Tier obsidianTier = new Tier(Utils.id("obsidian"), 108, Settings::requiresTool, UnaryOperator.identity());
        final Tier netheriteTier = new Tier(Utils.id("netherite"), 135, Settings::requiresTool, Item.Settings::fireproof);

        //<editor-fold desc="-- Base Content">
        //noinspection unchecked
        Pair<Identifier, Item>[] baseContent = new Pair[16];
        baseContent[0] = new Pair<>(Utils.id("storage_mutator"), new StorageMutator(new Item.Settings().maxCount(1).group(group)));
        Common.defineTierUpgradePath(baseContent, manuallyWrapTooltips, group, woodTier, ironTier, goldTier, diamondTier, obsidianTier, netheriteTier);
        baseRegistration.accept(baseContent);
        //</editor-fold>
        //<editor-fold desc="-- Chest Content">
        // Init block settings
        Settings woodSettings = Settings.of(Material.WOOD, MapColor.OAK_TAN).strength(2.5f).sounds(BlockSoundGroup.WOOD);
        Settings pumpkinSettings = Settings.of(Material.GOURD, MapColor.ORANGE).strength(1).sounds(BlockSoundGroup.WOOD);
        Settings presentSettings = Settings.of(Material.WOOD, state -> {
            CursedChestType type = state.get(AbstractChestBlock.CURSED_CHEST_TYPE);
            if (type == CursedChestType.SINGLE) return MapColor.RED;
            else if (type == CursedChestType.FRONT || type == CursedChestType.BACK) return MapColor.DARK_GREEN;
            return MapColor.WHITE;
        }).strength(2.5f).sounds(BlockSoundGroup.WOOD);
        Settings ironSettings = Settings.of(Material.METAL, MapColor.IRON_GRAY).strength(5, 6).sounds(BlockSoundGroup.METAL);
        Settings goldSettings = Settings.of(Material.METAL, MapColor.GOLD).strength(3, 6).sounds(BlockSoundGroup.METAL);
        Settings diamondSettings = Settings.of(Material.METAL, MapColor.DIAMOND_BLUE).strength(5, 6).sounds(BlockSoundGroup.METAL);
        Settings obsidianSettings = Settings.of(Material.STONE, MapColor.BLACK).strength(50, 1200);
        Settings netheriteSettings = Settings.of(Material.METAL, MapColor.BLACK).strength(50, 1200).sounds(BlockSoundGroup.NETHERITE);
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
        Common.chestBlockEntityType = BlockEntityType.Builder.create((pos, state) -> new ChestBlockEntity(Common.getChestBlockEntityType(), pos, state, ((OpenableBlock) state.getBlock()).getBlockId(), chestAccessMaker, Common.lockable), chestContent.getBlocks()).build(Util.getChoiceType(TypeReferences.BLOCK_ENTITY, Common.CHEST_BLOCK_TYPE.toString()));
        chestRegistration.accept(chestContent, Common.chestBlockEntityType);
        // Register chest upgrade behaviours
        Predicate<Block> isUpgradableChestBlock = (block) -> block instanceof ChestBlock || block instanceof net.minecraft.block.ChestBlock || chestTag.contains(block);
        Common.defineBlockUpgradeBehaviour(isUpgradableChestBlock, (context, from, to) -> {
            World world = context.getWorld();
            BlockPos pos = context.getBlockPos();
            BlockState state = world.getBlockState(pos);
            PlayerEntity player = context.getPlayer();
            ItemStack handStack = context.getStack();
            if (state.getBlock() instanceof ChestBlock) {
                CursedChestType type = state.get(AbstractChestBlock.CURSED_CHEST_TYPE);
                Direction facing = state.get(Properties.HORIZONTAL_FACING);
                if (AbstractChestBlock.getBlockType(type) == DoubleBlockProperties.Type.SINGLE) {
                    boolean upgradeSucceeded = Common.upgradeSingleBlockToChest(world, state, pos, from, to);
                    if (upgradeSucceeded) handStack.decrement(1);
                    return upgradeSucceeded;
                } else if (handStack.getCount() > 1 || (player != null && player.isCreative())) {
                    BlockPos otherPos = pos.offset(AbstractChestBlock.getDirectionToAttached(type, facing));
                    BlockState otherState = world.getBlockState(otherPos);
                    boolean firstSucceeded = Common.upgradeSingleBlockToChest(world, state, pos, from, to);
                    boolean secondSucceeded = Common.upgradeSingleBlockToChest(world, otherState, otherPos, from, to);
                    if (firstSucceeded && secondSucceeded) handStack.decrement(2);
                    else if (firstSucceeded || secondSucceeded) handStack.decrement(1);
                    return firstSucceeded || secondSucceeded;
                }
            } else {
                if (net.minecraft.block.ChestBlock.getDoubleBlockType(state) == DoubleBlockProperties.Type.SINGLE) {
                    boolean upgradeSucceeded = Common.upgradeSingleBlockToChest(world, state, pos, from, to);
                    if (upgradeSucceeded) handStack.decrement(1);
                    return upgradeSucceeded;
                } else if (handStack.getCount() > 1 || (player != null && player.isCreative())) {
                    BlockPos otherPos = pos.offset(net.minecraft.block.ChestBlock.getFacing(state));
                    BlockState otherState = world.getBlockState(otherPos);
                    boolean firstSucceeded = Common.upgradeSingleBlockToChest(world, state, pos, from, to);
                    boolean secondSucceeded = Common.upgradeSingleBlockToChest(world, otherState, otherPos, from, to);
                    if (firstSucceeded && secondSucceeded) handStack.decrement(2);
                    else if (firstSucceeded || secondSucceeded) handStack.decrement(1);
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
        Common.oldChestBlockEntityType = BlockEntityType.Builder.create((pos, state) -> new OldChestBlockEntity(Common.getOldChestBlockEntityType(), pos, state, ((OpenableBlock) state.getBlock()).getBlockId(), chestAccessMaker, Common.lockable), oldChestContent.getBlocks()).build(Util.getChoiceType(TypeReferences.BLOCK_ENTITY, Common.OLD_CHEST_BLOCK_TYPE.toString()));
        oldChestRegistration.accept(oldChestContent, Common.oldChestBlockEntityType);
        // Register upgrade behaviours
        Predicate<Block> isUpgradableOldChestBlock = (block) -> block.getClass() == AbstractChestBlock.class;
        Common.defineBlockUpgradeBehaviour(isUpgradableOldChestBlock, (context, from, to) -> {
            World world = context.getWorld();
            BlockPos pos = context.getBlockPos();
            BlockState state = world.getBlockState(pos);
            PlayerEntity player = context.getPlayer();
            ItemStack handStack = context.getStack();
            if (AbstractChestBlock.getBlockType(state.get(AbstractChestBlock.CURSED_CHEST_TYPE)) == DoubleBlockProperties.Type.SINGLE) {
                boolean upgradeSucceeded = Common.upgradeSingleBlockToOldChest(world, state, pos, from, to);
                if (upgradeSucceeded) handStack.decrement(1);
                return upgradeSucceeded;
            } else if (handStack.getCount() > 1 || (player != null && player.isCreative())) {
                BlockPos otherPos = pos.offset(AbstractChestBlock.getDirectionToAttached(state));
                BlockState otherState = world.getBlockState(otherPos);
                boolean firstSucceeded = Common.upgradeSingleBlockToOldChest(world, state, pos, from, to);
                boolean secondSucceeded = Common.upgradeSingleBlockToOldChest(world, otherState, otherPos, from, to);
                if (firstSucceeded && secondSucceeded) handStack.decrement(2);
                else if (firstSucceeded || secondSucceeded) handStack.decrement(1);
                return firstSucceeded || secondSucceeded;
            }
            return false;
        });
        //</editor-fold>
        //<editor-fold desc="-- Barrel Content">
        // Init block settings
        Settings ironBarrelSettings = Settings.of(Material.WOOD).strength(5, 6).sounds(BlockSoundGroup.WOOD);
        Settings goldBarrelSettings = Settings.of(Material.WOOD).strength(3, 6).sounds(BlockSoundGroup.WOOD);
        Settings diamondBarrelSettings = Settings.of(Material.WOOD).strength(5, 6).sounds(BlockSoundGroup.WOOD);
        Settings obsidianBarrelSettings = Settings.of(Material.WOOD).strength(50, 1200).sounds(BlockSoundGroup.WOOD);
        Settings netheriteBarrelSettings = Settings.of(Material.WOOD).strength(50, 1200).sounds(BlockSoundGroup.WOOD);
        // Init content
        BlockItemCollection<BarrelBlock, BlockItem> barrelContent = BlockItemCollection.of(BarrelBlock[]::new, BlockItem[]::new,
                Common.barrelBlock(Utils.id("iron_barrel"), Common.stat("open_iron_barrel"), ironTier, ironBarrelSettings, group),
                Common.barrelBlock(Utils.id("gold_barrel"), Common.stat("open_gold_barrel"), goldTier, goldBarrelSettings, group),
                Common.barrelBlock(Utils.id("diamond_barrel"), Common.stat("open_diamond_barrel"), diamondTier, diamondBarrelSettings, group),
                Common.barrelBlock(Utils.id("obsidian_barrel"), Common.stat("open_obsidian_barrel"), obsidianTier, obsidianBarrelSettings, group),
                Common.barrelBlock(Utils.id("netherite_barrel"), Common.stat("open_netherite_barrel"), netheriteTier, netheriteBarrelSettings, group)
        );
        // Init block entity type
        Common.barrelBlockEntityType = BlockEntityType.Builder.create((pos, state) -> new BarrelBlockEntity(Common.getBarrelBlockEntityType(), pos, state, ((OpenableBlock) state.getBlock()).getBlockId(), Common.itemAccess, Common.lockable), barrelContent.getBlocks()).build(Util.getChoiceType(TypeReferences.BLOCK_ENTITY, Common.BARREL_BLOCK_TYPE.toString()));
        barrelRegistration.accept(barrelContent, Common.barrelBlockEntityType);
        // Register upgrade behaviours
        Predicate<Block> isUpgradableBarrelBlock = (block) -> block instanceof BarrelBlock || block instanceof net.minecraft.block.BarrelBlock || barrelTag.contains(block);
        Common.defineBlockUpgradeBehaviour(isUpgradableBarrelBlock, (context, from, to) -> {
            World world = context.getWorld();
            BlockPos pos = context.getBlockPos();
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            boolean isExpandedStorageBarrel = block instanceof BarrelBlock;
            int inventorySize = !isExpandedStorageBarrel ? Utils.WOOD_STACK_COUNT : Common.getTieredBlock(Common.BARREL_BLOCK_TYPE, ((OpenableBlock) block).getBlockTier()).getSlotCount();
            if (isExpandedStorageBarrel && ((OpenableBlock) block).getBlockTier() == from || !isExpandedStorageBarrel && from == Utils.WOOD_TIER_ID) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                //noinspection ConstantConditions
                NbtCompound tag = blockEntity.createNbt();
                boolean verifiedSize = blockEntity instanceof Inventory inventory && inventory.size() == inventorySize;
                if (!verifiedSize) { // Cannot verify inventory size, we'll let it upgrade if it has or has less than 27 items
                    if (tag.contains("Items", NbtElement.LIST_TYPE)) {
                        NbtList items = tag.getList("Items", NbtElement.COMPOUND_TYPE);
                        if (items.size() <= inventorySize) {
                            verifiedSize = true;
                        }
                    }
                }
                if (verifiedSize) {
                    OpenableBlock toBlock = Common.getTieredBlock(Common.BARREL_BLOCK_TYPE, to);
                    DefaultedList<ItemStack> inventory = DefaultedList.ofSize(toBlock.getSlotCount(), ItemStack.EMPTY);
                    ContainerLock code = ContainerLock.fromNbt(tag);
                    Inventories.readNbt(tag, inventory);
                    world.removeBlockEntity(pos);
                    BlockState newState = toBlock.getDefaultState().with(Properties.FACING, state.get(Properties.FACING));
                    if (world.setBlockState(pos, newState)) {
                        BlockEntity newEntity = world.getBlockEntity(pos);
                        //noinspection ConstantConditions
                        NbtCompound newTag = newEntity.createNbt();
                        Inventories.writeNbt(newTag, inventory);
                        code.writeNbt(newTag);
                        newEntity.readNbt(newTag);
                        context.getStack().decrement(1);
                        return true;
                    } else {
                        world.addBlockEntity(blockEntity);
                    }
                }
            }
            return false;
        });
        //</editor-fold>
        //<editor-fold desc="-- Mini Chest Content">
        // Init and register opening stats
        Identifier woodStat = Common.stat("open_wood_mini_chest");
        Identifier pumpkinStat = Common.stat("open_pumpkin_mini_chest");
        Identifier redPresentStat = Common.stat("open_red_mini_present");
        Identifier whitePresentStat = Common.stat("open_white_mini_present");
        Identifier candyCanePresentStat = Common.stat("open_candy_cane_mini_present");
        Identifier greenPresentStat = Common.stat("open_green_mini_present");
        Identifier lavenderPresentStat = Common.stat("open_lavender_mini_present");
        Identifier pinkAmethystPresentStat = Common.stat("open_pink_amethyst_mini_present");
        // Init block settings
        Settings redPresentSettings = Settings.of(Material.WOOD, MapColor.RED).strength(2.5f).sounds(BlockSoundGroup.WOOD);
        Settings whitePresentSettings = Settings.of(Material.WOOD, MapColor.WHITE).strength(2.5f).sounds(BlockSoundGroup.WOOD);
        Settings candyCanePresentSettings = Settings.of(Material.WOOD, MapColor.WHITE).strength(2.5f).sounds(BlockSoundGroup.WOOD);
        Settings greenPresentSettings = Settings.of(Material.WOOD, MapColor.DARK_GREEN).strength(2.5f).sounds(BlockSoundGroup.WOOD);
        Settings lavenderPresentSettings = Settings.of(Material.WOOD, MapColor.PURPLE).strength(2.5f).sounds(BlockSoundGroup.WOOD);
        Settings pinkAmethystPresentSettings = Settings.of(Material.WOOD, MapColor.PURPLE).strength(2.5f).sounds(BlockSoundGroup.WOOD);
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
        Common.miniChestBlockEntityType = BlockEntityType.Builder.create((pos, state) -> new MiniChestBlockEntity(Common.getMiniChestBlockEntityType(), pos, state, ((OpenableBlock) state.getBlock()).getBlockId(), Common.itemAccess, Common.lockable), miniChestContent.getBlocks()).build(Util.getChoiceType(TypeReferences.BLOCK_ENTITY, Common.MINI_CHEST_BLOCK_TYPE.toString()));
        miniChestRegistration.accept(miniChestContent, Common.miniChestBlockEntityType);
        //</editor-fold>
        Common.miniChestScreenHandler = miniChestScreenHandlerType;
        //<editor-fold desc="-- Storage mutator logic">
        Predicate<Block> isChestBlock = b -> b instanceof AbstractChestBlock;
        Common.registerMutationBehaviour(isChestBlock, MutationMode.MERGE, (context, world, state, pos, stack) -> {
            PlayerEntity player = context.getPlayer();
            if (state.get(AbstractChestBlock.CURSED_CHEST_TYPE) == CursedChestType.SINGLE) {
                NbtCompound tag = stack.getOrCreateNbt();
                if (tag.contains("pos")) {
                    BlockPos otherPos = NbtHelper.toBlockPos(tag.getCompound("pos"));
                    BlockState otherState = world.getBlockState(otherPos);
                    Direction direction = Direction.fromVector(otherPos.subtract(pos));
                    if (direction != null) {
                        if (state.getBlock() == otherState.getBlock()) {
                            if (otherState.get(AbstractChestBlock.CURSED_CHEST_TYPE) == CursedChestType.SINGLE) {
                                if (state.get(Properties.HORIZONTAL_FACING) == otherState.get(Properties.HORIZONTAL_FACING)) {
                                    if (!world.isClient()) {
                                        CursedChestType chestType = AbstractChestBlock.getChestType(state.get(Properties.HORIZONTAL_FACING), direction);
                                        world.setBlockState(pos, state.with(AbstractChestBlock.CURSED_CHEST_TYPE, chestType));
                                        // note: other state is updated via neighbour update
                                        tag.remove("pos");
                                        //noinspection ConstantConditions
                                        player.sendMessage(new TranslatableText("tooltip.expandedstorage.storage_mutator.merge_end"), true);
                                    }
                                    return ActionResult.SUCCESS;
                                } else {
                                    //noinspection ConstantConditions
                                    player.sendMessage(new TranslatableText("tooltip.expandedstorage.storage_mutator.merge_wrong_facing"), true);
                                }
                            } else {
                                //noinspection ConstantConditions
                                player.sendMessage(new TranslatableText("tooltip.expandedstorage.storage_mutator.merge_already_double_chest"), true);
                            }
                        } else {
                            //noinspection ConstantConditions
                            player.sendMessage(new TranslatableText("tooltip.expandedstorage.storage_mutator.merge_wrong_block", state.getBlock().getName()), true);
                        }
                    } else {
                        //noinspection ConstantConditions
                        player.sendMessage(new TranslatableText("tooltip.expandedstorage.storage_mutator.merge_not_adjacent"), true);
                    }
                } else {
                    if (!world.isClient()) {
                        tag.put("pos", NbtHelper.fromBlockPos(pos));
                        //noinspection ConstantConditions
                        player.sendMessage(new TranslatableText("tooltip.expandedstorage.storage_mutator.merge_start", Utils.ALT_USE), true);
                    }
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.FAIL;
        });
        Common.registerMutationBehaviour(isChestBlock, MutationMode.SPLIT, (context, world, state, pos, stack) -> {
            if (state.get(AbstractChestBlock.CURSED_CHEST_TYPE) != CursedChestType.SINGLE) {
                if (!world.isClient()) {
                    world.setBlockState(pos, state.with(AbstractChestBlock.CURSED_CHEST_TYPE, CursedChestType.SINGLE));
                    // note: other state is updated to single via neighbour update
                }
                return ActionResult.SUCCESS;
            }
            return ActionResult.FAIL;
        });
        Common.registerMutationBehaviour(isChestBlock, MutationMode.ROTATE, (context, world, state, pos, stack) -> {
            if (!world.isClient()) {
                CursedChestType chestType = state.get(AbstractChestBlock.CURSED_CHEST_TYPE);
                if (chestType == CursedChestType.SINGLE) {
                    world.setBlockState(pos, state.with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING).rotateYClockwise()));
                } else {
                    BlockPos otherPos = pos.offset(AbstractChestBlock.getDirectionToAttached(state));
                    BlockState otherState = world.getBlockState(otherPos);
                    if (chestType == CursedChestType.TOP || chestType == CursedChestType.BOTTOM) {
                        world.setBlockState(pos, state.with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING).rotateYClockwise()));
                        world.setBlockState(otherPos, otherState.with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING).rotateYClockwise()));
                    } else {
                        world.setBlockState(pos, state.with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING).getOpposite()).with(AbstractChestBlock.CURSED_CHEST_TYPE, state.get(AbstractChestBlock.CURSED_CHEST_TYPE).getOpposite()));
                        world.setBlockState(otherPos, otherState.with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING).getOpposite()).with(AbstractChestBlock.CURSED_CHEST_TYPE, otherState.get(AbstractChestBlock.CURSED_CHEST_TYPE).getOpposite()));
                    }
                }
            }
            return ActionResult.SUCCESS;
        });
        Common.registerMutationBehaviour(b -> b instanceof ChestBlock, MutationMode.SWAP_THEME, (context, world, state, pos, stack) -> {
            List<Block> blocks = chestCycle.values();
            int index = blocks.indexOf(state.getBlock());
            if (index != -1) { // Cannot change style e.g. iron chest, ect.
                Block next = blocks.get((index + 1) % blocks.size());
                CursedChestType chestType = state.get(AbstractChestBlock.CURSED_CHEST_TYPE);
                if (chestType != CursedChestType.SINGLE) {
                    BlockPos otherPos = pos.offset(AbstractChestBlock.getDirectionToAttached(state));
                    BlockState otherState = world.getBlockState(otherPos);
                    world.setBlockState(otherPos, next.getDefaultState()
                                                      .with(Properties.HORIZONTAL_FACING, otherState.get(Properties.HORIZONTAL_FACING))
                                                      .with(Properties.WATERLOGGED, otherState.get(Properties.WATERLOGGED))
                                                      .with(AbstractChestBlock.CURSED_CHEST_TYPE, chestType.getOpposite()), Block.SKIP_LIGHTING_UPDATES | Block.NOTIFY_LISTENERS);
                }
                world.setBlockState(pos, next.getDefaultState()
                                             .with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING))
                                             .with(Properties.WATERLOGGED, state.get(Properties.WATERLOGGED))
                                             .with(AbstractChestBlock.CURSED_CHEST_TYPE, chestType), Block.SKIP_LIGHTING_UPDATES | Block.NOTIFY_LISTENERS);
                return ActionResult.SUCCESS;
            }
            return ActionResult.FAIL;
        });
        Predicate<Block> isBarrelBlock = b -> b instanceof BarrelBlock || b instanceof net.minecraft.block.BarrelBlock || barrelTag.contains(b);
        Common.registerMutationBehaviour(isBarrelBlock, MutationMode.ROTATE, (context, world, state, pos, stack) -> {
            if (state.contains(Properties.FACING)) {
                if (!world.isClient()) {
                    world.setBlockState(pos, state.cycle(Properties.FACING));
                }
                return ActionResult.SUCCESS;
            }
            return ActionResult.FAIL;
        });
        Predicate<Block> isMiniChest = b -> b instanceof MiniChestBlock;
        Common.registerMutationBehaviour(isMiniChest, MutationMode.ROTATE, (context, world, state, pos, stack) -> {
            if (!world.isClient()) {
                world.setBlockState(pos, state.rotate(BlockRotation.CLOCKWISE_90));
            }
            return ActionResult.SUCCESS;
        });
        Common.registerMutationBehaviour(isMiniChest, MutationMode.SWAP_THEME, (context, world, state, pos, stack) -> {
            String nameHash = DigestUtils.md5Hex(stack.getName().asString());
            List<Block> blocks;
            if (nameHash.equals("4c88924788f419b562d50acfddc3a781")) {
                blocks = miniChestSecretCycle.values();
            } else if (nameHash.equals("ef5a30521df4c0dc7568844eefe7e7e3")) {
                blocks = miniChestSecretCycle2.values();
            } else {
                blocks = miniChestCycle.values();
            }
            int index = blocks.indexOf(state.getBlock());
            if (index != -1) { // Illegal state / misconfigured tag
                Block next = blocks.get((index + 1) % blocks.size());
                world.setBlockState(pos, next.getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING)).with(Properties.WATERLOGGED, state.get(Properties.WATERLOGGED)));
                return ActionResult.SUCCESS;
            }
            return ActionResult.FAIL;
        });
        //</editor-fold>
    }

    public static ScreenHandlerType<MiniChestScreenHandler> getMiniChestScreenHandlerType() {
        return miniChestScreenHandler;
    }
}
