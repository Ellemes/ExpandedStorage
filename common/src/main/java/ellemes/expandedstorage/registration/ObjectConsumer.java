package ellemes.expandedstorage.registration;

import ellemes.expandedstorage.tier.Tier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;

public interface ObjectConsumer {
    void apply(ResourceLocation id, ResourceLocation statId, Tier tier, BlockBehaviour.Properties settings);
}
