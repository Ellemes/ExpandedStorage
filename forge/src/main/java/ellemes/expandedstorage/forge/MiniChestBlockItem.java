package ellemes.expandedstorage.forge;

import ellemes.expandedstorage.block.MiniChestBlock;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class MiniChestBlockItem extends BlockItem {
    public MiniChestBlockItem(MiniChestBlock block, Properties settings) {
        super(block, settings);
    }

    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }
}
