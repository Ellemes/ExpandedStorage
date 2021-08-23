package ninjaphenix.expandedstorage.base.wrappers;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import ninjaphenix.expandedstorage.base.client.menu.PickScreen;
import ninjaphenix.expandedstorage.base.internal_api.Utils;
import ninjaphenix.expandedstorage.base.internal_api.inventory.ServerMenuFactory;
import ninjaphenix.expandedstorage.base.inventory.PagedMenu;
import ninjaphenix.expandedstorage.base.inventory.ScrollableMenu;
import ninjaphenix.expandedstorage.base.inventory.SingleMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

final class NetworkWrapperImpl implements NetworkWrapper {
    private static final ResourceLocation UPDATE_PLAYER_PREFERENCE = Utils.resloc("update_player_preference");
    private static NetworkWrapperImpl INSTANCE;
    private final Map<UUID, ResourceLocation> playerPreferences = new HashMap<>();
    private final Map<ResourceLocation, ServerMenuFactory> menuFactories = Utils.unmodifiableMap(map -> {
        map.put(Utils.SINGLE_SCREEN_TYPE, SingleMenu::new);
        map.put(Utils.SCROLLABLE_SCREEN_TYPE, ScrollableMenu::new);
        map.put(Utils.PAGED_SCREEN_TYPE, PagedMenu::new);
    });

    public static NetworkWrapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NetworkWrapperImpl();
        }
        return INSTANCE;
    }

    public void initialise() {
        if (PlatformUtils.getInstance().isClient()) {
            new Client().initialise();
        }
        // Register Server Receivers
        ServerPlayConnectionEvents.INIT.register((listener_init, server_unused) -> {
            ServerPlayNetworking.registerReceiver(listener_init, NetworkWrapperImpl.UPDATE_PLAYER_PREFERENCE, this::s_handleUpdatePlayerPreference);
        });
        ServerPlayConnectionEvents.DISCONNECT.register((listener, server) -> this.s_setPlayerScreenType(listener.player, Utils.UNSET_SCREEN_TYPE));
    }

    public void c2s_setSendTypePreference(ResourceLocation selection) {
        if (ConfigWrapper.getInstance().setPreferredScreenType(selection)) {
            this.c2s_sendTypePreference(selection);
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

    private void s_handleUpdatePlayerPreference(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener,
                                                FriendlyByteBuf buffer, PacketSender sender) {
        ResourceLocation screenType = buffer.readResourceLocation();
        server.submit(() -> this.s_setPlayerScreenType(player, screenType));
    }

    public boolean isValidScreenType(ResourceLocation screenType) {
        return screenType != null && menuFactories.containsKey(screenType);
    }

    @Override
    public void c2s_sendTypePreference(ResourceLocation selection) {
        if (ClientPlayNetworking.canSend(NetworkWrapperImpl.UPDATE_PLAYER_PREFERENCE)) {
            ClientPlayNetworking.send(NetworkWrapperImpl.UPDATE_PLAYER_PREFERENCE, new FriendlyByteBuf(Unpooled.buffer()).writeResourceLocation(selection));
        }
    }

    @Override
    public void openInventoryAt(Level level, BlockPos pos) {
        if (ConfigWrapper.getInstance().getPreferredScreenType() == Utils.UNSET_SCREEN_TYPE) {
            Minecraft.getInstance().setScreen(new PickScreen(menuFactories.keySet(), null, (preference) -> {
                this.c2s_setSendTypePreference(preference);
                this.c2s_openInventoryAt(level, pos);
            }));
        } else {
            this.c2s_openInventoryAt(level, pos);
        }
    }

    private void c2s_openInventoryAt(Level level, BlockPos pos) {

    }

    private static class Client {
        public void initialise() {
            ClientPlayConnectionEvents.JOIN.register((listener_play, sender, client) -> sender.sendPacket(NetworkWrapperImpl.UPDATE_PLAYER_PREFERENCE, new FriendlyByteBuf(Unpooled.buffer()).writeResourceLocation(ConfigWrapper.getInstance().getPreferredScreenType())));
        }
    }
}
