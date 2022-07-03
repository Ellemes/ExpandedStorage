package ellemes.expandedstorage.thread;

import ellemes.expandedstorage.Common;
import ellemes.expandedstorage.TagReloadListener;
import ellemes.expandedstorage.api.EsChestType;
import ellemes.expandedstorage.block.AbstractChestBlock;
import ellemes.expandedstorage.block.BarrelBlock;
import ellemes.expandedstorage.block.ChestBlock;
import ellemes.expandedstorage.block.MiniChestBlock;
import ellemes.expandedstorage.block.OpenableBlock;
import ellemes.expandedstorage.block.entity.BarrelBlockEntity;
import ellemes.expandedstorage.block.entity.ChestBlockEntity;
import ellemes.expandedstorage.block.entity.MiniChestBlockEntity;
import ellemes.expandedstorage.block.entity.OldChestBlockEntity;
import ellemes.expandedstorage.block.entity.extendable.OpenableBlockEntity;
import ellemes.expandedstorage.block.misc.BasicLockable;
import ellemes.expandedstorage.block.misc.DoubleItemAccess;
import ellemes.expandedstorage.client.ChestBlockEntityRenderer;
import ellemes.expandedstorage.registration.ContentConsumer;
import ellemes.expandedstorage.registration.NamedValue;
import ellemes.expandedstorage.thread.compat.carrier.CarrierCompat;
import ellemes.expandedstorage.thread.compat.htm.HTMLockable;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Thread {
    @SuppressWarnings({"UnstableApiUsage"})
    public static Storage<ItemVariant> getItemAccess(Level world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, @SuppressWarnings("unused") Direction context) {
        if (blockEntity instanceof OldChestBlockEntity entity) {
            DoubleItemAccess access = entity.getItemAccess();
            EsChestType type = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (access.hasCachedAccess() || type == EsChestType.SINGLE) {
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

        } else if (blockEntity instanceof OpenableBlockEntity entity) {
            //noinspection unchecked
            return (Storage<ItemVariant>) entity.getItemAccess().get();
        }
        return null;
    }

    public static void constructContent(boolean htmPresent, CreativeModeTab group, boolean isClient, TagReloadListener tagReloadListener, ContentConsumer contentRegistrationConsumer) {
        Common.constructContent(GenericItemAccess::new, htmPresent ? HTMLockable::new : BasicLockable::new, group, isClient, tagReloadListener, contentRegistrationConsumer,
                /*Base*/ true,
                /*Chest*/ TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("c", "wooden_chests")), BlockItem::new, ChestItemAccess::new,
                /*Old Chest*/
                /*Barrel*/ TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("c", "wooden_barrels")),
                /*Mini Chest*/ BlockItem::new);
    }

    public static void registerContent(List<ResourceLocation> stats, List<NamedValue<Item>> baseItems,
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

        //noinspection UnstableApiUsage
        ItemStorage.SIDED.registerForBlocks(Thread::getItemAccess, allBlocks.stream().map(NamedValue::getValue).toArray(OpenableBlock[]::new));

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
    }

    public static void registerCarrierCompat(List<ResourceLocation> stats, List<NamedValue<Item>> baseItems,
                                             List<NamedValue<ChestBlock>> chestBlocks, List<NamedValue<BlockItem>> chestItems, NamedValue<BlockEntityType<ChestBlockEntity>> chestBlockEntityType,
                                             List<NamedValue<AbstractChestBlock>> oldChestBlocks, List<NamedValue<BlockItem>> oldChestItems, NamedValue<BlockEntityType<OldChestBlockEntity>> oldChestBlockEntityType,
                                             List<NamedValue<BarrelBlock>> barrelBlocks, List<NamedValue<BlockItem>> barrelItems, NamedValue<BlockEntityType<BarrelBlockEntity>> barrelBlockEntityType,
                                             List<NamedValue<MiniChestBlock>> miniChestBlocks, List<NamedValue<BlockItem>> miniChestItems, NamedValue<BlockEntityType<MiniChestBlockEntity>> miniChestBlockEntityType) {
        for (NamedValue<ChestBlock> block : chestBlocks) {
            CarrierCompat.registerChestBlock(block.getValue());
        }

        for (NamedValue<AbstractChestBlock> block : oldChestBlocks) {
            CarrierCompat.registerOldChestBlock(block.getValue());
        }
    }

    public static void registerClientStuff(List<ResourceLocation> stats, List<NamedValue<Item>> baseItems,
                                           List<NamedValue<ChestBlock>> chestBlocks, List<NamedValue<BlockItem>> chestItems, NamedValue<BlockEntityType<ChestBlockEntity>> chestBlockEntityType,
                                           List<NamedValue<AbstractChestBlock>> oldChestBlocks, List<NamedValue<BlockItem>> oldChestItems, NamedValue<BlockEntityType<OldChestBlockEntity>> oldChestBlockEntityType,
                                           List<NamedValue<BarrelBlock>> barrelBlocks, List<NamedValue<BlockItem>> barrelItems, NamedValue<BlockEntityType<BarrelBlockEntity>> barrelBlockEntityType,
                                           List<NamedValue<MiniChestBlock>> miniChestBlocks, List<NamedValue<BlockItem>> miniChestItems, NamedValue<BlockEntityType<MiniChestBlockEntity>> miniChestBlockEntityType) {
        Thread.Client.registerChestTextures(chestBlocks.stream().map(NamedValue::getName).collect(Collectors.toList()));
        Thread.Client.registerItemRenderers(chestItems);
    }

    public static class Client {
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
