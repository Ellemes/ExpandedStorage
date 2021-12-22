package ninjaphenix.expandedstorage;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.ToolType;
import ninjaphenix.expandedstorage.registration.BlockSettings;

public class BlockSettingsImpl extends BlockSettings {
    public BlockSettingsImpl(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public BlockSettings setMiningData(String tool, int miningLevel) {
        blockSettings.harvestTool(ToolType.get(tool));
        blockSettings.harvestLevel(miningLevel);
        return this;
    }
}
