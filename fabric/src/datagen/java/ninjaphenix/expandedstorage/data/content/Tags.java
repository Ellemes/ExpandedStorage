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

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import ninjaphenix.expandedstorage.Utils;

public final class Tags {
    public static class Items {
        public static final Tag.Identified<Item> WOODEN_CHESTS = tag(commonId("wooden_chests"));
        public static final Tag.Identified<Item> WOODEN_BARRELS = tag(commonId("wooden_barrels"));
        public static final Tag.Identified<Item> GLASS_BLOCKS = tag(commonId("glass_blocks"));
        public static final Tag.Identified<Item> DIAMONDS = tag(commonId("diamonds"));
        public static final Tag.Identified<Item> IRON_INGOTS = tag(commonId("iron_ingots"));
        public static final Tag.Identified<Item> GOLD_INGOTS = tag(commonId("gold_ingots"));
        public static final Tag.Identified<Item> NETHERITE_INGOTS = tag(commonId("netherite_ingots"));
        public static final Tag.Identified<Item> RED_DYES = tag(commonId("red_dyes"));
        public static final Tag.Identified<Item> WHITE_DYES = tag(commonId("white_dyes"));
        public static final Tag.Identified<Item> PIGLIN_LOVED = tag(mcId("piglin_loved"));

        private static Tag.Identified<Item> tag(Identifier id) {
            return TagFactory.ITEM.create(id);
        }
    }

    public static class Blocks {
        public static final Tag.Identified<Block> WOODEN_CHESTS = tag(commonId("wooden_chests"));
        public static final Tag.Identified<Block> WOODEN_BARRELS = tag(commonId("wooden_barrels"));
        public static final Tag.Identified<Block> MINEABLE_AXE = tag(mcId("mineable/axe"));
        public static final Tag.Identified<Block> MINEABLE_PICKAXE = tag(mcId("mineable/pickaxe"));
        public static final Tag.Identified<Block> GUARDED_BY_PIGLINS = tag(mcId("guarded_by_piglins"));
        public static final Tag.Identified<Block> NEEDS_DIAMOND_TOOL = tag(mcId("needs_diamond_tool"));
        public static final Tag.Identified<Block> NEEDS_IRON_TOOL = tag(mcId("needs_iron_tool"));
        public static final Tag.Identified<Block> NEEDS_STONE_TOOL = tag(mcId("needs_stone_tool"));
        public static final Tag.Identified<Block> CHEST_CYCLE = tag(Utils.id("chest_cycle"));
        public static final Tag.Identified<Block> MINI_CHEST_CYCLE = tag(Utils.id("mini_chest_cycle"));
        public static final Tag.Identified<Block> MINI_CHEST_SECRET_CYCLE = tag(Utils.id("mini_chest_secret_cycle"));
        public static final Tag.Identified<Block> MINI_CHEST_SECRET_CYCLE_2 = tag(Utils.id("mini_chest_secret_cycle_2"));

        private static Tag.Identified<Block> tag(Identifier id) {
            return TagFactory.BLOCK.create(id);
        }
    }

    private static Identifier mcId(String path) {
        return new Identifier(path);
    }

    private static Identifier commonId(String path) {
        return new Identifier("c", path);
    }
}
