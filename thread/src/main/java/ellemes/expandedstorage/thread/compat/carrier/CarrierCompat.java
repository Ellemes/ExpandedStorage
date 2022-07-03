package ellemes.expandedstorage.thread.compat.carrier;

import ellemes.expandedstorage.block.AbstractChestBlock;
import ellemes.expandedstorage.block.ChestBlock;
import me.steven.carrier.api.CarriableRegistry;

public final class CarrierCompat {
    public static void registerChestBlock(ChestBlock block) {
        CarriableRegistry.INSTANCE.register(block.getBlockId(), new CarriableChest(block.getBlockId(), block));
    }

    public static void registerOldChestBlock(AbstractChestBlock block) {
        CarriableRegistry.INSTANCE.register(block.getBlockId(), new CarriableOldChest(block.getBlockId(), block));
    }
}
