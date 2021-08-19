package ninjaphenix.expandedstorage.chest;

import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import ninjaphenix.expandedstorage.base.internal_api.ModuleInitializer;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.AbstractOpenableStorageBlockEntity;
import ninjaphenix.expandedstorage.base.wrappers.PlatformUtils;
import ninjaphenix.expandedstorage.chest.block.ChestBlock;
import ninjaphenix.expandedstorage.chest.block.misc.ChestBlockEntity;
import ninjaphenix.expandedstorage.chest.client.ChestBlockEntityRenderer;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public final class Main implements ModuleInitializer {
    @Override
    public void initialize() {
        // This is nasty, can I make this code better?
        AtomicReference<Set<ChestBlock>> b = new AtomicReference<>();
        AtomicReference<Set<BlockItem>> i = new AtomicReference<>();
        Consumer<Set<ChestBlock>> registerBlocks = (blocks) -> {
            b.set(blocks);
            blocks.forEach(block -> Registry.register(Registry.BLOCK, block.getBlockId(), block));
        };
        Consumer<Set<BlockItem>> registerItems = (items) -> {
            i.set(items);
            items.forEach(item -> Registry.register(Registry.ITEM, ((ChestBlock) item.getBlock()).getBlockId(), item));
        };
        Consumer<BlockEntityType<ChestBlockEntity>> registerBET = (blockEntityType) -> {
            Registry.register(Registry.BLOCK_ENTITY_TYPE, ChestCommon.BLOCK_TYPE, blockEntityType);
            ItemStorage.SIDED.registerForBlocks(AbstractOpenableStorageBlockEntity::getItemStorage, blockEntityType.validBlocks.toArray(Block[]::new));
            if (PlatformUtils.getInstance().isClient()) {
                Client.registerChestTextures(b.get());
                Client.registerItemRenderers(i.get());
            }
        };
        ChestCommon.registerContent(registerBlocks, registerItems, registerBET, TagRegistry.block(new ResourceLocation("c", "wooden_chests")), BlockItem::new);
    }

    private static class Client {
        public static void registerChestTextures(Set<ChestBlock> blocks) {
            ChestCommon.registerChestTextures(blocks);
            ClientSpriteRegistryCallback.event(Sheets.CHEST_SHEET).register((atlasTexture, registry) -> ChestCommon.getChestTextures(blocks).forEach(registry::register));

            BlockEntityRendererRegistry.INSTANCE.register(ChestCommon.getBlockEntityType(), ChestBlockEntityRenderer::new);
        }

        public static void registerItemRenderers(Set<BlockItem> items) {
            items.forEach(item -> {
                ChestBlockEntity renderEntity = new ChestBlockEntity(ChestCommon.getBlockEntityType(), BlockPos.ZERO, item.getBlock().defaultBlockState());
                BuiltinItemRendererRegistry.INSTANCE.register(item, (itemStack, transform, stack, source, light, overlay) ->
                        Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(renderEntity, stack, source, light, overlay));
            });
            ModelLayers.ALL_MODELS.add(ChestBlockEntityRenderer.SINGLE_LAYER);
            ModelLayers.ALL_MODELS.add(ChestBlockEntityRenderer.VANILLA_LEFT_LAYER);
            ModelLayers.ALL_MODELS.add(ChestBlockEntityRenderer.VANILLA_RIGHT_LAYER);
            ModelLayers.ALL_MODELS.add(ChestBlockEntityRenderer.TALL_TOP_LAYER);
            ModelLayers.ALL_MODELS.add(ChestBlockEntityRenderer.TALL_BOTTOM_LAYER);
            ModelLayers.ALL_MODELS.add(ChestBlockEntityRenderer.LONG_FRONT_LAYER);
            ModelLayers.ALL_MODELS.add(ChestBlockEntityRenderer.LONG_BACK_LAYER);
        }
    }
}
