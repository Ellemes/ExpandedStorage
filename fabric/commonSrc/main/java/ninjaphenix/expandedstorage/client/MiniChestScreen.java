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
package ninjaphenix.expandedstorage.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.screen.slot.Slot;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ninjaphenix.container_library.api.client.function.ScreenSize;
import ninjaphenix.container_library.api.client.gui.AbstractScreen;
import ninjaphenix.container_library.api.inventory.AbstractHandler;
import ninjaphenix.container_library.api.v2.client.NCL_ClientApiV2;
import ninjaphenix.expandedstorage.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class MiniChestScreen extends AbstractScreen {
    private static final Identifier TEXTURE = Utils.id("textures/gui/mini_chest_screen.png");
    private static final int TEXTURE_WIDTH = 176;
    private static final int TEXTURE_HEIGHT = 176;

    public MiniChestScreen(AbstractHandler handler, PlayerInventory playerInventory, Text title, ScreenSize screenSize) {
        super(handler, playerInventory, title, screenSize);
        this.initializeSlots(playerInventory);
    }

    private void initializeSlots(PlayerInventory playerInventory) {
        handler.addClientSlot(new Slot(handler.getInventory(), 0, 80, 35));
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                handler.addClientSlot(new Slot(playerInventory, 9 + x + y * 9, 8 + x * 18, 84 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            handler.addClientSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }
    }

    @Override
    protected void drawBackground(MatrixStack stack, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        DrawableHelper.drawTexture(stack, x, y, 0, 0, backgroundWidth, backgroundHeight, MiniChestScreen.TEXTURE_WIDTH, MiniChestScreen.TEXTURE_HEIGHT);
    }

    @NotNull
    @Override
    public List<Rect2i> getExclusionZones() {
        return List.of();
    }

    public static ScreenSize retrieveScreenSize(int slots, int scaledWidth, int scaledHeight) {
        return ScreenSize.of(1, 1);
    }

    public static void registerScreenType() {
        NCL_ClientApiV2.registerScreenType(Utils.id("mini_chest"), MiniChestScreen::new);
        NCL_ClientApiV2.registerDefaultScreenSize(Utils.id("mini_chest"), MiniChestScreen::retrieveScreenSize);
    }
}
