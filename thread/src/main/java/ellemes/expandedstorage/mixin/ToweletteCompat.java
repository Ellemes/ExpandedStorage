package ellemes.expandedstorage.mixin;

import ellemes.expandedstorage.block.ChestBlock;
import ellemes.expandedstorage.block.MiniChestBlock;
import org.spongepowered.asm.mixin.Mixin;
import virtuoel.towelette.api.Fluidloggable;

@SuppressWarnings("unused")
@Mixin({ChestBlock.class, MiniChestBlock.class})
public abstract class ToweletteCompat implements Fluidloggable {
}
