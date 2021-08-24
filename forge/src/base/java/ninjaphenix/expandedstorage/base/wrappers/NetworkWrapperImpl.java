package ninjaphenix.expandedstorage.base.wrappers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;
import ninjaphenix.expandedstorage.base.client.menu.PickScreen;
import ninjaphenix.expandedstorage.base.internal_api.Utils;
import ninjaphenix.expandedstorage.base.internal_api.block.AbstractOpenableStorageBlock;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.AbstractOpenableStorageBlockEntity;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.AbstractStorageBlockEntity;
import ninjaphenix.expandedstorage.base.internal_api.inventory.ServerMenuFactory;
import ninjaphenix.expandedstorage.base.inventory.PagedMenu;
import ninjaphenix.expandedstorage.base.inventory.ScrollableMenu;
import ninjaphenix.expandedstorage.base.inventory.SingleMenu;
import ninjaphenix.expandedstorage.base.network.NotifyServerOptionsMessage;
import ninjaphenix.expandedstorage.base.network.OpenInventoryMessage;
import ninjaphenix.expandedstorage.base.network.ScreenTypeUpdateMessage;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class NetworkWrapperImpl implements NetworkWrapper {
    private static NetworkWrapperImpl INSTANCE;
    private final Map<UUID, ResourceLocation> playerPreferences = new HashMap<>();
    private final Map<ResourceLocation, ServerMenuFactory> menuFactories = Utils.unmodifiableMap(map -> {
        map.put(Utils.SINGLE_SCREEN_TYPE, SingleMenu::new);
        map.put(Utils.SCROLLABLE_SCREEN_TYPE, ScrollableMenu::new);
        map.put(Utils.PAGED_SCREEN_TYPE, PagedMenu::new);
    });
    private SimpleChannel channel;
    private Set<ResourceLocation> screenOptions;

    public static NetworkWrapperImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NetworkWrapperImpl();
        }
        return INSTANCE;
    }

    @SubscribeEvent
    public static void sOnPlayerConnected(PlayerEvent.PlayerLoggedInEvent event) {
        NetworkWrapperImpl.INSTANCE.channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getPlayer()), new NotifyServerOptionsMessage(NetworkWrapperImpl.INSTANCE.menuFactories.keySet()));
    }

    @SubscribeEvent
    public static void sOnPlayerDisconnected(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) { // Probably called on both sides.
            NetworkWrapper.getInstance().s_setPlayerScreenType(player, Utils.UNSET_SCREEN_TYPE);
        }
    }

    @SubscribeEvent
    public static void cOnPlayerDisconnected(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        NetworkWrapperImpl.INSTANCE.screenOptions = NetworkWrapperImpl.INSTANCE.menuFactories.keySet();
    }

    public void initialise() {
        String channelVersion = "3";
        channel = NetworkRegistry.newSimpleChannel(Utils.resloc("channel"), () -> channelVersion, channelVersion::equals, channelVersion::equals);

        channel.registerMessage(0, ScreenTypeUpdateMessage.class, ScreenTypeUpdateMessage::encode, ScreenTypeUpdateMessage::decode, ScreenTypeUpdateMessage::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        channel.registerMessage(1, NotifyServerOptionsMessage.class, NotifyServerOptionsMessage::encode, NotifyServerOptionsMessage::decode, NotifyServerOptionsMessage::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        channel.registerMessage(2, OpenInventoryMessage.class, OpenInventoryMessage::encode, OpenInventoryMessage::decode, OpenInventoryMessage::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));

        if (PlatformUtils.getInstance().isClient()) {
            new Client().initialize();
        }
        MinecraftForge.EVENT_BUS.addListener(NetworkWrapperImpl::sOnPlayerConnected);
        MinecraftForge.EVENT_BUS.addListener(NetworkWrapperImpl::sOnPlayerDisconnected);
    }

    @Override
    public void c2s_sendTypePreference(ResourceLocation selection) {
        ClientPacketListener listener = Minecraft.getInstance().getConnection();
        if (listener != null && channel.isRemotePresent(listener.getConnection())) {
            channel.sendToServer(new ScreenTypeUpdateMessage(selection));
        }
    }

    @Override
    public void s_setPlayerScreenType(ServerPlayer player, ResourceLocation screenType) {
        UUID uuid = player.getUUID();
        if (menuFactories.containsKey(screenType)) {
            playerPreferences.put(uuid, screenType);
        } else {
            playerPreferences.remove(uuid);
        }
    }

    @Override
    public Set<ResourceLocation> getScreenOptions() {
        return screenOptions;
    }

    @Override
    public void c_openInventoryAt(BlockPos pos) {
        Client.INSTANCE.openInventoryAt(pos);
    }

    @Override
    public void c_openInventoryAt(BlockPos pos, ResourceLocation selection) {
        Client.INSTANCE.openInventoryAt(pos, selection);
    }

    public void c_setServerOptions(Set<ResourceLocation> options) {
        options.removeIf(option -> !menuFactories.containsKey(option));
        screenOptions = Set.copyOf(options);
        ResourceLocation option = ConfigWrapper.getInstance().getPreferredScreenType();
        if (screenOptions.contains(option)) {
            channel.sendToServer(new ScreenTypeUpdateMessage(option));
        } else {
            ConfigWrapper.getInstance().setPreferredScreenType(Utils.UNSET_SCREEN_TYPE);
        }
    }

    public void handleOpenInventory(BlockPos pos, ServerPlayer player, ResourceLocation preference) {
        if (preference != null) {
            playerPreferences.put(player.getUUID(), preference);
        }
        UUID uuid = player.getUUID();
        ResourceLocation playerPreference;
        if (playerPreferences.containsKey(uuid) && menuFactories.containsKey(playerPreference = playerPreferences.get(uuid))) {
            var level = player.getLevel();
            var state = level.getBlockState(pos);
            if (state.getBlock() instanceof AbstractOpenableStorageBlock block) {
                var inventories = block.getInventoryParts(level, state, pos);
                if (inventories.size() == 1 || inventories.size() == 2) {
                    var displayName = AbstractStorageBlockEntity.getDisplayName(inventories);
                    if (player.containerMenu == null || player.containerMenu == player.inventoryMenu) {
                        if (inventories.stream().allMatch(entity -> entity.canPlayerInteractWith(player))) {
                            block.awardOpeningStat(player);
                        } else {
                            player.displayClientMessage(new TranslatableComponent("container.isLocked", displayName), true);
                            player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
                            return;
                        }
                    }
                    NetworkHooks.openGui(player, new MenuProvider() {
                        @Override
                        public Component getDisplayName() {
                            return displayName;
                        }

                        @Nullable
                        @Override
                        public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
                            for (AbstractOpenableStorageBlockEntity entity : inventories) {
                                if (!entity.canContinueUse(player)) {
                                    return null;
                                }
                            }
                            if (inventories.size() == 1) {
                                Container container = inventories.get(0).getContainerWrapper();
                                return menuFactories.get(playerPreference).create(windowId, inventories.get(0).getBlockPos(), container, playerInventory, this.getDisplayName());
                            } else if (inventories.size() == 2) {
                                var container = new CompoundContainer(inventories.get(0).getContainerWrapper(), inventories.get(1).getContainerWrapper());
                                return menuFactories.get(playerPreference).create(windowId, inventories.get(0).getBlockPos(), container, playerInventory, this.getDisplayName());
                            }
                            throw new IllegalStateException("inventories size is > 2");
                        }
                    }, buffer -> buffer.writeBlockPos(pos).writeInt(inventories.stream().mapToInt(AbstractOpenableStorageBlockEntity::getSlotCount).sum()));
                }
            }
        }
    }

    private class Client {
        private static Client INSTANCE;

        private void initialize() {
            MinecraftForge.EVENT_BUS.addListener(NetworkWrapperImpl::cOnPlayerDisconnected);
            INSTANCE = this;
        }

        private void openInventoryAt(BlockPos pos) {
            if (ConfigWrapper.getInstance().getPreferredScreenType().equals(Utils.UNSET_SCREEN_TYPE)) {
                Minecraft.getInstance().setScreen(new PickScreen(NetworkWrapper.getInstance().getScreenOptions(), null, (preference) -> this.openInventoryAt(pos, preference)));
            } else {
                this.openInventoryAt(pos, null);
            }
        }

        private void openInventoryAt(BlockPos pos, @Nullable ResourceLocation preference) {
            ClientPacketListener listener = Minecraft.getInstance().getConnection();
            if (listener != null && NetworkWrapperImpl.this.channel.isRemotePresent(listener.getConnection())) {
                NetworkWrapperImpl.this.channel.sendToServer(new OpenInventoryMessage(pos, preference));
            }
        }
    }
}
