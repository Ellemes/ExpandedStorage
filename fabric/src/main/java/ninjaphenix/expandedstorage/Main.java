package ninjaphenix.expandedstorage;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;

public final class Main implements ModInitializer {
    @Override
    public void onInitialize() {
        BaseCommon.initialize();
        BaseApi.getInstance().getAndClearItems().forEach((id, item) -> Registry.register(Registry.ITEM, id, item));

        BarrelMain.initialize();
        ChestMain.initialize();
        OldChestMain.initialize();
    }
}
