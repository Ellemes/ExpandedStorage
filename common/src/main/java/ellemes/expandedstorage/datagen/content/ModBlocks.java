package ellemes.expandedstorage.datagen.content;

import ellemes.expandedstorage.Utils;
import ellemes.expandedstorage.block.AbstractChestBlock;
import ellemes.expandedstorage.block.BarrelBlock;
import ellemes.expandedstorage.block.ChestBlock;
import ellemes.expandedstorage.block.MiniChestBlock;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public final class ModBlocks {
    public static final ChestBlock WOOD_CHEST = block(Utils.id("wood_chest"));
    public static final ChestBlock PUMPKIN_CHEST = block(Utils.id("pumpkin_chest"));
    public static final ChestBlock PRESENT = block(Utils.id("present"));
    public static final ChestBlock BAMBOO_CHEST = block(Utils.id("bamboo_chest"));
    public static final ChestBlock IRON_CHEST = block(Utils.id("iron_chest"));
    public static final ChestBlock GOLD_CHEST = block(Utils.id("gold_chest"));
    public static final ChestBlock DIAMOND_CHEST = block(Utils.id("diamond_chest"));
    public static final ChestBlock OBSIDIAN_CHEST = block(Utils.id("obsidian_chest"));
    public static final ChestBlock NETHERITE_CHEST = block(Utils.id("netherite_chest"));

    public static final AbstractChestBlock OLD_WOOD_CHEST = block(Utils.id("old_wood_chest"));
    public static final AbstractChestBlock OLD_IRON_CHEST = block(Utils.id("old_iron_chest"));
    public static final AbstractChestBlock OLD_GOLD_CHEST = block(Utils.id("old_gold_chest"));
    public static final AbstractChestBlock OLD_DIAMOND_CHEST = block(Utils.id("old_diamond_chest"));
    public static final AbstractChestBlock OLD_OBSIDIAN_CHEST = block(Utils.id("old_obsidian_chest"));
    public static final AbstractChestBlock OLD_NETHERITE_CHEST = block(Utils.id("old_netherite_chest"));

    public static final BarrelBlock IRON_BARREL = block(Utils.id("iron_barrel"));
    public static final BarrelBlock GOLD_BARREL = block(Utils.id("gold_barrel"));
    public static final BarrelBlock DIAMOND_BARREL = block(Utils.id("diamond_barrel"));
    public static final BarrelBlock OBSIDIAN_BARREL = block(Utils.id("obsidian_barrel"));
    public static final BarrelBlock NETHERITE_BARREL = block(Utils.id("netherite_barrel"));

    public static final MiniChestBlock VANILLA_WOOD_MINI_CHEST = block(Utils.id("vanilla_wood_mini_chest"));
    public static final MiniChestBlock WOOD_MINI_CHEST = block(Utils.id("wood_mini_chest"));
    public static final MiniChestBlock PUMPKIN_MINI_CHEST = block(Utils.id("pumpkin_mini_chest"));
    public static final MiniChestBlock RED_MINI_PRESENT = block(Utils.id("red_mini_present"));
    public static final MiniChestBlock WHITE_MINI_PRESENT = block(Utils.id("white_mini_present"));
    public static final MiniChestBlock CANDY_CANE_MINI_PRESENT = block(Utils.id("candy_cane_mini_present"));
    public static final MiniChestBlock GREEN_MINI_PRESENT = block(Utils.id("green_mini_present"));
    public static final MiniChestBlock LAVENDER_MINI_PRESENT = block(Utils.id("lavender_mini_present"));
    public static final MiniChestBlock PINK_AMETHYST_MINI_PRESENT = block(Utils.id("pink_amethyst_mini_present"));

    public static final MiniChestBlock VANILLA_WOOD_MINI_CHEST_WITH_SPARROW = block(Utils.id("vanilla_wood_mini_chest_with_sparrow"));
    public static final MiniChestBlock WOOD_MINI_CHEST_WITH_SPARROW = block(Utils.id("wood_mini_chest_with_sparrow"));
    public static final MiniChestBlock PUMPKIN_MINI_CHEST_WITH_SPARROW = block(Utils.id("pumpkin_mini_chest_with_sparrow"));
    public static final MiniChestBlock RED_MINI_PRESENT_WITH_SPARROW = block(Utils.id("red_mini_present_with_sparrow"));
    public static final MiniChestBlock WHITE_MINI_PRESENT_WITH_SPARROW = block(Utils.id("white_mini_present_with_sparrow"));
    public static final MiniChestBlock CANDY_CANE_MINI_PRESENT_WITH_SPARROW = block(Utils.id("candy_cane_mini_present_with_sparrow"));
    public static final MiniChestBlock GREEN_MINI_PRESENT_WITH_SPARROW = block(Utils.id("green_mini_present_with_sparrow"));
    public static final MiniChestBlock LAVENDER_MINI_PRESENT_WITH_SPARROW = block(Utils.id("lavender_mini_present_with_sparrow"));
    public static final MiniChestBlock PINK_AMETHYST_MINI_PRESENT_WITH_SPARROW = block(Utils.id("pink_amethyst_mini_present_with_sparrow"));

    private static <T extends Block> T block(ResourceLocation id) {
        //noinspection unchecked
        return (T) Registry.BLOCK.get(id);
    }
}
