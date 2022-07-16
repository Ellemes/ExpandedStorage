package ellemes.expandedstorage.quilt;

import ellemes.expandedstorage.misc.TagReloadListener;
import ellemes.expandedstorage.Utils;
import ellemes.expandedstorage.block.BarrelBlock;
import ellemes.expandedstorage.registration.Content;
import ellemes.expandedstorage.registration.ContentConsumer;
import ellemes.expandedstorage.registration.NamedValue;
import ellemes.expandedstorage.thread.ThreadMain;
import net.fabricmc.api.EnvType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.Version;
import org.quiltmc.loader.api.minecraft.MinecraftQuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;
import org.quiltmc.qsl.resource.loader.api.ResourceLoaderEvents;
import org.slf4j.LoggerFactory;

public final class QuiltMain implements ModInitializer {
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
        ThreadMain.constructContent(QuiltLoader.isModLoaded("htm"), group, isClient, tagReloadListener,
                ((ContentConsumer) ThreadMain::registerContent)
                        .andThenIf(isCarrierCompatEnabled, ThreadMain::registerCarrierCompat)
                        .andThenIf(isClient, ThreadMain::registerClientStuff)
                        .andThenIf(isClient, this::registerBarrelRenderLayers)
        );
        ResourceLoaderEvents.END_DATA_PACK_RELOAD.register(((server, resourceManager, error) -> tagReloadListener.postDataReload()));
    }

    private void registerBarrelRenderLayers(Content content) {
        for (NamedValue<BarrelBlock> block : content.getBarrelBlocks()) {
            BlockRenderLayerMap.put(RenderType.cutoutMipped(), block.getValue());
        }
    }
}
