package ninjaphenix.expandedstorage.base;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import ninjaphenix.expandedstorage.base.internal_api.BaseApi;
import ninjaphenix.expandedstorage.base.internal_api.Utils;
import ninjaphenix.expandedstorage.base.item.StorageMutator;

public final class BaseCommon {
    private static final int ICON_SUITABILITY = 0;

    private BaseCommon() {

    }

    static void initialize() {
        BaseApi.getInstance().offerTabIcon(Items.CHEST, ICON_SUITABILITY);
        BaseApi.getInstance().defineTierUpgradePath(Utils.translation("itemGroup.expandedstorage"), Utils.WOOD_TIER, Utils.IRON_TIER,
                Utils.GOLD_TIER, Utils.DIAMOND_TIER, Utils.OBSIDIAN_TIER, Utils.NETHERITE_TIER);
        BaseApi.getInstance().register(Utils.resloc("chest_mutator"), new StorageMutator(new Item.Properties().stacksTo(1).tab(Utils.TAB)));
    }

    public static ResourceLocation registerStat(ResourceLocation stat) {
        ResourceLocation rv = Registry.register(Registry.CUSTOM_STAT, stat, stat); // Forge doesn't provide registries for stats
        Stats.CUSTOM.get(rv);
        return rv;
    }
}
