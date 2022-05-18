/*
 * Copyright 2021-2022 Ellemes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ellemes.expandedstorage.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import ellemes.expandedstorage.Common;
import ellemes.expandedstorage.block.ChestBlock;
import ellemes.expandedstorage.Utils;
import ninjaphenix.expandedstorage.block.AbstractChestBlock;
import ellemes.expandedstorage.block.entity.ChestBlockEntity;
import ninjaphenix.expandedstorage.block.misc.CursedChestType;
import ellemes.expandedstorage.block.misc.Property;
import ellemes.expandedstorage.block.misc.PropertyRetriever;

public final class ChestBlockEntityRenderer implements BlockEntityRenderer<ChestBlockEntity> {
    public static final ModelLayerLocation SINGLE_LAYER = new ModelLayerLocation(Utils.id("single_chest"), "main");
    public static final ModelLayerLocation LEFT_LAYER = new ModelLayerLocation(Utils.id("left_chest"), "main");
    public static final ModelLayerLocation RIGHT_LAYER = new ModelLayerLocation(Utils.id("right_chest"), "main");
    public static final ModelLayerLocation TOP_LAYER = new ModelLayerLocation(Utils.id("top_chest"), "main");
    public static final ModelLayerLocation BOTTOM_LAYER = new ModelLayerLocation(Utils.id("bottom_chest"), "main");
    public static final ModelLayerLocation FRONT_LAYER = new ModelLayerLocation(Utils.id("front_chest"), "main");
    public static final ModelLayerLocation BACK_LAYER = new ModelLayerLocation(Utils.id("back_chest"), "main");
    private static final BlockState DEFAULT_STATE = Registry.BLOCK.get(Utils.id("wood_chest")).defaultBlockState();

    private static final Property<ChestBlockEntity, Float2FloatFunction> LID_OPENNESS_FUNCTION_GETTER = new Property<>() {
        @Override
        public Float2FloatFunction get(ChestBlockEntity first, ChestBlockEntity second) {
            return (delta) -> Math.max(first.getLidOpenness(delta), second.getLidOpenness(delta));
        }

        @Override
        public Float2FloatFunction get(ChestBlockEntity single) {
            return single::getLidOpenness;
        }
    };

    private static final Property<ChestBlockEntity, Int2IntFunction> BRIGHTNESS_PROPERTY = new Property<>() {
        @Override
        public Int2IntFunction get(ChestBlockEntity first, ChestBlockEntity second) {
            return i -> {
                int firstLightColor = LevelRenderer.getLightColor(first.getLevel(), first.getBlockPos());
                int firstBlockLight = LightTexture.block(firstLightColor);
                int firstSkyLight = LightTexture.sky(firstLightColor);

                int secondLightColor = LevelRenderer.getLightColor(second.getLevel(), second.getBlockPos());
                int secondBlockLight = LightTexture.block(secondLightColor);
                int secondSkyLight = LightTexture.sky(secondLightColor);
                return LightTexture.pack(Math.max(firstBlockLight, secondBlockLight), Math.max(firstSkyLight, secondSkyLight));
            };
        }

        @Override
        public Int2IntFunction get(ChestBlockEntity single) {
            return i -> i;
        }
    };

    private final ModelPart singleBottom, singleLid, singleLock;
    private final ModelPart leftBottom, leftLid, leftLock;
    private final ModelPart rightBottom, rightLid, rightLock;
    private final ModelPart topBottom, topLid, topLock;
    private final ModelPart bottomBottom;
    private final ModelPart frontBottom, frontLid, frontLock;
    private final ModelPart backBottom, backLid;

    public ChestBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart single = context.bakeLayer(ChestBlockEntityRenderer.SINGLE_LAYER);
        singleBottom = single.getChild("bottom");
        singleLid = single.getChild("lid");
        singleLock = single.getChild("lock");
        ModelPart left = context.bakeLayer(ChestBlockEntityRenderer.LEFT_LAYER);
        leftBottom = left.getChild("bottom");
        leftLid = left.getChild("lid");
        leftLock = left.getChild("lock");
        ModelPart right = context.bakeLayer(ChestBlockEntityRenderer.RIGHT_LAYER);
        rightBottom = right.getChild("bottom");
        rightLid = right.getChild("lid");
        rightLock = right.getChild("lock");
        ModelPart top = context.bakeLayer(ChestBlockEntityRenderer.TOP_LAYER);
        topBottom = top.getChild("bottom");
        topLid = top.getChild("lid");
        topLock = top.getChild("lock");
        ModelPart bottom = context.bakeLayer(ChestBlockEntityRenderer.BOTTOM_LAYER);
        bottomBottom = bottom.getChild("bottom");
        ModelPart front = context.bakeLayer(ChestBlockEntityRenderer.FRONT_LAYER);
        frontBottom = front.getChild("bottom");
        frontLid = front.getChild("lid");
        frontLock = front.getChild("lock");
        ModelPart back = context.bakeLayer(ChestBlockEntityRenderer.BACK_LAYER);
        backBottom = back.getChild("bottom");
        backLid = back.getChild("lid");
    }

    public static LayerDefinition createSingleBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 19).addBox(1, 0, 1, 14, 10, 14), PartPose.ZERO);
        partDefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(1, 0, 0, 14, 5, 14), PartPose.offset(0, 9, 1));
        partDefinition.addOrReplaceChild("lock", CubeListBuilder.create().texOffs(0, 0).addBox(7, -1, 15, 2, 4, 1), PartPose.offset(0, 8, 0));
        return LayerDefinition.create(meshDefinition, 64, 48);
    }

    public static LayerDefinition createLeftBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 19).addBox(1, 0, 1, 15, 10, 14), PartPose.ZERO);
        partDefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(1, 0, 0, 15, 5, 14), PartPose.offset(0, 9, 1));
        partDefinition.addOrReplaceChild("lock", CubeListBuilder.create().texOffs(0, 0).addBox(15, -1, 15, 1, 4, 1), PartPose.offset(0, 8, 0));
        return LayerDefinition.create(meshDefinition, 64, 48);
    }

    public static LayerDefinition createRightBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 19).addBox(0, 0, 1, 15, 10, 14), PartPose.ZERO);
        partDefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(0, 0, 0, 15, 5, 14), PartPose.offset(0, 9, 1));
        partDefinition.addOrReplaceChild("lock", CubeListBuilder.create().texOffs(0, 0).addBox(0, -1, 15, 1, 4, 1), PartPose.offset(0, 8, 0));
        return LayerDefinition.create(meshDefinition, 64, 48);
    }

    public static LayerDefinition createTopBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 19).addBox(1, 0, 1, 14, 10, 14), PartPose.ZERO);
        partDefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(1, 0, 0, 14, 5, 14), PartPose.offset(0, 9, 1));
        partDefinition.addOrReplaceChild("lock", CubeListBuilder.create().texOffs(0, 0).addBox(7, -1, 15, 2, 4, 1), PartPose.offset(0, 8, 0));
        return LayerDefinition.create(meshDefinition, 64, 48);
    }

    public static LayerDefinition createBottomBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 0).addBox(1, 0, 1, 14, 16, 14), PartPose.ZERO);
        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    public static LayerDefinition createFrontBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 20).addBox(1, 0, 0, 14, 10, 15), PartPose.ZERO);
        partDefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(1, 0, 15, 14, 5, 15), PartPose.offset(0, 9, -15));
        partDefinition.addOrReplaceChild("lock", CubeListBuilder.create().texOffs(0, 0).addBox(7, -1, 31, 2, 4, 1), PartPose.offset(0, 8, -16));
        return LayerDefinition.create(meshDefinition, 64, 48);
    }

    public static LayerDefinition createBackBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 20).addBox(1, 0, 1, 14, 10, 15), PartPose.ZERO);
        partDefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(1, 0, 0, 14, 5, 15), PartPose.offset(0, 9, 1));
        return LayerDefinition.create(meshDefinition, 48, 48);
    }

    @Override
    public void render(ChestBlockEntity entity, float delta, PoseStack stack, MultiBufferSource provider, int light, int overlay) {
        ResourceLocation blockId = entity.getBlockId();
        BlockState state = entity.hasLevel() ? entity.getBlockState() : ChestBlockEntityRenderer.DEFAULT_STATE.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
        if (blockId == null || !(state.getBlock() instanceof ChestBlock block)) {
            return;
        }
        CursedChestType chestType = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
        stack.pushPose();
        stack.translate(0.5D, 0.5D, 0.5D);
        stack.mulPose(Vector3f.YP.rotationDegrees(-state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()));
        stack.translate(-0.5D, -0.5D, -0.5D);
        PropertyRetriever<ChestBlockEntity> retriever;
        if (entity.hasLevel()) {
            retriever = AbstractChestBlock.createPropertyRetriever(block, state, entity.getLevel(), entity.getBlockPos(), true);
        } else {
            retriever = PropertyRetriever.createDirect(entity);
        }
        VertexConsumer consumer = new Material(Sheets.CHEST_SHEET, Common.getChestTexture(blockId, chestType)).buffer(provider, RenderType::entityCutout);
        float lidOpenness = ChestBlockEntityRenderer.getLidOpenness(retriever.get(ChestBlockEntityRenderer.LID_OPENNESS_FUNCTION_GETTER).orElse(f -> 0).get(delta));
        int brightness = retriever.get(ChestBlockEntityRenderer.BRIGHTNESS_PROPERTY).orElse(i -> i).applyAsInt(light);
        if (chestType == CursedChestType.SINGLE) {
            ChestBlockEntityRenderer.renderBottom(stack, consumer, singleBottom, brightness, overlay);
            ChestBlockEntityRenderer.renderTop(stack, consumer, singleLid, brightness, overlay, lidOpenness);
            ChestBlockEntityRenderer.renderTop(stack, consumer, singleLock, brightness, overlay, lidOpenness);
        } else if (chestType == CursedChestType.TOP) {
            ChestBlockEntityRenderer.renderBottom(stack, consumer, topBottom, brightness, overlay);
            ChestBlockEntityRenderer.renderTop(stack, consumer, topLid, brightness, overlay, lidOpenness);
            ChestBlockEntityRenderer.renderTop(stack, consumer, topLock, brightness, overlay, lidOpenness);
        } else if (chestType == CursedChestType.BOTTOM) {
            ChestBlockEntityRenderer.renderBottom(stack, consumer, bottomBottom, brightness, overlay);
        } else if (chestType == CursedChestType.FRONT) {
            ChestBlockEntityRenderer.renderBottom(stack, consumer, frontBottom, brightness, overlay);
            ChestBlockEntityRenderer.renderTop(stack, consumer, frontLid, brightness, overlay, lidOpenness);
            ChestBlockEntityRenderer.renderTop(stack, consumer, frontLock, brightness, overlay, lidOpenness);
        } else if (chestType == CursedChestType.BACK) {
            ChestBlockEntityRenderer.renderBottom(stack, consumer, backBottom, brightness, overlay);
            ChestBlockEntityRenderer.renderTop(stack, consumer, backLid, brightness, overlay, lidOpenness);
        } else if (chestType == CursedChestType.LEFT) {
            ChestBlockEntityRenderer.renderBottom(stack, consumer, leftBottom, brightness, overlay);
            //noinspection SuspiciousNameCombination
            ChestBlockEntityRenderer.renderTop(stack, consumer, leftLid, brightness, overlay, lidOpenness);
            //noinspection SuspiciousNameCombination
            ChestBlockEntityRenderer.renderTop(stack, consumer, leftLock, brightness, overlay, lidOpenness);
        } else if (chestType == CursedChestType.RIGHT) {
            ChestBlockEntityRenderer.renderBottom(stack, consumer, rightBottom, brightness, overlay);
            //noinspection SuspiciousNameCombination
            ChestBlockEntityRenderer.renderTop(stack, consumer, rightLid, brightness, overlay, lidOpenness);
            //noinspection SuspiciousNameCombination
            ChestBlockEntityRenderer.renderTop(stack, consumer, rightLock, brightness, overlay, lidOpenness);
        }
        stack.popPose();
    }

    private static float getLidOpenness(float delta) {
        delta = 1 - delta;
        delta = 1 - delta * delta * delta;
        return -delta * Mth.HALF_PI;
    }

    private static void renderBottom(PoseStack stack, VertexConsumer consumer, ModelPart bottom, int light, int overlay) {
        bottom.render(stack, consumer, light, overlay);
    }

    private static void renderTop(PoseStack stack, VertexConsumer consumer, ModelPart top, int light, int overlay, float openness) {
        top.xRot = openness;
        top.render(stack, consumer, light, overlay);
    }
}
