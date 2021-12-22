package ninjaphenix.expandedstorage.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;

import java.util.function.BiConsumer;

public class ChestModel extends Model {
    protected final ModelPart lid;
    protected final ModelPart base;

    public ChestModel(int textureWidth, int textureHeight, BiConsumer<ModelPart, ModelPart> consumer) {
        super(RenderType::entityCutout);
        texWidth = textureWidth;
        texHeight = textureHeight;
        lid = new ModelPart(this, 0, 0);
        base = new ModelPart(this, 0, 19);
        consumer.accept(lid, base);
    }

    public final void setLidPitch(float pitch) {
        float p = 1.0f - pitch;
        lid.xRot = -((1.0F - p * p * p) * 1.5707964F);
    }

    public final void render(PoseStack stack, VertexConsumer consumer, int light, int overlay) {
        this.renderToBuffer(stack, consumer, light, overlay, 1, 1, 1, 1);
    }

    @Override
    public final void renderToBuffer(PoseStack stack, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float f) {
        base.render(stack, consumer, light, overlay);
        lid.render(stack, consumer, light, overlay);
    }
}
