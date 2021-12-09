package ninjaphenix.expandedstorage;

import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import ninjaphenix.expandedstorage.tier.Tier;
import ninjaphenix.expandedstorage.block.OldChestBlock;
import ninjaphenix.expandedstorage.block.misc.OldChestBlockEntity;

import java.util.Collections;
import java.util.Set;

public class OldChestMain {
    public static void initialize() {
        // Init and register opening stats
        ResourceLocation woodOpenStat = BaseCommon.registerStat(Utils.resloc("open_old_wood_chest"));
        ResourceLocation ironOpenStat = BaseCommon.registerStat(Utils.resloc("open_old_iron_chest"));
        ResourceLocation goldOpenStat = BaseCommon.registerStat(Utils.resloc("open_old_gold_chest"));
        ResourceLocation diamondOpenStat = BaseCommon.registerStat(Utils.resloc("open_old_diamond_chest"));
        ResourceLocation obsidianOpenStat = BaseCommon.registerStat(Utils.resloc("open_old_obsidian_chest"));
        ResourceLocation netheriteOpenStat = BaseCommon.registerStat(Utils.resloc("open_old_netherite_chest"));
        // Init block properties
        BlockBehaviour.Properties woodProperties = BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
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
        OldChestBlock woodChestBlock = OldChestMain.oldChestBlock(Utils.resloc("old_wood_chest"), woodOpenStat, Utils.WOOD_TIER, woodProperties);
        OldChestBlock ironChestBlock = OldChestMain.oldChestBlock(Utils.resloc("old_iron_chest"), ironOpenStat, Utils.IRON_TIER, ironProperties);
        OldChestBlock goldChestBlock = OldChestMain.oldChestBlock(Utils.resloc("old_gold_chest"), goldOpenStat, Utils.GOLD_TIER, goldProperties);
        OldChestBlock diamondChestBlock = OldChestMain.oldChestBlock(Utils.resloc("old_diamond_chest"), diamondOpenStat, Utils.DIAMOND_TIER, diamondProperties);
        OldChestBlock obsidianChestBlock = OldChestMain.oldChestBlock(Utils.resloc("old_obsidian_chest"), obsidianOpenStat, Utils.OBSIDIAN_TIER, obsidianProperties);
        OldChestBlock netheriteChestBlock = OldChestMain.oldChestBlock(Utils.resloc("old_netherite_chest"), netheriteOpenStat, Utils.NETHERITE_TIER, netheriteProperties);
        Set<OldChestBlock> blocks = ImmutableSet.copyOf(new OldChestBlock[]{woodChestBlock, ironChestBlock, goldChestBlock, diamondChestBlock, obsidianChestBlock, netheriteChestBlock});
        // Init items
        BlockItem woodChestItem = OldChestMain.oldChestItem(Utils.WOOD_TIER, woodChestBlock);
        BlockItem ironChestItem = OldChestMain.oldChestItem(Utils.IRON_TIER, ironChestBlock);
        BlockItem goldChestItem = OldChestMain.oldChestItem(Utils.GOLD_TIER, goldChestBlock);
        BlockItem diamondChestItem = OldChestMain.oldChestItem(Utils.DIAMOND_TIER, diamondChestBlock);
        BlockItem obsidianChestItem = OldChestMain.oldChestItem(Utils.OBSIDIAN_TIER, obsidianChestBlock);
        BlockItem netheriteChestItem = OldChestMain.oldChestItem(Utils.NETHERITE_TIER, netheriteChestBlock);
        Set<BlockItem> items = ImmutableSet.copyOf(new BlockItem[]{woodChestItem, ironChestItem, goldChestItem, diamondChestItem, obsidianChestItem, netheriteChestItem});
        // Init block entity type
        BlockEntityType<OldChestBlockEntity> blockEntityType = new BlockEntityType<>(() -> new OldChestBlockEntity(OldChestCommon.getBlockEntityType(), null), Collections.unmodifiableSet(blocks), null);
        blockEntityType.setRegistryName(OldChestCommon.BLOCK_TYPE);
        // Register content
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addGenericListener(BlockEntityType.class, (RegistryEvent.Register<BlockEntityType<?>> event) -> {
            event.getRegistry().register(blockEntityType);
            OldChestCommon.setBlockEntityType(blockEntityType);
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
        OldChestCommon.registerTabIcon(netheriteChestItem);
        OldChestCommon.registerUpgradeBehaviours();
    }

    private static BlockItem oldChestItem(Tier tier, OldChestBlock block) {
        Item.Properties itemProperties = tier.getItemProperties().apply(new Item.Properties().tab(Utils.TAB));
        BlockItem item = new BlockItem(block, itemProperties);
        item.setRegistryName(block.getBlockId());
        return item;
    }

    private static OldChestBlock oldChestBlock(ResourceLocation id, ResourceLocation stat, Tier tier, BlockBehaviour.Properties properties) {
        tier.getBlockProperties().apply(properties);
        OldChestBlock block = new OldChestBlock(properties, id, tier.getId(), stat, tier.getSlotCount());
        block.setRegistryName(id);
        BaseApi.getInstance().registerTieredBlock(block);
        return block;
    }
}
