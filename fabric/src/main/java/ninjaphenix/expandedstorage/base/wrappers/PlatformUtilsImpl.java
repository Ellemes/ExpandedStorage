package ninjaphenix.expandedstorage.base.wrappers;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import ninjaphenix.expandedstorage.base.internal_api.Utils;

import java.util.function.Supplier;

public final class PlatformUtilsImpl implements PlatformUtils {
    private static PlatformUtilsImpl INSTANCE;
    private final boolean isClient;

    private PlatformUtilsImpl() {
        isClient = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    public static PlatformUtilsImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlatformUtilsImpl();
        }
        return INSTANCE;
    }

    @Override
    public CreativeModeTab createTab(Supplier<ItemStack> icon) {
        FabricItemGroupBuilder.build(new ResourceLocation("dummy"), null); // Fabric API is dumb.
        return new CreativeModeTab(CreativeModeTab.TABS.length - 1, Utils.MOD_ID) {
            @Override
            public ItemStack makeIcon() {
                return icon.get();
            }
        };
    }

    @Override
    public boolean isClient() {
        return isClient;
    }
}
