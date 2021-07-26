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
import ninjaphenix.expandedstorage.barrel.block.misc.BarrelBlockEntity;
import ninjaphenix.expandedstorage.base.wrappers.PlatformUtils;

import java.util.Set;

public final class Main {
    public Main() {
        BarrelCommon.registerContent(this::registerBlocks, this::registerItems, this::registerBET,
                BlockTags.createOptional(new ResourceLocation("forge", "barrels/wooden")));
    }

    private void registerBET(BlockEntityType<BarrelBlockEntity> blockEntityType) {
        blockEntityType.setRegistryName(BarrelCommon.BLOCK_TYPE);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addGenericListener(BlockEntityType.class, (RegistryEvent.Register<BlockEntityType<?>> event) -> {
            event.getRegistry().register(blockEntityType);
        });
    }

    private void registerItems(Set<BlockItem> items) {
        for (BlockItem item : items) {
            item.setRegistryName(((BarrelBlock) item.getBlock()).blockId());
        }
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addGenericListener(Item.class, (RegistryEvent.Register<Item> event) -> {
            IForgeRegistry<Item> registry = event.getRegistry();
            items.forEach(registry::register);
        });
    }

    private void registerBlocks(Set<BarrelBlock> blocks) {
        for (BarrelBlock block : blocks) {
            block.setRegistryName(block.blockId());
        }
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addGenericListener(Block.class, (RegistryEvent.Register<Block> event) -> {
            IForgeRegistry<Block> registry = event.getRegistry();
            for (BarrelBlock block : blocks) {
                registry.register(block);
            }
        });
        if (PlatformUtils.getInstance().isClient()) {
            modEventBus.addListener((FMLClientSetupEvent event) -> {
                for (BarrelBlock block : blocks) {
                    ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped());
                }
            });
        }
    }
}
