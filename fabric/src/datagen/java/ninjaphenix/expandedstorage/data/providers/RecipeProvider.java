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
package ninjaphenix.expandedstorage.data.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipesProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;
import net.minecraft.data.server.recipe.SmithingRecipeJsonFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.registry.Registry;
import ninjaphenix.expandedstorage.Utils;
import ninjaphenix.expandedstorage.data.content.ModItems;
import ninjaphenix.expandedstorage.data.content.ModTags;

import java.util.function.Consumer;

public class RecipeProvider extends FabricRecipesProvider {
    public RecipeProvider(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        shapedRecipe(ModItems.STORAGE_MUTATOR, 1, "has_chest", ModTags.Items.WOODEN_CHESTS)
                .pattern("  C")
                .pattern(" S ")
                .pattern("S  ")
                .input('C', ModTags.Items.WOODEN_CHESTS)
                .input('S', Items.STICK)
                .offerTo(exporter);
        this.offerConversionKitRecipes(exporter);
        this.offerChestRecipes(exporter);
        this.offerOldChestRecipes(exporter);
        this.offerChestToOldChestRecipes(exporter);
        this.offerOldChestToChestRecipes(exporter);
        this.offerBarrelRecipes(exporter);
        this.offerMiniChestRecipes(exporter);
    }

