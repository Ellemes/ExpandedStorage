package ninjaphenix.expandedstorage.registration;

import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class BlockSettings {
    protected final BlockBehaviour.Properties blockSettings;

    public BlockSettings(BlockBehaviour.Properties settings) {
        blockSettings = settings;
    }

    public abstract BlockSettings setMiningData(String tool, int miningLevel);

    public BlockBehaviour.Properties get() {
        return blockSettings;
    }
}
