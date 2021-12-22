package ninjaphenix.expandedstorage;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;
import ninjaphenix.expandedstorage.tier.Tier;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@Internal
@Experimental
public final class Utils {
    @Internal
    public static final String MOD_ID = "expandedstorage";
    @Internal
    public static final Component ALT_USE = new TranslatableComponent("tooltip.expandedstorage.alt_use",
            new KeybindComponent("key.sneak").withStyle(ChatFormatting.GOLD),
            new KeybindComponent("key.use").withStyle(ChatFormatting.GOLD));

    // Slots for Storage Tiers
    public static final int IRON_STACK_COUNT = 54;
    public static final int GOLD_STACK_COUNT = 81;
    public static final int DIAMOND_STACK_COUNT = 108;
    public static final int OBSIDIAN_STACK_COUNT = 108;
    public static final int NETHERITE_STACK_COUNT = 135;

    public static final int WOOD_STACK_COUNT = 27;
    public static final ResourceLocation WOOD_TIER_ID = Utils.id("wood");

    // NBT Tag Types
    /**
     * @deprecated Removing in 1.17, in 1.17 use {@link net.minecraft.nbt.Tag.TAG_STRING} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "8 (MC=1.17)")
    public static final int NBT_STRING_TYPE = 8;

    /**
     * @deprecated Removing in 1.17, in 1.17 use {@link net.minecraft.nbt.Tag.TAG_COMPOUND} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "8 (MC=1.17)")
    public static final int NBT_COMPOUND_TYPE = 10;

    /**
     * @deprecated Removing in 1.17, in 1.17 use {@link net.minecraft.nbt.Tag.TAG_BYTE} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "8 (MC=1.17)")
    public static final int NBT_BYTE_TYPE = 1;

    /**
     * @deprecated Removing in 1.17, in 1.17 use {@link net.minecraft.nbt.Tag.TAG_LIST} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "8 (MC=1.17)")
    public static final int NBT_LIST_TYPE = 9;

    // Item Cooldown
    public static final int QUARTER_SECOND = 5;

    private Utils() {

    }

    @Internal
    public static ResourceLocation id(String path) {
        return new ResourceLocation(Utils.MOD_ID, path);
    }

    @Internal
    public static MutableComponent translation(String key, Object... params) {
        return new TranslatableComponent(key, params);
    }
}
