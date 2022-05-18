/*
 * Copyright 2021 - 2022 Ellemes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
