package ellemes.expandedstorage.registration;

import ellemes.expandedstorage.block.AbstractChestBlock;
import ellemes.expandedstorage.block.BarrelBlock;
import ellemes.expandedstorage.block.ChestBlock;
import ellemes.expandedstorage.block.MiniChestBlock;
import ellemes.expandedstorage.block.OpenableBlock;
import ellemes.expandedstorage.block.entity.BarrelBlockEntity;
import ellemes.expandedstorage.block.entity.ChestBlockEntity;
import ellemes.expandedstorage.block.entity.MiniChestBlockEntity;
import ellemes.expandedstorage.block.entity.OldChestBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;

public class Content {
    private final List<ResourceLocation> stats;
//    private final List<NamedValue<Item>> baseItems;

    private final List<NamedValue<ChestBlock>> chestBlocks;
    private final List<NamedValue<BlockItem>> chestItems;
    private final NamedValue<BlockEntityType<ChestBlockEntity>> chestBlockEntityType;

    private final List<NamedValue<AbstractChestBlock>> oldChestBlocks;
//    private final List<NamedValue<BlockItem>> oldChestItems;
    private final NamedValue<BlockEntityType<OldChestBlockEntity>> oldChestBlockEntityType;

    private final List<NamedValue<BarrelBlock>> barrelBlocks;
//    private final List<NamedValue<BlockItem>> barrelItems;
    private final NamedValue<BlockEntityType<BarrelBlockEntity>> barrelBlockEntityType;

//    private final List<NamedValue<MiniChestBlock>> miniChestBlocks;
//    private final List<NamedValue<BlockItem>> miniChestItems;
    private final NamedValue<BlockEntityType<MiniChestBlockEntity>> miniChestBlockEntityType;

    private final List<NamedValue<? extends OpenableBlock>> blocks;
    private final List<NamedValue<? extends Item>> items;

    public Content(
            List<ResourceLocation> stats,
            List<NamedValue<Item>> baseItems,

            List<NamedValue<ChestBlock>> chestBlocks,
            List<NamedValue<BlockItem>> chestItems,
            NamedValue<BlockEntityType<ChestBlockEntity>> chestBlockEntityType,

            List<NamedValue<AbstractChestBlock>> oldChestBlocks,
            List<NamedValue<BlockItem>> oldChestItems,
            NamedValue<BlockEntityType<OldChestBlockEntity>> oldChestBlockEntityType,

            List<NamedValue<BarrelBlock>> barrelBlocks,
            List<NamedValue<BlockItem>> barrelItems,
            NamedValue<BlockEntityType<BarrelBlockEntity>> barrelBlockEntityType,

            List<NamedValue<MiniChestBlock>> miniChestBlocks,
            List<NamedValue<BlockItem>> miniChestItems,
            NamedValue<BlockEntityType<MiniChestBlockEntity>> miniChestBlockEntityType
    ) {
        this.stats = stats;
//        this.baseItems = baseItems;

        this.chestBlocks = chestBlocks;
        this.chestItems = chestItems;
        this.chestBlockEntityType = chestBlockEntityType;

        this.oldChestBlocks = oldChestBlocks;
//        this.oldChestItems = oldChestItems;
        this.oldChestBlockEntityType = oldChestBlockEntityType;

        this.barrelBlocks = barrelBlocks;
//        this.barrelItems = barrelItems;
        this.barrelBlockEntityType = barrelBlockEntityType;

//        this.miniChestBlocks = miniChestBlocks;
//        this.miniChestItems = miniChestItems;
        this.miniChestBlockEntityType = miniChestBlockEntityType;

        this.blocks = new ArrayList<>();
        blocks.addAll(chestBlocks);
        blocks.addAll(oldChestBlocks);
        blocks.addAll(barrelBlocks);
        blocks.addAll(miniChestBlocks);

        this.items = new ArrayList<>();
        items.addAll(baseItems);
        items.addAll(chestItems);
        items.addAll(oldChestItems);
        items.addAll(barrelItems);
        items.addAll(miniChestItems);
    }

    public List<ResourceLocation> getStats() {
        return stats;
    }

//    public List<NamedValue<Item>> getBaseItems() {
//        return baseItems;
//    }

    public List<NamedValue<ChestBlock>> getChestBlocks() {
        return chestBlocks;
    }

    public List<NamedValue<BlockItem>> getChestItems() {
        return chestItems;
    }

    public NamedValue<BlockEntityType<ChestBlockEntity>> getChestBlockEntityType() {
        return chestBlockEntityType;
    }

    public List<NamedValue<AbstractChestBlock>> getOldChestBlocks() {
        return oldChestBlocks;
    }

//    public List<NamedValue<BlockItem>> getOldChestItems() {
//        return oldChestItems;
//    }

    public NamedValue<BlockEntityType<OldChestBlockEntity>> getOldChestBlockEntityType() {
        return oldChestBlockEntityType;
    }

    public List<NamedValue<BarrelBlock>> getBarrelBlocks() {
        return barrelBlocks;
    }

//    public List<NamedValue<BlockItem>> getBarrelItems() {
//        return barrelItems;
//    }

    public NamedValue<BlockEntityType<BarrelBlockEntity>> getBarrelBlockEntityType() {
        return barrelBlockEntityType;
    }

//    public List<NamedValue<MiniChestBlock>> getMiniChestBlocks() {
//        return miniChestBlocks;
//    }

//    public List<NamedValue<BlockItem>> getMiniChestItems() {
//        return miniChestItems;
//    }

    public NamedValue<BlockEntityType<MiniChestBlockEntity>> getMiniChestBlockEntityType() {
        return miniChestBlockEntityType;
    }

    public List<NamedValue<? extends OpenableBlock>> getBlocks() {
        return blocks;
    }

    public List<NamedValue<? extends Item>> getItems() {
        return items;
    }
}
