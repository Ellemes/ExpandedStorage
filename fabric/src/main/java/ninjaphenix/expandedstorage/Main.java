package ninjaphenix.expandedstorage;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
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
import ninjaphenix.expandedstorage.registration.BlockItemCollection;
import org.jetbrains.annotations.Nullable;

public final class Main implements ModInitializer {
    private final FabricLoaderImpl fabricLoader = FabricLoaderImpl.INSTANCE;
    @Override
    public void onInitialize() {
        FabricItemGroupBuilder.build(new ResourceLocation("dummy"), null); // Fabric API is dumb.
        CreativeModeTab group = new CreativeModeTab(CreativeModeTab.TABS.length - 1, Utils.MOD_ID) {
            @Override
            public ItemStack makeIcon() {
                return new ItemStack(Registry.ITEM.get(Utils.id("netherite_chest")));
            }
        };
        Common.registerContent(GenericItemAccess::new, BasicLockable::new,
                group, fabricLoader.getEnvironmentType() == EnvType.CLIENT,
                this::baseRegistration, true,
                this::chestRegistration, TagRegistry.block(new ResourceLocation("c", "wooden_chests")), BlockItem::new, ChestItemAccess::new,
                this::oldChestRegistration,
                this::barrelRegistration, TagRegistry.block(new ResourceLocation("c", "wooden_barrels")),
                this::miniChestRegistration, BlockItem::new, ScreenHandlerRegistry.registerSimple(Utils.id("mini_chest_handler"), MiniChestScreenHandler::createClientMenu),
                TagRegistry.block(Utils.id("chest_cycle")), TagRegistry.block(Utils.id("mini_chest_cycle")), TagRegistry.block(Utils.id("mini_chest_secret_cycle")), TagRegistry.block(Utils.id("mini_chest_secret_cycle_2")));
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
            //if (isCarrierCompatEnabled) CarrierCompat.registerChestBlock(block);
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
            //if (isCarrierCompatEnabled) CarrierCompat.registerOldChestBlock(block);
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
            ClientSpriteRegistryCallback.event(TextureAtlas.LOCATION_BLOCKS).register((atlasTexture, registry) -> {
                for (ResourceLocation texture : Common.getChestTextures(blocks)) registry.register(texture);
            });
            BlockEntityRendererRegistry.INSTANCE.register(Common.getChestBlockEntityType(), ChestBlockEntityRenderer::new);
        }

        public static void registerItemRenderers(BlockItem[] items) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("x", 0);
            tag.putInt("y", 0);
            tag.putInt("z", 0);
            for (BlockItem item : items) {
                ChestBlockEntity renderEntity = Common.getChestBlockEntityType().create();
                renderEntity.load(item.getBlock().defaultBlockState(), tag);
                BuiltinItemRendererRegistry.INSTANCE.register(item, (itemStack, transform, stack, source, light, overlay) ->
                        BlockEntityRenderDispatcher.instance.renderItem(renderEntity, stack, source, light, overlay));
            }
        }
    }
}
