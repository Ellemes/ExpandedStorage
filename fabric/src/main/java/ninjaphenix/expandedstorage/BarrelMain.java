package ninjaphenix.expandedstorage;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import ninjaphenix.expandedstorage.block.BarrelBlock;
import ninjaphenix.expandedstorage.block.misc.BarrelBlockEntity;
import ninjaphenix.expandedstorage.tier.Tier;
import ninjaphenix.expandedstorage.wrappers.PlatformUtils;

import java.util.Collections;
import java.util.Set;

public final class BarrelMain {
    public static void initialize() {
        // Init and register opening stats
        ResourceLocation ironOpenStat = BaseCommon.registerStat(Utils.resloc("open_iron_barrel"));
        ResourceLocation goldOpenStat = BaseCommon.registerStat(Utils.resloc("open_gold_barrel"));
        ResourceLocation diamondOpenStat = BaseCommon.registerStat(Utils.resloc("open_diamond_barrel"));
        ResourceLocation obsidianOpenStat = BaseCommon.registerStat(Utils.resloc("open_obsidian_barrel"));
        ResourceLocation netheriteOpenStat = BaseCommon.registerStat(Utils.resloc("open_netherite_barrel"));
        // Init block properties
        BlockBehaviour.Properties ironProperties = FabricBlockSettings.of(Material.WOOD, MaterialColor.WOOD)
                                                                      .breakByTool(FabricToolTags.AXES, Tiers.STONE.getLevel())
                                                                      .requiresCorrectToolForDrops() // End of FBS
                                                                      .strength(5.0F, 6.0F)
                                                                      .sound(SoundType.WOOD);
        BlockBehaviour.Properties goldProperties = FabricBlockSettings.of(Material.WOOD, MaterialColor.WOOD)
                                                                      .breakByTool(FabricToolTags.AXES, Tiers.STONE.getLevel())
                                                                      .requiresCorrectToolForDrops() // End of FBS
                                                                      .strength(3.0F, 6.0F)
                                                                      .sound(SoundType.WOOD);
        BlockBehaviour.Properties diamondProperties = FabricBlockSettings.of(Material.WOOD, MaterialColor.WOOD)
                                                                         .breakByTool(FabricToolTags.AXES, Tiers.IRON.getLevel())
                                                                         .requiresCorrectToolForDrops() // End of FBS
                                                                         .strength(5.0F, 6.0F)
                                                                         .sound(SoundType.WOOD);
        BlockBehaviour.Properties obsidianProperties = FabricBlockSettings.of(Material.WOOD, MaterialColor.WOOD)
                                                                          .breakByTool(FabricToolTags.AXES, Tiers.DIAMOND.getLevel())
                                                                          .requiresCorrectToolForDrops() // End of FBS
                                                                          .strength(50.0F, 1200.0F)
                                                                          .sound(SoundType.WOOD);
        BlockBehaviour.Properties netheriteProperties = FabricBlockSettings.of(Material.WOOD, MaterialColor.WOOD)
                                                                           .breakByTool(FabricToolTags.AXES, Tiers.DIAMOND.getLevel())
                                                                           .requiresCorrectToolForDrops() // End of FBS
                                                                           .strength(50.0F, 1200.0F)
                                                                           .sound(SoundType.WOOD);
        // Init blocks
        BarrelBlock ironBarrelBlock = BarrelMain.barrelBlock(Utils.resloc("iron_barrel"), ironOpenStat, Utils.IRON_TIER, ironProperties);
        BarrelBlock goldBarrelBlock = BarrelMain.barrelBlock(Utils.resloc("gold_barrel"), goldOpenStat, Utils.GOLD_TIER, goldProperties);
        BarrelBlock diamondBarrelBlock = BarrelMain.barrelBlock(Utils.resloc("diamond_barrel"), diamondOpenStat, Utils.DIAMOND_TIER, diamondProperties);
        BarrelBlock obsidianBarrelBlock = BarrelMain.barrelBlock(Utils.resloc("obsidian_barrel"), obsidianOpenStat, Utils.OBSIDIAN_TIER, obsidianProperties);
        BarrelBlock netheriteBarrelBlock = BarrelMain.barrelBlock(Utils.resloc("netherite_barrel"), netheriteOpenStat, Utils.NETHERITE_TIER, netheriteProperties);
        Set<BarrelBlock> blocks = ImmutableSet.copyOf(new BarrelBlock[]{ironBarrelBlock, goldBarrelBlock, diamondBarrelBlock, obsidianBarrelBlock, netheriteBarrelBlock});
        // Init items
        BarrelMain.barrelItem(Utils.IRON_TIER, ironBarrelBlock);
        BarrelMain.barrelItem(Utils.GOLD_TIER, goldBarrelBlock);
        BarrelMain.barrelItem(Utils.DIAMOND_TIER, diamondBarrelBlock);
        BarrelMain.barrelItem(Utils.OBSIDIAN_TIER, obsidianBarrelBlock);
        BlockItem netheriteBarrelItem = BarrelMain.barrelItem(Utils.NETHERITE_TIER, netheriteBarrelBlock);
        // Init block entity type
        BlockEntityType<BarrelBlockEntity> blockEntityType = new BlockEntityType<>(() -> new BarrelBlockEntity(BarrelCommon.getBlockEntityType(), null), Collections.unmodifiableSet(blocks), null);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, BarrelCommon.BLOCK_TYPE, blockEntityType);
        BarrelCommon.setBlockEntityType(blockEntityType);
        // Register chest module icon & upgrade behaviours
        BarrelCommon.registerTabIcon(netheriteBarrelItem);
        BarrelCommon.registerUpgradeBehaviours(TagRegistry.block(new ResourceLocation("c", "wooden_barrels")));
        // Do client side stuff
        if (PlatformUtils.getInstance().isClient()) {
            blocks.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutoutMipped()));
        }
    }

    private static BarrelBlock barrelBlock(ResourceLocation blockId, ResourceLocation stat, Tier tier, BlockBehaviour.Properties properties) {
        BarrelBlock block = Registry.register(Registry.BLOCK, blockId, new BarrelBlock(tier.getBlockProperties().apply(properties), blockId, tier.getId(), stat, tier.getSlotCount()));
        BaseApi.getInstance().registerTieredBlock(block);
        return block;
    }

    private static BlockItem barrelItem(Tier tier, BarrelBlock block) {
        Item.Properties itemProperties = tier.getItemProperties().apply(new Item.Properties().tab(Utils.TAB));
        return Registry.register(Registry.ITEM, block.getBlockId(), new BlockItem(block, itemProperties));
    }
}