    private void offerConversionKitRecipes(Consumer<RecipeJsonProvider> exporter) {
        shapedRecipe(ModItems.WOOD_TO_IRON_CONVERSION_KIT, 1, Criterions.HAS_ITEM, ItemTags.PLANKS)
                .pattern("III")
                .pattern("IPI")
                .pattern("III")
                .input('I', ModTags.Items.IRON_INGOTS)
                .input('P', ItemTags.PLANKS)
                .offerTo(exporter);
        shapedRecipe(ModItems.WOOD_TO_GOLD_CONVERSION_KIT, 1, Criterions.HAS_PREVIOUS_KIT, ModItems.WOOD_TO_IRON_CONVERSION_KIT)
                .pattern("GGG")
                .pattern("GKG")
                .pattern("GGG")
                .input('G', ModTags.Items.GOLD_INGOTS)
                .input('K', ModItems.WOOD_TO_IRON_CONVERSION_KIT)
                .offerTo(exporter);
        shapedRecipe(ModItems.WOOD_TO_DIAMOND_CONVERSION_KIT, 1, Criterions.HAS_PREVIOUS_KIT, ModItems.WOOD_TO_GOLD_CONVERSION_KIT)
                .pattern("GGG")
                .pattern("DKD")
                .pattern("GGG")
                .input('G', ModTags.Items.GLASS_BLOCKS)
                .input('D', ModTags.Items.DIAMONDS)
                .input('K', ModItems.WOOD_TO_GOLD_CONVERSION_KIT)
                .offerTo(exporter);
        shapedRecipe(ModItems.WOOD_TO_OBSIDIAN_CONVERSION_KIT, 1, Criterions.HAS_PREVIOUS_KIT, ModItems.WOOD_TO_DIAMOND_CONVERSION_KIT)
                .pattern("OOO")
                .pattern("OKO")
                .pattern("OOO")
                .input('O', Items.OBSIDIAN)
                .input('K', ModItems.WOOD_TO_DIAMOND_CONVERSION_KIT)
                .offerTo(exporter);
        smithingRecipe(ModItems.WOOD_TO_NETHERITE_CONVERSION_KIT, ModItems.WOOD_TO_OBSIDIAN_CONVERSION_KIT, ModTags.Items.NETHERITE_INGOTS, Criterions.HAS_PREVIOUS_KIT, exporter);
        shapedRecipe(ModItems.IRON_TO_GOLD_CONVERSION_KIT, 1, Criterions.HAS_ITEM, ModTags.Items.IRON_INGOTS)
                .pattern("GGG")
                .pattern("GIG")
                .pattern("GGG")
                .input('G', ModTags.Items.GOLD_INGOTS)
                .input('I', ModTags.Items.IRON_INGOTS)
                .offerTo(exporter);
        shapedRecipe(ModItems.IRON_TO_DIAMOND_CONVERSION_KIT, 1, Criterions.HAS_PREVIOUS_KIT, ModItems.IRON_TO_GOLD_CONVERSION_KIT)
                .pattern("GGG")
                .pattern("DKD")
                .pattern("GGG")
                .input('G', ModTags.Items.GLASS_BLOCKS)
                .input('D', ModTags.Items.DIAMONDS)
                .input('K', ModItems.IRON_TO_GOLD_CONVERSION_KIT)
                .offerTo(exporter);
        shapedRecipe(ModItems.IRON_TO_OBSIDIAN_CONVERSION_KIT, 1, Criterions.HAS_PREVIOUS_KIT, ModItems.IRON_TO_DIAMOND_CONVERSION_KIT)
                .pattern("OOO")
                .pattern("OKO")
                .pattern("OOO")
                .input('O', Items.OBSIDIAN)
                .input('K', ModItems.IRON_TO_DIAMOND_CONVERSION_KIT)
                .offerTo(exporter);
        smithingRecipe(ModItems.IRON_TO_NETHERITE_CONVERSION_KIT, ModItems.IRON_TO_OBSIDIAN_CONVERSION_KIT, ModTags.Items.NETHERITE_INGOTS, Criterions.HAS_PREVIOUS_KIT, exporter);
        shapedRecipe(ModItems.GOLD_TO_DIAMOND_CONVERSION_KIT, 1, Criterions.HAS_ITEM, ModTags.Items.GOLD_INGOTS)
                .pattern("GGG")
                .pattern("IDI")
                .pattern("GGG")
                .input('G', ModTags.Items.GLASS_BLOCKS)
                .input('I', ModTags.Items.GOLD_INGOTS)
                .input('D', ModTags.Items.DIAMONDS)
                .offerTo(exporter);
        shapedRecipe(ModItems.GOLD_TO_OBSIDIAN_CONVERSION_KIT, 1, Criterions.HAS_PREVIOUS_KIT, ModItems.GOLD_TO_DIAMOND_CONVERSION_KIT)
                .pattern("OOO")
                .pattern("OKO")
                .pattern("OOO")
                .input('O', Items.OBSIDIAN)
                .input('K', ModItems.GOLD_TO_DIAMOND_CONVERSION_KIT)
                .offerTo(exporter);
        smithingRecipe(ModItems.GOLD_TO_NETHERITE_CONVERSION_KIT, ModItems.GOLD_TO_OBSIDIAN_CONVERSION_KIT, ModTags.Items.NETHERITE_INGOTS, Criterions.HAS_PREVIOUS_KIT, exporter);
        shapedRecipe(ModItems.DIAMOND_TO_OBSIDIAN_CONVERSION_KIT, 1, Criterions.HAS_ITEM, ModTags.Items.DIAMONDS)
                .pattern("OOO")
                .pattern("ODO")
                .pattern("OOO")
                .input('O', Items.OBSIDIAN)
                .input('D', ModTags.Items.DIAMONDS)
                .offerTo(exporter);
        smithingRecipe(ModItems.DIAMOND_TO_NETHERITE_CONVERSION_KIT, ModItems.DIAMOND_TO_OBSIDIAN_CONVERSION_KIT, ModTags.Items.NETHERITE_INGOTS, Criterions.HAS_PREVIOUS_KIT, exporter);
        smithingRecipe(ModItems.OBSIDIAN_TO_NETHERITE_CONVERSION_KIT, Items.OBSIDIAN, ModTags.Items.NETHERITE_INGOTS, Criterions.HAS_ITEM, exporter);

    }

