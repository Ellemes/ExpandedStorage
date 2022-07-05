package ellemes.expandedstorage.forge.datagen.content;

import ellemes.expandedstorage.Utils;
import ellemes.expandedstorage.item.ChestMinecartItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModItems {
    public static final Item STORAGE_MUTATOR = item(Utils.id("storage_mutator"));

    public static final Item WOOD_TO_IRON_CONVERSION_KIT = item(Utils.id("wood_to_iron_conversion_kit"));
    public static final Item WOOD_TO_GOLD_CONVERSION_KIT = item(Utils.id("wood_to_gold_conversion_kit"));
    public static final Item WOOD_TO_DIAMOND_CONVERSION_KIT = item(Utils.id("wood_to_diamond_conversion_kit"));
    public static final Item WOOD_TO_OBSIDIAN_CONVERSION_KIT = item(Utils.id("wood_to_obsidian_conversion_kit"));
    public static final Item WOOD_TO_NETHERITE_CONVERSION_KIT = item(Utils.id("wood_to_netherite_conversion_kit"));
    public static final Item IRON_TO_GOLD_CONVERSION_KIT = item(Utils.id("iron_to_gold_conversion_kit"));
    public static final Item IRON_TO_DIAMOND_CONVERSION_KIT = item(Utils.id("iron_to_diamond_conversion_kit"));
    public static final Item IRON_TO_OBSIDIAN_CONVERSION_KIT = item(Utils.id("iron_to_obsidian_conversion_kit"));
    public static final Item IRON_TO_NETHERITE_CONVERSION_KIT = item(Utils.id("iron_to_netherite_conversion_kit"));
    public static final Item GOLD_TO_DIAMOND_CONVERSION_KIT = item(Utils.id("gold_to_diamond_conversion_kit"));
    public static final Item GOLD_TO_OBSIDIAN_CONVERSION_KIT = item(Utils.id("gold_to_obsidian_conversion_kit"));
    public static final Item GOLD_TO_NETHERITE_CONVERSION_KIT = item(Utils.id("gold_to_netherite_conversion_kit"));
    public static final Item DIAMOND_TO_OBSIDIAN_CONVERSION_KIT = item(Utils.id("diamond_to_obsidian_conversion_kit"));
    public static final Item DIAMOND_TO_NETHERITE_CONVERSION_KIT = item(Utils.id("diamond_to_netherite_conversion_kit"));
    public static final Item OBSIDIAN_TO_NETHERITE_CONVERSION_KIT = item(Utils.id("obsidian_to_netherite_conversion_kit"));

    public static final BlockItem WOOD_CHEST = item(Utils.id("wood_chest"));
    public static final BlockItem PUMPKIN_CHEST = item(Utils.id("pumpkin_chest"));
    public static final BlockItem PRESENT = item(Utils.id("present"));
    public static final BlockItem IRON_CHEST = item(Utils.id("iron_chest"));
    public static final BlockItem GOLD_CHEST = item(Utils.id("gold_chest"));
    public static final BlockItem DIAMOND_CHEST = item(Utils.id("diamond_chest"));
    public static final BlockItem OBSIDIAN_CHEST = item(Utils.id("obsidian_chest"));
    public static final BlockItem NETHERITE_CHEST = item(Utils.id("netherite_chest"));

    public static final ChestMinecartItem WOOD_CHEST_MINECART = item(Utils.id("wood_chest_minecart"));
    public static final ChestMinecartItem PUMPKIN_CHEST_MINECART = item(Utils.id("pumpkin_chest_minecart"));
    public static final ChestMinecartItem PRESENT_MINECART = item(Utils.id("present_minecart"));
    public static final ChestMinecartItem IRON_CHEST_MINECART = item(Utils.id("iron_chest_minecart"));
    public static final ChestMinecartItem GOLD_CHEST_MINECART = item(Utils.id("gold_chest_minecart"));
    public static final ChestMinecartItem DIAMOND_CHEST_MINECART = item(Utils.id("diamond_chest_minecart"));
    public static final ChestMinecartItem OBSIDIAN_CHEST_MINECART = item(Utils.id("obsidian_chest_minecart"));
    public static final ChestMinecartItem NETHERITE_CHEST_MINECART = item(Utils.id("netherite_chest_minecart"));

    public static final BlockItem OLD_WOOD_CHEST = item(Utils.id("old_wood_chest"));
    public static final BlockItem OLD_IRON_CHEST = item(Utils.id("old_iron_chest"));
    public static final BlockItem OLD_GOLD_CHEST = item(Utils.id("old_gold_chest"));
    public static final BlockItem OLD_DIAMOND_CHEST = item(Utils.id("old_diamond_chest"));
    public static final BlockItem OLD_OBSIDIAN_CHEST = item(Utils.id("old_obsidian_chest"));
    public static final BlockItem OLD_NETHERITE_CHEST = item(Utils.id("old_netherite_chest"));

    public static final BlockItem IRON_BARREL = item(Utils.id("iron_barrel"));
    public static final BlockItem GOLD_BARREL = item(Utils.id("gold_barrel"));
    public static final BlockItem DIAMOND_BARREL = item(Utils.id("diamond_barrel"));
    public static final BlockItem OBSIDIAN_BARREL = item(Utils.id("obsidian_barrel"));
    public static final BlockItem NETHERITE_BARREL = item(Utils.id("netherite_barrel"));

    public static final BlockItem VANILLA_WOOD_MINI_CHEST = item(Utils.id("vanilla_wood_mini_chest"));
    public static final BlockItem WOOD_MINI_CHEST = item(Utils.id("wood_mini_chest"));
    public static final BlockItem PUMPKIN_MINI_CHEST = item(Utils.id("pumpkin_mini_chest"));

    public static final BlockItem RED_MINI_PRESENT = item(Utils.id("red_mini_present"));
    public static final BlockItem WHITE_MINI_PRESENT = item(Utils.id("white_mini_present"));
    public static final BlockItem CANDY_CANE_MINI_PRESENT = item(Utils.id("candy_cane_mini_present"));
    public static final BlockItem GREEN_MINI_PRESENT = item(Utils.id("green_mini_present"));

    private static <T extends Item> T item(ResourceLocation id) {
        //noinspection unchecked
        return (T) ForgeRegistries.ITEMS.getValue(id);
    }
}
