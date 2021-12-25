/*
 * Copyright 2021 NinjaPhenix
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
package ninjaphenix.expandedstorage;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLLoader;
import ninjaphenix.expandedstorage.block.ChestBlock;
import ninjaphenix.expandedstorage.block.entity.ChestBlockEntity;

public final class ChestBlockItem extends BlockItem {
    public ChestBlockItem(ChestBlock block, Properties properties) {
        super(block, ChestBlockItem.configure(properties, block));
    }

    private static Properties configure(Properties properties, ChestBlock block) {
        if (FMLLoader.getDist() == Dist.CLIENT) {
            ChestBlockItem.addClientProperties(properties, block);
        }
        return properties;
    }

    @OnlyIn(Dist.CLIENT)
    private static void addClientProperties(Properties properties, ChestBlock block) {
        properties.setISTER(() -> () -> new BlockEntityWithoutLevelRenderer() {
            ChestBlockEntity renderEntity = null;

            @Override
            public void renderByItem(ItemStack item, ItemTransforms.TransformType transform, PoseStack pose, MultiBufferSource source, int light, int overlay) {
                BlockEntityRenderDispatcher.instance.renderItem(this.getOrCreateBlockEntity(), pose, source, light, overlay);
            }

            private ChestBlockEntity getOrCreateBlockEntity() {
                if (renderEntity == null) {
                    renderEntity = Common.getChestBlockEntityType().create();
                    CompoundTag tag = new CompoundTag();
                    tag.putInt("x", 0);
                    tag.putInt("y", 0);
                    tag.putInt("z", 0);
                    renderEntity.load(block.defaultBlockState(), tag);
                }
                return renderEntity;
            }
        });
    }
}
