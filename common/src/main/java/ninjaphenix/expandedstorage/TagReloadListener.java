/*
 * Copyright 2022 NinjaPhenix
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

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class TagReloadListener {
    public static final TagKey<Block> chestCycle = TagKey.create(Registry.BLOCK_REGISTRY, Utils.id("chest_cycle"));
    private List<Block> chestCycleBlocks = null;
    public static final TagKey<Block> miniChestCycle = TagKey.create(Registry.BLOCK_REGISTRY, Utils.id("mini_chest_cycle"));
    private List<Block> miniChestCycleBlocks = null;
    public static final TagKey<Block> miniChestSecretCycle = TagKey.create(Registry.BLOCK_REGISTRY, Utils.id("mini_chest_secret_cycle"));
    private List<Block> miniChestSecretCycleBlocks = null;
    public static final TagKey<Block> miniChestSecretCycle2 = TagKey.create(Registry.BLOCK_REGISTRY, Utils.id("mini_chest_secret_cycle_2"));
    private List<Block> miniChestSecretCycle2Blocks = null;

    public void postDataReload() {
        chestCycleBlocks = Registry.BLOCK.getOrCreateTag(chestCycle).stream().map(Holder::value).toList();
        miniChestCycleBlocks = Registry.BLOCK.getOrCreateTag(miniChestCycle).stream().map(Holder::value).toList();
        miniChestSecretCycleBlocks = Registry.BLOCK.getOrCreateTag(miniChestSecretCycle).stream().map(Holder::value).toList();
        miniChestSecretCycle2Blocks = Registry.BLOCK.getOrCreateTag(miniChestSecretCycle2).stream().map(Holder::value).toList();
    }

    public List<Block> getChestCycleBlocks() {
        if (chestCycleBlocks == null) { // In case no reload has happened yet, any better way to do this?
            this.postDataReload();
        }
        return chestCycleBlocks;
    }

    public List<Block> getMiniChestCycleBlocks() {
        if (miniChestCycleBlocks == null) { // In case no reload has happened yet, any better way to do this?
            this.postDataReload();
        }
        return miniChestCycleBlocks;
    }

    public List<Block> getMiniChestSecretCycleBlocks() {
        if (miniChestSecretCycleBlocks == null) { // In case no reload has happened yet, any better way to do this?
            this.postDataReload();
        }
        return miniChestSecretCycleBlocks;
    }

    public List<Block> getMiniChestSecretCycle2Blocks() {
        if (miniChestSecretCycle2Blocks == null) { // In case no reload has happened yet, any better way to do this?
            this.postDataReload();
        }
        return miniChestSecretCycle2Blocks;
    }
}
