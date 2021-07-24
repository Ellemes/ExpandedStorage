package ninjaphenix.expandedstorage.old_chest;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import ninjaphenix.expandedstorage.old_chest.block.OldChestBlock;

public class Main {
    public Main() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        OldChestCommon.registerContent(blocks -> {
            for (OldChestBlock block : blocks) {
                block.setRegistryName(block.blockId());
            }
            modEventBus.addGenericListener(Block.class, (RegistryEvent.Register<Block> event) -> {
                IForgeRegistry<Block> registry = event.getRegistry();
                blocks.forEach(registry::register);
            });
        }, items -> {
            for (BlockItem item : items) {
                item.setRegistryName(((OldChestBlock) item.getBlock()).blockId());
            }
            modEventBus.addGenericListener(Item.class, (RegistryEvent.Register<Item> event) -> {
                IForgeRegistry<Item> registry = event.getRegistry();
                items.forEach(registry::register);
            });
        }, blockEntityType -> {
            blockEntityType.setRegistryName(OldChestCommon.BLOCK_TYPE);
            modEventBus.addGenericListener(BlockEntityType.class, (RegistryEvent.Register<BlockEntityType<?>> event) -> {
                event.getRegistry().register(blockEntityType);
            });
        });
    }
}
