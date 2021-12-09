package ninjaphenix.expandedstorage.item;

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
import ninjaphenix.expandedstorage.BaseApi;
import ninjaphenix.expandedstorage.Utils;
import ninjaphenix.expandedstorage.item.BlockUpgradeBehaviour;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public final class StorageConversionKit extends Item {
    private final ResourceLocation from;
    private final ResourceLocation to;
    private final Component instructionsFirst;
    private final Component instructionsSecond;

    public StorageConversionKit(Properties properties, ResourceLocation from, ResourceLocation to) {
        super(properties);
        this.from = from;
        this.to = to;
        this.instructionsFirst = Utils.translation("tooltip.expandedstorage.conversion_kit_" + from.getPath() + "_" + to.getPath() + "_1", Utils.ALT_USE)
                                      .withStyle(ChatFormatting.GRAY);
        this.instructionsSecond = Utils.translation("tooltip.expandedstorage.conversion_kit_" + from.getPath() + "_" + to.getPath() + "_2", Utils.ALT_USE)
                                       .withStyle(ChatFormatting.GRAY);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (player != null && player.isShiftKeyDown()) {
            Block block = level.getBlockState(context.getClickedPos()).getBlock();
            Optional<BlockUpgradeBehaviour> maybeBehaviour = BaseApi.getInstance().getBlockUpgradeBehaviour(block);
            if (maybeBehaviour.isPresent()) {
                if (level.isClientSide()) {
                    return InteractionResult.CONSUME;
                } else if (maybeBehaviour.get().tryUpgradeBlock(context, from, to)) {
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(instructionsFirst);
        if (!instructionsSecond.getString().equals("")) {
            list.add(instructionsSecond);
        }
    }
}
