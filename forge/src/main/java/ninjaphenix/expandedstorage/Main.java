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

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import ninjaphenix.expandedstorage.block.AbstractChestBlock;
import ninjaphenix.expandedstorage.block.BarrelBlock;
import ninjaphenix.expandedstorage.block.ChestBlock;
import ninjaphenix.expandedstorage.block.MiniChestBlock;
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
import ninjaphenix.expandedstorage.registration.BlockItemCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod("expandedstorage")
public final class Main {
    private final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

    public Main() {
        Common.setSharedStrategies(GenericItemAccess::new, (entity) -> new BasicLockable());
        Tag.Named<Block> chestCycle = BlockTags.createOptional(Utils.id("chest_cycle"));
        Tag.Named<Block> miniChestCycle = BlockTags.createOptional(Utils.id("mini_chest_cycle"));
        Tag.Named<Block> miniChestSecretCycle = BlockTags.createOptional(Utils.id("mini_chest_secret_cycle"));
        Tag.Named<Block> miniChestSecretCycle2 = BlockTags.createOptional(Utils.id("mini_chest_secret_cycle_2"));
        MenuType<MiniChestScreenHandler> screenHandlerType = new MenuType<>(MiniChestScreenHandler::createClientMenu);
        screenHandlerType.setRegistryName(Utils.id("minichest_handler"));
        Common.registerContent(new CreativeModeTab(Utils.MOD_ID) {
                                   @Override
                                   public ItemStack makeIcon() {
                                       return new ItemStack(ForgeRegistries.ITEMS.getValue(Utils.id("netherite_chest")), 1);
                                   }
                               }, FMLLoader.getDist().isClient(),
                this::baseRegistration, false,
                this::chestRegistration, BlockTags.createOptional(new ResourceLocation("forge", "chests/wooden")), ChestBlockItem::new, ChestItemAccess::new,
                this::oldChestRegistration,
                this::barrelRegistration, BlockTags.createOptional(new ResourceLocation("forge", "barrels/wooden")),
                this::miniChestRegistration, MiniChestBlockItem::new, screenHandlerType,
                chestCycle, miniChestCycle, miniChestSecretCycle, miniChestSecretCycle2
        );
        modBus.addGenericListener(MenuType.class, (RegistryEvent.Register<MenuType<?>> event) -> {
            IForgeRegistry<MenuType<?>> registry = event.getRegistry();
            registry.registerAll(Common.getMiniChestScreenHandlerType());
        });
        modBus.addListener((FMLClientSetupEvent event) -> MenuScreens.register(Common.getMiniChestScreenHandlerType(), MiniChestScreen::new));

        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, (AttachCapabilitiesEvent<BlockEntity> event) -> {
            if (event.getObject() instanceof OpenableBlockEntity entity) {
                event.addCapability(Utils.id("item_access"), new ICapabilityProvider() {
                    @NotNull
                    @Override
                    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
                        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                            return LazyOptional.of(() -> {
                                if (entity instanceof OldChestBlockEntity chestBlockEntity) {
                                    BlockState state = chestBlockEntity.getBlockState();
                                    DoubleItemAccess access = chestBlockEntity.getItemAccess();
                                    if (access.hasCachedAccess() || state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) == CursedChestType.SINGLE) {
                                        //noinspection unchecked
                                        return (T) access.get();
                                    }
                                    Level world = entity.getLevel();
                                    BlockPos pos = entity.getBlockPos();
                                    if (world.getBlockEntity(pos.relative(AbstractChestBlock.getDirectionToAttached(state))) instanceof OldChestBlockEntity otherEntity) {
                                        DoubleItemAccess first, second;
                                        if (AbstractChestBlock.getBlockType(state) == DoubleBlockCombiner.BlockType.FIRST) {
                                            first = access;
                                            second = otherEntity.getItemAccess();
                                        } else {
                                            first = otherEntity.getItemAccess();
                                            second = access;
                                        }
                                        first.setOther(second);
                                        //noinspection unchecked
                                        return (T) first.get();
                                    }
                                }
                                //noinspection unchecked
                                return (T) entity.getItemAccess().get();
                            });
                        }
                        return LazyOptional.empty();
                    }
                });
            }
        });
    }

    private void baseRegistration(Pair<ResourceLocation, Item>[] items) {
        modBus.addGenericListener(Item.class, (RegistryEvent.Register<Item> event) -> {
            IForgeRegistry<Item> registry = event.getRegistry();
            for (Pair<ResourceLocation, Item> item : items) {
                registry.register(item.getSecond().setRegistryName(item.getFirst()));
            }
        });
    }

    private void chestRegistration(BlockItemCollection<ChestBlock, BlockItem> content, BlockEntityType<ChestBlockEntity> blockEntityType) {
        modBus.addGenericListener(Block.class, (RegistryEvent.Register<Block> event) -> {
            IForgeRegistry<Block> registry = event.getRegistry();
            for (ChestBlock block : content.getBlocks()) {
                registry.register(block.setRegistryName(block.getBlockId()));
            }
        });
        modBus.addGenericListener(Item.class, (RegistryEvent.Register<Item> event) -> {
            IForgeRegistry<Item> registry = event.getRegistry();
            for (BlockItem item : content.getItems()) {
                registry.register(item.setRegistryName(((ChestBlock) item.getBlock()).getBlockId()));
            }
        });
        blockEntityType.setRegistryName(Common.CHEST_BLOCK_TYPE);
        modBus.addGenericListener(BlockEntityType.class, (RegistryEvent.Register<BlockEntityType<?>> event) -> {
            event.getRegistry().register(blockEntityType);
        });

        if (FMLLoader.getDist() == Dist.CLIENT) {
            modBus.addListener((TextureStitchEvent.Pre event) -> {
                if (!event.getMap().location().equals(Sheets.CHEST_SHEET)) {
                    return;
                }
                for (ResourceLocation texture : Common.getChestTextures(content.getBlocks())) {
                    event.addSprite(texture);
                }
            });
            Client.registerEvents(modBus, blockEntityType);
        }
    }

    private void oldChestRegistration(BlockItemCollection<AbstractChestBlock, BlockItem> content, BlockEntityType<OldChestBlockEntity> blockEntityType) {
        modBus.addGenericListener(Block.class, (RegistryEvent.Register<Block> event) -> {
            IForgeRegistry<Block> registry = event.getRegistry();
            for (AbstractChestBlock block : content.getBlocks()) {
                registry.register(block.setRegistryName(block.getBlockId()));
            }
        });
        modBus.addGenericListener(Item.class, (RegistryEvent.Register<Item> event) -> {
            IForgeRegistry<Item> registry = event.getRegistry();
            for (BlockItem item : content.getItems()) {
                registry.register(item.setRegistryName(((AbstractChestBlock) item.getBlock()).getBlockId()));
            }
        });
        blockEntityType.setRegistryName(Common.OLD_CHEST_BLOCK_TYPE);
        modBus.addGenericListener(BlockEntityType.class, (RegistryEvent.Register<BlockEntityType<?>> event) -> {
            event.getRegistry().register(blockEntityType);
        });
    }

    private void barrelRegistration(BlockItemCollection<BarrelBlock, BlockItem> content, BlockEntityType<BarrelBlockEntity> blockEntityType) {
        modBus.addGenericListener(Block.class, (RegistryEvent.Register<Block> event) -> {
            IForgeRegistry<Block> registry = event.getRegistry();
            for (BarrelBlock block : content.getBlocks()) {
                registry.register(block.setRegistryName(block.getBlockId()));
            }
        });
        modBus.addGenericListener(Item.class, (RegistryEvent.Register<Item> event) -> {
            IForgeRegistry<Item> registry = event.getRegistry();
            for (BlockItem item : content.getItems()) {
                registry.register(item.setRegistryName(((BarrelBlock) item.getBlock()).getBlockId()));
            }
        });
        blockEntityType.setRegistryName(Common.BARREL_BLOCK_TYPE);
        modBus.addGenericListener(BlockEntityType.class, (RegistryEvent.Register<BlockEntityType<?>> event) -> {
            event.getRegistry().register(blockEntityType);
        });

        if (FMLLoader.getDist() == Dist.CLIENT) {
            modBus.addListener((FMLClientSetupEvent event) -> {
                for (BarrelBlock block : content.getBlocks()) {
                    ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped());
                }
            });
        }
    }

    private void miniChestRegistration(BlockItemCollection<MiniChestBlock, BlockItem> content, BlockEntityType<MiniChestBlockEntity> blockEntityType) {
        modBus.addGenericListener(Block.class, (RegistryEvent.Register<Block> event) -> {
            IForgeRegistry<Block> registry = event.getRegistry();
            for (MiniChestBlock block : content.getBlocks()) {
                registry.register(block.setRegistryName(block.getBlockId()));
            }
        });
        modBus.addGenericListener(Item.class, (RegistryEvent.Register<Item> event) -> {
            IForgeRegistry<Item> registry = event.getRegistry();
            for (BlockItem item : content.getItems()) {
                registry.register(item.setRegistryName(((MiniChestBlock) item.getBlock()).getBlockId()));
            }
        });
        blockEntityType.setRegistryName(Common.MINI_CHEST_BLOCK_TYPE);
        modBus.addGenericListener(BlockEntityType.class, (RegistryEvent.Register<BlockEntityType<?>> event) -> {
            event.getRegistry().register(blockEntityType);
        });
    }

    private static class Client {
        private static void registerEvents(IEventBus modBus, BlockEntityType<ChestBlockEntity> type) {
            modBus.addListener((EntityRenderersEvent.RegisterRenderers.RegisterRenderers event) -> {
                event.registerBlockEntityRenderer(type, ChestBlockEntityRenderer::new);
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
        }
    }
}
