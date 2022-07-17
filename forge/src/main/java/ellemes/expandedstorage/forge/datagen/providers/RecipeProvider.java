package ellemes.expandedstorage.forge.datagen.providers;

import ellemes.expandedstorage.datagen.providers.RecipeHelper;
import ellemes.expandedstorage.forge.datagen.content.ForgeTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public final class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    public RecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> exporter) {
        RecipeHelper recipeHelper = new RecipeHelper(
                ForgeRegistries.ITEMS::getKey,
                Tags.Items.INGOTS_IRON, Tags.Items.INGOTS_GOLD, Tags.Items.GEMS_DIAMOND, Tags.Items.OBSIDIAN, Tags.Items.INGOTS_NETHERITE,
                Tags.Items.CHESTS_WOODEN, Tags.Items.BARRELS_WOODEN,
                Tags.Items.GLASS, Tags.Items.DYES_RED, Tags.Items.DYES_WHITE, ForgeTags.Items.BAMBOO
        );
        recipeHelper.registerRecipes(exporter);
    }

    @Override
    public String getName() {
        return "Expanded Storage - Recipes";
    }
}
