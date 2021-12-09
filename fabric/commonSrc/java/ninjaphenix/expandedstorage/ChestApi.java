package ninjaphenix.expandedstorage;

import net.minecraft.resources.ResourceLocation;
import ninjaphenix.expandedstorage.block.misc.CursedChestType;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

/**
 * Note this API is client side only, never call it on a dedicated server.
 */
@Experimental
@Internal
public interface ChestApi {
    ChestApi INSTANCE = ChestImpl.getInstance();

    void declareChestTextures(ResourceLocation block,
                              ResourceLocation singleTexture,
                              ResourceLocation leftTexture,
                              ResourceLocation rightTexture,
                              ResourceLocation topTexture,
                              ResourceLocation bottomTexture,
                              ResourceLocation frontTexture,
                              ResourceLocation backTexture);

    ResourceLocation getChestTexture(ResourceLocation block, CursedChestType chestType);
}
