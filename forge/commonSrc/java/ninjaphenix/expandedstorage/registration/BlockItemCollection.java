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
package ninjaphenix.expandedstorage.registration;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

import java.util.function.IntFunction;

public final class BlockItemCollection<B extends Block, I extends BlockItem> {
    private final B[] blocks;
    private final I[] items;

    @SafeVarargs
    private BlockItemCollection(IntFunction<B[]> blockArrayMaker, IntFunction<I[]> itemArrayMaker, BlockItemPair<B, I>... pairs) {
        blocks = blockArrayMaker.apply(pairs.length);
        items = itemArrayMaker.apply(pairs.length);
        for (int i = 0; i < pairs.length; i++) {
            BlockItemPair<B, I> pair = pairs[i];
            blocks[i] = pair.getBlock();
            items[i] = pair.getItem();
        }
    }

    @SafeVarargs
    public static <B extends Block, I extends BlockItem> BlockItemCollection<B, I> of(IntFunction<B[]> blockArrayMaker, IntFunction<I[]> itemArrayMaker, BlockItemPair<B, I>... pairs) {
        return new BlockItemCollection<>(blockArrayMaker, itemArrayMaker, pairs);
    }

    public B[] getBlocks() {
        return blocks;
    }

    public I[] getItems() {
        return items;
    }
}