    private void offerChestRecipes(Consumer<RecipeJsonProvider> exporter) {
        ShapelessRecipeJsonFactory.create(ModItems.WOOD_CHEST)
                                  .input(Items.CHEST)
                                  .group(id(ModItems.WOOD_CHEST))
                                  .criterion(Criterions.HAS_PREVIOUS_BLOCK, RecipeProvider.conditionsFromItem(Items.CHEST))
                                  .offerTo(exporter);
        shapedRecipe(ModItems.PUMPKIN_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModTags.Items.WOODEN_CHESTS)
                .pattern("SSS")
                .pattern("SBS")
                .pattern("SSS")
                .input('S', Items.PUMPKIN_SEEDS)
                .input('B', ModTags.Items.WOODEN_CHESTS)
                .group(id(ModItems.PUMPKIN_CHEST))
                .offerTo(exporter);
        shapedRecipe(ModItems.PRESENT, 1, Criterions.HAS_PREVIOUS_BLOCK, ModTags.Items.WOODEN_CHESTS)
                .pattern(" B ")
                .pattern("RCW")
                .pattern(" S ")
                .input('B', Items.SWEET_BERRIES)
                .input('R', ModTags.Items.RED_DYES)
                .input('C', ModTags.Items.WOODEN_CHESTS)
                .input('W', ModTags.Items.WHITE_DYES)
                .input('S', Items.SPRUCE_SAPLING)
                .group(id(ModItems.PRESENT))
                .offerTo(exporter);
        shapedRecipe(ModItems.IRON_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModTags.Items.WOODEN_CHESTS)
                .pattern("III")
                .pattern("IBI")
                .pattern("III")
                .input('I', ModTags.Items.IRON_INGOTS)
                .input('B', ModTags.Items.WOODEN_CHESTS)
                .group(id(ModItems.IRON_CHEST))
                .offerTo(exporter);
        shapedRecipe(ModItems.GOLD_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.IRON_CHEST)
                .pattern("GGG")
                .pattern("GBG")
                .pattern("GGG")
                .input('G', ModTags.Items.GOLD_INGOTS)
                .input('B', ModItems.IRON_CHEST)
                .group(id(ModItems.GOLD_CHEST))
                .offerTo(exporter);
        shapedRecipe(ModItems.DIAMOND_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.GOLD_CHEST)
                .pattern("GGG")
                .pattern("DBD")
                .pattern("GGG")
                .input('G', ModTags.Items.GLASS_BLOCKS)
                .input('D', ModTags.Items.DIAMONDS)
                .input('B', ModItems.GOLD_CHEST)
                .group(id(ModItems.DIAMOND_CHEST))
                .offerTo(exporter);
        shapedRecipe(ModItems.OBSIDIAN_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.DIAMOND_CHEST)
                .pattern("OOO")
                .pattern("OBO")
                .pattern("OOO")
                .input('O', Items.OBSIDIAN)
                .input('B', ModItems.DIAMOND_CHEST)
                .group(id(ModItems.OBSIDIAN_CHEST))
                .offerTo(exporter);
        smithingRecipe(ModItems.NETHERITE_CHEST, ModItems.OBSIDIAN_CHEST, ModTags.Items.NETHERITE_INGOTS, Criterions.HAS_PREVIOUS_BLOCK, exporter);
    }

    private void offerOldChestRecipes(Consumer<RecipeJsonProvider> exporter) {
        shapedRecipe(ModItems.OLD_IRON_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.OLD_WOOD_CHEST)
                .pattern("III")
                .pattern("IBI")
                .pattern("III")
                .input('I', ModTags.Items.IRON_INGOTS)
                .input('B', ModItems.OLD_WOOD_CHEST)
                .group(id(ModItems.OLD_IRON_CHEST))
                .offerTo(exporter);
        shapedRecipe(ModItems.OLD_GOLD_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.OLD_IRON_CHEST)
                .pattern("GGG")
                .pattern("GBG")
                .pattern("GGG")
                .input('G', ModTags.Items.GOLD_INGOTS)
                .input('B', ModItems.OLD_IRON_CHEST)
                .group(id(ModItems.OLD_GOLD_CHEST))
                .offerTo(exporter);
        shapedRecipe(ModItems.OLD_DIAMOND_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.OLD_GOLD_CHEST)
                .pattern("GGG")
                .pattern("DBD")
                .pattern("GGG")
                .input('G', ModTags.Items.GLASS_BLOCKS)
                .input('D', ModTags.Items.DIAMONDS)
                .input('B', ModItems.OLD_GOLD_CHEST)
                .group(id(ModItems.OLD_DIAMOND_CHEST))
                .offerTo(exporter);
        shapedRecipe(ModItems.OLD_OBSIDIAN_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.OLD_DIAMOND_CHEST)
                .pattern("OOO")
                .pattern("OBO")
                .pattern("OOO")
                .input('O', Items.OBSIDIAN)
                .input('B', ModItems.OLD_DIAMOND_CHEST)
                .group(id(ModItems.OLD_OBSIDIAN_CHEST))
                .offerTo(exporter);
        smithingRecipe(ModItems.OLD_NETHERITE_CHEST, ModItems.OLD_OBSIDIAN_CHEST, ModTags.Items.NETHERITE_INGOTS, Criterions.HAS_PREVIOUS_BLOCK, exporter);
    }

