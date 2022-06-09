package ellemes.expandedstorage.quilt.datagen.providers;

import ellemes.expandedstorage.quilt.datagen.content.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;

public class BlockStateProvider extends FabricModelProvider {
    public BlockStateProvider(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        generator.generateFlatItem(ModItems.STORAGE_MUTATOR, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.WOOD_TO_IRON_CONVERSION_KIT, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.WOOD_TO_GOLD_CONVERSION_KIT, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.WOOD_TO_DIAMOND_CONVERSION_KIT, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.WOOD_TO_OBSIDIAN_CONVERSION_KIT, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.WOOD_TO_NETHERITE_CONVERSION_KIT, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.IRON_TO_GOLD_CONVERSION_KIT, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.IRON_TO_DIAMOND_CONVERSION_KIT, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.IRON_TO_OBSIDIAN_CONVERSION_KIT, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.IRON_TO_NETHERITE_CONVERSION_KIT, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.GOLD_TO_DIAMOND_CONVERSION_KIT, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.GOLD_TO_OBSIDIAN_CONVERSION_KIT, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.GOLD_TO_NETHERITE_CONVERSION_KIT, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.DIAMOND_TO_OBSIDIAN_CONVERSION_KIT, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.DIAMOND_TO_NETHERITE_CONVERSION_KIT, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.OBSIDIAN_TO_NETHERITE_CONVERSION_KIT, ModelTemplates.FLAT_ITEM);
    }

    @Override
    public String getName() {
        return "Expanded Storage - BlockStates / Models";
    }
}
