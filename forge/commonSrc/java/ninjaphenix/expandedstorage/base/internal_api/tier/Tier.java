package ninjaphenix.expandedstorage.base.internal_api.tier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.function.UnaryOperator;

@Internal
@Experimental
@SuppressWarnings("ClassCanBeRecord")
public class Tier {
    private final ResourceLocation id;
    private final UnaryOperator<Item.Properties> itemProperties;
    private final UnaryOperator<BlockBehaviour.Properties> blockProperties;
    private final int slots;

    public Tier(ResourceLocation id, int slots, UnaryOperator<BlockBehaviour.Properties> blockProperties,
                UnaryOperator<Item.Properties> itemProperties) {
        this.id = id;
        this.slots = slots;
        this.itemProperties = itemProperties;
        this.blockProperties = blockProperties;
    }

    public final ResourceLocation getId() {
        return id;
    }

    public final UnaryOperator<Item.Properties> getItemProperties() {
        return itemProperties;
    }

    public UnaryOperator<BlockBehaviour.Properties> getBlockProperties() {
        return blockProperties;
    }

    public final int getSlotCount() {
        return slots;
    }
}


