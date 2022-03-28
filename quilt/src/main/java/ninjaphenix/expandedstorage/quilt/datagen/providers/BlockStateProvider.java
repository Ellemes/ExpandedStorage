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
package ninjaphenix.expandedstorage.quilt.datagen.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import ninjaphenix.expandedstorage.quilt.datagen.content.ModItems;

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
