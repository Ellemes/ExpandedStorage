package ellemes.expandedstorage.thread.datagen.content;

import ellemes.expandedstorage.Utils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class ThreadTags {
    private static ResourceLocation commonId(String path) {
        return new ResourceLocation("c", path);
    }

    public static class Items {
        public static final TagKey<Item> WOODEN_CHESTS = tag(commonId("wooden_chests"));
        public static final TagKey<Item> WOODEN_BARRELS = tag(commonId("wooden_barrels"));
        public static final TagKey<Item> GLASS_BLOCKS = tag(commonId("glass"));
        public static final TagKey<Item> DIAMONDS = tag(commonId("diamonds"));
        public static final TagKey<Item> IRON_INGOTS = tag(commonId("iron_ingots"));
        public static final TagKey<Item> GOLD_INGOTS = tag(commonId("gold_ingots"));
        public static final TagKey<Item> NETHERITE_INGOTS = tag(commonId("netherite_ingots"));
        public static final TagKey<Item> RED_DYES = tag(commonId("red_dyes"));
        public static final TagKey<Item> WHITE_DYES = tag(commonId("white_dyes"));

        private static TagKey<Item> tag(ResourceLocation id) {
            return TagKey.create(Registry.ITEM_REGISTRY, id);
        }
    }

    public static class Blocks {
        public static final TagKey<Block> WOODEN_CHESTS = tag(commonId("wooden_chests"));
        public static final TagKey<Block> WOODEN_BARRELS = tag(commonId("wooden_barrels"));

        private static TagKey<Block> tag(ResourceLocation id) {
            return TagKey.create(Registry.BLOCK_REGISTRY, id);
        }
    }
}
