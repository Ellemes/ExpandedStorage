package ellemes.expandedstorage.fabric.datagen.providers;

import ellemes.expandedstorage.fabric.datagen.content.ModItems;
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

    //private static ResourceLocation blockId(Block block) {
    //    ResourceLocation id = Registry.BLOCK.getKey(block);
    //    return new ResourceLocation(Utils.MOD_ID, "block/" + id.getPath());
    //}

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
}
