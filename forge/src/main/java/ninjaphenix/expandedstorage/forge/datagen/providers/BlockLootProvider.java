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
package ninjaphenix.expandedstorage.forge.datagen.providers;

import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import ninjaphenix.expandedstorage.Utils;
import ninjaphenix.expandedstorage.forge.datagen.content.ModBlocks;

import java.util.stream.Collectors;

public final class BlockLootProvider extends BlockLoot {
    @Override
    protected void addTables() {
        this.add(ModBlocks.WOOD_CHEST, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.PUMPKIN_CHEST, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.PRESENT, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.IRON_CHEST, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.GOLD_CHEST, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.DIAMOND_CHEST, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.OBSIDIAN_CHEST, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.NETHERITE_CHEST, BlockLoot::createNameableBlockEntityTable);

        this.add(ModBlocks.OLD_WOOD_CHEST, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.OLD_IRON_CHEST, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.OLD_GOLD_CHEST, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.OLD_DIAMOND_CHEST, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.OLD_OBSIDIAN_CHEST, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.OLD_NETHERITE_CHEST, BlockLoot::createNameableBlockEntityTable);

        this.add(ModBlocks.IRON_BARREL, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.GOLD_BARREL, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.DIAMOND_BARREL, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.OBSIDIAN_BARREL, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.NETHERITE_BARREL, BlockLoot::createNameableBlockEntityTable);

        this.add(ModBlocks.VANILLA_WOOD_MINI_CHEST, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.WOOD_MINI_CHEST, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.PUMPKIN_MINI_CHEST, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.RED_MINI_PRESENT, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.WHITE_MINI_PRESENT, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.CANDY_CANE_MINI_PRESENT, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.GREEN_MINI_PRESENT, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.LAVENDER_MINI_PRESENT, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.PINK_AMETHYST_MINI_PRESENT, BlockLoot::createNameableBlockEntityTable);

        this.add(ModBlocks.VANILLA_WOOD_MINI_CHEST_WITH_SPARROW, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.WOOD_MINI_CHEST_WITH_SPARROW, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.PUMPKIN_MINI_CHEST_WITH_SPARROW, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.RED_MINI_PRESENT_WITH_SPARROW, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.WHITE_MINI_PRESENT_WITH_SPARROW, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.CANDY_CANE_MINI_PRESENT_WITH_SPARROW, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.GREEN_MINI_PRESENT_WITH_SPARROW, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.LAVENDER_MINI_PRESENT_WITH_SPARROW, BlockLoot::createNameableBlockEntityTable);
        this.add(ModBlocks.PINK_AMETHYST_MINI_PRESENT_WITH_SPARROW, BlockLoot::createNameableBlockEntityTable);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getValues().stream().filter(block -> Utils.MOD_ID.equals(block.getRegistryName().getNamespace())).collect(Collectors.toSet());
    }
}
