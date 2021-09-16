package ninjaphenix.expandedstorage.base.internal_api;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;
import ninjaphenix.expandedstorage.base.internal_api.tier.Tier;
import ninjaphenix.expandedstorage.base.wrappers.PlatformUtils;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.function.UnaryOperator;

@Internal
@Experimental
public final class Utils {
    @Internal
    public static final String MOD_ID = "expandedstorage";
    public static final CreativeModeTab TAB = PlatformUtils.getInstance().createTab(BaseApi.getInstance()::tabIcon);
    @Internal
    public static final Component ALT_USE = new TranslatableComponent("tooltip.expandedstorage.alt_use",
            new KeybindComponent("key.sneak").withStyle(ChatFormatting.GOLD),
            new KeybindComponent("key.use").withStyle(ChatFormatting.GOLD));

    // Slots for Storage Tiers
    public static final int WOOD_STACK_COUNT = 27;
    public static final int IRON_STACK_COUNT = 54;
    public static final int GOLD_STACK_COUNT = 81;
    public static final int DIAMOND_STACK_COUNT = 108;
    public static final int OBSIDIAN_STACK_COUNT = 108;
    public static final int NETHERITE_STACK_COUNT = 135;

    // Default tiers which all modules can, but don't need to, specify blocks for.
    public static final Tier WOOD_TIER = new Tier(Utils.resloc("wood"), WOOD_STACK_COUNT, UnaryOperator.identity(), UnaryOperator.identity());
    public static final Tier IRON_TIER = new Tier(Utils.resloc("iron"), IRON_STACK_COUNT, BlockBehaviour.Properties::requiresCorrectToolForDrops, UnaryOperator.identity());
    public static final Tier GOLD_TIER = new Tier(Utils.resloc("gold"), GOLD_STACK_COUNT, BlockBehaviour.Properties::requiresCorrectToolForDrops, UnaryOperator.identity());
    public static final Tier DIAMOND_TIER = new Tier(Utils.resloc("diamond"), DIAMOND_STACK_COUNT, BlockBehaviour.Properties::requiresCorrectToolForDrops, UnaryOperator.identity());
    public static final Tier OBSIDIAN_TIER = new Tier(Utils.resloc("obsidian"), OBSIDIAN_STACK_COUNT, BlockBehaviour.Properties::requiresCorrectToolForDrops, UnaryOperator.identity());
    public static final Tier NETHERITE_TIER = new Tier(Utils.resloc("netherite"), NETHERITE_STACK_COUNT, BlockBehaviour.Properties::requiresCorrectToolForDrops, Item.Properties::fireResistant);

    // Item Cooldown
    public static final int QUARTER_SECOND = 5;

    private Utils() {

    }

    @Internal
    public static ResourceLocation resloc(String path) {
        return new ResourceLocation(Utils.MOD_ID, path);
    }

    @Internal
    public static MutableComponent translation(String key, Object... params) {
        return new TranslatableComponent(key, params);
    }
}
