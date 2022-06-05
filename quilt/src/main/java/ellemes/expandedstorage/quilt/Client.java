package ellemes.expandedstorage.quilt;

import ellemes.expandedstorage.Utils;
import net.minecraft.network.chat.TextComponent;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType;

public class Client implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        ResourceLoader.registerBuiltinResourcePack(Utils.id("legacy_textures"), mod, ResourcePackActivationType.NORMAL, new TextComponent("ES Legacy Textures"));
    }
}
