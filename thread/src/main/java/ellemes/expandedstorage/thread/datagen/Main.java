package ellemes.expandedstorage.thread.datagen;

import ellemes.expandedstorage.thread.datagen.providers.BlockLootProvider;
import ellemes.expandedstorage.thread.datagen.providers.BlockStateProvider;
import ellemes.expandedstorage.thread.datagen.providers.RecipeProvider;
import ellemes.expandedstorage.thread.datagen.providers.TagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public final class Main implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.addProvider(RecipeProvider::new);
        generator.addProvider(TagProvider.Block::new);
        generator.addProvider(TagProvider.Item::new);
        generator.addProvider(BlockLootProvider::new);
        generator.addProvider(BlockStateProvider::new);
    }
}