    private void offerChestToOldChestRecipes(Consumer<RecipeJsonProvider> exporter) {
        ShapelessRecipeJsonFactory.create(ModItems.OLD_WOOD_CHEST)
                                  .input(ModItems.WOOD_CHEST)
                                  .group(id(ModItems.OLD_WOOD_CHEST))
                                  .criterion(Criterions.HAS_ITEM, RecipeProvider.conditionsFromItem(ModItems.WOOD_CHEST))
                                  .offerTo(exporter, Utils.id("wood_to_old_wood_chest"));
        ShapelessRecipeJsonFactory.create(ModItems.OLD_IRON_CHEST)
                                  .input(ModItems.IRON_CHEST)
                                  .group(id(ModItems.OLD_IRON_CHEST))
                                  .criterion(Criterions.HAS_ITEM, RecipeProvider.conditionsFromItem(ModItems.IRON_CHEST))
                                  .offerTo(exporter, Utils.id("iron_to_old_iron_chest"));
        ShapelessRecipeJsonFactory.create(ModItems.OLD_GOLD_CHEST)
                                  .input(ModItems.GOLD_CHEST)
                                  .group(id(ModItems.OLD_GOLD_CHEST))
                                  .criterion(Criterions.HAS_ITEM, RecipeProvider.conditionsFromItem(ModItems.GOLD_CHEST))
                                  .offerTo(exporter, Utils.id("gold_to_old_gold_chest"));
        ShapelessRecipeJsonFactory.create(ModItems.OLD_DIAMOND_CHEST)
                                  .input(ModItems.DIAMOND_CHEST)
                                  .group(id(ModItems.OLD_DIAMOND_CHEST))
                                  .criterion(Criterions.HAS_ITEM, RecipeProvider.conditionsFromItem(ModItems.DIAMOND_CHEST))
                                  .offerTo(exporter, Utils.id("diamond_to_old_diamond_chest"));
        ShapelessRecipeJsonFactory.create(ModItems.OLD_OBSIDIAN_CHEST)
                                  .input(ModItems.OBSIDIAN_CHEST)
                                  .group(id(ModItems.OLD_OBSIDIAN_CHEST))
                                  .criterion(Criterions.HAS_ITEM, RecipeProvider.conditionsFromItem(ModItems.OBSIDIAN_CHEST))
                                  .offerTo(exporter, Utils.id("obsidian_to_old_obsidian_chest"));
        ShapelessRecipeJsonFactory.create(ModItems.OLD_NETHERITE_CHEST)
                                  .input(ModItems.NETHERITE_CHEST)
                                  .group(id(ModItems.OLD_NETHERITE_CHEST))
                                  .criterion(Criterions.HAS_ITEM, RecipeProvider.conditionsFromItem(ModItems.NETHERITE_CHEST))
                                  .offerTo(exporter, Utils.id("netherite_to_old_netherite_chest"));
    }

