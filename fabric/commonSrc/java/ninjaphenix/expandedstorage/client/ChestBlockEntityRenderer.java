package ninjaphenix.expandedstorage.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import ninjaphenix.expandedstorage.Common;
import ninjaphenix.expandedstorage.Utils;
import ninjaphenix.expandedstorage.block.AbstractChestBlock;
import ninjaphenix.expandedstorage.block.ChestBlock;
import ninjaphenix.expandedstorage.block.entity.ChestBlockEntity;
import ninjaphenix.expandedstorage.block.misc.CursedChestType;
import ninjaphenix.expandedstorage.block.misc.Property;
import ninjaphenix.expandedstorage.block.misc.PropertyRetriever;

import java.util.Map;

public final class ChestBlockEntityRenderer extends BlockEntityRenderer<ChestBlockEntity> {
    private static final BlockState DEFAULT_STATE = Registry.BLOCK.get(Utils.id("wood_chest")).defaultBlockState();

    private static final Map<CursedChestType, ChestModel> MODELS = ImmutableMap.<CursedChestType, ChestModel>builder()
                                                                                   .put(CursedChestType.SINGLE, new ChestModel(64, 48, (lid, base) -> {
                                                                                       lid.addBox(0, 0, 0, 14, 5, 14, 0);
                                                                                       lid.addBox(6, -2, 14, 2, 4, 1, 0);
                                                                                       lid.setPos(1, 9, 1);
                                                                                       base.addBox(0, 0, 0, 14, 10, 14, 0);
                                                                                       base.setPos(1, 0, 1);
                                                                                   }))
                                                                                   .put(CursedChestType.FRONT, new ChestModel(64, 48, (lid, base) -> {
                                                                                       lid.addBox(0, 0, 15, 14, 5, 15, 0);
                                                                                       lid.addBox(6, -2, 30, 2, 4, 1, 0);
                                                                                       lid.setPos(1, 9, -15);
                                                                                       base.texOffs(0, 20);
                                                                                       base.addBox(0, 0, 0, 14, 10, 15, 0);
                                                                                       base.setPos(1, 0, 0);
                                                                                   }))
                                                                                   .put(CursedChestType.BACK, new ChestModel(48, 48, (lid, base) -> {
                                                                                       lid.addBox(0, 0, 0, 14, 5, 15, 0);
                                                                                       lid.setPos(1, 9, 1);
                                                                                       base.texOffs(0, 20);
                                                                                       base.addBox(0, 0, 0, 14, 10, 15, 0);
                                                                                       base.setPos(1, 0, 1);
                                                                                   }))
                                                                                   .put(CursedChestType.TOP, new ChestModel(64, 48, (lid, base) -> {
                                                                                       lid.addBox(0, 0, 0, 14, 5, 14, 0);
                                                                                       lid.addBox(6, -2, 14, 2, 4, 1, 0);
                                                                                       lid.setPos(1, 9, 1);
                                                                                       base.addBox(0, 0, 0, 14, 10, 14, 0);
                                                                                       base.setPos(1, 0, 1);
                                                                                   }))
                                                                                   .put(CursedChestType.BOTTOM, new ChestModel(64, 32, (lid, base) -> {
                                                                                       base.texOffs(0, 0);
                                                                                       base.addBox(0, 0, 0, 14, 16, 14, 0);
                                                                                       base.setPos(1, 0, 1);
                                                                                   }))
                                                                                   .put(CursedChestType.LEFT, new ChestModel(64, 48, (lid, base) -> {
                                                                                       lid.addBox(0, 0, 0, 15, 5, 14, 0);
                                                                                       lid.addBox(14, -2, 14, 1, 4, 1, 0);
                                                                                       lid.setPos(1, 9, 1);
                                                                                       base.addBox(0, 0, 0, 15, 10, 14, 0);
                                                                                       base.setPos(1, 0, 1);
                                                                                   }))
                                                                                   .put(CursedChestType.RIGHT, new ChestModel(64, 48, (lid, base) -> {
                                                                                       lid.addBox(0, 0, 0, 15, 5, 14, 0);
                                                                                       lid.addBox(0, -2, 14, 1, 4, 1, 0);
                                                                                       lid.setPos(0, 9, 1);
                                                                                       base.addBox(0, 0, 0, 15, 10, 14, 0);
                                                                                       base.setPos(0, 0, 1);
                                                                                   }))
                                                                                   .build();

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
                //noinspection ConstantConditions
                int firstLightColor = LevelRenderer.getLightColor(first.getLevel(), first.getBlockPos());
                int firstBlockLight = LightTexture.block(firstLightColor);
                int firstSkyLight = LightTexture.sky(firstLightColor);
                //noinspection ConstantConditions
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

    public ChestBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(ChestBlockEntity entity, float delta, PoseStack stack, MultiBufferSource source, int light, int overlay) {
        ResourceLocation blockId = entity.getBlockId();
        BlockState state = entity.hasLevel() ? entity.getBlockState() :
                ChestBlockEntityRenderer.DEFAULT_STATE.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
        if (blockId == null || !(state.getBlock() instanceof ChestBlock block)) {
            return;
        }
        CursedChestType chestType = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
        ChestModel model = ChestBlockEntityRenderer.MODELS.get(chestType);
        stack.pushPose();
        stack.translate(0.5D, 0.5D, 0.5D);
        stack.mulPose(Vector3f.YP.rotationDegrees(-state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()));
        stack.translate(-0.5D, -0.5D, -0.5D);
        PropertyRetriever<ChestBlockEntity> compoundPropertyAccessor = entity.hasLevel() ?
                AbstractChestBlock.createPropertyRetriever(block, state, entity.getLevel(), entity.getBlockPos(), true) :
                PropertyRetriever.createDirect(entity);
        VertexConsumer consumer = new Material(Sheets.CHEST_SHEET, Common.getChestTexture(blockId, chestType)).buffer(source, RenderType::entityCutout);
        float lidOpenness = compoundPropertyAccessor.get(ChestBlockEntityRenderer.LID_OPENNESS_FUNCTION_GETTER).orElse(f -> 0).get(delta);
        int brightness = compoundPropertyAccessor.get(ChestBlockEntityRenderer.BRIGHTNESS_PROPERTY).orElse(i -> i).get(light);
        model.setLidPitch(lidOpenness);
        model.render(stack, consumer, brightness, overlay);
        stack.popPose();
    }
}
