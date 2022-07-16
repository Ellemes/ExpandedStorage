package ellemes.expandedstorage.forge;

import ellemes.container_library.client.gui.PickScreen;
import ellemes.expandedstorage.Common;
import ellemes.expandedstorage.client.ChestBlockEntityRenderer;
import ellemes.expandedstorage.entity.ChestMinecart;
import ellemes.expandedstorage.registration.Content;
import ellemes.expandedstorage.registration.NamedValue;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;

import java.util.stream.Collectors;

public class ForgeClient {
    public static void initialize() {
        ModLoadingContext.get().getActiveContainer().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((client, screen) -> new PickScreen(() -> screen, null))
        );
    }

    public static void registerListeners(IEventBus modBus, Content content) {
        modBus.addListener((TextureStitchEvent.Pre event) -> {
            if (!event.getAtlas().location().equals(Sheets.CHEST_SHEET)) {
                return;
            }
            for (ResourceLocation texture : Common.getChestTextures(content.getChestBlocks().stream().map(NamedValue::getName).collect(Collectors.toList()))) {
                event.addSprite(texture);
            }
        });

        modBus.addListener((EntityRenderersEvent.RegisterRenderers event) -> {
            event.registerBlockEntityRenderer(content.getChestBlockEntityType().getValue(), ChestBlockEntityRenderer::new);
        });

        modBus.addListener((EntityRenderersEvent.RegisterLayerDefinitions event) -> {
            event.registerLayerDefinition(ChestBlockEntityRenderer.SINGLE_LAYER, ChestBlockEntityRenderer::createSingleBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.LEFT_LAYER, ChestBlockEntityRenderer::createLeftBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.RIGHT_LAYER, ChestBlockEntityRenderer::createRightBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.TOP_LAYER, ChestBlockEntityRenderer::createTopBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.BOTTOM_LAYER, ChestBlockEntityRenderer::createBottomBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.FRONT_LAYER, ChestBlockEntityRenderer::createFrontBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.BACK_LAYER, ChestBlockEntityRenderer::createBackBodyLayer);
        });

        modBus.addListener((EntityRenderersEvent.RegisterRenderers event) -> {
            for (NamedValue<EntityType<ChestMinecart>> type : content.getChestMinecartEntityTypes()) {
                event.registerEntityRenderer(type.getValue(), context -> new MinecartRenderer<>(context, ModelLayers.CHEST_MINECART));
            }
        });
    }
}
