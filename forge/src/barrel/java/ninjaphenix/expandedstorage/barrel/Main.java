package ninjaphenix.expandedstorage.barrel;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import ninjaphenix.expandedstorage.barrel.block.BarrelBlock;
import ninjaphenix.expandedstorage.base.wrappers.PlatformUtils;

public final class Main {
    public Main() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BarrelCommon.registerContent(blocks -> {
            for (BarrelBlock block : blocks) {
                block.setRegistryName(block.blockId());
            }
            modEventBus.addGenericListener(Block.class, (RegistryEvent.Register<Block> event) -> {
                IForgeRegistry<Block> registry = event.getRegistry();
                blocks.forEach(registry::register);
            });
            // Do client side stuff
            if (PlatformUtils.getInstance().isClient()) {
                modEventBus.addListener((FMLClientSetupEvent event) -> {
                    blocks.forEach(block -> ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped()));
                });
            }
        }, items -> {
            for (BlockItem item : items) {
                item.setRegistryName(((BarrelBlock) item.getBlock()).blockId());
            }
            modEventBus.addGenericListener(Item.class, (RegistryEvent.Register<Item> event) -> {
                IForgeRegistry<Item> registry = event.getRegistry();
                items.forEach(registry::register);
            });
        }, blockEntityType -> {
            blockEntityType.setRegistryName(BarrelCommon.BLOCK_TYPE);
            modEventBus.addGenericListener(BlockEntityType.class, (RegistryEvent.Register<BlockEntityType<?>> event) -> {
                event.getRegistry().register(blockEntityType);
            });
        }, BlockTags.createOptional(new ResourceLocation("forge", "barrels/wooden")));
    }
}
