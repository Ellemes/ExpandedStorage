package ninjaphenix.expandedstorage.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import ninjaphenix.expandedstorage.data.providers.BlockStatesAndModels;
import ninjaphenix.expandedstorage.data.providers.ItemModelProvider;
import ninjaphenix.expandedstorage.data.providers.LanguageProvider;
import ninjaphenix.expandedstorage.data.providers.LootTableProvider;
import ninjaphenix.expandedstorage.data.providers.RecipeProvider;
import ninjaphenix.expandedstorage.data.providers.TagProvider;

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
