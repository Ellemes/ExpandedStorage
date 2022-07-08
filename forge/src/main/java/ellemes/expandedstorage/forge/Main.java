package ellemes.expandedstorage.forge;

import ellemes.expandedstorage.Common;
import ellemes.expandedstorage.TagReloadListener;
import ellemes.expandedstorage.Utils;
import ellemes.expandedstorage.api.EsChestType;
import ellemes.expandedstorage.block.AbstractChestBlock;
import ellemes.expandedstorage.block.entity.ChestBlockEntity;
import ellemes.expandedstorage.block.entity.OldChestBlockEntity;
import ellemes.expandedstorage.block.entity.extendable.OpenableBlockEntity;
import ellemes.expandedstorage.block.misc.BasicLockable;
import ellemes.expandedstorage.block.misc.DoubleItemAccess;
import ellemes.expandedstorage.client.ChestBlockEntityRenderer;
import ellemes.expandedstorage.registration.Content;
import ellemes.expandedstorage.registration.NamedValue;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

@Mod("expandedstorage")
public final class Main {
    private final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

    public Main() {
        TagReloadListener tagReloadListener = new TagReloadListener();

        Common.constructContent(GenericItemAccess::new, BasicLockable::new,
                new CreativeModeTab(Utils.MOD_ID + ".tab") {
                    @NotNull
                    @Override
                    public ItemStack makeIcon() {
                        return new ItemStack(ForgeRegistries.ITEMS.getValue(Utils.id("netherite_chest")), 1);
                    }
                }, FMLLoader.getDist().isClient(), tagReloadListener, this::registerContent,
                /*Base*/ false,
                /*Chest*/ TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("forge", "chests/wooden")), ChestBlockItem::new, ChestItemAccess::new,
                /*Old Chest*/
                /*Barrel*/ TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("forge", "barrels/wooden")),
                /*Mini Chest*/ MiniChestBlockItem::new);

        MinecraftForge.EVENT_BUS.addListener((TagsUpdatedEvent event) -> tagReloadListener.postDataReload());

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
                                    EsChestType chestType = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
                                    Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                                    if (access.hasCachedAccess() || chestType == EsChestType.SINGLE) {
                                        //noinspection unchecked
                                        return (T) access.get();
                                    }
                                    Level world = entity.getLevel();
                                    BlockPos pos = entity.getBlockPos();
                                    if (world.getBlockEntity(pos.relative(AbstractChestBlock.getDirectionToAttached(chestType, facing))) instanceof OldChestBlockEntity otherEntity) {
                                        DoubleItemAccess otherAccess = otherEntity.getItemAccess();
                                        if (otherAccess.hasCachedAccess()) {
                                            //noinspection unchecked
                                            return (T) otherAccess.get();
                                        }
                                        DoubleItemAccess first, second;
                                        if (AbstractChestBlock.getBlockType(chestType) == DoubleBlockCombiner.BlockType.FIRST) {
                                            first = access;
                                            second = otherAccess;
                                        } else {
                                            first = otherAccess;
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

    private void registerContent(Content content) {
        modBus.addListener((RegisterEvent event) -> {
            event.register(ForgeRegistries.Keys.STAT_TYPES, helper -> {
                content.getStats().forEach(it -> Registry.register(Registry.CUSTOM_STAT, it, it));
            });

            event.register(ForgeRegistries.Keys.BLOCKS, helper -> {
                Common.iterateNamedList(content.getBlocks(), (name, value) -> {
                    helper.register(name, value);
                    Common.registerTieredBlock(value);
                });
            });

            event.register(ForgeRegistries.Keys.ITEMS, helper -> {
                Common.iterateNamedList(content.getItems(), helper::register);
            });

            event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, helper -> {
                Main.registerBlockEntity(helper, content.getChestBlockEntityType());
                Main.registerBlockEntity(helper, content.getOldChestBlockEntityType());
                Main.registerBlockEntity(helper, content.getBarrelBlockEntityType());
                Main.registerBlockEntity(helper, content.getMiniChestBlockEntityType());
            });
        });

        if (FMLLoader.getDist() == Dist.CLIENT) {
            modBus.addListener((TextureStitchEvent.Pre event) -> {
                if (!event.getAtlas().location().equals(Sheets.CHEST_SHEET)) {
                    return;
                }
                for (ResourceLocation texture : Common.getChestTextures(content.getChestBlocks().stream().map(NamedValue::getName).collect(Collectors.toList()))) {
                    event.addSprite(texture);
                }
            });

            Client.registerEvents(modBus, content.getChestBlockEntityType());
        }
    }

    private static <T extends BlockEntity> void registerBlockEntity(RegisterEvent.RegisterHelper<BlockEntityType<?>> helper, NamedValue<BlockEntityType<T>> blockEntityType) {
        helper.register(blockEntityType.getName(), blockEntityType.getValue());
    }

    private static class Client {
        private static void registerEvents(IEventBus modBus, NamedValue<BlockEntityType<ChestBlockEntity>> type) {
            modBus.addListener((EntityRenderersEvent.RegisterRenderers event) -> {
                event.registerBlockEntityRenderer(type.getValue(), ChestBlockEntityRenderer::new);
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
