package ellemes.expandedstorage.thread.datagen.providers;

import ellemes.expandedstorage.Utils;
import ellemes.expandedstorage.item.ChestMinecartItem;
import ellemes.expandedstorage.thread.datagen.content.ModItems;
import ellemes.expandedstorage.thread.datagen.content.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.UpgradeRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class RecipeProvider extends FabricRecipeProvider {
    public RecipeProvider(FabricDataGenerator generator) {
        super(generator);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static void smithingRecipe(Item output, Item base, TagKey<Item> addition, String criterion, Consumer<FinishedRecipe> exporter) {
        UpgradeRecipeBuilder.smithing(Ingredient.of(base), Ingredient.of(addition), output)
                            .unlocks(criterion, RecipeProvider.has(base))
                            .save(exporter, Registry.ITEM.getKey(output));
    }

    private static ShapedRecipeBuilder shapedRecipe(ItemLike output, int count, String criterion, TagKey<Item> tag) {
        return ShapedRecipeBuilder.shaped(output, count)
                                  .unlockedBy(criterion, RecipeProvider.has(tag));
    }

    private static ShapedRecipeBuilder shapedRecipe(ItemLike output, int count, String criterion, Item item) {
        return ShapedRecipeBuilder.shaped(output, count)
                                  .unlockedBy(criterion, RecipeProvider.has(item));
    }

    @Override
    protected void generateRecipes(Consumer<FinishedRecipe> exporter) {
        shapedRecipe(ModItems.STORAGE_MUTATOR, 1, "has_chest", ModTags.Items.ES_WOODEN_CHESTS)
                .pattern("  C")
                .pattern(" S ")
                .pattern("S  ")
                .define('C', ModTags.Items.ES_WOODEN_CHESTS)
                .define('S', Items.STICK)
                .save(exporter);
        this.offerConversionKitRecipes(exporter);
        this.offerChestRecipes(exporter);
        this.offerChestMinecartRecipes(exporter);
        this.offerOldChestRecipes(exporter);
        this.offerChestToOldChestRecipes(exporter);
        this.offerOldChestToChestRecipes(exporter);
        this.offerBarrelRecipes(exporter);
        this.offerMiniChestRecipes(exporter);
    }

    private void offerConversionKitRecipes(Consumer<FinishedRecipe> exporter) {
        shapedRecipe(ModItems.WOOD_TO_IRON_CONVERSION_KIT, 1, Criterions.HAS_ITEM, ItemTags.PLANKS)
                .pattern("III")
                .pattern("IPI")
                .pattern("III")
                .define('I', ModTags.Items.IRON_INGOTS)
                .define('P', ItemTags.PLANKS)
                .save(exporter);
        shapedRecipe(ModItems.WOOD_TO_GOLD_CONVERSION_KIT, 1, Criterions.HAS_PREVIOUS_KIT, ModItems.WOOD_TO_IRON_CONVERSION_KIT)
                .pattern("GGG")
                .pattern("GKG")
                .pattern("GGG")
                .define('G', ModTags.Items.GOLD_INGOTS)
                .define('K', ModItems.WOOD_TO_IRON_CONVERSION_KIT)
                .save(exporter);
        shapedRecipe(ModItems.WOOD_TO_DIAMOND_CONVERSION_KIT, 1, Criterions.HAS_PREVIOUS_KIT, ModItems.WOOD_TO_GOLD_CONVERSION_KIT)
                .pattern("GGG")
                .pattern("DKD")
                .pattern("GGG")
                .define('G', ModTags.Items.GLASS_BLOCKS)
                .define('D', ModTags.Items.DIAMONDS)
                .define('K', ModItems.WOOD_TO_GOLD_CONVERSION_KIT)
                .save(exporter);
        shapedRecipe(ModItems.WOOD_TO_OBSIDIAN_CONVERSION_KIT, 1, Criterions.HAS_PREVIOUS_KIT, ModItems.WOOD_TO_DIAMOND_CONVERSION_KIT)
                .pattern("OOO")
                .pattern("OKO")
                .pattern("OOO")
                .define('O', Items.OBSIDIAN)
                .define('K', ModItems.WOOD_TO_DIAMOND_CONVERSION_KIT)
                .save(exporter);
        smithingRecipe(ModItems.WOOD_TO_NETHERITE_CONVERSION_KIT, ModItems.WOOD_TO_OBSIDIAN_CONVERSION_KIT, ModTags.Items.NETHERITE_INGOTS, Criterions.HAS_PREVIOUS_KIT, exporter);
        shapedRecipe(ModItems.IRON_TO_GOLD_CONVERSION_KIT, 1, Criterions.HAS_ITEM, ModTags.Items.IRON_INGOTS)
                .pattern("GGG")
                .pattern("GIG")
                .pattern("GGG")
                .define('G', ModTags.Items.GOLD_INGOTS)
                .define('I', ModTags.Items.IRON_INGOTS)
                .save(exporter);
        shapedRecipe(ModItems.IRON_TO_DIAMOND_CONVERSION_KIT, 1, Criterions.HAS_PREVIOUS_KIT, ModItems.IRON_TO_GOLD_CONVERSION_KIT)
                .pattern("GGG")
                .pattern("DKD")
                .pattern("GGG")
                .define('G', ModTags.Items.GLASS_BLOCKS)
                .define('D', ModTags.Items.DIAMONDS)
                .define('K', ModItems.IRON_TO_GOLD_CONVERSION_KIT)
                .save(exporter);
        shapedRecipe(ModItems.IRON_TO_OBSIDIAN_CONVERSION_KIT, 1, Criterions.HAS_PREVIOUS_KIT, ModItems.IRON_TO_DIAMOND_CONVERSION_KIT)
                .pattern("OOO")
                .pattern("OKO")
                .pattern("OOO")
                .define('O', Items.OBSIDIAN)
                .define('K', ModItems.IRON_TO_DIAMOND_CONVERSION_KIT)
                .save(exporter);
        smithingRecipe(ModItems.IRON_TO_NETHERITE_CONVERSION_KIT, ModItems.IRON_TO_OBSIDIAN_CONVERSION_KIT, ModTags.Items.NETHERITE_INGOTS, Criterions.HAS_PREVIOUS_KIT, exporter);
        shapedRecipe(ModItems.GOLD_TO_DIAMOND_CONVERSION_KIT, 1, Criterions.HAS_ITEM, ModTags.Items.GOLD_INGOTS)
                .pattern("GGG")
                .pattern("DID")
                .pattern("GGG")
                .define('G', ModTags.Items.GLASS_BLOCKS)
                .define('D', ModTags.Items.DIAMONDS)
                .define('I', ModTags.Items.GOLD_INGOTS)
                .save(exporter);
        shapedRecipe(ModItems.GOLD_TO_OBSIDIAN_CONVERSION_KIT, 1, Criterions.HAS_PREVIOUS_KIT, ModItems.GOLD_TO_DIAMOND_CONVERSION_KIT)
                .pattern("OOO")
                .pattern("OKO")
                .pattern("OOO")
                .define('O', Items.OBSIDIAN)
                .define('K', ModItems.GOLD_TO_DIAMOND_CONVERSION_KIT)
                .save(exporter);
        smithingRecipe(ModItems.GOLD_TO_NETHERITE_CONVERSION_KIT, ModItems.GOLD_TO_OBSIDIAN_CONVERSION_KIT, ModTags.Items.NETHERITE_INGOTS, Criterions.HAS_PREVIOUS_KIT, exporter);
        shapedRecipe(ModItems.DIAMOND_TO_OBSIDIAN_CONVERSION_KIT, 1, Criterions.HAS_ITEM, ModTags.Items.DIAMONDS)
                .pattern("OOO")
                .pattern("ODO")
                .pattern("OOO")
                .define('O', Items.OBSIDIAN)
                .define('D', ModTags.Items.DIAMONDS)
                .save(exporter);
        smithingRecipe(ModItems.DIAMOND_TO_NETHERITE_CONVERSION_KIT, ModItems.DIAMOND_TO_OBSIDIAN_CONVERSION_KIT, ModTags.Items.NETHERITE_INGOTS, Criterions.HAS_PREVIOUS_KIT, exporter);
        smithingRecipe(ModItems.OBSIDIAN_TO_NETHERITE_CONVERSION_KIT, Items.OBSIDIAN, ModTags.Items.NETHERITE_INGOTS, Criterions.HAS_ITEM, exporter);

    }

    private void offerChestRecipes(Consumer<FinishedRecipe> exporter) {
        ShapelessRecipeBuilder.shapeless(ModItems.WOOD_CHEST)
                              .requires(Items.CHEST)
                              .group(id(ModItems.WOOD_CHEST))
                              .unlockedBy(Criterions.HAS_PREVIOUS_BLOCK, RecipeProvider.has(Items.CHEST))
                              .save(exporter);
        shapedRecipe(ModItems.PUMPKIN_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModTags.Items.WOODEN_CHESTS)
                .pattern("SSS")
                .pattern("SBS")
                .pattern("SSS")
                .define('S', Items.PUMPKIN_SEEDS)
                .define('B', ModTags.Items.WOODEN_CHESTS)
                .group(id(ModItems.PUMPKIN_CHEST))
                .save(exporter);
        shapedRecipe(ModItems.PRESENT, 1, Criterions.HAS_PREVIOUS_BLOCK, ModTags.Items.WOODEN_CHESTS)
                .pattern(" B ")
                .pattern("RCW")
                .pattern(" S ")
                .define('B', Items.SWEET_BERRIES)
                .define('R', ModTags.Items.RED_DYES)
                .define('C', ModTags.Items.WOODEN_CHESTS)
                .define('W', ModTags.Items.WHITE_DYES)
                .define('S', Items.SPRUCE_SAPLING)
                .group(id(ModItems.PRESENT))
                .save(exporter);
        shapedRecipe(ModItems.BAMBOO_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModTags.Items.WOODEN_CHESTS)
                .pattern("BBB")
                .pattern("BCB")
                .pattern("BBB")
                .define('B', Items.BAMBOO)
                .define('C', ModTags.Items.WOODEN_CHESTS)
                .group(id(ModItems.BAMBOO_CHEST))
                .save(exporter);
        shapedRecipe(ModItems.IRON_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModTags.Items.ES_WOODEN_CHESTS)
                .pattern("III")
                .pattern("IBI")
                .pattern("III")
                .define('I', ModTags.Items.IRON_INGOTS)
                .define('B', ModTags.Items.ES_WOODEN_CHESTS)
                .group(id(ModItems.IRON_CHEST))
                .save(exporter);
        shapedRecipe(ModItems.GOLD_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.IRON_CHEST)
                .pattern("GGG")
                .pattern("GBG")
                .pattern("GGG")
                .define('G', ModTags.Items.GOLD_INGOTS)
                .define('B', ModItems.IRON_CHEST)
                .group(id(ModItems.GOLD_CHEST))
                .save(exporter);
        shapedRecipe(ModItems.DIAMOND_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.GOLD_CHEST)
                .pattern("GGG")
                .pattern("DBD")
                .pattern("GGG")
                .define('G', ModTags.Items.GLASS_BLOCKS)
                .define('D', ModTags.Items.DIAMONDS)
                .define('B', ModItems.GOLD_CHEST)
                .group(id(ModItems.DIAMOND_CHEST))
                .save(exporter);
        shapedRecipe(ModItems.OBSIDIAN_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.DIAMOND_CHEST)
                .pattern("OOO")
                .pattern("OBO")
                .pattern("OOO")
                .define('O', Items.OBSIDIAN)
                .define('B', ModItems.DIAMOND_CHEST)
                .group(id(ModItems.OBSIDIAN_CHEST))
                .save(exporter);
        smithingRecipe(ModItems.NETHERITE_CHEST, ModItems.OBSIDIAN_CHEST, ModTags.Items.NETHERITE_INGOTS, Criterions.HAS_PREVIOUS_BLOCK, exporter);
    }

    private void offerChestMinecartRecipes(Consumer<FinishedRecipe> exporter) {
        cartRecipe(ModItems.WOOD_CHEST, ModItems.WOOD_CHEST_MINECART, exporter);
        cartRecipe(ModItems.PUMPKIN_CHEST, ModItems.PUMPKIN_CHEST_MINECART, exporter);
        cartRecipe(ModItems.PRESENT, ModItems.PRESENT_MINECART, exporter);
        cartRecipe(ModItems.BAMBOO_CHEST, ModItems.BAMBOO_CHEST_MINECART, exporter);
        cartRecipe(ModItems.IRON_CHEST, ModItems.IRON_CHEST_MINECART, exporter);
        cartRecipe(ModItems.GOLD_CHEST, ModItems.GOLD_CHEST_MINECART, exporter);
        cartRecipe(ModItems.DIAMOND_CHEST, ModItems.DIAMOND_CHEST_MINECART, exporter);
        cartRecipe(ModItems.OBSIDIAN_CHEST, ModItems.OBSIDIAN_CHEST_MINECART, exporter);
        cartRecipe(ModItems.NETHERITE_CHEST, ModItems.NETHERITE_CHEST_MINECART, exporter);
    }

    private void cartRecipe(BlockItem chest, ChestMinecartItem cart, Consumer<FinishedRecipe> exporter) {
        shapedRecipe(cart, 1, "has_chest", chest)
                .pattern("C")
                .pattern("M")
                .define('C', chest)
                .define('M', Items.MINECART)
                .save(exporter);
    }

    private void offerOldChestRecipes(Consumer<FinishedRecipe> exporter) {
        shapedRecipe(ModItems.OLD_IRON_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.OLD_WOOD_CHEST)
                .pattern("III")
                .pattern("IBI")
                .pattern("III")
                .define('I', ModTags.Items.IRON_INGOTS)
                .define('B', ModItems.OLD_WOOD_CHEST)
                .group(id(ModItems.OLD_IRON_CHEST))
                .save(exporter);
        shapedRecipe(ModItems.OLD_GOLD_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.OLD_IRON_CHEST)
                .pattern("GGG")
                .pattern("GBG")
                .pattern("GGG")
                .define('G', ModTags.Items.GOLD_INGOTS)
                .define('B', ModItems.OLD_IRON_CHEST)
                .group(id(ModItems.OLD_GOLD_CHEST))
                .save(exporter);
        shapedRecipe(ModItems.OLD_DIAMOND_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.OLD_GOLD_CHEST)
                .pattern("GGG")
                .pattern("DBD")
                .pattern("GGG")
                .define('G', ModTags.Items.GLASS_BLOCKS)
                .define('D', ModTags.Items.DIAMONDS)
                .define('B', ModItems.OLD_GOLD_CHEST)
                .group(id(ModItems.OLD_DIAMOND_CHEST))
                .save(exporter);
        shapedRecipe(ModItems.OLD_OBSIDIAN_CHEST, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.OLD_DIAMOND_CHEST)
                .pattern("OOO")
                .pattern("OBO")
                .pattern("OOO")
                .define('O', Items.OBSIDIAN)
                .define('B', ModItems.OLD_DIAMOND_CHEST)
                .group(id(ModItems.OLD_OBSIDIAN_CHEST))
                .save(exporter);
        smithingRecipe(ModItems.OLD_NETHERITE_CHEST, ModItems.OLD_OBSIDIAN_CHEST, ModTags.Items.NETHERITE_INGOTS, Criterions.HAS_PREVIOUS_BLOCK, exporter);
    }

    private void offerChestToOldChestRecipes(Consumer<FinishedRecipe> exporter) {
        ShapelessRecipeBuilder.shapeless(ModItems.OLD_WOOD_CHEST)
                              .requires(ModItems.WOOD_CHEST)
                              .group(id(ModItems.OLD_WOOD_CHEST))
                              .unlockedBy(Criterions.HAS_ITEM, RecipeProvider.has(ModItems.WOOD_CHEST))
                              .save(exporter, Utils.id("wood_to_old_wood_chest"));
        ShapelessRecipeBuilder.shapeless(ModItems.OLD_IRON_CHEST)
                              .requires(ModItems.IRON_CHEST)
                              .group(id(ModItems.OLD_IRON_CHEST))
                              .unlockedBy(Criterions.HAS_ITEM, RecipeProvider.has(ModItems.IRON_CHEST))
                              .save(exporter, Utils.id("iron_to_old_iron_chest"));
        ShapelessRecipeBuilder.shapeless(ModItems.OLD_GOLD_CHEST)
                              .requires(ModItems.GOLD_CHEST)
                              .group(id(ModItems.OLD_GOLD_CHEST))
                              .unlockedBy(Criterions.HAS_ITEM, RecipeProvider.has(ModItems.GOLD_CHEST))
                              .save(exporter, Utils.id("gold_to_old_gold_chest"));
        ShapelessRecipeBuilder.shapeless(ModItems.OLD_DIAMOND_CHEST)
                              .requires(ModItems.DIAMOND_CHEST)
                              .group(id(ModItems.OLD_DIAMOND_CHEST))
                              .unlockedBy(Criterions.HAS_ITEM, RecipeProvider.has(ModItems.DIAMOND_CHEST))
                              .save(exporter, Utils.id("diamond_to_old_diamond_chest"));
        ShapelessRecipeBuilder.shapeless(ModItems.OLD_OBSIDIAN_CHEST)
                              .requires(ModItems.OBSIDIAN_CHEST)
                              .group(id(ModItems.OLD_OBSIDIAN_CHEST))
                              .unlockedBy(Criterions.HAS_ITEM, RecipeProvider.has(ModItems.OBSIDIAN_CHEST))
                              .save(exporter, Utils.id("obsidian_to_old_obsidian_chest"));
        ShapelessRecipeBuilder.shapeless(ModItems.OLD_NETHERITE_CHEST)
                              .requires(ModItems.NETHERITE_CHEST)
                              .group(id(ModItems.OLD_NETHERITE_CHEST))
                              .unlockedBy(Criterions.HAS_ITEM, RecipeProvider.has(ModItems.NETHERITE_CHEST))
                              .save(exporter, Utils.id("netherite_to_old_netherite_chest"));
    }

    private void offerOldChestToChestRecipes(Consumer<FinishedRecipe> exporter) {
        ShapelessRecipeBuilder.shapeless(ModItems.WOOD_CHEST)
                              .requires(ModItems.OLD_WOOD_CHEST)
                              .group(id(ModItems.WOOD_CHEST))
                              .unlockedBy(Criterions.HAS_ITEM, RecipeProvider.has(ModItems.OLD_WOOD_CHEST))
                              .save(exporter, Utils.id("old_wood_to_wood_chest"));
        ShapelessRecipeBuilder.shapeless(ModItems.IRON_CHEST)
                              .requires(ModItems.OLD_IRON_CHEST)
                              .group(id(ModItems.IRON_CHEST))
                              .unlockedBy(Criterions.HAS_ITEM, RecipeProvider.has(ModItems.OLD_IRON_CHEST))
                              .save(exporter, Utils.id("old_iron_to_iron_chest"));
        ShapelessRecipeBuilder.shapeless(ModItems.GOLD_CHEST)
                              .requires(ModItems.OLD_GOLD_CHEST)
                              .group(id(ModItems.GOLD_CHEST))
                              .unlockedBy(Criterions.HAS_ITEM, RecipeProvider.has(ModItems.OLD_GOLD_CHEST))
                              .save(exporter, Utils.id("old_gold_to_gold_chest"));
        ShapelessRecipeBuilder.shapeless(ModItems.DIAMOND_CHEST)
                              .requires(ModItems.OLD_DIAMOND_CHEST)
                              .group(id(ModItems.DIAMOND_CHEST))
                              .unlockedBy(Criterions.HAS_ITEM, RecipeProvider.has(ModItems.OLD_DIAMOND_CHEST))
                              .save(exporter, Utils.id("old_diamond_to_diamond_chest"));
        ShapelessRecipeBuilder.shapeless(ModItems.OBSIDIAN_CHEST)
                              .requires(ModItems.OLD_OBSIDIAN_CHEST)
                              .group(id(ModItems.OBSIDIAN_CHEST))
                              .unlockedBy(Criterions.HAS_ITEM, RecipeProvider.has(ModItems.OLD_OBSIDIAN_CHEST))
                              .save(exporter, Utils.id("old_obsidian_to_obsidian_chest"));
        ShapelessRecipeBuilder.shapeless(ModItems.NETHERITE_CHEST)
                              .requires(ModItems.OLD_NETHERITE_CHEST)
                              .group(id(ModItems.NETHERITE_CHEST))
                              .unlockedBy(Criterions.HAS_ITEM, RecipeProvider.has(ModItems.OLD_NETHERITE_CHEST))
                              .save(exporter, Utils.id("old_netherite_to_netherite_chest"));
    }

    private void offerBarrelRecipes(Consumer<FinishedRecipe> exporter) {
        shapedRecipe(ModItems.IRON_BARREL, 1, Criterions.HAS_PREVIOUS_BLOCK, ModTags.Items.WOODEN_BARRELS)
                .pattern("III")
                .pattern("IBI")
                .pattern("III")
                .define('I', ModTags.Items.IRON_INGOTS)
                .define('B', ModTags.Items.WOODEN_BARRELS)
                .save(exporter);
        shapedRecipe(ModItems.GOLD_BARREL, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.IRON_BARREL)
                .pattern("GGG")
                .pattern("GBG")
                .pattern("GGG")
                .define('G', ModTags.Items.GOLD_INGOTS)
                .define('B', ModItems.IRON_BARREL)
                .save(exporter);
        shapedRecipe(ModItems.DIAMOND_BARREL, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.GOLD_BARREL)
                .pattern("GGG")
                .pattern("DBD")
                .pattern("GGG")
                .define('G', ModTags.Items.GLASS_BLOCKS)
                .define('D', ModTags.Items.DIAMONDS)
                .define('B', ModItems.GOLD_BARREL)
                .save(exporter);
        shapedRecipe(ModItems.OBSIDIAN_BARREL, 1, Criterions.HAS_PREVIOUS_BLOCK, ModItems.DIAMOND_BARREL)
                .pattern("OOO")
                .pattern("OBO")
                .pattern("OOO")
                .define('O', Items.OBSIDIAN)
                .define('B', ModItems.DIAMOND_BARREL)
                .save(exporter);
        smithingRecipe(ModItems.NETHERITE_BARREL, ModItems.OBSIDIAN_BARREL, ModTags.Items.NETHERITE_INGOTS, Criterions.HAS_PREVIOUS_BLOCK, exporter);
    }

    private void offerMiniChestRecipes(Consumer<FinishedRecipe> exporter) {
        shapedRecipe(ModItems.VANILLA_WOOD_MINI_CHEST, 4, Criterions.HAS_ITEM, Items.CHEST)
                .pattern(" P ")
                .pattern("PBP")
                .pattern(" P ")
                .define('P', Items.PAPER)
                .define('B', Items.CHEST)
                .save(exporter);
        shapedRecipe(ModItems.WOOD_MINI_CHEST, 4, Criterions.HAS_ITEM, ModItems.WOOD_CHEST)
                .pattern(" P ")
                .pattern("PBP")
                .pattern(" P ")
                .define('P', Items.PAPER)
                .define('B', ModItems.WOOD_CHEST)
                .save(exporter);
        shapedRecipe(ModItems.PUMPKIN_MINI_CHEST, 4, Criterions.HAS_ITEM, ModItems.PUMPKIN_CHEST)
                .pattern(" P ")
                .pattern("PBP")
                .pattern(" P ")
                .define('P', Items.PAPER)
                .define('B', ModItems.PUMPKIN_CHEST)
                .save(exporter);
        shapedRecipe(ModItems.RED_MINI_PRESENT, 4, Criterions.HAS_ITEM, ModItems.PRESENT)
                .pattern(" P ")
                .pattern("PBP")
                .pattern(" P ")
                .define('P', Items.PAPER)
                .define('B', ModItems.PRESENT)
                .group(id(ModItems.RED_MINI_PRESENT))
                .save(exporter);
        ShapelessRecipeBuilder.shapeless(ModItems.WHITE_MINI_PRESENT)
                              .requires(ModItems.RED_MINI_PRESENT)
                              .unlockedBy(Criterions.HAS_PREVIOUS_BLOCK, RecipeProvider.has(ModItems.RED_MINI_PRESENT))
                              .save(exporter);
        ShapelessRecipeBuilder.shapeless(ModItems.CANDY_CANE_MINI_PRESENT)
                              .requires(ModItems.WHITE_MINI_PRESENT)
                              .unlockedBy(Criterions.HAS_PREVIOUS_BLOCK, RecipeProvider.has(ModItems.WHITE_MINI_PRESENT))
                              .save(exporter);
        ShapelessRecipeBuilder.shapeless(ModItems.GREEN_MINI_PRESENT)
                              .requires(ModItems.CANDY_CANE_MINI_PRESENT)
                              .unlockedBy(Criterions.HAS_PREVIOUS_BLOCK, RecipeProvider.has(ModItems.CANDY_CANE_MINI_PRESENT))
                              .save(exporter);
        ShapelessRecipeBuilder.shapeless(ModItems.RED_MINI_PRESENT)
                              .requires(ModItems.GREEN_MINI_PRESENT)
                              .group(id(ModItems.RED_MINI_PRESENT))
                              .unlockedBy(Criterions.HAS_PREVIOUS_BLOCK, RecipeProvider.has(ModItems.GREEN_MINI_PRESENT))
                              .save(exporter, Utils.MOD_ID + ":red_mini_present_cycle");
    }

    private String id(ItemLike item) {
        return Registry.ITEM.getKey(item.asItem()).toString();
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static class Criterions {
        public static final String HAS_ITEM = "has_item";
        private static final String HAS_PREVIOUS_KIT = "has_previous_kit";
        private static final String HAS_PREVIOUS_BLOCK = "has_previous_block";
    }

    @Override
    public String getName() {
        return "Expanded Storage - Recipes";
    }
}
