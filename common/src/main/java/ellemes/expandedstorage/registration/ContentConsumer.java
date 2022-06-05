package ellemes.expandedstorage.registration;

import ellemes.expandedstorage.block.AbstractChestBlock;
import ellemes.expandedstorage.block.BarrelBlock;
import ellemes.expandedstorage.block.ChestBlock;
import ellemes.expandedstorage.block.MiniChestBlock;
import ellemes.expandedstorage.block.entity.BarrelBlockEntity;
import ellemes.expandedstorage.block.entity.ChestBlockEntity;
import ellemes.expandedstorage.block.entity.MiniChestBlockEntity;
import ellemes.expandedstorage.block.entity.OldChestBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;

public interface ContentConsumer {
    void accept(List<ResourceLocation> stats, List<NamedValue<Item>> baseContent,
                List<NamedValue<ChestBlock>> chestBlocks, List<NamedValue<BlockItem>> chestItems, NamedValue<BlockEntityType<ChestBlockEntity>> chestBlockEntityType,
                List<NamedValue<AbstractChestBlock>> oldChestBlocks, List<NamedValue<BlockItem>> oldChestItems, NamedValue<BlockEntityType<OldChestBlockEntity>> oldChestBlockEntityType,
                List<NamedValue<BarrelBlock>> barrelBlocks, List<NamedValue<BlockItem>> barrelItems, NamedValue<BlockEntityType<BarrelBlockEntity>> barrelBlockEntityType,
                List<NamedValue<MiniChestBlock>> miniChestBlocks, List<NamedValue<BlockItem>> miniChestItems, NamedValue<BlockEntityType<MiniChestBlockEntity>> miniChestBlockEntityType
    );
}
