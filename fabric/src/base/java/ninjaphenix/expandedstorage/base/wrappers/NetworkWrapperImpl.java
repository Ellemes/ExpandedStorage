package ninjaphenix.expandedstorage.base.wrappers;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import ninjaphenix.expandedstorage.base.client.menu.PickScreen;
import ninjaphenix.expandedstorage.base.internal_api.Utils;
import ninjaphenix.expandedstorage.base.internal_api.block.AbstractOpenableStorageBlock;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.AbstractOpenableStorageBlockEntity;
import ninjaphenix.expandedstorage.base.internal_api.inventory.ServerMenuFactory;
import ninjaphenix.expandedstorage.base.inventory.PagedMenu;
import ninjaphenix.expandedstorage.base.inventory.ScrollableMenu;
import ninjaphenix.expandedstorage.base.inventory.SingleMenu;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

final class NetworkWrapperImpl implements NetworkWrapper {
    private static final ResourceLocation UPDATE_PLAYER_PREFERENCE = Utils.resloc("update_player_preference");
    private static final ResourceLocation OPEN_INVENTORY = Utils.resloc("open_inventory");
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
            ServerPlayNetworking.registerReceiver(listener_init, NetworkWrapperImpl.OPEN_INVENTORY, this::s_handleOpenInventory);
        });
        ServerPlayConnectionEvents.DISCONNECT.register((listener, server) -> this.s_setPlayerScreenType(listener.player, Utils.UNSET_SCREEN_TYPE));
    }

    private void s_handleOpenInventory(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, FriendlyByteBuf buffer, PacketSender sender) {
        var pos = buffer.readBlockPos();
        var level = player.getLevel();
        server.execute(() -> {
            var state = level.getBlockState(pos);
            if (state.getBlock() instanceof AbstractOpenableStorageBlock block) {
                if (player.containerMenu == null || player.containerMenu == player.inventoryMenu) {
                    block.awardOpeningStat(player);
                }
                var inventories = block.getInventoryParts(level, state, pos);
                UUID uuid = player.getUUID();
                ResourceLocation playerPreference;
                if (playerPreferences.containsKey(uuid) && menuFactories.containsKey(playerPreference = playerPreferences.get(uuid))) {
                    if (inventories.size() == 1 || inventories.size() == 2) {
                        player.openMenu(new ExtendedScreenHandlerFactory() {
                            @Override
                            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                                buf.writeBlockPos(pos).writeInt(inventories.stream().mapToInt(AbstractOpenableStorageBlockEntity::getSlotCount).sum());
                            }

                            @Override
                            public Component getDisplayName() {
                                for (AbstractOpenableStorageBlockEntity inventory : inventories) {
                                    if (inventory.hasCustomName()) {
                                        return inventory.getName();
                                    }
                                }
                                if (inventories.size() == 1) {
                                    return inventories.get(0).getName();
                                } else if (inventories.size() == 2) {
                                    return Utils.translation("container.expandedstorage.generic_double", inventories.get(0).getName());
                                }
                                throw new IllegalStateException("inventories size is > 2");
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
                                    CompoundContainer container = new CompoundContainer(inventories.get(0).getContainerWrapper(), inventories.get(1).getContainerWrapper());
                                    return menuFactories.get(playerPreference).create(windowId, inventories.get(0).getBlockPos(), container, playerInventory, this.getDisplayName());
                                }
                                throw new IllegalStateException("inventories size is > 2");
                            }
                        });
                    }
                }
            }
        });
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
    public void c_openInventoryAt(BlockPos pos) {
        if (ConfigWrapper.getInstance().getPreferredScreenType() == Utils.UNSET_SCREEN_TYPE) {
            Minecraft.getInstance().setScreen(new PickScreen(menuFactories.keySet(), null, (preference) -> {
                this.c2s_setSendTypePreference(preference);
                Client.openInventoryAt(pos);
            }));
        } else {
            Client.openInventoryAt(pos);
        }
    }

    private static class Client {
        public void initialise() {
            ClientPlayConnectionEvents.JOIN.register((listener_play, sender, client) -> sender.sendPacket(NetworkWrapperImpl.UPDATE_PLAYER_PREFERENCE, new FriendlyByteBuf(Unpooled.buffer()).writeResourceLocation(ConfigWrapper.getInstance().getPreferredScreenType())));
        }

        public static void openInventoryAt(BlockPos pos) {
            if (ClientPlayNetworking.canSend(NetworkWrapperImpl.OPEN_INVENTORY)) {
                var buffer = new FriendlyByteBuf(Unpooled.buffer());
                buffer.writeBlockPos(pos);
                ClientPlayNetworking.send(NetworkWrapperImpl.OPEN_INVENTORY, buffer);
            }
        }
    }
}
