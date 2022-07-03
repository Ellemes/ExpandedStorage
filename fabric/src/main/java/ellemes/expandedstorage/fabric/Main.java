package ellemes.expandedstorage.fabric;

import ellemes.expandedstorage.TagReloadListener;
import ellemes.expandedstorage.Utils;
import ellemes.expandedstorage.block.AbstractChestBlock;
import ellemes.expandedstorage.block.BarrelBlock;
import ellemes.expandedstorage.block.ChestBlock;
import ellemes.expandedstorage.block.MiniChestBlock;
import ellemes.expandedstorage.block.entity.BarrelBlockEntity;
import ellemes.expandedstorage.block.entity.ChestBlockEntity;
import ellemes.expandedstorage.block.entity.MiniChestBlockEntity;
import ellemes.expandedstorage.block.entity.OldChestBlockEntity;
import ellemes.expandedstorage.registration.ContentConsumer;
import ellemes.expandedstorage.thread.Thread;
import ellemes.expandedstorage.registration.NamedValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class Main implements ModInitializer {

    @Override
    public void onInitialize() {
        FabricLoader fabricLoader = FabricLoader.getInstance();
        if (fabricLoader.isModLoaded("quilt_loader")) {
            LoggerFactory.getLogger(Utils.MOD_ID).warn("Please use Expanded Storage for Quilt instead.");
            System.exit(0);
            return;
        }
        boolean isCarrierCompatEnabled;
        try {
            SemanticVersion version = SemanticVersion.parse("1.8.0");
            isCarrierCompatEnabled = fabricLoader.getModContainer("carrier").map(it -> {
                return it.getMetadata().getVersion().compareTo(version) > 0;
            }).orElse(false);
        } catch (VersionParsingException e) {
            throw new IllegalStateException("Author made a typo: ", e);
        }

        CreativeModeTab group = FabricItemGroupBuilder.build(Utils.id("tab"), () -> new ItemStack(Registry.ITEM.get(Utils.id("netherite_chest")))); // Fabric API is dumb.
        boolean isClient = fabricLoader.getEnvironmentType() == EnvType.CLIENT;
        TagReloadListener tagReloadListener = new TagReloadListener();
        Thread.constructContent(
                fabricLoader.isModLoaded("htm"), group, isClient, tagReloadListener,
                ((ContentConsumer) Thread::registerContent)
                        .andThenIf(isCarrierCompatEnabled, Thread::registerCarrierCompat)
                        .andThenIf(isClient, Thread::registerClientStuff)
                        .andThenIf(isClient, this::registerBarrelRenderLayers)
        );
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> tagReloadListener.postDataReload());
    }

    private void registerBarrelRenderLayers(List<ResourceLocation> stats, List<NamedValue<Item>> baseItems,
                                            List<NamedValue<ChestBlock>> chestBlocks, List<NamedValue<BlockItem>> chestItems, NamedValue<BlockEntityType<ChestBlockEntity>> chestBlockEntityType,
                                            List<NamedValue<AbstractChestBlock>> oldChestBlocks, List<NamedValue<BlockItem>> oldChestItems, NamedValue<BlockEntityType<OldChestBlockEntity>> oldChestBlockEntityType,
                                            List<NamedValue<BarrelBlock>> barrelBlocks, List<NamedValue<BlockItem>> barrelItems, NamedValue<BlockEntityType<BarrelBlockEntity>> barrelBlockEntityType,
                                            List<NamedValue<MiniChestBlock>> miniChestBlocks, List<NamedValue<BlockItem>> miniChestItems, NamedValue<BlockEntityType<MiniChestBlockEntity>> miniChestBlockEntityType) {
        for (NamedValue<BarrelBlock> block : barrelBlocks) {
            BlockRenderLayerMap.INSTANCE.putBlock(block.getValue(), RenderType.cutoutMipped());
        }
    }
}
