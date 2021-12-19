package ninjaphenix.expandedstorage.mixin;

import ninjaphenix.expandedstorage.block.ChestBlock;
import ninjaphenix.expandedstorage.block.MiniChestBlock;
import org.spongepowered.asm.mixin.Mixin;
import virtuoel.towelette.api.Fluidloggable;

@Mixin({ChestBlock.class, MiniChestBlock.class})
public abstract class ToweletteSupport implements Fluidloggable {
}
