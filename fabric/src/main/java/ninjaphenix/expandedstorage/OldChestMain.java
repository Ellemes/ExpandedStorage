package ninjaphenix.expandedstorage;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
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
import ninjaphenix.expandedstorage.tier.Tier;
import ninjaphenix.expandedstorage.block.OldChestBlock;
import ninjaphenix.expandedstorage.block.misc.OldChestBlockEntity;

import java.util.Collections;
import java.util.Set;

public final class OldChestMain {
    public static void initialize() {
        // Init and register opening stats
        ResourceLocation woodOpenStat = BaseCommon.registerStat(Utils.resloc("open_old_wood_chest"));
        ResourceLocation ironOpenStat = BaseCommon.registerStat(Utils.resloc("open_old_iron_chest"));
        ResourceLocation goldOpenStat = BaseCommon.registerStat(Utils.resloc("open_old_gold_chest"));
        ResourceLocation diamondOpenStat = BaseCommon.registerStat(Utils.resloc("open_old_diamond_chest"));
        ResourceLocation obsidianOpenStat = BaseCommon.registerStat(Utils.resloc("open_old_obsidian_chest"));
        ResourceLocation netheriteOpenStat = BaseCommon.registerStat(Utils.resloc("open_old_netherite_chest"));
        // Init block properties
        BlockBehaviour.Properties woodProperties = FabricBlockSettings.of(Material.WOOD, MaterialColor.WOOD)
                                                                      .breakByTool(FabricToolTags.AXES, Tiers.WOOD.getLevel())
                                                                      .strength(2.5F) // End of FBS
                                                                      .sound(SoundType.WOOD);
        BlockBehaviour.Properties ironProperties = FabricBlockSettings.of(Material.METAL, MaterialColor.METAL)
                                                                      .breakByTool(FabricToolTags.PICKAXES, Tiers.STONE.getLevel())
                                                                      .requiresCorrectToolForDrops() // End of FBS
                                                                      .strength(5.0F, 6.0F)
                                                                      .sound(SoundType.METAL);
        BlockBehaviour.Properties goldProperties = FabricBlockSettings.of(Material.METAL, MaterialColor.GOLD)
                                                                      .breakByTool(FabricToolTags.PICKAXES, Tiers.STONE.getLevel())
                                                                      .requiresCorrectToolForDrops() // End of FBS
                                                                      .strength(3.0F, 6.0F)
                                                                      .sound(SoundType.METAL);
        BlockBehaviour.Properties diamondProperties = FabricBlockSettings.of(Material.METAL, MaterialColor.DIAMOND)
                                                                         .breakByTool(FabricToolTags.PICKAXES, Tiers.IRON.getLevel())
                                                                         .requiresCorrectToolForDrops() // End of FBS
                                                                         .strength(5.0F, 6.0F)
                                                                         .sound(SoundType.METAL);
        BlockBehaviour.Properties obsidianProperties = FabricBlockSettings.of(Material.STONE, MaterialColor.COLOR_BLACK)
                                                                          .breakByTool(FabricToolTags.PICKAXES, Tiers.DIAMOND.getLevel())
                                                                          .requiresCorrectToolForDrops() // End of FBS
                                                                          .strength(50.0F, 1200.0F);
        BlockBehaviour.Properties netheriteProperties = FabricBlockSettings.of(Material.METAL, MaterialColor.COLOR_BLACK)
                                                                           .breakByTool(FabricToolTags.PICKAXES, Tiers.DIAMOND.getLevel())
                                                                           .requiresCorrectToolForDrops() // End of FBS
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
        OldChestMain.oldChestItem(Utils.WOOD_TIER, woodChestBlock);
        OldChestMain.oldChestItem(Utils.IRON_TIER, ironChestBlock);
        OldChestMain.oldChestItem(Utils.GOLD_TIER, goldChestBlock);
        OldChestMain.oldChestItem(Utils.DIAMOND_TIER, diamondChestBlock);
        OldChestMain.oldChestItem(Utils.OBSIDIAN_TIER, obsidianChestBlock);
        BlockItem netheriteChestItem = OldChestMain.oldChestItem(Utils.NETHERITE_TIER, netheriteChestBlock);
        // Init block entity type
        BlockEntityType<OldChestBlockEntity> blockEntityType = new BlockEntityType<>(() -> new OldChestBlockEntity(OldChestCommon.getBlockEntityType(), null), Collections.unmodifiableSet(blocks), null);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, OldChestCommon.BLOCK_TYPE, blockEntityType);
        OldChestCommon.setBlockEntityType(blockEntityType);
        // Register chest module icon & upgrade behaviours
        OldChestCommon.registerTabIcon(netheriteChestItem);
        OldChestCommon.registerUpgradeBehaviours();
    }

    private static BlockItem oldChestItem(Tier tier, OldChestBlock block) {
        Item.Properties itemProperties = tier.getItemProperties().apply(new Item.Properties().tab(Utils.TAB));
        return Registry.register(Registry.ITEM, block.getBlockId(), new BlockItem(block, itemProperties));
    }

    private static OldChestBlock oldChestBlock(ResourceLocation blockId, ResourceLocation stat, Tier tier, BlockBehaviour.Properties properties) {
        tier.getBlockProperties().apply(properties);
        OldChestBlock block = Registry.register(Registry.BLOCK, blockId, new OldChestBlock(properties, blockId, tier.getId(), stat, tier.getSlotCount()));
        BaseApi.getInstance().registerTieredBlock(block);
        return block;
    }
}
