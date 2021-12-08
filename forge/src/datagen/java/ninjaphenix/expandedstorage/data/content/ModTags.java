/*
 * Copyright 2021 NinjaPhenix
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
package ninjaphenix.expandedstorage.data.content;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import ninjaphenix.expandedstorage.Utils;

public final class ModTags {
    public static class Blocks {
        public static final Tag.Named<Block> CHEST_CYCLE = tag(Utils.id("chest_cycle"));
        public static final Tag.Named<Block> MINI_CHEST_CYCLE = tag(Utils.id("mini_chest_cycle"));
        public static final Tag.Named<Block> MINI_CHEST_SECRET_CYCLE = tag(Utils.id("mini_chest_secret_cycle"));
        public static final Tag.Named<Block> MINI_CHEST_SECRET_CYCLE_2 = tag(Utils.id("mini_chest_secret_cycle_2"));

        private static Tag.Named<Block> tag(ResourceLocation id) {
            return BlockTags.createOptional(id, null);
        }
    }
    public static class Items {
        public static final Tag.Named<Item> ES_WOODEN_CHESTS = tag(Utils.id("wooden_chests"));

        private static Tag.Named<Item> tag(ResourceLocation id) {
            return ItemTags.createOptional(id, null);
        }
    }
}
