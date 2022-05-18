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
package ellemes.expandedstorage.forge.datagen.providers;

import ellemes.expandedstorage.forge.datagen.content.ModBlocks;
import ellemes.expandedstorage.forge.datagen.content.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.CreativeModeTab;
import ellemes.expandedstorage.Utils;

import java.util.Arrays;
import java.util.Locale;

public final class LanguageProvider extends net.minecraftforge.common.data.LanguageProvider {
    public LanguageProvider(DataGenerator generator) {
        super(generator, Utils.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(ModBlocks.WOOD_CHEST, "Chest");
        this.add(ModBlocks.PUMPKIN_CHEST, "Pumpkin Chest");
        this.add(ModBlocks.PRESENT, "Present");
        this.add(ModBlocks.IRON_CHEST, "Iron Chest");
        this.add(ModBlocks.GOLD_CHEST, "Gold Chest");
        this.add(ModBlocks.DIAMOND_CHEST, "Diamond Chest");
        this.add(ModBlocks.OBSIDIAN_CHEST, "Obsidian Chest");
        this.add(ModBlocks.NETHERITE_CHEST, "Netherite Chest");

        this.add(ModBlocks.OLD_WOOD_CHEST, "Old Chest");
        this.add(ModBlocks.OLD_IRON_CHEST, "Old Iron Chest");
        this.add(ModBlocks.OLD_GOLD_CHEST, "Old Gold Chest");
        this.add(ModBlocks.OLD_DIAMOND_CHEST, "Old Diamond Chest");
        this.add(ModBlocks.OLD_OBSIDIAN_CHEST, "Old Obsidian Chest");
        this.add(ModBlocks.OLD_NETHERITE_CHEST, "Old Netherite Chest");

        this.add(ModBlocks.IRON_BARREL, "Iron Barrel");
        this.add(ModBlocks.GOLD_BARREL, "Gold Barrel");
        this.add(ModBlocks.DIAMOND_BARREL, "Diamond Barrel");
        this.add(ModBlocks.OBSIDIAN_BARREL, "Obsidian Barrel");
        this.add(ModBlocks.NETHERITE_BARREL, "Netherite Barrel");

        this.add(ModBlocks.VANILLA_WOOD_MINI_CHEST, "Mini Chest");
        this.add(ModBlocks.WOOD_MINI_CHEST, "Mini Chest");
        this.add(ModBlocks.PUMPKIN_MINI_CHEST, "Pumpkin Mini Chest");
        this.add(ModBlocks.RED_MINI_PRESENT, "Red Mini Present");
        this.add(ModBlocks.WHITE_MINI_PRESENT, "White Mini Present");
        this.add(ModBlocks.CANDY_CANE_MINI_PRESENT, "Candy cane Mini Present");
        this.add(ModBlocks.GREEN_MINI_PRESENT, "Green Mini Present");
        this.add(ModBlocks.LAVENDER_MINI_PRESENT, "Lavender Mini Present");
        this.add(ModBlocks.PINK_AMETHYST_MINI_PRESENT, "Pink Amethyst Mini Present");

        this.add(ModBlocks.VANILLA_WOOD_MINI_CHEST_WITH_SPARROW, "Mini Chest");
        this.add(ModBlocks.WOOD_MINI_CHEST_WITH_SPARROW, "Mini Chest");
        this.add(ModBlocks.PUMPKIN_MINI_CHEST_WITH_SPARROW, "Pumpkin Mini Chest");
        this.add(ModBlocks.RED_MINI_PRESENT_WITH_SPARROW, "Red mini Present");
        this.add(ModBlocks.WHITE_MINI_PRESENT_WITH_SPARROW, "White Mini Present");
        this.add(ModBlocks.CANDY_CANE_MINI_PRESENT_WITH_SPARROW, "Candy cane Mini Present");
        this.add(ModBlocks.GREEN_MINI_PRESENT_WITH_SPARROW, "Green Mini Present");
        this.add(ModBlocks.LAVENDER_MINI_PRESENT_WITH_SPARROW, "Lavender Mini Present");
        this.add(ModBlocks.PINK_AMETHYST_MINI_PRESENT_WITH_SPARROW, "Pink Amethyst Mini Present");

        this.add(ModItems.WOOD_TO_IRON_CONVERSION_KIT, "Wood to Iron upgrade");
        this.add(ModItems.WOOD_TO_GOLD_CONVERSION_KIT, "Wood to Gold upgrade");
        this.add(ModItems.WOOD_TO_DIAMOND_CONVERSION_KIT, "Wood to Diamond upgrade");
        this.add(ModItems.WOOD_TO_OBSIDIAN_CONVERSION_KIT, "Wood to Obsidian upgrade");
        this.add(ModItems.WOOD_TO_NETHERITE_CONVERSION_KIT, "Wood to Netherite upgrade");

        this.add(ModItems.IRON_TO_GOLD_CONVERSION_KIT, "Iron to Gold upgrade");
        this.add(ModItems.IRON_TO_DIAMOND_CONVERSION_KIT, "Iron to Diamond upgrade");
        this.add(ModItems.IRON_TO_OBSIDIAN_CONVERSION_KIT, "Iron to Obsidian upgrade");
        this.add(ModItems.IRON_TO_NETHERITE_CONVERSION_KIT, "Iron to Netherite upgrade");

        this.add(ModItems.GOLD_TO_DIAMOND_CONVERSION_KIT, "Gold to Diamond upgrade");
        this.add(ModItems.GOLD_TO_OBSIDIAN_CONVERSION_KIT, "Gold to Obsidian upgrade");
        this.add(ModItems.GOLD_TO_NETHERITE_CONVERSION_KIT, "Gold to Netherite upgrade");

        this.add(ModItems.DIAMOND_TO_OBSIDIAN_CONVERSION_KIT, "Diamond to Obsidian upgrade");
        this.add(ModItems.DIAMOND_TO_NETHERITE_CONVERSION_KIT, "Diamond to Netherite upgrade");

        this.add(ModItems.OBSIDIAN_TO_NETHERITE_CONVERSION_KIT, "Obsidian to Netherite upgrade");

        this.add(ModItems.STORAGE_MUTATOR, "Storage Mutator");

        this.add("container.expandedstorage.generic_double", "Large %s");

        this.add("tooltip.expandedstorage.alt_use", "%1$s + %2$s");

        this.add("tooltip.expandedstorage.storage_mutator.tool_mode", "Tool Mode: %s");
        this.add("tooltip.expandedstorage.storage_mutator.merge", "Merge");
        this.add("tooltip.expandedstorage.storage_mutator.description_merge", "%s on two adjacent chests to merge them.");
        this.add("tooltip.expandedstorage.storage_mutator.merge_already_double_chest", "Merging failed, original block is now a double chest.");
        this.add("tooltip.expandedstorage.storage_mutator.merge_wrong_facing", "Merging failed, chests are facing different directions.");
        this.add("tooltip.expandedstorage.storage_mutator.merge_wrong_block", "Merging failed, chests are different types.");
        this.add("tooltip.expandedstorage.storage_mutator.merge_not_adjacent", "Merging failed, chests are not adjacent.");
        this.add("tooltip.expandedstorage.storage_mutator.merge_end", "Merging finished.");
        this.add("tooltip.expandedstorage.storage_mutator.merge_start", "Merging started, now %s the other chest.");
        this.add("tooltip.expandedstorage.storage_mutator.rotate", "Rotate");
        this.add("tooltip.expandedstorage.storage_mutator.description_rotate", "%s on a storage block to rotate it.");
        this.add("tooltip.expandedstorage.storage_mutator.split", "Split");
        this.add("tooltip.expandedstorage.storage_mutator.description_split", "%s on a chest to split it into two single chests.");
        this.add("tooltip.expandedstorage.storage_mutator.swap_theme", "Swap theme");
        this.add("tooltip.expandedstorage.storage_mutator.description_swap_theme", "%s on a block to change its style.");

        this.add("tooltip.expandedstorage.conversion_kit_wood_iron_1", "%s on a Wooden Storage Block");
        this.add("tooltip.expandedstorage.conversion_kit_wood_iron_2", " to convert it to an Iron Storage Block.");
        this.add("tooltip.expandedstorage.conversion_kit_wood_gold_1", "%s on a Wooden Storage Block");
        this.add("tooltip.expandedstorage.conversion_kit_wood_gold_2", " to convert it to a Gold Storage Block.");
        this.add("tooltip.expandedstorage.conversion_kit_wood_diamond_1", "%s on a Wooden Storage Block");
        this.add("tooltip.expandedstorage.conversion_kit_wood_diamond_2", " to convert it to a Diamond Storage Block.");
        this.add("tooltip.expandedstorage.conversion_kit_wood_obsidian_1", "%s on a Wooden Storage Block");
        this.add("tooltip.expandedstorage.conversion_kit_wood_obsidian_2", " to convert it to a Obsidian Storage Block.");
        this.add("tooltip.expandedstorage.conversion_kit_wood_netherite_1", "%s on a Wooden Storage Block");
        this.add("tooltip.expandedstorage.conversion_kit_wood_netherite_2", " to convert it to a Netherite Storage Block.");
        this.add("tooltip.expandedstorage.conversion_kit_iron_gold_1", "%s on an Iron Storage Block");
        this.add("tooltip.expandedstorage.conversion_kit_iron_gold_2", " to convert it to a Gold Storage Block.");
        this.add("tooltip.expandedstorage.conversion_kit_iron_diamond_1", "%s on an Iron Storage Block");
        this.add("tooltip.expandedstorage.conversion_kit_iron_diamond_2", " to convert it to a Diamond Storage Block.");
        this.add("tooltip.expandedstorage.conversion_kit_iron_obsidian_1", "%s on an Iron Storage Block");
        this.add("tooltip.expandedstorage.conversion_kit_iron_obsidian_2", " to convert it to a Obsidian Storage Block.");
        this.add("tooltip.expandedstorage.conversion_kit_iron_netherite_1", "%s on an Iron Storage Block");
        this.add("tooltip.expandedstorage.conversion_kit_iron_netherite_2", " to convert it to a Netherite Storage Block.");
        this.add("tooltip.expandedstorage.conversion_kit_gold_diamond_1", "%s on a Gold Storage Block");
        this.add("tooltip.expandedstorage.conversion_kit_gold_diamond_2", " to convert it to a Diamond Storage Block.");
        this.add("tooltip.expandedstorage.conversion_kit_gold_obsidian_1", "%s on a Gold Storage Block");
        this.add("tooltip.expandedstorage.conversion_kit_gold_obsidian_2", " to convert it to a Obsidian Storage Block.");
        this.add("tooltip.expandedstorage.conversion_kit_gold_netherite_1", "%s on a Gold Storage Block");
        this.add("tooltip.expandedstorage.conversion_kit_gold_netherite_2", " to convert it to a Netherite Storage Block.");
        this.add("tooltip.expandedstorage.conversion_kit_diamond_obsidian_1", "%s on a Diamond Storage Block");
        this.add("tooltip.expandedstorage.conversion_kit_diamond_obsidian_2", " to convert it to a Obsidian Storage Block.");
        this.add("tooltip.expandedstorage.conversion_kit_diamond_netherite_1", "%s on a Diamond Storage Block");
        this.add("tooltip.expandedstorage.conversion_kit_diamond_netherite_2", " to convert it to a Netherite Storage Block.");
        this.add("tooltip.expandedstorage.conversion_kit_obsidian_netherite_1", "%s on an Obsidian Storage Block");
        this.add("tooltip.expandedstorage.conversion_kit_obsidian_netherite_2", " to convert it to a Netherite Storage Block.");
        this.add("tooltip.expandedstorage.conversion_kit.need_x_upgrades", "Cannot upgrade %ss, you need %s more upgrade(s).");

        this.add("tooltip.expandedstorage.stores_x_stacks", "Stores %s stacks");

        String itemGroupId = Arrays.stream(CreativeModeTab.TABS)
                                   .map(it -> ((TranslatableComponent) it.getDisplayName()).getKey())
                                   .filter(it -> it.toLowerCase(Locale.ROOT).contains(Utils.MOD_ID))
                                   .findFirst()
                                   .orElseThrow();
        this.add(itemGroupId, "Expanded Storage");
    }

    @Override
    public String getName() {
        return "Expanded Storage - Language (en_us)";
    }
}
