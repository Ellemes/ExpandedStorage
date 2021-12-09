package ninjaphenix.expandedstorage.mixin.chest;

import ninjaphenix.expandedstorage.chest.block.ChestBlock;
import org.spongepowered.asm.mixin.Mixin;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(ChestBlock.class)
public abstract class ToweletteSupport implements Fluidloggable {
}
