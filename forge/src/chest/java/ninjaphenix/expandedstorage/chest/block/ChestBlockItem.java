package ninjaphenix.expandedstorage.chest.block;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;
import ninjaphenix.expandedstorage.chest.ChestCommon;
import ninjaphenix.expandedstorage.chest.block.misc.ChestBlockEntity;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ChestBlockItem extends BlockItem {
    public ChestBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            final Supplier<BlockEntityWithoutLevelRenderer> renderer = Suppliers.memoize(this::createItemStackRenderer);

            private BlockEntityWithoutLevelRenderer createItemStackRenderer() {
                ChestBlockEntity renderEntity = new ChestBlockEntity(ChestCommon.getBlockEntityType(), BlockPos.ZERO, ChestBlockItem.this.getBlock().defaultBlockState());
                var minecraft = Minecraft.getInstance();
                return new BlockEntityWithoutLevelRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels()) {
                    @Override
                    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transform, PoseStack poses, MultiBufferSource source, int light, int overlay) {
                        Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(renderEntity, poses, source, light, overlay);
                    }
                };
            }

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer.get();
            }
        });
    }
}
