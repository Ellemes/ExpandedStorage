package ellemes.expandedstorage.forge.datagen.content;

import ellemes.expandedstorage.Utils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class ModTags {
    public static class Blocks {
        public static final TagKey<Block> CHEST_CYCLE = tag(Utils.id("chest_cycle"));
        public static final TagKey<Block> MINI_CHEST_CYCLE = tag(Utils.id("mini_chest_cycle"));
        public static final TagKey<Block> MINI_CHEST_SECRET_CYCLE = tag(Utils.id("mini_chest_secret_cycle"));
        public static final TagKey<Block> MINI_CHEST_SECRET_CYCLE_2 = tag(Utils.id("mini_chest_secret_cycle_2"));

        private static TagKey<Block> tag(ResourceLocation id) {
            return TagKey.create(Registry.BLOCK_REGISTRY, id);
        }
    }

    public static class Items {
        public static final TagKey<Item> ES_WOODEN_CHESTS = tag(Utils.id("wooden_chests"));

        private static TagKey<Item> tag(ResourceLocation id) {
            return TagKey.create(Registry.ITEM_REGISTRY, id);
        }
    }
}
