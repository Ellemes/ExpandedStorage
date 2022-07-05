package ellemes.expandedstorage.item;

import ellemes.expandedstorage.entity.ChestMinecart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class ChestMinecartItem extends Item {
    private static final DispenseItemBehavior DISPENSER_BEHAVIOR = new DefaultDispenseItemBehavior() {
        private final DispenseItemBehavior defaultDispenserBehaviour = new DefaultDispenseItemBehavior();
        @Override
        public ItemStack execute(BlockSource block, ItemStack stack) {
            Direction forward = block.getBlockState().getValue(DispenserBlock.FACING);
            double x = block.x() + forward.getStepX() * 1.125D;
            double y = Math.floor(block.y()) + forward.getStepY();
            double z = block.z() + forward.getStepZ() * 1.125D;
            BlockPos frontPos = block.getPos().relative(forward);
            Level level = block.getLevel();
            BlockState frontState = level.getBlockState(frontPos);
            double railHeight = -2;
            if (BaseRailBlock.isRail(frontState)) {
                RailShape railShape = frontState.getValue(((BaseRailBlock) frontState.getBlock()).getShapeProperty());
                railHeight = railShape.isAscending() ? 0.5 : 0;
            } else if (frontState.isAir()) {
                BlockState belowFrontState = level.getBlockState(frontPos.below());
                if (BaseRailBlock.isRail(belowFrontState)) {
                    RailShape railShape = belowFrontState.getValue(((BaseRailBlock) belowFrontState.getBlock()).getShapeProperty());
                    railHeight = forward != Direction.DOWN && railShape.isAscending() ? -0.5 : -1;
                }
            }
            if (railHeight == -2) { // No rail detected
                this.defaultDispenserBehaviour.dispense(block, stack);
            } else {
                Vec3 posVec = new Vec3(x, y +  railHeight + 0.0625, z);
                ChestMinecart cart = ChestMinecart.createMinecart(level, posVec, ((ChestMinecartItem) stack.getItem()).cartId); // 1/16th of a block above the rail
                if (stack.hasCustomHoverName()) {
                    cart.setCustomName(stack.getHoverName());
                }
                level.addFreshEntity(cart);
            }
            stack.shrink(1);
            return stack;
        }
    };

    private final ResourceLocation cartId;

    public ChestMinecartItem(Item.Properties properties, ResourceLocation cartId) {
        super(properties);
        this.cartId = cartId;
        DispenserBlock.registerBehavior(this, ChestMinecartItem.DISPENSER_BEHAVIOR);
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
