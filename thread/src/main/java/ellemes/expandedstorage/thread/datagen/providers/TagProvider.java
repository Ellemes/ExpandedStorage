package ellemes.expandedstorage.thread.datagen.providers;

import ellemes.expandedstorage.thread.datagen.content.ModBlocks;
import ellemes.expandedstorage.thread.datagen.content.ModItems;
import ellemes.expandedstorage.thread.datagen.content.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public final class TagProvider {
    public static final class Block extends FabricTagProvider.BlockTagProvider {
        public Block(FabricDataGenerator generator) {
            super(generator);
        }

        @Override
        protected void generateTags() {
            this.getOrCreateTagBuilder(ModTags.Blocks.WOODEN_CHESTS)
                .add(Blocks.CHEST)
                .add(Blocks.TRAPPED_CHEST)
                .add(ModBlocks.WOOD_CHEST);
            this.getOrCreateTagBuilder(ModTags.Blocks.WOODEN_BARRELS)
                .add(Blocks.BARREL);
            this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.IRON_BARREL)
                .add(ModBlocks.GOLD_BARREL)
                .add(ModBlocks.DIAMOND_BARREL)
                .add(ModBlocks.OBSIDIAN_BARREL)
                .add(ModBlocks.NETHERITE_BARREL)
                .add(ModBlocks.WOOD_CHEST)
                .add(ModBlocks.PUMPKIN_CHEST)
                .add(ModBlocks.PRESENT)
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
            this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
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
            this.getOrCreateTagBuilder(BlockTags.GUARDED_BY_PIGLINS)
                .add(ModBlocks.IRON_BARREL)
                .add(ModBlocks.GOLD_BARREL)
                .add(ModBlocks.DIAMOND_BARREL)
                .add(ModBlocks.OBSIDIAN_BARREL)
                .add(ModBlocks.NETHERITE_BARREL)
                .add(ModBlocks.WOOD_CHEST)
                .add(ModBlocks.PUMPKIN_CHEST)
                .add(ModBlocks.PRESENT)
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
            this.getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.OBSIDIAN_BARREL)
                .add(ModBlocks.NETHERITE_BARREL)
                .add(ModBlocks.OBSIDIAN_CHEST)
                .add(ModBlocks.NETHERITE_CHEST)
                .add(ModBlocks.OLD_OBSIDIAN_CHEST)
                .add(ModBlocks.OLD_NETHERITE_CHEST);
            this.getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.DIAMOND_BARREL)
                .add(ModBlocks.DIAMOND_CHEST)
                .add(ModBlocks.OLD_DIAMOND_CHEST);
            this.getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.IRON_BARREL)
                .add(ModBlocks.GOLD_BARREL)
                .add(ModBlocks.IRON_CHEST)
                .add(ModBlocks.GOLD_CHEST)
                .add(ModBlocks.OLD_IRON_CHEST)
                .add(ModBlocks.OLD_GOLD_CHEST);
            this.getOrCreateTagBuilder(ModTags.Blocks.CHEST_CYCLE)
                .add(ModBlocks.WOOD_CHEST)
                .add(ModBlocks.PUMPKIN_CHEST)
                .add(ModBlocks.PRESENT);
            this.getOrCreateTagBuilder(ModTags.Blocks.MINI_CHEST_CYCLE)
                .add(ModBlocks.VANILLA_WOOD_MINI_CHEST)
                .add(ModBlocks.WOOD_MINI_CHEST)
                .add(ModBlocks.PUMPKIN_MINI_CHEST)
                .add(ModBlocks.RED_MINI_PRESENT)
                .add(ModBlocks.WHITE_MINI_PRESENT)
                .add(ModBlocks.CANDY_CANE_MINI_PRESENT)
                .add(ModBlocks.GREEN_MINI_PRESENT);
            this.getOrCreateTagBuilder(ModTags.Blocks.MINI_CHEST_SECRET_CYCLE)
                .add(ModBlocks.VANILLA_WOOD_MINI_CHEST)
                .add(ModBlocks.WOOD_MINI_CHEST)
                .add(ModBlocks.PUMPKIN_MINI_CHEST)
                .add(ModBlocks.RED_MINI_PRESENT)
                .add(ModBlocks.WHITE_MINI_PRESENT)
                .add(ModBlocks.CANDY_CANE_MINI_PRESENT)
                .add(ModBlocks.GREEN_MINI_PRESENT)
                .add(ModBlocks.LAVENDER_MINI_PRESENT)
                .add(ModBlocks.PINK_AMETHYST_MINI_PRESENT);
            this.getOrCreateTagBuilder(ModTags.Blocks.MINI_CHEST_SECRET_CYCLE_2)
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

        @Override
        public String getName() {
            return "Expanded Storage - Block Tags";
        }
    }

    public static final class Item extends FabricTagProvider.ItemTagProvider {
        public Item(FabricDataGenerator generator) {
            super(generator);
        }

        @Override
        protected void generateTags() {
            this.getOrCreateTagBuilder(ModTags.Items.WOODEN_CHESTS)
                .add(Items.CHEST)
                .add(Items.TRAPPED_CHEST)
                .add(ModItems.WOOD_CHEST);
            this.getOrCreateTagBuilder(ModTags.Items.ES_WOODEN_CHESTS)
                .addTag(ModTags.Items.WOODEN_CHESTS)
                .add(ModItems.PUMPKIN_CHEST)
                .add(ModItems.PRESENT);
            this.getOrCreateTagBuilder(ModTags.Items.WOODEN_BARRELS)
                .add(Items.BARREL);
            this.getOrCreateTagBuilder(ModTags.Items.GLASS_BLOCKS)
                .add(Items.GLASS)
                .add(Items.TINTED_GLASS)
                .add(Items.WHITE_STAINED_GLASS)
                .add(Items.ORANGE_STAINED_GLASS)
                .add(Items.MAGENTA_STAINED_GLASS)
                .add(Items.LIGHT_BLUE_STAINED_GLASS)
                .add(Items.YELLOW_STAINED_GLASS)
                .add(Items.LIME_STAINED_GLASS)
                .add(Items.PINK_STAINED_GLASS)
                .add(Items.GRAY_STAINED_GLASS)
                .add(Items.LIGHT_GRAY_STAINED_GLASS)
                .add(Items.CYAN_STAINED_GLASS)
                .add(Items.PURPLE_STAINED_GLASS)
                .add(Items.BLUE_STAINED_GLASS)
                .add(Items.BROWN_STAINED_GLASS)
                .add(Items.GREEN_STAINED_GLASS)
                .add(Items.RED_STAINED_GLASS)
                .add(Items.BLACK_STAINED_GLASS);
            this.getOrCreateTagBuilder(ModTags.Items.DIAMONDS)
                .add(Items.DIAMOND);
            this.getOrCreateTagBuilder(ModTags.Items.IRON_INGOTS)
                .add(Items.IRON_INGOT);
            this.getOrCreateTagBuilder(ModTags.Items.GOLD_INGOTS)
                .add(Items.GOLD_INGOT);
            this.getOrCreateTagBuilder(ModTags.Items.NETHERITE_INGOTS)
                .add(Items.NETHERITE_INGOT);
            this.getOrCreateTagBuilder(ModTags.Items.RED_DYES)
                .add(Items.RED_DYE);
            this.getOrCreateTagBuilder(ModTags.Items.WHITE_DYES)
                .add(Items.WHITE_DYE);
            this.getOrCreateTagBuilder(ItemTags.PIGLIN_LOVED)
                .add(ModItems.WOOD_TO_GOLD_CONVERSION_KIT)
                .add(ModItems.IRON_TO_GOLD_CONVERSION_KIT)
                .add(ModItems.GOLD_TO_DIAMOND_CONVERSION_KIT)
                .add(ModItems.GOLD_TO_OBSIDIAN_CONVERSION_KIT)
                .add(ModItems.GOLD_TO_NETHERITE_CONVERSION_KIT)
                .add(ModItems.GOLD_BARREL)
                .add(ModItems.GOLD_CHEST)
                .add(ModItems.OLD_GOLD_CHEST);
        }

        @Override
        public String getName() {
            return "Expanded Storage - Item Tags";
        }
    }
}
