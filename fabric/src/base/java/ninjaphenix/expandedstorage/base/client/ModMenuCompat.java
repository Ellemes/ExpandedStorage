package ninjaphenix.expandedstorage.base.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.resources.ResourceLocation;
import ninjaphenix.expandedstorage.base.client.menu.PickScreen;
import ninjaphenix.expandedstorage.base.internal_api.Utils;
import ninjaphenix.expandedstorage.base.wrappers.ConfigWrapper;
import ninjaphenix.expandedstorage.base.wrappers.NetworkWrapper;

import java.util.HashSet;
import java.util.Set;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return returnToScreen -> {
            Set<ResourceLocation> values = new HashSet<>();
            values.add(Utils.SINGLE_SCREEN_TYPE);
            values.add(Utils.PAGED_SCREEN_TYPE);
            values.add(Utils.SCROLLABLE_SCREEN_TYPE);
            return new PickScreen(values, returnToScreen, (selection) -> {
                ConfigWrapper.getInstance().setPreferredScreenType(selection);
                NetworkWrapper.getInstance().c2s_sendTypePreference(selection);
            });
        };
    }
}
