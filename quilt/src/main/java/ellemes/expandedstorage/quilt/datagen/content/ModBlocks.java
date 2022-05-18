/*
 * Copyright 2021-2022 Ellemes
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
package ellemes.expandedstorage.quilt.datagen.content;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import ellemes.expandedstorage.Utils;
import ninjaphenix.expandedstorage.block.AbstractChestBlock;
import ellemes.expandedstorage.block.BarrelBlock;
import ellemes.expandedstorage.block.ChestBlock;
import ellemes.expandedstorage.block.MiniChestBlock;

public final class ModBlocks {
    public static final ChestBlock WOOD_CHEST = block(Utils.id("wood_chest"));
    public static final ChestBlock PUMPKIN_CHEST = block(Utils.id("pumpkin_chest"));
    public static final ChestBlock PRESENT = block(Utils.id("present"));
    public static final ChestBlock IRON_CHEST = block(Utils.id("iron_chest"));
    public static final ChestBlock GOLD_CHEST = block(Utils.id("gold_chest"));
    public static final ChestBlock DIAMOND_CHEST = block(Utils.id("diamond_chest"));
    public static final ChestBlock OBSIDIAN_CHEST = block(Utils.id("obsidian_chest"));
    public static final ChestBlock NETHERITE_CHEST = block(Utils.id("netherite_chest"));

    public static final AbstractChestBlock OLD_WOOD_CHEST = block(Utils.id("old_wood_chest"));
    public static final AbstractChestBlock OLD_IRON_CHEST = block(Utils.id("old_iron_chest"));
    public static final AbstractChestBlock OLD_GOLD_CHEST = block(Utils.id("old_gold_chest"));
    public static final AbstractChestBlock OLD_DIAMOND_CHEST = block(Utils.id("old_diamond_chest"));
    public static final AbstractChestBlock OLD_OBSIDIAN_CHEST = block(Utils.id("old_obsidian_chest"));
    public static final AbstractChestBlock OLD_NETHERITE_CHEST = block(Utils.id("old_netherite_chest"));

    public static final BarrelBlock IRON_BARREL = block(Utils.id("iron_barrel"));
    public static final BarrelBlock GOLD_BARREL = block(Utils.id("gold_barrel"));
    public static final BarrelBlock DIAMOND_BARREL = block(Utils.id("diamond_barrel"));
    public static final BarrelBlock OBSIDIAN_BARREL = block(Utils.id("obsidian_barrel"));
    public static final BarrelBlock NETHERITE_BARREL = block(Utils.id("netherite_barrel"));

    public static final MiniChestBlock VANILLA_WOOD_MINI_CHEST = block(Utils.id("vanilla_wood_mini_chest"));
    public static final MiniChestBlock WOOD_MINI_CHEST = block(Utils.id("wood_mini_chest"));
    public static final MiniChestBlock PUMPKIN_MINI_CHEST = block(Utils.id("pumpkin_mini_chest"));
    public static final MiniChestBlock RED_MINI_PRESENT = block(Utils.id("red_mini_present"));
    public static final MiniChestBlock WHITE_MINI_PRESENT = block(Utils.id("white_mini_present"));
    public static final MiniChestBlock CANDY_CANE_MINI_PRESENT = block(Utils.id("candy_cane_mini_present"));
    public static final MiniChestBlock GREEN_MINI_PRESENT = block(Utils.id("green_mini_present"));
    public static final MiniChestBlock LAVENDER_MINI_PRESENT = block(Utils.id("lavender_mini_present"));
    public static final MiniChestBlock PINK_AMETHYST_MINI_PRESENT = block(Utils.id("pink_amethyst_mini_present"));

    public static final MiniChestBlock VANILLA_WOOD_MINI_CHEST_WITH_SPARROW = block(Utils.id("vanilla_wood_mini_chest_with_sparrow"));
    public static final MiniChestBlock WOOD_MINI_CHEST_WITH_SPARROW = block(Utils.id("wood_mini_chest_with_sparrow"));
    public static final MiniChestBlock PUMPKIN_MINI_CHEST_WITH_SPARROW = block(Utils.id("pumpkin_mini_chest_with_sparrow"));
    public static final MiniChestBlock RED_MINI_PRESENT_WITH_SPARROW = block(Utils.id("red_mini_present_with_sparrow"));
    public static final MiniChestBlock WHITE_MINI_PRESENT_WITH_SPARROW = block(Utils.id("white_mini_present_with_sparrow"));
    public static final MiniChestBlock CANDY_CANE_MINI_PRESENT_WITH_SPARROW = block(Utils.id("candy_cane_mini_present_with_sparrow"));
    public static final MiniChestBlock GREEN_MINI_PRESENT_WITH_SPARROW = block(Utils.id("green_mini_present_with_sparrow"));
    public static final MiniChestBlock LAVENDER_MINI_PRESENT_WITH_SPARROW = block(Utils.id("lavender_mini_present_with_sparrow"));
    public static final MiniChestBlock PINK_AMETHYST_MINI_PRESENT_WITH_SPARROW = block(Utils.id("pink_amethyst_mini_present_with_sparrow"));

    private static <T extends Block> T block(ResourceLocation id) {
        //noinspection unchecked
        return (T) Registry.BLOCK.get(id);
    }
}
