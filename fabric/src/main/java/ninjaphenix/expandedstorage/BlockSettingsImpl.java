package ninjaphenix.expandedstorage;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.state.BlockBehaviour;
import ninjaphenix.expandedstorage.registration.BlockSettings;

public class BlockSettingsImpl extends BlockSettings {
    public BlockSettingsImpl(BlockBehaviour.Properties settings) {
        super(FabricBlockSettings.copyOf(settings));
    }

    @Override
    public BlockSettings setMiningData(String tool, int miningLevel) {
        if (tool.equals("axe")) {
            ((FabricBlockSettings) blockSettings).breakByTool(FabricToolTags.AXES, miningLevel);
        } else if (tool.equals("pickaxe")) {
            ((FabricBlockSettings) blockSettings).breakByTool(FabricToolTags.PICKAXES, miningLevel);
        }
        return this;
    }
}