    private void offerOldChestToChestRecipes(Consumer<RecipeJsonProvider> exporter) {
        ShapelessRecipeJsonFactory.create(ModItems.WOOD_CHEST)
                                  .input(ModItems.OLD_WOOD_CHEST)
                                  .group(id(ModItems.WOOD_CHEST))
                                  .criterion(Criterions.HAS_ITEM, RecipeProvider.conditionsFromItem(ModItems.OLD_WOOD_CHEST))
                                  .offerTo(exporter, Utils.id("old_wood_to_wood_chest"));
        ShapelessRecipeJsonFactory.create(ModItems.IRON_CHEST)
                                  .input(ModItems.OLD_IRON_CHEST)
                                  .group(id(ModItems.IRON_CHEST))
                                  .criterion(Criterions.HAS_ITEM, RecipeProvider.conditionsFromItem(ModItems.OLD_IRON_CHEST))
                                  .offerTo(exporter, Utils.id("old_iron_to_iron_chest"));
        ShapelessRecipeJsonFactory.create(ModItems.GOLD_CHEST)
                                  .input(ModItems.OLD_GOLD_CHEST)
                                  .group(id(ModItems.GOLD_CHEST))
                                  .criterion(Criterions.HAS_ITEM, RecipeProvider.conditionsFromItem(ModItems.OLD_GOLD_CHEST))
                                  .offerTo(exporter, Utils.id("old_gold_to_gold_chest"));
        ShapelessRecipeJsonFactory.create(ModItems.DIAMOND_CHEST)
                                  .input(ModItems.OLD_DIAMOND_CHEST)
                                  .group(id(ModItems.DIAMOND_CHEST))
                                  .criterion(Criterions.HAS_ITEM, RecipeProvider.conditionsFromItem(ModItems.OLD_DIAMOND_CHEST))
                                  .offerTo(exporter, Utils.id("old_diamond_to_diamond_chest"));
        ShapelessRecipeJsonFactory.create(ModItems.OBSIDIAN_CHEST)
                                  .input(ModItems.OLD_OBSIDIAN_CHEST)
                                  .group(id(ModItems.OBSIDIAN_CHEST))
                                  .criterion(Criterions.HAS_ITEM, RecipeProvider.conditionsFromItem(ModItems.OLD_OBSIDIAN_CHEST))
                                  .offerTo(exporter, Utils.id("old_obsidian_to_obsidian_chest"));
        ShapelessRecipeJsonFactory.create(ModItems.NETHERITE_CHEST)
                                  .input(ModItems.OLD_NETHERITE_CHEST)
                                  .group(id(ModItems.NETHERITE_CHEST))
                                  .criterion(Criterions.HAS_ITEM, RecipeProvider.conditionsFromItem(ModItems.OLD_NETHERITE_CHEST))
                                  .offerTo(exporter, Utils.id("old_netherite_to_netherite_chest"));
    }

    private void offerBarrelRecipes(Consumer<RecipeJsonProvider> exporter) {
        shapedRecipe(ModItems.IRON_BARREL, 1, Criterions.HAS_PREVIOUS_BLOCK, ModTags.Items.WOODEN_BARRELS)
                .pattern("III")
                .pattern("IBI")
                .pattern("III")
                .input('I', ModTags.Items.IRON_INGOTS)
                .input('B', ModTags.Items.WOODEN_BARRELS)
                .offerTo(exporter);
        shapedRecipe(ModItems.GOLD_BARREL, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.IRON_BARREL)
                .pattern("GGG")
                .pattern("GBG")
                .pattern("GGG")
                .input('G', ModTags.Items.GOLD_INGOTS)
                .input('B', ModItems.IRON_BARREL)
                .offerTo(exporter);
        shapedRecipe(ModItems.DIAMOND_BARREL, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.GOLD_BARREL)
                .pattern("GGG")
                .pattern("DBD")
                .pattern("GGG")
                .input('G', ModTags.Items.GLASS_BLOCKS)
                .input('D', ModTags.Items.DIAMONDS)
                .input('B', ModItems.GOLD_BARREL)
                .offerTo(exporter);
        shapedRecipe(ModItems.OBSIDIAN_BARREL, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.DIAMOND_BARREL)
                .pattern("OOO")
                .pattern("OBO")
                .pattern("OOO")
                .input('O', Items.OBSIDIAN)
                .input('B', ModItems.DIAMOND_BARREL)
                .offerTo(exporter);
        smithingRecipe(ModItems.NETHERITE_BARREL, ModItems.OBSIDIAN_BARREL, ModTags.Items.NETHERITE_INGOTS, Criterions.HAS_PREVIOUS_BLOCK, exporter);
    }

