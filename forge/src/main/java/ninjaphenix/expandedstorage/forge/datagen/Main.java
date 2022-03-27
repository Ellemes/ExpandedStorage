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
package ninjaphenix.expandedstorage.forge.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import ninjaphenix.expandedstorage.forge.datagen.providers.BlockStatesAndModels;
import ninjaphenix.expandedstorage.forge.datagen.providers.ItemModelProvider;
import ninjaphenix.expandedstorage.forge.datagen.providers.LootTableProvider;
import ninjaphenix.expandedstorage.forge.datagen.providers.RecipeProvider;
import ninjaphenix.expandedstorage.forge.datagen.providers.TagProvider;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Main {
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event)
    {
        final DataGenerator generator = event.getGenerator();
        final ExistingFileHelper fileHelper = event.getExistingFileHelper();
        final BlockTagsProvider blockTagsProvider = new TagProvider.Block(generator, fileHelper);
        generator.addProvider(blockTagsProvider);
        generator.addProvider(new TagProvider.Item(generator, blockTagsProvider, fileHelper));
        generator.addProvider(new RecipeProvider(generator));
        generator.addProvider(new LootTableProvider(generator));
        //generator.addProvider(new LanguageProvider(generator));
        generator.addProvider(new BlockStatesAndModels(generator, fileHelper));
        generator.addProvider(new ItemModelProvider(generator, fileHelper));
    }
}
