package ellemes.expandedstorage.item;

import ellemes.expandedstorage.CommonMain;
import ellemes.expandedstorage.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class StorageConversionKit extends Item {
    private final ResourceLocation from;
    private final ResourceLocation to;
    private final Component instructionsFirst;
    private final Component instructionsSecond;

    public StorageConversionKit(Properties settings, ResourceLocation from, ResourceLocation to, boolean manuallyWrapTooltips) {
        super(settings);
        this.from = from;
        this.to = to;
        if (manuallyWrapTooltips) {
            this.instructionsFirst = Component.translatable("tooltip.expandedstorage.conversion_kit_" + from.getPath() + "_" + to.getPath() + "_1", Utils.ALT_USE).withStyle(ChatFormatting.GRAY);
            this.instructionsSecond = Component.translatable("tooltip.expandedstorage.conversion_kit_" + from.getPath() + "_" + to.getPath() + "_2", Utils.ALT_USE).withStyle(ChatFormatting.GRAY);
        } else {
            this.instructionsFirst = Component.translatable("tooltip.expandedstorage.conversion_kit_" + from.getPath() + "_" + to.getPath() + "_1", Utils.ALT_USE).withStyle(ChatFormatting.GRAY).append(Component.translatable("tooltip.expandedstorage.conversion_kit_" + from.getPath() + "_" + to.getPath() + "_2", Utils.ALT_USE).withStyle(ChatFormatting.GRAY));
            this.instructionsSecond = Component.literal("");
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        Player player = context.getPlayer();
        if (player != null) {
            if (player.isShiftKeyDown()) {
                Block block = world.getBlockState(context.getClickedPos()).getBlock();
                BlockUpgradeBehaviour behaviour = CommonMain.getBlockUpgradeBehaviour(block);
                if (behaviour != null) {
                    if (world.isClientSide()) {
                        return InteractionResult.CONSUME;
                    } else if (behaviour.tryUpgradeBlock(context, from, to)) {
                        return InteractionResult.SUCCESS;
                    }
                    player.getCooldowns().addCooldown(this, Utils.QUARTER_SECOND);
                }
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag context) {
        list.add(instructionsFirst);
        if (!instructionsSecond.getString().equals("")) {
            list.add(instructionsSecond);
        }
    }
}
