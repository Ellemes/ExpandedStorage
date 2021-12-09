package ninjaphenix.expandedstorage;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import ninjaphenix.expandedstorage.tier.Tier;
import ninjaphenix.expandedstorage.wrappers.PlatformUtils;
import ninjaphenix.expandedstorage.block.ChestBlock;
import ninjaphenix.expandedstorage.block.misc.ChestBlockEntity;
import ninjaphenix.expandedstorage.client.ChestBlockEntityRenderer;

import java.util.Collections;
import java.util.Set;

public class ChestMain {
    public static void initialize() {
        // Init and register opening stats
        ResourceLocation woodOpenStat = BaseCommon.registerStat(Utils.resloc("open_wood_chest"));
        ResourceLocation pumpkinOpenStat = BaseCommon.registerStat(Utils.resloc("open_pumpkin_chest"));
        ResourceLocation christmasOpenStat = BaseCommon.registerStat(Utils.resloc("open_christmas_chest"));
        ResourceLocation ironOpenStat = BaseCommon.registerStat(Utils.resloc("open_iron_chest"));
        ResourceLocation goldOpenStat = BaseCommon.registerStat(Utils.resloc("open_gold_chest"));
        ResourceLocation diamondOpenStat = BaseCommon.registerStat(Utils.resloc("open_diamond_chest"));
        ResourceLocation obsidianOpenStat = BaseCommon.registerStat(Utils.resloc("open_obsidian_chest"));
        ResourceLocation netheriteOpenStat = BaseCommon.registerStat(Utils.resloc("open_netherite_chest"));
        // Init block properties
        BlockBehaviour.Properties woodProperties = BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                                                                            .harvestTool(ToolType.AXE)
                                                                            .harvestLevel(Tiers.WOOD.getLevel())
                                                                            .strength(2.5F)
                                                                            .sound(SoundType.WOOD);
        BlockBehaviour.Properties pumpkinProperties = BlockBehaviour.Properties.of(Material.VEGETABLE, MaterialColor.COLOR_ORANGE)
                                                                               .harvestTool(ToolType.AXE)
                                                                               .harvestLevel(Tiers.WOOD.getLevel())
                                                                               .strength(1.0F)
                                                                               .sound(SoundType.WOOD);
        BlockBehaviour.Properties christmasProperties = BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                                                                                 .harvestTool(ToolType.AXE)
                                                                                 .harvestLevel(Tiers.WOOD.getLevel())
                                                                                 .strength(2.5F)
                                                                                 .sound(SoundType.WOOD);
        BlockBehaviour.Properties ironProperties = BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL)
                                                                            .harvestTool(ToolType.PICKAXE)
                                                                            .harvestLevel(Tiers.STONE.getLevel())
                                                                            .requiresCorrectToolForDrops()
                                                                            .strength(5.0F, 6.0F)
                                                                            .sound(SoundType.METAL);
        BlockBehaviour.Properties goldProperties = BlockBehaviour.Properties.of(Material.METAL, MaterialColor.GOLD)
                                                                            .harvestTool(ToolType.PICKAXE)
                                                                            .harvestLevel(Tiers.STONE.getLevel())
                                                                            .requiresCorrectToolForDrops()
                                                                            .strength(3.0F, 6.0F)
                                                                            .sound(SoundType.METAL);
        BlockBehaviour.Properties diamondProperties = BlockBehaviour.Properties.of(Material.METAL, MaterialColor.DIAMOND)
                                                                               .harvestTool(ToolType.PICKAXE)
                                                                               .harvestLevel(Tiers.IRON.getLevel())
                                                                               .requiresCorrectToolForDrops()
                                                                               .strength(5.0F, 6.0F)
                                                                               .sound(SoundType.METAL);
        BlockBehaviour.Properties obsidianProperties = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
                                                                                .harvestTool(ToolType.PICKAXE)
                                                                                .harvestLevel(Tiers.DIAMOND.getLevel())
                                                                                .requiresCorrectToolForDrops()
                                                                                .strength(50.0F, 1200.0F);
        BlockBehaviour.Properties netheriteProperties = BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK)
                                                                                 .harvestTool(ToolType.PICKAXE)
                                                                                 .harvestLevel(Tiers.DIAMOND.getLevel())
                                                                                 .requiresCorrectToolForDrops()
                                                                                 .strength(50.0F, 1200.0F)
                                                                                 .sound(SoundType.NETHERITE_BLOCK);
        // Init blocks
        ChestBlock woodChestBlock = ChestMain.chestBlock(Utils.resloc("wood_chest"), woodOpenStat, Utils.WOOD_TIER, woodProperties);
        ChestBlock pumpkinChestBlock = ChestMain.chestBlock(Utils.resloc("pumpkin_chest"), pumpkinOpenStat, Utils.WOOD_TIER, pumpkinProperties);
        ChestBlock christmasChestBlock = ChestMain.chestBlock(Utils.resloc("christmas_chest"), christmasOpenStat, Utils.WOOD_TIER, christmasProperties);
        ChestBlock ironChestBlock = ChestMain.chestBlock(Utils.resloc("iron_chest"), ironOpenStat, Utils.IRON_TIER, ironProperties);
        ChestBlock goldChestBlock = ChestMain.chestBlock(Utils.resloc("gold_chest"), goldOpenStat, Utils.GOLD_TIER, goldProperties);
        ChestBlock diamondChestBlock = ChestMain.chestBlock(Utils.resloc("diamond_chest"), diamondOpenStat, Utils.DIAMOND_TIER, diamondProperties);
        ChestBlock obsidianChestBlock = ChestMain.chestBlock(Utils.resloc("obsidian_chest"), obsidianOpenStat, Utils.OBSIDIAN_TIER, obsidianProperties);
        ChestBlock netheriteChestBlock = ChestMain.chestBlock(Utils.resloc("netherite_chest"), netheriteOpenStat, Utils.NETHERITE_TIER, netheriteProperties);
        Set<ChestBlock> blocks = ImmutableSet.copyOf(new ChestBlock[]{woodChestBlock, pumpkinChestBlock, christmasChestBlock, ironChestBlock, goldChestBlock, diamondChestBlock, obsidianChestBlock, netheriteChestBlock});
        // Init items
        BlockItem woodChestItem = ChestMain.chestItem(Utils.WOOD_TIER, woodChestBlock);
        BlockItem pumpkinChestItem = ChestMain.chestItem(Utils.WOOD_TIER, pumpkinChestBlock);
        BlockItem christmasChestItem = ChestMain.chestItem(Utils.WOOD_TIER, christmasChestBlock);
        BlockItem ironChestItem = ChestMain.chestItem(Utils.IRON_TIER, ironChestBlock);
        BlockItem goldChestItem = ChestMain.chestItem(Utils.GOLD_TIER, goldChestBlock);
        BlockItem diamondChestItem = ChestMain.chestItem(Utils.DIAMOND_TIER, diamondChestBlock);
        BlockItem obsidianChestItem = ChestMain.chestItem(Utils.OBSIDIAN_TIER, obsidianChestBlock);
        BlockItem netheriteChestItem = ChestMain.chestItem(Utils.NETHERITE_TIER, netheriteChestBlock);
        Set<BlockItem> items = ImmutableSet.copyOf(new BlockItem[]{woodChestItem, pumpkinChestItem, christmasChestItem, ironChestItem, goldChestItem, diamondChestItem, obsidianChestItem, netheriteChestItem});
        // Init block entity type
        BlockEntityType<ChestBlockEntity> blockEntityType = new BlockEntityType<>(() -> new ChestBlockEntity(ChestCommon.getBlockEntityType(), null), Collections.unmodifiableSet(blocks), null);
        blockEntityType.setRegistryName(ChestCommon.BLOCK_TYPE);
        // Register content
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addGenericListener(BlockEntityType.class, (RegistryEvent.Register<BlockEntityType<?>> event) -> {
            event.getRegistry().register(blockEntityType);
            ChestCommon.setBlockEntityType(blockEntityType);
        });
        modEventBus.addGenericListener(Block.class, (RegistryEvent.Register<Block> event) -> {
            IForgeRegistry<Block> registry = event.getRegistry();
            blocks.forEach(registry::register);
        });
        modEventBus.addGenericListener(Item.class, (RegistryEvent.Register<Item> event) -> {
            IForgeRegistry<Item> registry = event.getRegistry();
            items.forEach(registry::register);
        });
        // Register chest module icon & upgrade behaviours
        ChestCommon.registerTabIcon(netheriteChestItem);
        ChestCommon.registerUpgradeBehaviours(BlockTags.createOptional(new ResourceLocation("forge", "chests/wooden")));
        // Do client side stuff
        if (PlatformUtils.getInstance().isClient()) {
            modEventBus.addListener((FMLClientSetupEvent event) -> {
                ClientRegistry.bindTileEntityRenderer(ChestCommon.getBlockEntityType(), ChestBlockEntityRenderer::new);
            });
            ChestCommon.registerChestTextures(blocks);
            modEventBus.addListener((TextureStitchEvent.Pre event) -> {
                if (!event.getMap().location().equals(Sheets.CHEST_SHEET)) {
                    return;
                }
                ChestCommon.getChestTextures(blocks).forEach(event::addSprite);
            });
        }
    }

    private static ChestBlock chestBlock(ResourceLocation blockId, ResourceLocation stat, Tier tier, BlockBehaviour.Properties properties) {
        tier.getBlockProperties().apply(properties.dynamicShape());
        // Forge makes it so correct tool harvest tier and requires tool are copied.
        ChestBlock block = new ChestBlock(properties, blockId, tier.getId(), stat, tier.getSlotCount());
        block.setRegistryName(blockId);
        BaseApi.getInstance().registerTieredBlock(block);
        return block;
    }

    private static BlockItem chestItem(Tier tier, ChestBlock block) {
        Item.Properties itemProperties = tier.getItemProperties().apply(new Item.Properties().tab(Utils.TAB));
        if (PlatformUtils.getInstance().isClient()) {
            ChestMain.addItemBlockEntityRenderer(itemProperties, block);
        }
        BlockItem item = new BlockItem(block, itemProperties);
        item.setRegistryName(block.getBlockId());
        return item;
    }

    @OnlyIn(Dist.CLIENT)
    private static void addItemBlockEntityRenderer(Item.Properties itemProperties, ChestBlock block) {
        itemProperties.setISTER(() -> () -> new BlockEntityWithoutLevelRenderer() {
            ChestBlockEntity renderEntity = null;

            @Override
            public void renderByItem(ItemStack item, ItemTransforms.TransformType transform, PoseStack pose, MultiBufferSource source, int light, int overlay) {
                BlockEntityRenderDispatcher.instance.renderItem(this.getOrCreateBlockEntity(), pose, source, light, overlay);
            }

            private ChestBlockEntity getOrCreateBlockEntity() {
                if (renderEntity == null) {
                    renderEntity = new ChestBlockEntity(ChestCommon.getBlockEntityType(), block.getBlockId());
                }
                return renderEntity;
            }
        });
    }
}
