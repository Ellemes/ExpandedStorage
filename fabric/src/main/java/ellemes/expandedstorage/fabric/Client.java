package ellemes.expandedstorage.fabric;

import ellemes.expandedstorage.Utils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public class Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModContainer mod = FabricLoader.getInstance().getModContainer(Utils.MOD_ID).orElseThrow();
        ResourceManagerHelper.registerBuiltinResourcePack(Utils.id("legacy_textures"), mod, "ES Legacy Textures", ResourcePackActivationType.NORMAL);
    }
}
