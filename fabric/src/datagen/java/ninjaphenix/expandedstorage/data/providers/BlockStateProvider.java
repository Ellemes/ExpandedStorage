package ninjaphenix.expandedstorage.data.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockStateDefinitionProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.model.BlockStateModelGenerator;
import net.minecraft.data.client.model.Models;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ninjaphenix.expandedstorage.Utils;
import ninjaphenix.expandedstorage.data.content.ModItems;

public class BlockStateProvider extends FabricBlockStateDefinitionProvider {
    public BlockStateProvider(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
    }

    private static Identifier blockId(Block block) {
        Identifier id = Registry.BLOCK.getId(block);
        return new Identifier(Utils.MOD_ID, "block/" + id.getPath());
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(ModItems.STORAGE_MUTATOR, Models.GENERATED);
        generator.register(ModItems.WOOD_TO_IRON_CONVERSION_KIT, Models.GENERATED);
        generator.register(ModItems.WOOD_TO_GOLD_CONVERSION_KIT, Models.GENERATED);
        generator.register(ModItems.WOOD_TO_DIAMOND_CONVERSION_KIT, Models.GENERATED);
        generator.register(ModItems.WOOD_TO_OBSIDIAN_CONVERSION_KIT, Models.GENERATED);
        generator.register(ModItems.WOOD_TO_NETHERITE_CONVERSION_KIT, Models.GENERATED);
        generator.register(ModItems.IRON_TO_GOLD_CONVERSION_KIT, Models.GENERATED);
        generator.register(ModItems.IRON_TO_DIAMOND_CONVERSION_KIT, Models.GENERATED);
        generator.register(ModItems.IRON_TO_OBSIDIAN_CONVERSION_KIT, Models.GENERATED);
        generator.register(ModItems.IRON_TO_NETHERITE_CONVERSION_KIT, Models.GENERATED);
        generator.register(ModItems.GOLD_TO_DIAMOND_CONVERSION_KIT, Models.GENERATED);
        generator.register(ModItems.GOLD_TO_OBSIDIAN_CONVERSION_KIT, Models.GENERATED);
        generator.register(ModItems.GOLD_TO_NETHERITE_CONVERSION_KIT, Models.GENERATED);
        generator.register(ModItems.DIAMOND_TO_OBSIDIAN_CONVERSION_KIT, Models.GENERATED);
        generator.register(ModItems.DIAMOND_TO_NETHERITE_CONVERSION_KIT, Models.GENERATED);
        generator.register(ModItems.OBSIDIAN_TO_NETHERITE_CONVERSION_KIT, Models.GENERATED);
    }
}
