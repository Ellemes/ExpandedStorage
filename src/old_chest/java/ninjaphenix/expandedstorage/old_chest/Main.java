package ninjaphenix.expandedstorage.old_chest;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import ninjaphenix.expandedstorage.base.BaseCommon;
import ninjaphenix.expandedstorage.base.internal_api.BaseApi;
import ninjaphenix.expandedstorage.base.internal_api.Utils;
import ninjaphenix.expandedstorage.base.internal_api.tier.OpenableTier;
import ninjaphenix.expandedstorage.base.wrappers.PlatformUtils;
import ninjaphenix.expandedstorage.chest.ChestCommon;
import ninjaphenix.expandedstorage.chest.block.ChestBlock;
import ninjaphenix.expandedstorage.chest.client.ChestBlockEntityRenderer;
import ninjaphenix.expandedstorage.old_chest.block.OldChestBlock;
import ninjaphenix.expandedstorage.old_chest.block.misc.OldChestBlockEntity;

import java.util.Collections;
import java.util.Set;

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
