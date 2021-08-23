package ninjaphenix.expandedstorage.base.wrappers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;
import ninjaphenix.expandedstorage.base.internal_api.Utils;
import ninjaphenix.expandedstorage.base.internal_api.inventory.ServerMenuFactory;
import ninjaphenix.expandedstorage.base.inventory.PagedMenu;
import ninjaphenix.expandedstorage.base.inventory.ScrollableMenu;
import ninjaphenix.expandedstorage.base.inventory.SingleMenu;
import ninjaphenix.expandedstorage.base.network.NotifyServerOptionsMessage;
import ninjaphenix.expandedstorage.base.network.ScreenTypeUpdateMessage;

import java.util.HashMap;
import java.util.HashSet;
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
    public static void cOnPlayerConnected(ClientPlayerNetworkEvent.LoggedInEvent event) {
        NetworkWrapper.getInstance().c2s_sendTypePreference(ConfigWrapper.getInstance().getPreferredScreenType());
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

    }

    public void initialise() {
        String channelVersion = "2";
        channel = NetworkRegistry.newSimpleChannel(Utils.resloc("channel"), () -> channelVersion, channelVersion::equals, channelVersion::equals);

        channel.registerMessage(0, ScreenTypeUpdateMessage.class, ScreenTypeUpdateMessage::encode, ScreenTypeUpdateMessage::decode, ScreenTypeUpdateMessage::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        channel.registerMessage(1, NotifyServerOptionsMessage.class, NotifyServerOptionsMessage::encode, NotifyServerOptionsMessage::decode, NotifyServerOptionsMessage::handle, Optional.of(NetworkDirection.LOGIN_TO_CLIENT));

        if (PlatformUtils.getInstance().isClient()) {
            MinecraftForge.EVENT_BUS.addListener(NetworkWrapperImpl::cOnPlayerConnected);
            MinecraftForge.EVENT_BUS.addListener(NetworkWrapperImpl::cOnPlayerDisconnected);
        }
        MinecraftForge.EVENT_BUS.addListener(NetworkWrapperImpl::sOnPlayerConnected);
        MinecraftForge.EVENT_BUS.addListener(NetworkWrapperImpl::sOnPlayerDisconnected);
    }

    @Override
    public boolean isValidScreenType(ResourceLocation screenType) {
        return screenType != null && menuFactories.containsKey(screenType);
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

    }

    @Override
    public void c_openInventoryAt(BlockPos pos, ResourceLocation selection) {

    }

    public void c_setServerOptions(Set<ResourceLocation> options) {
        var newOptions = new HashSet<ResourceLocation>();
        for (ResourceLocation option : options) {
            if (this.isValidScreenType(option)) {
                newOptions.add(option);
            }
        }
        this.screenOptions = newOptions;
    }
}
