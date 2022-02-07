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
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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
import ninjaphenix.expandedstorage.compat.carrier.CarrierCompat;
import ninjaphenix.expandedstorage.compat.htm.HTMLockable;
import ninjaphenix.expandedstorage.registration.BlockItemCollection;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
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

        CreativeModeTab group = FabricItemGroupBuilder.build(Utils.id("tab"), () -> new ItemStack(Registry.ITEM.get(Utils.id("netherite_chest")))); // Fabric API is dumb.
        boolean isClient = fabricLoader.getEnvironmentType() == EnvType.CLIENT;
        Common.registerContent(GenericItemAccess::new, fabricLoader.isModLoaded("htm") ? HTMLockable::new : BasicLockable::new,
                group, isClient,
                this::baseRegistration, true,
                this::chestRegistration, TagFactory.BLOCK.create(new ResourceLocation("c", "wooden_chests")), BlockItem::new, ChestItemAccess::new,
                this::oldChestRegistration,
                this::barrelRegistration, TagFactory.BLOCK.create(new ResourceLocation("c", "wooden_barrels")),
                this::miniChestRegistration, BlockItem::new,
                TagFactory.BLOCK.create(Utils.id("chest_cycle")), TagFactory.BLOCK.create(Utils.id("mini_chest_cycle")), TagFactory.BLOCK.create(Utils.id("mini_chest_secret_cycle")), TagFactory.BLOCK.create(Utils.id("mini_chest_secret_cycle_2")));
    }

    @SuppressWarnings({"UnstableApiUsage"})
    private static Storage<ItemVariant> getItemAccess(Level world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, @SuppressWarnings("unused") Direction context) {
        if (blockEntity instanceof OldChestBlockEntity entity) {
            DoubleItemAccess access = entity.getItemAccess();
            CursedChestType type = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (access.hasCachedAccess() || type == CursedChestType.SINGLE) {
                //noinspection unchecked
                return (Storage<ItemVariant>) access.get();
            }
            if (world.getBlockEntity(pos.relative(AbstractChestBlock.getDirectionToAttached(type, facing))) instanceof OldChestBlockEntity otherEntity) {
                DoubleItemAccess otherAccess = otherEntity.getItemAccess();
                if (otherAccess.hasCachedAccess()) {
                    //noinspection unchecked
                    return (Storage<ItemVariant>) otherAccess.get();
                }
                DoubleItemAccess first, second;
                if (AbstractChestBlock.getBlockType(type) == DoubleBlockCombiner.BlockType.FIRST) {
                    first = access;
                    second = otherAccess;
                } else {
                    first = otherAccess;
                    second = access;
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

    private void baseRegistration(Pair<ResourceLocation, Item>[] items) {
        for (Pair<ResourceLocation, Item> item : items) Registry.register(Registry.ITEM, item.getFirst(), item.getSecond());
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
            if (isClient) BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutoutMipped());
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
            ClientSpriteRegistryCallback.event(Sheets.CHEST_SHEET).register((atlasTexture, registry) -> {
                for (ResourceLocation texture : Common.getChestTextures(blocks)) registry.register(texture);
            });
            BlockEntityRendererRegistry.register(Common.getChestBlockEntityType(), ChestBlockEntityRenderer::new);
        }

        public static void registerItemRenderers(BlockItem[] items) {
            for (BlockItem item : items) {
                ChestBlockEntity renderEntity = Common.getChestBlockEntityType().create(BlockPos.ZERO, item.getBlock().defaultBlockState());
                BuiltinItemRendererRegistry.INSTANCE.register(item, (itemStack, transform, stack, source, light, overlay) ->
                        Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(renderEntity, stack, source, light, overlay));
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
