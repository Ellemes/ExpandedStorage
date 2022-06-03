package ellemes.expandedstorage;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class TagReloadListener {
    public static final TagKey<Block> chestCycle = TagKey.create(Registry.BLOCK_REGISTRY, Utils.id("chest_cycle"));
    public static final TagKey<Block> miniChestCycle = TagKey.create(Registry.BLOCK_REGISTRY, Utils.id("mini_chest_cycle"));
    public static final TagKey<Block> miniChestSecretCycle = TagKey.create(Registry.BLOCK_REGISTRY, Utils.id("mini_chest_secret_cycle"));
    public static final TagKey<Block> miniChestSecretCycle2 = TagKey.create(Registry.BLOCK_REGISTRY, Utils.id("mini_chest_secret_cycle_2"));
    private List<Block> chestCycleBlocks = null;
    private List<Block> miniChestCycleBlocks = null;
    private List<Block> miniChestSecretCycleBlocks = null;
    private List<Block> miniChestSecretCycle2Blocks = null;

    public void postDataReload() {
        chestCycleBlocks = Registry.BLOCK.getOrCreateTag(chestCycle).stream().map(Holder::value).toList();
        miniChestCycleBlocks = Registry.BLOCK.getOrCreateTag(miniChestCycle).stream().map(Holder::value).toList();
        miniChestSecretCycleBlocks = Registry.BLOCK.getOrCreateTag(miniChestSecretCycle).stream().map(Holder::value).toList();
        miniChestSecretCycle2Blocks = Registry.BLOCK.getOrCreateTag(miniChestSecretCycle2).stream().map(Holder::value).toList();
    }

    public List<Block> getChestCycleBlocks() {
        if (chestCycleBlocks == null) { // In case no reload has happened yet, any better way to do this?
            this.postDataReload();
        }
        return chestCycleBlocks;
    }

    public List<Block> getMiniChestCycleBlocks() {
        if (miniChestCycleBlocks == null) { // In case no reload has happened yet, any better way to do this?
            this.postDataReload();
        }
        return miniChestCycleBlocks;
    }

    public List<Block> getMiniChestSecretCycleBlocks() {
        if (miniChestSecretCycleBlocks == null) { // In case no reload has happened yet, any better way to do this?
            this.postDataReload();
        }
        return miniChestSecretCycleBlocks;
    }

    public List<Block> getMiniChestSecretCycle2Blocks() {
        if (miniChestSecretCycle2Blocks == null) { // In case no reload has happened yet, any better way to do this?
            this.postDataReload();
        }
        return miniChestSecretCycle2Blocks;
    }
}
