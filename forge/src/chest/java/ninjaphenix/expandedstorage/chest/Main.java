package ninjaphenix.expandedstorage.chest;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import ninjaphenix.expandedstorage.base.wrappers.PlatformUtils;
import ninjaphenix.expandedstorage.chest.block.ChestBlock;
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
        }, BlockTags.createOptional(new ResourceLocation("forge", "chests/wooden")));

        modEventBus.addListener((FMLClientSetupEvent event) -> {
            ChestBlockEntityRenderer.registerModelLayersDefinitions();
        });
    }

    private static class Client {
        private static void registerBER(BlockEntityType<ChestBlockEntity> type) {
            BlockEntityRenderers.register(type, ChestBlockEntityRenderer::new);
        }
    }
}
