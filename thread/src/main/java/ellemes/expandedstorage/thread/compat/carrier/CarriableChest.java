package ellemes.expandedstorage.thread.compat.carrier;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import me.steven.carrier.api.CarrierComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

final class CarriableChest extends CarriableOldChest {
    public CarriableChest(ResourceLocation id, Block parent) {
        super(id, parent);
    }

    @Override
    protected void preRenderBlock(Player player, CarrierComponent component, PoseStack stack, MultiBufferSource consumer, float delta, int light) {
        stack.translate(0.5D, 0.5D, 0.5D);
        stack.mulPose(Vector3f.YN.rotationDegrees(180.0F));
        stack.translate(-0.5D, -0.5D, -0.5D);
    }
}
