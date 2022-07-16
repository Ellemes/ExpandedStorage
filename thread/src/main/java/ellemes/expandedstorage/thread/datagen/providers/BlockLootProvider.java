package ellemes.expandedstorage.thread.datagen.providers;

import ellemes.expandedstorage.datagen.providers.BlockLootTableHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.data.loot.BlockLoot;

public final class BlockLootProvider extends FabricBlockLootTableProvider {
    public BlockLootProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateBlockLootTables() {
        BlockLootTableHelper.registerLootTables(this::add, BlockLoot::createNameableBlockEntityTable);
    }

    @Override
    public String getName() {
        return "Expanded Storage - Loot Tables";
    }
}
