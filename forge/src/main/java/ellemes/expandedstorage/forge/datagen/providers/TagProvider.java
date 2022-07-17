package ellemes.expandedstorage.forge.datagen.providers;

import ellemes.expandedstorage.Utils;
import ellemes.expandedstorage.datagen.content.ModBlocks;
import ellemes.expandedstorage.datagen.content.ModItems;
import ellemes.expandedstorage.datagen.content.ModTags;
import ellemes.expandedstorage.datagen.providers.TagHelper;
import ellemes.expandedstorage.forge.datagen.content.ForgeTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public final class TagProvider {
    public static final class Block extends BlockTagsProvider {
        public Block(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, Utils.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            TagHelper.registerBlockTags(this::tag);
            this.tag(Tags.Blocks.CHESTS_WOODEN).add(ModBlocks.WOOD_CHEST);
        }

        @Override
        public String getName() {
            return "Expanded Storage - Block Tags";
        }
    }

    public static final class Item extends ItemTagsProvider {
        public Item(DataGenerator generator, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
            super(generator, blockTagsProvider, Utils.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            TagHelper.registerItemTags(this::tag);
            this.tag(Tags.Items.CHESTS_WOODEN).add(ModItems.WOOD_CHEST);
            this.tag(ModTags.Items.ES_WOODEN_CHESTS)
                .addTag(Tags.Items.CHESTS_WOODEN);
            this.tag(ForgeTags.Items.BAMBOO)
                .add(Items.BAMBOO);
        }

        @Override
        public String getName() {
            return "Expanded Storage - Item Tags";
        }
    }
}
