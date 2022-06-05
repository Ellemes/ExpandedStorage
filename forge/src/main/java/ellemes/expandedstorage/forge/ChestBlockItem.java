package ellemes.expandedstorage.forge;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import ellemes.expandedstorage.Common;
import ellemes.expandedstorage.block.entity.ChestBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ChestBlockItem extends BlockItem {
    public ChestBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            final Supplier<BlockEntityWithoutLevelRenderer> renderer = Suppliers.memoize(this::createItemRenderer);

            private BlockEntityWithoutLevelRenderer createItemRenderer() {
                ChestBlockEntity renderEntity = Common.getChestBlockEntityType().create(BlockPos.ZERO, ChestBlockItem.this.getBlock().defaultBlockState());
                Minecraft minecraft = Minecraft.getInstance();
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
