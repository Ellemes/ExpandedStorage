package ninjaphenix.expandedstorage.base.wrappers;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import ninjaphenix.expandedstorage.base.internal_api.Utils;
import ninjaphenix.expandedstorage.base.internal_api.block.AbstractOpenableStorageBlock;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.AbstractOpenableStorageBlockEntity;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.AbstractStorageBlockEntity;
import ninjaphenix.expandedstorage.base.internal_api.inventory.ServerMenuFactory;
import ninjaphenix.expandedstorage.base.inventory.PagedMenu;
import ninjaphenix.expandedstorage.base.inventory.ScrollableMenu;
import ninjaphenix.expandedstorage.base.inventory.SingleMenu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public abstract class NetworkWrapper {
    final Map<UUID, ResourceLocation> playerPreferences = new HashMap<>();
    final Map<ResourceLocation, ServerMenuFactory> menuFactories = Utils.unmodifiableMap(map -> {
        map.put(Utils.SINGLE_SCREEN_TYPE, SingleMenu::new);
        map.put(Utils.SCROLLABLE_SCREEN_TYPE, ScrollableMenu::new);
        map.put(Utils.PAGED_SCREEN_TYPE, PagedMenu::new);
    });

    public static NetworkWrapper getInstance() {
        return NetworkWrapperImpl.getInstance();
    }

    public abstract void initialise();

    public abstract void c2s_sendTypePreference(ResourceLocation selection);

    public final void s_setPlayerScreenType(ServerPlayer player, ResourceLocation selection) {
        UUID uuid = player.getUUID();
        if (menuFactories.containsKey(selection)) {
            playerPreferences.put(uuid, selection);
        } else {
            playerPreferences.remove(uuid);
        }
    }

    public abstract Set<ResourceLocation> getScreenOptions();

    public abstract void c_openInventoryAt(BlockPos pos);

    public abstract void c_openInventoryAt(BlockPos pos, ResourceLocation selection);

    private static Component getDisplayName(List<? extends AbstractStorageBlockEntity> inventories) {
        for (AbstractStorageBlockEntity inventory : inventories) {
            if (inventory.hasCustomName()) {
                return inventory.getName();
            }
        }
        return switch (inventories.size()) {
            case 1 -> inventories.get(0).getName();
            case 2 -> Utils.translation("container.expandedstorage.generic_double", inventories.get(0).getName());
            default -> throw new IllegalStateException("Inventory size too large, must be either 1 or 2.");
        };
    }

    protected final void openMenuIfAllowed(BlockPos pos, ServerPlayer player) {
        UUID uuid = player.getUUID();
        ResourceLocation playerPreference;
        if (playerPreferences.containsKey(uuid) && menuFactories.containsKey(playerPreference = playerPreferences.get(uuid))) {
            var level = player.getLevel();
            var state = level.getBlockState(pos);
            if (state.getBlock() instanceof AbstractOpenableStorageBlock block) {
                var inventories = block.getInventoryParts(level, state, pos);
                if (inventories.size() == 1 || inventories.size() == 2) {
                    var displayName = NetworkWrapper.getDisplayName(inventories);
                    if (player.containerMenu == null || player.containerMenu == player.inventoryMenu) {
                        if (inventories.stream().allMatch(entity -> entity.canPlayerInteractWith(player))) {
                            block.awardOpeningStat(player);
                        } else {
                            player.displayClientMessage(new TranslatableComponent("container.isLocked", displayName), true);
                            player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
                            return;
                        }
                    }
                    for (AbstractOpenableStorageBlockEntity entity : inventories) {
                        if (!entity.canContinueUse(player)) {
                            return;
                        }
                    }
                    Container container = switch (inventories.size()) {
                        case 1 -> inventories.get(0).getContainerWrapper();
                        case 2 -> new CompoundContainer(inventories.get(0).getContainerWrapper(), inventories.get(1).getContainerWrapper());
                        default -> throw new IllegalStateException("Inventory size too large, must be either 1 or 2.");
                    };
                    this.openMenu(player, inventories.get(0).getBlockPos(), container, menuFactories.get(playerPreference), displayName);
                }
            }
        }
    }

    protected abstract void openMenu(ServerPlayer player, BlockPos pos, Container container, ServerMenuFactory factory, Component displayName);
}
