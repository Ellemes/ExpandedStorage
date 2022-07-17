package ellemes.expandedstorage.thread.datagen.providers;

import ellemes.expandedstorage.datagen.content.ModBlocks;
import ellemes.expandedstorage.datagen.content.ModItems;
import ellemes.expandedstorage.datagen.content.ModTags;
import ellemes.expandedstorage.datagen.providers.TagHelper;
import ellemes.expandedstorage.thread.datagen.content.ThreadTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public final class TagProvider {
    public static final class Block extends FabricTagProvider.BlockTagProvider {
        public Block(FabricDataGenerator generator) {
            super(generator);
        }

        @Override
        protected void generateTags() {
            TagHelper.registerBlockTags(this::getOrCreateTagBuilder);
            this.getOrCreateTagBuilder(ThreadTags.Blocks.WOODEN_CHESTS)
                .add(Blocks.CHEST)
                .add(Blocks.TRAPPED_CHEST)
                .add(ModBlocks.WOOD_CHEST);
            this.getOrCreateTagBuilder(ThreadTags.Blocks.WOODEN_BARRELS)
                .add(Blocks.BARREL);
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
            TagHelper.registerItemTags(this::getOrCreateTagBuilder);
            this.getOrCreateTagBuilder(ThreadTags.Items.WOODEN_CHESTS)
                .add(Items.CHEST)
                .add(Items.TRAPPED_CHEST)
                .add(ModItems.WOOD_CHEST);
            this.getOrCreateTagBuilder(ModTags.Items.ES_WOODEN_CHESTS)
                .addTag(ThreadTags.Items.WOODEN_CHESTS);
            this.getOrCreateTagBuilder(ThreadTags.Items.WOODEN_BARRELS)
                .add(Items.BARREL);
            this.getOrCreateTagBuilder(ThreadTags.Items.GLASS_BLOCKS)
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
            this.getOrCreateTagBuilder(ThreadTags.Items.DIAMONDS)
                .add(Items.DIAMOND);
            this.getOrCreateTagBuilder(ThreadTags.Items.IRON_INGOTS)
                .add(Items.IRON_INGOT);
            this.getOrCreateTagBuilder(ThreadTags.Items.GOLD_INGOTS)
                .add(Items.GOLD_INGOT);
            this.getOrCreateTagBuilder(ThreadTags.Items.NETHERITE_INGOTS)
                .add(Items.NETHERITE_INGOT);
            this.getOrCreateTagBuilder(ThreadTags.Items.RED_DYES)
                .add(Items.RED_DYE);
            this.getOrCreateTagBuilder(ThreadTags.Items.WHITE_DYES)
                .add(Items.WHITE_DYE);
            this.getOrCreateTagBuilder(ThreadTags.Items.OBSIDIAN)
                .add(Items.OBSIDIAN);
            this.getOrCreateTagBuilder(ThreadTags.Items.BAMBOO)
                .add(Items.BAMBOO);
        }

        @Override
        public String getName() {
            return "Expanded Storage - Item Tags";
        }
    }
}
