package ninjaphenix.expandedstorage.chest;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlclient.registry.RenderingRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import ninjaphenix.expandedstorage.base.wrappers.PlatformUtils;
import ninjaphenix.expandedstorage.chest.block.ChestBlock;
import ninjaphenix.expandedstorage.chest.block.ChestBlockItem;
import ninjaphenix.expandedstorage.chest.block.misc.ChestBlockEntity;
import ninjaphenix.expandedstorage.chest.client.ChestBlockEntityRenderer;

public class Main {
    public Main() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ChestCommon.registerContent(blocks -> {
            for (ChestBlock block : blocks) {
                block.setRegistryName(block.blockId());
            }
            modEventBus.addGenericListener(Block.class, (RegistryEvent.Register<Block> event) -> {
                IForgeRegistry<Block> registry = event.getRegistry();
                blocks.forEach(registry::register);
            });

            if (PlatformUtils.getInstance().isClient()) {
                ChestCommon.registerChestTextures(blocks);
                modEventBus.addListener((TextureStitchEvent.Pre event) -> {
                    if (!event.getMap().location().equals(Sheets.CHEST_SHEET)) {
                        return;
                    }
                    ChestCommon.getChestTextures(blocks).forEach(event::addSprite);
                });
            }
        }, items -> {
            for (BlockItem item : items) {
                item.setRegistryName(((ChestBlock) item.getBlock()).blockId());
            }
            modEventBus.addGenericListener(Item.class, (RegistryEvent.Register<Item> event) -> {
                IForgeRegistry<Item> registry = event.getRegistry();
                items.forEach(registry::register);
            });
        }, blockEntityType -> {
            blockEntityType.setRegistryName(ChestCommon.BLOCK_TYPE);
            modEventBus.addGenericListener(BlockEntityType.class, (RegistryEvent.Register<BlockEntityType<?>> event) -> {
                event.getRegistry().register(blockEntityType);
            });
            if (PlatformUtils.getInstance().isClient()) {
                Client.registerBER(blockEntityType);
            }
        }, BlockTags.createOptional(new ResourceLocation("forge", "chests/wooden")), ChestBlockItem::new);

        if (PlatformUtils.getInstance().isClient()) {
            Client.registerModelLayers(modEventBus);
        }
    }

    private static class Client {
        private static void registerBER(BlockEntityType<ChestBlockEntity> type) {
            BlockEntityRenderers.register(type, ChestBlockEntityRenderer::new);
        }

        public static void registerModelLayers(IEventBus modEventBus) {
            modEventBus.addListener((FMLClientSetupEvent event) -> {
                RenderingRegistry.registerLayerDefinition(ChestBlockEntityRenderer.SINGLE_LAYER, ChestBlockEntityRenderer::createSingleBodyLayer);
                RenderingRegistry.registerLayerDefinition(ChestBlockEntityRenderer.VANILLA_LEFT_LAYER, ChestBlockEntityRenderer::createVanillaLeftBodyLayer);
                RenderingRegistry.registerLayerDefinition(ChestBlockEntityRenderer.VANILLA_RIGHT_LAYER, ChestBlockEntityRenderer::createVanillaRightBodyLayer);
                RenderingRegistry.registerLayerDefinition(ChestBlockEntityRenderer.TALL_TOP_LAYER, ChestBlockEntityRenderer::createTallTopBodyLayer);
                RenderingRegistry.registerLayerDefinition(ChestBlockEntityRenderer.TALL_BOTTOM_LAYER, ChestBlockEntityRenderer::createTallBottomBodyLayer);
                RenderingRegistry.registerLayerDefinition(ChestBlockEntityRenderer.LONG_FRONT_LAYER, ChestBlockEntityRenderer::createLongFrontBodyLayer);
                RenderingRegistry.registerLayerDefinition(ChestBlockEntityRenderer.LONG_BACK_LAYER, ChestBlockEntityRenderer::createLongBackBodyLayer);
            });
        }
    }
}
