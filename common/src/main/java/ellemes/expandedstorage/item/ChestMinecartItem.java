package ellemes.expandedstorage.item;

import ellemes.expandedstorage.entity.ChestMinecart;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class ChestMinecartItem extends Item {
    private final ResourceLocation cartId;

    public ChestMinecartItem(Item.Properties properties, ResourceLocation cartId) {
        super(properties);
        this.cartId = cartId;
        //DispenserBlock.registerBehavior(this, null);
    }

    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (!BaseRailBlock.isRail(state)) return InteractionResult.FAIL;
        ItemStack stack = context.getItemInHand();
        if (!level.isClientSide()) {
            RailShape railShape = state.getValue(((BaseRailBlock) state.getBlock()).getShapeProperty());
            double railHeight = railShape.isAscending() ? 0.5 : 0;
            Vec3 posVec = new Vec3(pos.getX() + 0.5, pos.getY() + railHeight + 0.0625, pos.getZ() + 0.5);
            ChestMinecart cart = ChestMinecart.createMinecart(level, posVec, cartId); // 1/16th of a block above the rail
            if (stack.hasCustomHoverName()) cart.setCustomName(stack.getHoverName());
            level.addFreshEntity(cart);
            level.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(context.getPlayer(), level.getBlockState(pos.below())));
        }
        stack.shrink(1);
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
