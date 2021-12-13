package ninjaphenix.expandedstorage.wrappers;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import ninjaphenix.expandedstorage.Utils;

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
    public CreativeModeTab createTab() {
        FabricItemGroupBuilder.build(new ResourceLocation("dummy"), null); // Fabric API is dumb.
        return new CreativeModeTab(CreativeModeTab.TABS.length - 1, Utils.MOD_ID) {
            @Override
            public ItemStack makeIcon() {
                return new ItemStack(Registry.ITEM.get(Utils.id("netherite_chest")));
            }
        };
    }

    @Override
    public boolean isClient() {
        return isClient;
    }
}
