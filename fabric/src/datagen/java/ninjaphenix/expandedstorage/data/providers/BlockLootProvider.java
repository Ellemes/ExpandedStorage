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
package ninjaphenix.expandedstorage.data.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTablesProvider;
import net.minecraft.data.server.BlockLootTableGenerator;
import ninjaphenix.expandedstorage.data.content.ModBlocks;

public final class BlockLootProvider extends FabricBlockLootTablesProvider {
    public BlockLootProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateBlockLootTables() {
        this.addDrop(ModBlocks.WOOD_CHEST, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.PUMPKIN_CHEST, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.PRESENT, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.IRON_CHEST, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.GOLD_CHEST, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.DIAMOND_CHEST, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.OBSIDIAN_CHEST, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.NETHERITE_CHEST, BlockLootTableGenerator::nameableContainerDrops);

        this.addDrop(ModBlocks.OLD_WOOD_CHEST, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.OLD_IRON_CHEST, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.OLD_GOLD_CHEST, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.OLD_DIAMOND_CHEST, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.OLD_OBSIDIAN_CHEST, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.OLD_NETHERITE_CHEST, BlockLootTableGenerator::nameableContainerDrops);

        this.addDrop(ModBlocks.IRON_BARREL, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.GOLD_BARREL, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.DIAMOND_BARREL, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.OBSIDIAN_BARREL, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.NETHERITE_BARREL, BlockLootTableGenerator::nameableContainerDrops);

        this.addDrop(ModBlocks.VANILLA_WOOD_MINI_CHEST, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.WOOD_MINI_CHEST, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.PUMPKIN_MINI_CHEST, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.RED_MINI_PRESENT, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.WHITE_MINI_PRESENT, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.CANDY_CANE_MINI_PRESENT, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.GREEN_MINI_PRESENT, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.LAVENDER_MINI_PRESENT, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.PINK_AMETHYST_MINI_PRESENT, BlockLootTableGenerator::nameableContainerDrops);

        this.addDrop(ModBlocks.VANILLA_WOOD_MINI_CHEST_WITH_SPARROW, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.WOOD_MINI_CHEST_WITH_SPARROW, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.PUMPKIN_MINI_CHEST_WITH_SPARROW, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.RED_MINI_PRESENT_WITH_SPARROW, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.WHITE_MINI_PRESENT_WITH_SPARROW, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.CANDY_CANE_MINI_PRESENT_WITH_SPARROW, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.GREEN_MINI_PRESENT_WITH_SPARROW, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.LAVENDER_MINI_PRESENT_WITH_SPARROW, BlockLootTableGenerator::nameableContainerDrops);
        this.addDrop(ModBlocks.PINK_AMETHYST_MINI_PRESENT_WITH_SPARROW, BlockLootTableGenerator::nameableContainerDrops);
    }
}
