package ellemes.expandedstorage.entity;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ChestMinecart extends AbstractMinecart {
    private final Item dropItem;

    public ChestMinecart(EntityType<?> entityType, Level level, Item dropItem) {
        super(entityType, level);
        this.dropItem = dropItem;
    }

    @Override
    protected Item getDropItem() {
        return dropItem;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand interactionHand) {
        return super.interact(player, interactionHand);
    }

    @Override
    public Type getMinecartType() {
        return Type.CHEST;
    }

    public static ChestMinecart createMinecart(Level level, Vec3 pos, ResourceLocation cartItemId) {
        ChestMinecart cart = (ChestMinecart) Registry.ENTITY_TYPE.get(cartItemId).create(level);
        cart.setPos(pos);
        return cart;
    }
}
