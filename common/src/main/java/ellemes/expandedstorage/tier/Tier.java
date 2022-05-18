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
package ellemes.expandedstorage.tier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.UnaryOperator;

public final class Tier {
    private final ResourceLocation id;
    private final UnaryOperator<Item.Properties> itemSettings;
    private final UnaryOperator<BlockBehaviour.Properties> blockSettings;
    private final int slots;

    public Tier(ResourceLocation id, int slots, UnaryOperator<BlockBehaviour.Properties> blockSettings, UnaryOperator<Item.Properties> itemSettings) {
        this.id = id;
        this.slots = slots;
        this.blockSettings = blockSettings;
        this.itemSettings = itemSettings;
    }

    public ResourceLocation getId() {
        return id;
    }

    public UnaryOperator<Item.Properties> getItemSettings() {
        return itemSettings;
    }

    public UnaryOperator<BlockBehaviour.Properties> getBlockSettings() {
        return blockSettings;
    }

    public int getSlotCount() {
        return slots;
    }
}


