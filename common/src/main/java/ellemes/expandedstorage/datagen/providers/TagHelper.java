package ellemes.expandedstorage.datagen.providers;

import ellemes.expandedstorage.datagen.content.ModBlocks;
import ellemes.expandedstorage.datagen.content.ModItems;
import ellemes.expandedstorage.datagen.content.ModTags;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public class TagHelper {
    public static void registerBlockTags(Function<TagKey<Block>, TagsProvider.TagAppender<Block>> tagMaker) {
        tagMaker.apply(BlockTags.MINEABLE_WITH_AXE)
            .add(ModBlocks.IRON_BARREL)
            .add(ModBlocks.GOLD_BARREL)
            .add(ModBlocks.DIAMOND_BARREL)
            .add(ModBlocks.OBSIDIAN_BARREL)
            .add(ModBlocks.NETHERITE_BARREL)
            .add(ModBlocks.WOOD_CHEST)
            .add(ModBlocks.PUMPKIN_CHEST)
            .add(ModBlocks.PRESENT)
            .add(ModBlocks.BAMBOO_CHEST)
            .add(ModBlocks.OLD_WOOD_CHEST)
            .add(ModBlocks.VANILLA_WOOD_MINI_CHEST)
            .add(ModBlocks.WOOD_MINI_CHEST)
            .add(ModBlocks.PUMPKIN_MINI_CHEST)
            .add(ModBlocks.RED_MINI_PRESENT)
            .add(ModBlocks.WHITE_MINI_PRESENT)
            .add(ModBlocks.CANDY_CANE_MINI_PRESENT)
            .add(ModBlocks.GREEN_MINI_PRESENT)
            .add(ModBlocks.LAVENDER_MINI_PRESENT)
            .add(ModBlocks.PINK_AMETHYST_MINI_PRESENT)
            .add(ModBlocks.VANILLA_WOOD_MINI_CHEST_WITH_SPARROW)
            .add(ModBlocks.WOOD_MINI_CHEST_WITH_SPARROW)
            .add(ModBlocks.PUMPKIN_MINI_CHEST_WITH_SPARROW)
            .add(ModBlocks.RED_MINI_PRESENT_WITH_SPARROW)
            .add(ModBlocks.WHITE_MINI_PRESENT_WITH_SPARROW)
            .add(ModBlocks.CANDY_CANE_MINI_PRESENT_WITH_SPARROW)
            .add(ModBlocks.GREEN_MINI_PRESENT_WITH_SPARROW)
            .add(ModBlocks.LAVENDER_MINI_PRESENT_WITH_SPARROW)
            .add(ModBlocks.PINK_AMETHYST_MINI_PRESENT_WITH_SPARROW);
        tagMaker.apply(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(ModBlocks.IRON_CHEST)
            .add(ModBlocks.GOLD_CHEST)
            .add(ModBlocks.DIAMOND_CHEST)
            .add(ModBlocks.OBSIDIAN_CHEST)
            .add(ModBlocks.NETHERITE_CHEST)
            .add(ModBlocks.OLD_IRON_CHEST)
            .add(ModBlocks.OLD_GOLD_CHEST)
            .add(ModBlocks.OLD_DIAMOND_CHEST)
            .add(ModBlocks.OLD_OBSIDIAN_CHEST)
            .add(ModBlocks.OLD_NETHERITE_CHEST);
        tagMaker.apply(BlockTags.GUARDED_BY_PIGLINS)
            .add(ModBlocks.IRON_BARREL)
            .add(ModBlocks.GOLD_BARREL)
            .add(ModBlocks.DIAMOND_BARREL)
            .add(ModBlocks.OBSIDIAN_BARREL)
            .add(ModBlocks.NETHERITE_BARREL)
            .add(ModBlocks.WOOD_CHEST)
            .add(ModBlocks.PUMPKIN_CHEST)
            .add(ModBlocks.PRESENT)
            .add(ModBlocks.BAMBOO_CHEST)
            .add(ModBlocks.IRON_CHEST)
            .add(ModBlocks.GOLD_CHEST)
            .add(ModBlocks.DIAMOND_CHEST)
            .add(ModBlocks.OBSIDIAN_CHEST)
            .add(ModBlocks.NETHERITE_CHEST)
            .add(ModBlocks.OLD_WOOD_CHEST)
            .add(ModBlocks.OLD_IRON_CHEST)
            .add(ModBlocks.OLD_GOLD_CHEST)
            .add(ModBlocks.OLD_DIAMOND_CHEST)
            .add(ModBlocks.OLD_OBSIDIAN_CHEST)
            .add(ModBlocks.OLD_NETHERITE_CHEST)
            .add(ModBlocks.VANILLA_WOOD_MINI_CHEST)
            .add(ModBlocks.WOOD_MINI_CHEST)
            .add(ModBlocks.PUMPKIN_MINI_CHEST)
            .add(ModBlocks.RED_MINI_PRESENT)
            .add(ModBlocks.WHITE_MINI_PRESENT)
            .add(ModBlocks.CANDY_CANE_MINI_PRESENT)
            .add(ModBlocks.GREEN_MINI_PRESENT)
            .add(ModBlocks.LAVENDER_MINI_PRESENT)
            .add(ModBlocks.PINK_AMETHYST_MINI_PRESENT)
            .add(ModBlocks.VANILLA_WOOD_MINI_CHEST_WITH_SPARROW)
            .add(ModBlocks.WOOD_MINI_CHEST_WITH_SPARROW)
            .add(ModBlocks.PUMPKIN_MINI_CHEST_WITH_SPARROW)
            .add(ModBlocks.RED_MINI_PRESENT_WITH_SPARROW)
            .add(ModBlocks.WHITE_MINI_PRESENT_WITH_SPARROW)
            .add(ModBlocks.CANDY_CANE_MINI_PRESENT_WITH_SPARROW)
            .add(ModBlocks.GREEN_MINI_PRESENT_WITH_SPARROW)
            .add(ModBlocks.LAVENDER_MINI_PRESENT_WITH_SPARROW)
            .add(ModBlocks.PINK_AMETHYST_MINI_PRESENT_WITH_SPARROW);
        tagMaker.apply(BlockTags.NEEDS_DIAMOND_TOOL)
            .add(ModBlocks.OBSIDIAN_BARREL)
            .add(ModBlocks.NETHERITE_BARREL)
            .add(ModBlocks.OBSIDIAN_CHEST)
            .add(ModBlocks.NETHERITE_CHEST)
            .add(ModBlocks.OLD_OBSIDIAN_CHEST)
            .add(ModBlocks.OLD_NETHERITE_CHEST);
        tagMaker.apply(BlockTags.NEEDS_IRON_TOOL)
            .add(ModBlocks.DIAMOND_BARREL)
            .add(ModBlocks.DIAMOND_CHEST)
            .add(ModBlocks.OLD_DIAMOND_CHEST);
        tagMaker.apply(BlockTags.NEEDS_STONE_TOOL)
            .add(ModBlocks.IRON_BARREL)
            .add(ModBlocks.GOLD_BARREL)
            .add(ModBlocks.IRON_CHEST)
            .add(ModBlocks.GOLD_CHEST)
            .add(ModBlocks.OLD_IRON_CHEST)
            .add(ModBlocks.OLD_GOLD_CHEST);
        tagMaker.apply(ModTags.Blocks.CHEST_CYCLE)
            .add(ModBlocks.WOOD_CHEST)
            .add(ModBlocks.PUMPKIN_CHEST)
            .add(ModBlocks.PRESENT)
            .add(ModBlocks.BAMBOO_CHEST);
        tagMaker.apply(ModTags.Blocks.MINI_CHEST_CYCLE)
            .add(ModBlocks.VANILLA_WOOD_MINI_CHEST)
            .add(ModBlocks.WOOD_MINI_CHEST)
            .add(ModBlocks.PUMPKIN_MINI_CHEST)
            .add(ModBlocks.RED_MINI_PRESENT)
            .add(ModBlocks.WHITE_MINI_PRESENT)
            .add(ModBlocks.CANDY_CANE_MINI_PRESENT)
            .add(ModBlocks.GREEN_MINI_PRESENT);
        tagMaker.apply(ModTags.Blocks.MINI_CHEST_SECRET_CYCLE)
            .add(ModBlocks.VANILLA_WOOD_MINI_CHEST)
            .add(ModBlocks.WOOD_MINI_CHEST)
            .add(ModBlocks.PUMPKIN_MINI_CHEST)
            .add(ModBlocks.RED_MINI_PRESENT)
            .add(ModBlocks.WHITE_MINI_PRESENT)
            .add(ModBlocks.CANDY_CANE_MINI_PRESENT)
            .add(ModBlocks.GREEN_MINI_PRESENT)
            .add(ModBlocks.LAVENDER_MINI_PRESENT)
            .add(ModBlocks.PINK_AMETHYST_MINI_PRESENT);
        tagMaker.apply(ModTags.Blocks.MINI_CHEST_SECRET_CYCLE_2)
            .add(ModBlocks.VANILLA_WOOD_MINI_CHEST)
            .add(ModBlocks.WOOD_MINI_CHEST)
            .add(ModBlocks.PUMPKIN_MINI_CHEST)
            .add(ModBlocks.RED_MINI_PRESENT)
            .add(ModBlocks.WHITE_MINI_PRESENT)
            .add(ModBlocks.CANDY_CANE_MINI_PRESENT)
            .add(ModBlocks.GREEN_MINI_PRESENT)
            .add(ModBlocks.LAVENDER_MINI_PRESENT)
            .add(ModBlocks.PINK_AMETHYST_MINI_PRESENT)
            .add(ModBlocks.VANILLA_WOOD_MINI_CHEST_WITH_SPARROW)
            .add(ModBlocks.WOOD_MINI_CHEST_WITH_SPARROW)
            .add(ModBlocks.PUMPKIN_MINI_CHEST_WITH_SPARROW)
            .add(ModBlocks.RED_MINI_PRESENT_WITH_SPARROW)
            .add(ModBlocks.WHITE_MINI_PRESENT_WITH_SPARROW)
            .add(ModBlocks.CANDY_CANE_MINI_PRESENT_WITH_SPARROW)
            .add(ModBlocks.GREEN_MINI_PRESENT_WITH_SPARROW)
            .add(ModBlocks.LAVENDER_MINI_PRESENT_WITH_SPARROW)
            .add(ModBlocks.PINK_AMETHYST_MINI_PRESENT_WITH_SPARROW);
    }

    public static void registerItemTags(Function<TagKey<Item>, TagsProvider.TagAppender<Item>> tagMaker) {
        tagMaker.apply(ModTags.Items.ES_WOODEN_CHESTS)
                .add(ModItems.PUMPKIN_CHEST)
                .add(ModItems.PRESENT)
                .add(ModItems.BAMBOO_CHEST);
        tagMaker.apply(ItemTags.PIGLIN_LOVED)
            .add(ModItems.WOOD_TO_GOLD_CONVERSION_KIT)
            .add(ModItems.IRON_TO_GOLD_CONVERSION_KIT)
            .add(ModItems.GOLD_TO_DIAMOND_CONVERSION_KIT)
            .add(ModItems.GOLD_TO_OBSIDIAN_CONVERSION_KIT)
            .add(ModItems.GOLD_TO_NETHERITE_CONVERSION_KIT)
            .add(ModItems.GOLD_BARREL)
            .add(ModItems.GOLD_CHEST)
//            .add(ModItems.GOLD_CHEST_MINECART)
            .add(ModItems.OLD_GOLD_CHEST);
    }
}
