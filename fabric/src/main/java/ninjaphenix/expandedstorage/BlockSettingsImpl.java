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

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.state.BlockBehaviour;
import ninjaphenix.expandedstorage.registration.BlockSettings;

public class BlockSettingsImpl extends BlockSettings {
    public BlockSettingsImpl(BlockBehaviour.Properties settings) {
        super(FabricBlockSettings.copyOf(settings));
    }

    @Override
    public BlockSettings setMiningData(String tool, int miningLevel) {
        if (tool.equals("axe")) {
            ((FabricBlockSettings) blockSettings).breakByTool(FabricToolTags.AXES, miningLevel);
        } else if (tool.equals("pickaxe")) {
            ((FabricBlockSettings) blockSettings).breakByTool(FabricToolTags.PICKAXES, miningLevel);
        }
        return this;
    }
}
