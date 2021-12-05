package ninjaphenix.expandedstorage.data.providers;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import ninjaphenix.expandedstorage.Utils;

public final class BlockStatesAndModels extends BlockStateProvider {
    public BlockStatesAndModels(DataGenerator generator, ExistingFileHelper fileHelper) {
        super(generator, Utils.MOD_ID, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }

    @Override
    public String getName() {
        return "Expanded Storage - BlockStates / Models";
    }
}
