package ellemes.expandedstorage.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.UseOnContext;

public interface BlockUpgradeBehaviour {
    boolean tryUpgradeBlock(UseOnContext context, ResourceLocation from, ResourceLocation to);
}
