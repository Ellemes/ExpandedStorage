package ninjaphenix.expandedstorage.base.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import ninjaphenix.expandedstorage.base.client.menu.PickScreen;
import ninjaphenix.expandedstorage.base.wrappers.NetworkWrapper;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return returnToScreen -> new PickScreen(NetworkWrapper.getInstance().getScreenOptions(), returnToScreen, (selection) -> NetworkWrapper.getInstance().c2s_sendTypePreference(selection));
    }
}
