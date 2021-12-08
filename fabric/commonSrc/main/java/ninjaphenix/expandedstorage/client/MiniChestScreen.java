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
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ninjaphenix.expandedstorage.MiniChestScreenHandler;
import ninjaphenix.expandedstorage.Utils;
import org.lwjgl.glfw.GLFW;

public final class MiniChestScreen extends HandledScreen<MiniChestScreenHandler> {
    private static final Identifier TEXTURE = Utils.id("textures/gui/mini_chest_screen.png");
    private static final int TEXTURE_WIDTH = 176;
    private static final int TEXTURE_HEIGHT = 176;

    public MiniChestScreen(MiniChestScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack stack, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        DrawableHelper.drawTexture(stack, x, y, 0, 0, backgroundWidth, backgroundHeight, MiniChestScreen.TEXTURE_WIDTH, MiniChestScreen.TEXTURE_HEIGHT);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(stack, mouseX, mouseY);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || this.client.options.keyInventory.matchesKey(keyCode, scanCode)) {
            this.client.player.closeHandledScreen();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
