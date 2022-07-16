package ellemes.expandedstorage.forge.datagen.providers;

import ellemes.expandedstorage.Utils;
import ellemes.expandedstorage.datagen.providers.BlockLootTableHelper;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.stream.Collectors;

public final class BlockLootProvider extends BlockLoot {
    @Override
    protected void addTables() {
        BlockLootTableHelper.registerLootTables(this::add, BlockLoot::createNameableBlockEntityTable);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getEntries().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(Utils.MOD_ID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }
}