    private void offerMiniChestRecipes(Consumer<RecipeJsonProvider> exporter) {
        shapedRecipe(ModItems.VANILLA_WOOD_MINI_CHEST, 4, Criterions.HAS_ITEM, Items.CHEST)
                .pattern(" P ")
                .pattern("PBP")
                .pattern(" P ")
                .input('P', Items.PAPER)
                .input('B', Items.CHEST)
                .offerTo(exporter);
        shapedRecipe(ModItems.WOOD_MINI_CHEST, 4, Criterions.HAS_ITEM, ModItems.WOOD_CHEST)
                .pattern(" P ")
                .pattern("PBP")
                .pattern(" P ")
                .input('P', Items.PAPER)
                .input('B', ModItems.WOOD_CHEST)
                .offerTo(exporter);
        shapedRecipe(ModItems.PUMPKIN_MINI_CHEST, 4, Criterions.HAS_ITEM, ModItems.PUMPKIN_CHEST)
                .pattern(" P ")
                .pattern("PBP")
                .pattern(" P ")
                .input('P', Items.PAPER)
                .input('B', ModItems.PUMPKIN_CHEST)
                .offerTo(exporter);
        shapedRecipe(ModItems.RED_MINI_PRESENT, 4, Criterions.HAS_ITEM, ModItems.PRESENT)
                .pattern(" P ")
                .pattern("PBP")
                .pattern(" P ")
                .input('P', Items.PAPER)
                .input('B', ModItems.PRESENT)
                .group(id(ModItems.RED_MINI_PRESENT))
                .offerTo(exporter);
        ShapelessRecipeJsonFactory.create(ModItems.WHITE_MINI_PRESENT)
                                  .input(ModItems.RED_MINI_PRESENT)
                                  .criterion(Criterions.HAS_PREVIOUS_BLOCK, RecipeProvider.conditionsFromItem(ModItems.RED_MINI_PRESENT))
                                  .offerTo(exporter);
        ShapelessRecipeJsonFactory.create(ModItems.CANDY_CANE_MINI_PRESENT)
                                  .input(ModItems.WHITE_MINI_PRESENT)
                                  .criterion(Criterions.HAS_PREVIOUS_BLOCK, RecipeProvider.conditionsFromItem(ModItems.WHITE_MINI_PRESENT))
                                  .offerTo(exporter);
        ShapelessRecipeJsonFactory.create(ModItems.GREEN_MINI_PRESENT)
                                  .input(ModItems.CANDY_CANE_MINI_PRESENT)
                                  .criterion(Criterions.HAS_PREVIOUS_BLOCK, RecipeProvider.conditionsFromItem(ModItems.CANDY_CANE_MINI_PRESENT))
                                  .offerTo(exporter);
        ShapelessRecipeJsonFactory.create(ModItems.RED_MINI_PRESENT)
                                  .input(ModItems.GREEN_MINI_PRESENT)
                                  .group(id(ModItems.RED_MINI_PRESENT))
                                  .criterion(Criterions.HAS_PREVIOUS_BLOCK, RecipeProvider.conditionsFromItem(ModItems.GREEN_MINI_PRESENT))
                                  .offerTo(exporter, Utils.MOD_ID + ":red_mini_present_cycle");
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static void smithingRecipe(Item output, Item base, Tag<Item> addition, String criterion, Consumer<RecipeJsonProvider> exporter) {
        SmithingRecipeJsonFactory.create(Ingredient.ofItems(base), Ingredient.fromTag(addition), output)
                                 .criterion(criterion, RecipeProvider.conditionsFromItem(base))
                                 .offerTo(exporter, Registry.ITEM.getId(output));

    }

    private static ShapedRecipeJsonFactory shapedRecipe(ItemConvertible output, int count, String criterion, Tag<Item> tag) {
        return ShapedRecipeJsonFactory.create(output, count)
                                      .criterion(criterion, RecipeProvider.conditionsFromTag(tag));
    }

    private static ShapedRecipeJsonFactory shapedRecipe(ItemConvertible output, int count, String criterion, Item item) {
        return ShapedRecipeJsonFactory.create(output, count)
                                      .criterion(criterion, RecipeProvider.conditionsFromItem(item));
    }

    private String id(ItemConvertible item) {
        return Registry.ITEM.getId(item.asItem()).toString();
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static class Criterions {
        public static final String HAS_ITEM = "has_item";
        private static final String HAS_PREVIOUS_KIT = "has_previous_kit";
        private static final String HAS_PREVIOUS_BLOCK = "has_previous_block";
    }
}
