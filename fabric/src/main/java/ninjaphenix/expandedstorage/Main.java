/*
 * Copyright 2021 NinjaPhenix
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ninjaphenix.expandedstorage;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import ninjaphenix.expandedstorage.block.AbstractChestBlock;
import ninjaphenix.expandedstorage.block.BarrelBlock;
import ninjaphenix.expandedstorage.block.ChestBlock;
import ninjaphenix.expandedstorage.block.MiniChestBlock;
import ninjaphenix.expandedstorage.block.OpenableBlock;
import ninjaphenix.expandedstorage.block.entity.BarrelBlockEntity;
import ninjaphenix.expandedstorage.block.entity.ChestBlockEntity;
import ninjaphenix.expandedstorage.block.entity.MiniChestBlockEntity;
import ninjaphenix.expandedstorage.block.entity.OldChestBlockEntity;
import ninjaphenix.expandedstorage.block.entity.extendable.OpenableBlockEntity;
import ninjaphenix.expandedstorage.block.misc.BasicLockable;
import ninjaphenix.expandedstorage.block.misc.CursedChestType;
import ninjaphenix.expandedstorage.block.misc.DoubleItemAccess;
import ninjaphenix.expandedstorage.client.ChestBlockEntityRenderer;
import ninjaphenix.expandedstorage.client.MiniChestScreen;
import ninjaphenix.expandedstorage.compat.carrier.CarrierCompat;
import ninjaphenix.expandedstorage.compat.htm.HTMLockable;
import ninjaphenix.expandedstorage.registration.BlockItemCollection;
import org.jetbrains.annotations.Nullable;

public final class Main implements ModInitializer {
    private final FabricLoaderImpl fabricLoader = FabricLoaderImpl.INSTANCE;
    private boolean isCarrierCompatEnabled = false;
    @Override
    public void onInitialize() {
        try {
            SemanticVersion version = SemanticVersion.parse("1.8.0");
            isCarrierCompatEnabled = fabricLoader.getModContainer("carrier").map(it -> {
                if (it.getMetadata().getVersion() instanceof SemanticVersion carrierVersion)
                    return carrierVersion.compareTo(version) > 0;

                return false;
            }).orElse(false);
        } catch (VersionParsingException ignored) {
        }

        Common.setSharedStrategies(GenericItemAccess::new, (entity) -> fabricLoader.isModLoaded("htm") ? new HTMLockable() : new BasicLockable());
        FabricItemGroupBuilder.build(new Identifier("dummy"), null); // Fabric API is dumb.
        ItemGroup group = new ItemGroup(ItemGroup.GROUPS.length - 1, Utils.MOD_ID) {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(Registry.ITEM.get(Utils.id("netherite_chest")));
            }
        };
        boolean isClient = fabricLoader.getEnvironmentType() == EnvType.CLIENT;
        Common.registerContent(group, isClient,
                this::baseRegistration, true,
                this::chestRegistration, TagFactory.BLOCK.create(new Identifier("c", "wooden_chests")), BlockItem::new, ChestItemAccess::new,
                this::oldChestRegistration,
                this::barrelRegistration, TagFactory.BLOCK.create(new Identifier("c", "wooden_barrels")),
                this::miniChestRegistration, BlockItem::new, ScreenHandlerRegistry.registerSimple(Utils.id("minichest_handler"), MiniChestScreenHandler::createClientMenu),
                TagFactory.BLOCK.create(Utils.id("chest_cycle")), TagFactory.BLOCK.create(Utils.id("mini_chest_cycle")), TagFactory.BLOCK.create(Utils.id("mini_chest_secret_cycle")), TagFactory.BLOCK.create(Utils.id("mini_chest_secret_cycle_2")));

        if (isClient) ScreenRegistry.register(Common.getMiniChestScreenHandlerType(), MiniChestScreen::new);
    }

    @SuppressWarnings({"UnstableApiUsage"})
    private static Storage<ItemVariant> getItemAccess(World world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, @SuppressWarnings("unused") Direction context) {
        if (blockEntity instanceof OldChestBlockEntity entity) {
            DoubleItemAccess access = entity.getItemAccess();
            if (access.hasCachedAccess() || state.get(AbstractChestBlock.CURSED_CHEST_TYPE) == CursedChestType.SINGLE) {
                //noinspection unchecked
                return (Storage<ItemVariant>) access.get();
            }
            if (world.getBlockEntity(pos.offset(AbstractChestBlock.getDirectionToAttached(state))) instanceof OldChestBlockEntity otherEntity) {
                DoubleItemAccess first, second;
                if (AbstractChestBlock.getBlockType(state) == DoubleBlockProperties.Type.FIRST) {
                    first = entity.getItemAccess();
                    second = otherEntity.getItemAccess();
                } else {
                    first = otherEntity.getItemAccess();
                    second = entity.getItemAccess();
                }
                first.setOther(second);
                //noinspection unchecked
                return (Storage<ItemVariant>) first.get();
            }

        }
        else if (blockEntity instanceof OpenableBlockEntity entity) {
            //noinspection unchecked
            return (Storage<ItemVariant>) entity.getItemAccess().get();
        }
        return null;
    }

    private void baseRegistration(Pair<Identifier, Item>[] items) {
        for (Pair<Identifier, Item> item : items) Registry.register(Registry.ITEM, item.getFirst(), item.getSecond());
    }

    private void chestRegistration(BlockItemCollection<ChestBlock, BlockItem> content, BlockEntityType<ChestBlockEntity> blockEntityType) {
        for (ChestBlock block : content.getBlocks()) {
            if (isCarrierCompatEnabled) CarrierCompat.registerChestBlock(block);
            Registry.register(Registry.BLOCK, block.getBlockId(), block);
        }
        for (BlockItem item : content.getItems())
            Registry.register(Registry.ITEM, ((OpenableBlock) item.getBlock()).getBlockId(), item);

        Registry.register(Registry.BLOCK_ENTITY_TYPE, Common.CHEST_BLOCK_TYPE, blockEntityType);
        // noinspection UnstableApiUsage
        ItemStorage.SIDED.registerForBlocks(Main::getItemAccess, content.getBlocks());
        if (fabricLoader.getEnvironmentType() == EnvType.CLIENT) {
            Main.Client.registerChestTextures(content.getBlocks());
            Main.Client.registerItemRenderers(content.getItems());
        }
    }

    private void oldChestRegistration(BlockItemCollection<AbstractChestBlock, BlockItem> content, BlockEntityType<OldChestBlockEntity> blockEntityType) {
        for (AbstractChestBlock block : content.getBlocks()) {
            if (isCarrierCompatEnabled) CarrierCompat.registerOldChestBlock(block);
            Registry.register(Registry.BLOCK, block.getBlockId(), block);
        }
        for (BlockItem item : content.getItems())
            Registry.register(Registry.ITEM, ((OpenableBlock) item.getBlock()).getBlockId(), item);

        Registry.register(Registry.BLOCK_ENTITY_TYPE, Common.OLD_CHEST_BLOCK_TYPE, blockEntityType);
        // noinspection UnstableApiUsage
        ItemStorage.SIDED.registerForBlocks(Main::getItemAccess, content.getBlocks());
    }

    private void barrelRegistration(BlockItemCollection<BarrelBlock, BlockItem> content, BlockEntityType<BarrelBlockEntity> blockEntityType) {
        boolean isClient = fabricLoader.getEnvironmentType() == EnvType.CLIENT;
        for (BarrelBlock block : content.getBlocks()) {
            Registry.register(Registry.BLOCK, block.getBlockId(), block);
            if (isClient) BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped());
        }
        for (BlockItem item : content.getItems())
            Registry.register(Registry.ITEM, ((OpenableBlock) item.getBlock()).getBlockId(), item);

        Registry.register(Registry.BLOCK_ENTITY_TYPE, Common.BARREL_BLOCK_TYPE, blockEntityType);
        // noinspection UnstableApiUsage
        ItemStorage.SIDED.registerForBlocks(Main::getItemAccess, content.getBlocks());
    }

    private void miniChestRegistration(BlockItemCollection<MiniChestBlock, BlockItem> content, BlockEntityType<MiniChestBlockEntity> blockEntityType) {
        for (MiniChestBlock block : content.getBlocks()) Registry.register(Registry.BLOCK, block.getBlockId(), block);
        for (BlockItem item : content.getItems())
            Registry.register(Registry.ITEM, ((OpenableBlock) item.getBlock()).getBlockId(), item);

        Registry.register(Registry.BLOCK_ENTITY_TYPE, Common.MINI_CHEST_BLOCK_TYPE, blockEntityType);
        //noinspection UnstableApiUsage
        ItemStorage.SIDED.registerForBlocks(Main::getItemAccess, content.getBlocks());
    }

    private static class Client {
        public static void registerChestTextures(ChestBlock[] blocks) {
            ClientSpriteRegistryCallback.event(TexturedRenderLayers.CHEST_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
                for (Identifier texture : Common.getChestTextures(blocks)) registry.register(texture);
            });
            BlockEntityRendererRegistry.register(Common.getChestBlockEntityType(), ChestBlockEntityRenderer::new);
        }

        public static void registerItemRenderers(BlockItem[] items) {
            for (BlockItem item : items) {
                ChestBlockEntity renderEntity = Common.getChestBlockEntityType().instantiate(BlockPos.ORIGIN, item.getBlock().getDefaultState());
                BuiltinItemRendererRegistry.INSTANCE.register(item, (itemStack, transform, stack, source, light, overlay) ->
                        MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(renderEntity, stack, source, light, overlay));
            }
            EntityModelLayerRegistry.registerModelLayer(ChestBlockEntityRenderer.SINGLE_LAYER, ChestBlockEntityRenderer::createSingleBodyLayer);
            EntityModelLayerRegistry.registerModelLayer(ChestBlockEntityRenderer.LEFT_LAYER, ChestBlockEntityRenderer::createLeftBodyLayer);
            EntityModelLayerRegistry.registerModelLayer(ChestBlockEntityRenderer.RIGHT_LAYER, ChestBlockEntityRenderer::createRightBodyLayer);
            EntityModelLayerRegistry.registerModelLayer(ChestBlockEntityRenderer.TOP_LAYER, ChestBlockEntityRenderer::createTopBodyLayer);
            EntityModelLayerRegistry.registerModelLayer(ChestBlockEntityRenderer.BOTTOM_LAYER, ChestBlockEntityRenderer::createBottomBodyLayer);
            EntityModelLayerRegistry.registerModelLayer(ChestBlockEntityRenderer.FRONT_LAYER, ChestBlockEntityRenderer::createFrontBodyLayer);
            EntityModelLayerRegistry.registerModelLayer(ChestBlockEntityRenderer.BACK_LAYER, ChestBlockEntityRenderer::createBackBodyLayer);
        }
    }
}
