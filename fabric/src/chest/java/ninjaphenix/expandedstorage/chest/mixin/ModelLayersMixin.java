package ninjaphenix.expandedstorage.chest.mixin;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import ninjaphenix.expandedstorage.chest.client.ChestBlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

// todo: hopefully remove this before 1.18
@Mixin(LayerDefinitions.class)
public class ModelLayersMixin {
    @Inject(method = "createRoots()Ljava/util/Map;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/builders/LayerDefinition;create(Lnet/minecraft/client/model/geom/builders/MeshDefinition;II)Lnet/minecraft/client/model/geom/builders/LayerDefinition;", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private static void addLayerDefinitions(CallbackInfoReturnable<Map<ModelLayerLocation, ModelPart>> cir,
                                            ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> builder) {
        builder.put(ChestBlockEntityRenderer.SINGLE_LAYER, ChestBlockEntityRenderer.createSingleBodyLayer());
        builder.put(ChestBlockEntityRenderer.VANILLA_LEFT_LAYER, ChestBlockEntityRenderer.createVanillaLeftBodyLayer());
        builder.put(ChestBlockEntityRenderer.VANILLA_RIGHT_LAYER, ChestBlockEntityRenderer.createVanillaRightBodyLayer());
        builder.put(ChestBlockEntityRenderer.TALL_TOP_LAYER, ChestBlockEntityRenderer.createTallTopBodyLayer());
        builder.put(ChestBlockEntityRenderer.TALL_BOTTOM_LAYER, ChestBlockEntityRenderer.createTallBottomBodyLayer());
        builder.put(ChestBlockEntityRenderer.LONG_FRONT_LAYER, ChestBlockEntityRenderer.createLongFrontBodyLayer());
        builder.put(ChestBlockEntityRenderer.LONG_BACK_LAYER, ChestBlockEntityRenderer.createLongBackBodyLayer());
    }
}
