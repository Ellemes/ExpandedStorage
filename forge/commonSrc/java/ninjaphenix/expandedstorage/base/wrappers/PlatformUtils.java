package ninjaphenix.expandedstorage.base.wrappers;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public interface PlatformUtils {
    static PlatformUtils getInstance() {
        return PlatformUtilsImpl.getInstance();
    }

    CreativeModeTab createTab(Supplier<ItemStack> icon);

    boolean isClient();
}
