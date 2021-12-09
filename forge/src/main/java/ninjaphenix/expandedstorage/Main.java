package ninjaphenix.expandedstorage;

import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;

@Mod("expandedstorage")
public final class Main {
    public Main() {
        BaseCommon.initialize();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addGenericListener(Item.class, (RegistryEvent.Register<Item> event) -> {
            IForgeRegistry<Item> registry = event.getRegistry();
            BaseApi.getInstance().getAndClearItems().forEach((key, value) -> registry.register(value.setRegistryName(key)));
        });

        BarrelMain.initialize();
        ChestMain.initialize();
        OldChestMain.initialize();
    }
}
