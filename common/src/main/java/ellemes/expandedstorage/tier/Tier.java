package ellemes.expandedstorage.tier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.UnaryOperator;

public final class Tier {
    private final ResourceLocation id;
    private final UnaryOperator<Item.Properties> itemSettings;
    private final UnaryOperator<BlockBehaviour.Properties> blockSettings;
    private final int slots;

    public Tier(ResourceLocation id, int slots, UnaryOperator<BlockBehaviour.Properties> blockSettings, UnaryOperator<Item.Properties> itemSettings) {
        this.id = id;
        this.slots = slots;
        this.blockSettings = blockSettings;
        this.itemSettings = itemSettings;
    }

    public ResourceLocation getId() {
        return id;
    }

    public UnaryOperator<Item.Properties> getItemSettings() {
        return itemSettings;
    }

    public UnaryOperator<BlockBehaviour.Properties> getBlockSettings() {
        return blockSettings;
    }

    public int getSlotCount() {
        return slots;
    }
}


