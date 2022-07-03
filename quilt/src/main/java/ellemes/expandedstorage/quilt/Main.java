package ellemes.expandedstorage.quilt;

import ellemes.expandedstorage.Common;
import ellemes.expandedstorage.TagReloadListener;
import ellemes.expandedstorage.Utils;
import ellemes.expandedstorage.block.AbstractChestBlock;
import ellemes.expandedstorage.block.BarrelBlock;
import ellemes.expandedstorage.block.ChestBlock;
import ellemes.expandedstorage.block.MiniChestBlock;
import ellemes.expandedstorage.block.OpenableBlock;
import ellemes.expandedstorage.block.entity.BarrelBlockEntity;
import ellemes.expandedstorage.block.entity.ChestBlockEntity;
import ellemes.expandedstorage.block.entity.MiniChestBlockEntity;
import ellemes.expandedstorage.block.entity.OldChestBlockEntity;
import ellemes.expandedstorage.block.misc.BasicLockable;
import ellemes.expandedstorage.thread.ChestItemAccess;
import ellemes.expandedstorage.thread.GenericItemAccess;
import ellemes.expandedstorage.thread.Thread;
import ellemes.expandedstorage.thread.compat.carrier.CarrierCompat;
import ellemes.expandedstorage.thread.compat.htm.HTMLockable;
import ellemes.expandedstorage.registration.NamedValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.Version;
import org.quiltmc.loader.api.minecraft.MinecraftQuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;
import org.quiltmc.qsl.resource.loader.api.ResourceLoaderEvents;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@SuppressWarnings("deprecation")
public final class Main implements ModInitializer {
    private boolean isCarrierCompatEnabled = false;

    @Override
    public void onInitialize(ModContainer mod) {
        if (QuiltLoader.isModLoaded("quilt_loader")) {
            isCarrierCompatEnabled = QuiltLoader.getModContainer("carrier").map(it -> {
                Version carrierVersion = it.metadata().version();
                if (carrierVersion.isSemantic()) {
                    return carrierVersion.semantic().compareTo(Version.of("1.8.0").semantic()) > 0;
                }
                return false;
            }).orElse(false);

            CreativeModeTab group = QuiltItemGroup.builder(Utils.id("tab")).icon(() -> new ItemStack(Registry.ITEM.get(Utils.id("netherite_chest")))).build();
            boolean isClient = MinecraftQuiltLoader.getEnvironmentType() == EnvType.CLIENT;
            TagReloadListener tagReloadListener = new TagReloadListener();
            Common.constructContent(GenericItemAccess::new, QuiltLoader.isModLoaded("htm") ? HTMLockable::new : BasicLockable::new, group, isClient, tagReloadListener, this::registerContent,
                    /*Base*/ true,
                    /*Chest*/ TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("c", "wooden_chests")), BlockItem::new, ChestItemAccess::new,
                    /*Old Chest*/
                    /*Barrel*/ TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("c", "wooden_barrels")),
                    /*Mini Chest*/ BlockItem::new);

            ResourceLoaderEvents.END_DATA_PACK_RELOAD.register(((server, resourceManager, error) -> tagReloadListener.postDataReload()));
        } else {
            LoggerFactory.getLogger(Utils.MOD_ID).warn("Please use Expanded Storage for Fabric instead.");
            System.exit(0);
        }
    }

    private void registerContent(List<ResourceLocation> stats, List<NamedValue<Item>> baseItems,
                                 List<NamedValue<ChestBlock>> chestBlocks, List<NamedValue<BlockItem>> chestItems, NamedValue<BlockEntityType<ChestBlockEntity>> chestBlockEntityType,
                                 List<NamedValue<AbstractChestBlock>> oldChestBlocks, List<NamedValue<BlockItem>> oldChestItems, NamedValue<BlockEntityType<OldChestBlockEntity>> oldChestBlockEntityType,
                                 List<NamedValue<BarrelBlock>> barrelBlocks, List<NamedValue<BlockItem>> barrelItems, NamedValue<BlockEntityType<BarrelBlockEntity>> barrelBlockEntityType,
                                 List<NamedValue<MiniChestBlock>> miniChestBlocks, List<NamedValue<BlockItem>> miniChestItems, NamedValue<BlockEntityType<MiniChestBlockEntity>> miniChestBlockEntityType) {
        for (ResourceLocation stat : stats) {
            Registry.register(Registry.CUSTOM_STAT, stat, stat);
        }

        List<NamedValue<? extends OpenableBlock>> allBlocks = new ArrayList<>();
        allBlocks.addAll(chestBlocks);
        allBlocks.addAll(oldChestBlocks);
        allBlocks.addAll(barrelBlocks);
        allBlocks.addAll(miniChestBlocks);
        Common.iterateNamedList(allBlocks, (name, value) -> {
            Registry.register(Registry.BLOCK, name, value);
            Common.registerTieredBlock(value);
        });

        ItemStorage.SIDED.registerForBlocks(Thread::getItemAccess, allBlocks.stream().map(NamedValue::getValue).toArray(OpenableBlock[]::new));

        if (isCarrierCompatEnabled) {
            for (NamedValue<ChestBlock> block : chestBlocks) {
                CarrierCompat.registerChestBlock(block.getValue());
            }

            for (NamedValue<AbstractChestBlock> block : oldChestBlocks) {
                CarrierCompat.registerOldChestBlock(block.getValue());
            }
        }

        List<NamedValue<? extends Item>> allItems = new ArrayList<>();
        allItems.addAll(baseItems);
        allItems.addAll(chestItems);
        allItems.addAll(oldChestItems);
        allItems.addAll(barrelItems);
        allItems.addAll(miniChestItems);
        Common.iterateNamedList(allItems, (name, value) -> Registry.register(Registry.ITEM, name, value));

        Registry.register(Registry.BLOCK_ENTITY_TYPE, chestBlockEntityType.getName(), chestBlockEntityType.getValue());
        Registry.register(Registry.BLOCK_ENTITY_TYPE, oldChestBlockEntityType.getName(), oldChestBlockEntityType.getValue());
        Registry.register(Registry.BLOCK_ENTITY_TYPE, barrelBlockEntityType.getName(), barrelBlockEntityType.getValue());
        Registry.register(Registry.BLOCK_ENTITY_TYPE, miniChestBlockEntityType.getName(), miniChestBlockEntityType.getValue());

        if (MinecraftQuiltLoader.getEnvironmentType() == EnvType.CLIENT) {
            Thread.Client.registerChestTextures(chestBlocks.stream().map(NamedValue::getName).collect(Collectors.toList()));
            Thread.Client.registerItemRenderers(chestItems);
            for (NamedValue<BarrelBlock> block : barrelBlocks) {
                BlockRenderLayerMap.put(RenderType.cutoutMipped(), block.getValue());
            }
        }
    }
}
