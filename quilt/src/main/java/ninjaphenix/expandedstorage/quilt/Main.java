/*
 * Copyright 2021-2022 NinjaPhenix
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
package ninjaphenix.expandedstorage.quilt;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
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
import ninjaphenix.expandedstorage.Common;
import ninjaphenix.expandedstorage.TagReloadListener;
import ninjaphenix.expandedstorage.Utils;
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
import ninjaphenix.expandedstorage.quilt.compat.carrier.CarrierCompat;
import ninjaphenix.expandedstorage.quilt.compat.htm.HTMLockable;
import ninjaphenix.expandedstorage.registration.NamedValue;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.minecraft.MinecraftQuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public final class Main implements ModInitializer {
    private boolean isCarrierCompatEnabled = false;
    @Override
    public void onInitialize(ModContainer mod) {
        if (QuiltLoader.isModLoaded("quilt_loader")) {
            try {
                SemanticVersion version = SemanticVersion.parse("1.8.0");
                isCarrierCompatEnabled = QuiltLoader.getModContainer("carrier").map(it -> {
                    if (it.metadata().version() instanceof SemanticVersion carrierVersion)
                        return carrierVersion.compareTo(version) > 0;

                    return false;
                }).orElse(false);
            } catch (VersionParsingException ignored) {
            }

            CreativeModeTab group = FabricItemGroupBuilder.build(Utils.id("tab"), () -> new ItemStack(Registry.ITEM.get(Utils.id("netherite_chest")))); // Fabric API is dumb.
            boolean isClient = MinecraftQuiltLoader.getEnvironmentType() == EnvType.CLIENT;
            TagReloadListener tagReloadListener = new TagReloadListener();
            Common.constructContent(GenericItemAccess::new, QuiltLoader.isModLoaded("htm") ? HTMLockable::new : BasicLockable::new, group, isClient, tagReloadListener, this::registerContent,
                    /*Base*/ true,
                    /*Chest*/ TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("c", "wooden_chests")), BlockItem::new, ChestItemAccess::new,
                    /*Old Chest*/
                    /*Barrel*/ TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("c", "wooden_barrels")),
                    /*Mini Chest*/ BlockItem::new);

            ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> tagReloadListener.postDataReload());
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

        ItemStorage.SIDED.registerForBlocks(Main::getItemAccess, allBlocks.stream().map(NamedValue::getValue).toArray(OpenableBlock[]::new));

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

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            Main.Client.registerChestTextures(chestBlocks.stream().map(NamedValue::getName).collect(Collectors.toList()));
            Main.Client.registerItemRenderers(chestItems);
            for (NamedValue<BarrelBlock> block : barrelBlocks) {
                BlockRenderLayerMap.INSTANCE.putBlock(block.getValue(), RenderType.cutoutMipped());
            }
        }
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

    private static class Client {
        public static void registerChestTextures(List<ResourceLocation> blocks) {
            ClientSpriteRegistryCallback.event(Sheets.CHEST_SHEET).register((atlasTexture, registry) -> {
                for (ResourceLocation texture : Common.getChestTextures(blocks)) registry.register(texture);
            });
            BlockEntityRendererRegistry.register(Common.getChestBlockEntityType(), ChestBlockEntityRenderer::new);
        }

        public static void registerItemRenderers(List<NamedValue<BlockItem>> items) {
            for (NamedValue<BlockItem> item : items) {
                ChestBlockEntity renderEntity = Common.getChestBlockEntityType().create(BlockPos.ZERO, item.getValue().getBlock().defaultBlockState());
                BuiltinItemRendererRegistry.INSTANCE.register(item.getValue(), (itemStack, transform, stack, source, light, overlay) ->
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
