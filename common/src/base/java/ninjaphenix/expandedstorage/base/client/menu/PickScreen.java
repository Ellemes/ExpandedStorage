package ninjaphenix.expandedstorage.base.client.menu;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import ninjaphenix.expandedstorage.base.client.menu.widget.ScreenPickButton;
import ninjaphenix.expandedstorage.base.internal_api.Utils;
import ninjaphenix.expandedstorage.base.wrappers.ConfigWrapper;
import ninjaphenix.expandedstorage.base.wrappers.NetworkWrapper;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public final class PickScreen extends Screen {
    private static final Map<ResourceLocation, Tuple<ResourceLocation, Component>> BUTTON_SETTINGS = new HashMap<>();
    private final Set<ResourceLocation> options;
    private final Screen returnToScreen;
    private final List<ScreenPickButton> optionWidgets;
    private final Consumer<ResourceLocation> onOptionPicked;
    private int topPadding;

    public PickScreen(Set<ResourceLocation> options, Screen returnToScreen, Consumer<ResourceLocation> onOptionPicked) {
        super(new TranslatableComponent("screen.expandedstorage.screen_picker_title"));
        this.options = options;
        this.optionWidgets = new ArrayList<>(options.size());
        this.onOptionPicked = onOptionPicked;
        this.returnToScreen = returnToScreen;
    }

    public static void declareButtonSettings(ResourceLocation screenType, ResourceLocation texture, Component text) {
        PickScreen.BUTTON_SETTINGS.putIfAbsent(screenType, new Tuple<>(texture, text));
    }

    @Override
    public void onClose() {
        //noinspection ConstantConditions
        minecraft.setScreen(returnToScreen);
    }

    @Override
    public boolean isPauseScreen() {
        //noinspection ConstantConditions
        return minecraft.level == null;
    }

    @Override
    protected void init() {
        super.init();
        boolean ignoreSingle = width < 370 || height < 386; // Smallest possible resolution a double netherite chest fits on.
        int choices = options.size() - (ignoreSingle ? 1 : 0);
        int columns = Math.min(Mth.intFloorDiv(width, 96), choices);
        int innerPadding = Math.min((width - columns * 96) / (columns + 1), 20); // 20 is smallest gap for any screen.
        int outerPadding = (width - (((columns - 1) * innerPadding) + (columns * 96))) / 2;
        int x = 0;
        int topPadding = (height - 96) / 2;
        this.topPadding = topPadding;
        optionWidgets.clear();
        for (ResourceLocation option : options) {
            if (!(ignoreSingle && Utils.SINGLE_SCREEN_TYPE.equals(option))) {
                Tuple<ResourceLocation, Component> settings = PickScreen.BUTTON_SETTINGS.get(option);
                optionWidgets.add(this.addRenderableWidget(new ScreenPickButton(outerPadding + (innerPadding + 96) * x, topPadding, 96, 96,
                        settings.getA(), settings.getB(), button -> this.updatePlayerPreference(option),
                        (button, matrices, tX, tY) -> this.renderTooltip(matrices, button.getMessage(), tX, tY))));
                x++;
            }
        }
    }

    private void updatePlayerPreference(ResourceLocation selection) {
        ConfigWrapper.getInstance().setPreferredScreenType(selection);
        onOptionPicked.accept(selection);
        this.onClose();
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, delta);
        optionWidgets.forEach(button -> button.renderTooltip(stack, mouseX, mouseY));
        GuiComponent.drawCenteredString(stack, font, title, width / 2, Math.max(topPadding / 2, 0), 0xFFFFFFFF);
    }
}
