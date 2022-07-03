package ellemes.expandedstorage.quilt;

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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
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

import java.util.List;

public final class Main implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        if (!QuiltLoader.isModLoaded("quilt_loader")) {
            LoggerFactory.getLogger(Utils.MOD_ID).warn("Please use Expanded Storage for Fabric instead.");
            System.exit(0);
            return;
        }
        boolean isCarrierCompatEnabled = QuiltLoader.getModContainer("carrier").map(it -> {
            Version carrierVersion = it.metadata().version();
            return carrierVersion.isSemantic() && carrierVersion.semantic().compareTo(Version.of("1.8.0").semantic()) > 0;
        }).orElse(false);

        CreativeModeTab group = QuiltItemGroup.builder(Utils.id("tab")).icon(() -> new ItemStack(Registry.ITEM.get(Utils.id("netherite_chest")))).build();
        boolean isClient = MinecraftQuiltLoader.getEnvironmentType() == EnvType.CLIENT;
        TagReloadListener tagReloadListener = new TagReloadListener();
        Thread.constructContent(QuiltLoader.isModLoaded("htm"), group, isClient, tagReloadListener,
                ((ContentConsumer) Thread::registerContent)
                        .andThenIf(isCarrierCompatEnabled, Thread::registerCarrierCompat)
                        .andThenIf(isClient, Thread::registerClientStuff)
                        .andThenIf(isClient, this::registerBarrelRenderLayers)
        );
        ResourceLoaderEvents.END_DATA_PACK_RELOAD.register(((server, resourceManager, error) -> tagReloadListener.postDataReload()));
    }

    private void registerBarrelRenderLayers(List<ResourceLocation> stats, List<NamedValue<Item>> baseItems,
                                            List<NamedValue<ChestBlock>> chestBlocks, List<NamedValue<BlockItem>> chestItems, NamedValue<BlockEntityType<ChestBlockEntity>> chestBlockEntityType,
                                            List<NamedValue<AbstractChestBlock>> oldChestBlocks, List<NamedValue<BlockItem>> oldChestItems, NamedValue<BlockEntityType<OldChestBlockEntity>> oldChestBlockEntityType,
                                            List<NamedValue<BarrelBlock>> barrelBlocks, List<NamedValue<BlockItem>> barrelItems, NamedValue<BlockEntityType<BarrelBlockEntity>> barrelBlockEntityType,
                                            List<NamedValue<MiniChestBlock>> miniChestBlocks, List<NamedValue<BlockItem>> miniChestItems, NamedValue<BlockEntityType<MiniChestBlockEntity>> miniChestBlockEntityType) {
        for (NamedValue<BarrelBlock> block : barrelBlocks) {
            BlockRenderLayerMap.put(RenderType.cutoutMipped(), block.getValue());
        }
    }
}
