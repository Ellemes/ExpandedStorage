package ninjaphenix.expandedstorage.barrel;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import ninjaphenix.expandedstorage.barrel.block.BarrelBlock;
import ninjaphenix.expandedstorage.barrel.block.misc.BarrelBlockEntity;
import ninjaphenix.expandedstorage.base.internal_api.ModuleInitializer;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.AbstractOpenableStorageBlockEntity;
import ninjaphenix.expandedstorage.base.wrappers.PlatformUtils;

import java.util.Set;

public final class Main implements ModuleInitializer {
    private static void registerBET(BlockEntityType<BarrelBlockEntity> blockEntityType) {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, BarrelCommon.BLOCK_TYPE, blockEntityType);
        ItemStorage.SIDED.registerForBlocks(AbstractOpenableStorageBlockEntity::getItemStorage, blockEntityType.validBlocks.toArray(Block[]::new));
    }

    private static void registerBlocks(Set<BarrelBlock> blocks) {
        blocks.forEach(block -> Registry.register(Registry.BLOCK, block.getBlockId(), block));
        if (PlatformUtils.getInstance().isClient()) {
            blocks.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutoutMipped()));
        }
    }

    private static void registerItems(Set<BlockItem> items) {
        items.forEach(item -> Registry.register(Registry.ITEM, ((BarrelBlock) item.getBlock()).getBlockId(), item));
    }

    @Override
    public void initialize() {
        BarrelCommon.registerContent(Main::registerBlocks, Main::registerItems, Main::registerBET, TagRegistry.block(new ResourceLocation("c", "wooden_barrels")));
    }
}
