package ninjaphenix.expandedstorage.base.wrappers;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import ninjaphenix.expandedstorage.base.internal_api.inventory.SyncedMenuFactory;

import java.util.Set;

public interface NetworkWrapper {
    static NetworkWrapper getInstance() {
        return NetworkWrapperImpl.getInstance();
    }

    void initialise();

    boolean isValidScreenType(ResourceLocation screenType);

    void c2s_sendTypePreference(ResourceLocation selection);

    void s_setPlayerScreenType(ServerPlayer player, ResourceLocation selection);

    Set<ResourceLocation> getScreenOptions();

    void c_openInventoryAt(BlockPos pos);

    void c_openInventoryAt(BlockPos pos, ResourceLocation selection);
}
