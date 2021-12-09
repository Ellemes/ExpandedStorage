package ninjaphenix.expandedstorage.wrappers;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;
import ninjaphenix.expandedstorage.Utils;

import java.util.function.Supplier;

final class PlatformUtilsImpl implements PlatformUtils {
    private static PlatformUtilsImpl INSTANCE;
    private final boolean isClient;

    private PlatformUtilsImpl() {
        isClient = FMLLoader.getDist() == Dist.CLIENT;
    }

    public static PlatformUtilsImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlatformUtilsImpl();
        }
        return INSTANCE;
    }

    @Override
    public CreativeModeTab createTab(Supplier<ItemStack> icon) {
        return new CreativeModeTab(Utils.MOD_ID) {
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
