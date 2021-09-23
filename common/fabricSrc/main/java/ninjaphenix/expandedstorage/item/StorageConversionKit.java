package ninjaphenix.expandedstorage.item;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import ninjaphenix.expandedstorage.Common;
import ninjaphenix.expandedstorage.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class StorageConversionKit extends Item {
    private final Identifier from;
    private final Identifier to;
    private final Text instructionsFirst;
    private final Text instructionsSecond;

    public StorageConversionKit(Settings properties, Identifier from, Identifier to) {
        super(properties);
        this.from = from;
        this.to = to;
        this.instructionsFirst = Utils.translation("tooltip.expandedstorage.conversion_kit_" + from.getPath() + "_" + to.getPath() + "_1", Utils.ALT_USE).formatted(Formatting.GRAY);
        this.instructionsSecond = Utils.translation("tooltip.expandedstorage.conversion_kit_" + from.getPath() + "_" + to.getPath() + "_2", Utils.ALT_USE).formatted(Formatting.GRAY);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        if (player != null) {
            player.getItemCooldownManager().set(this, Utils.QUARTER_SECOND);
            if (player.isSneaking()) {
                Block block = world.getBlockState(context.getBlockPos()).getBlock();
                BlockUpgradeBehaviour behaviour = Common.getBlockUpgradeBehaviour(block);
                if (behaviour != null) {
                    if (world.isClient()) {
                        return ActionResult.CONSUME;
                    } else if (behaviour.tryUpgradeBlock(context, from, to)) {
                        return ActionResult.SUCCESS;
                    }
                }
            }
        }
        return ActionResult.FAIL;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World level, List<Text> list, TooltipContext flag) {
        list.add(instructionsFirst);
        if (!instructionsSecond.getString().equals("")) {
            list.add(instructionsSecond);
        }
    }
}
