package ellemes.expandedstorage.forge.datagen.providers;

import ellemes.expandedstorage.Utils;
import ellemes.expandedstorage.forge.datagen.content.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;

public final class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {
    public ItemModelProvider(DataGenerator generator, ExistingFileHelper fileHelper) {
        super(generator, Utils.MOD_ID, fileHelper);
    }

    @Override
    protected void registerModels() {
        this.simple(ModItems.STORAGE_MUTATOR);
        this.simple(ModItems.WOOD_TO_IRON_CONVERSION_KIT);
        this.simple(ModItems.WOOD_TO_GOLD_CONVERSION_KIT);
        this.simple(ModItems.WOOD_TO_DIAMOND_CONVERSION_KIT);
        this.simple(ModItems.WOOD_TO_OBSIDIAN_CONVERSION_KIT);
        this.simple(ModItems.WOOD_TO_NETHERITE_CONVERSION_KIT);
        this.simple(ModItems.IRON_TO_GOLD_CONVERSION_KIT);
        this.simple(ModItems.IRON_TO_DIAMOND_CONVERSION_KIT);
        this.simple(ModItems.IRON_TO_OBSIDIAN_CONVERSION_KIT);
        this.simple(ModItems.IRON_TO_NETHERITE_CONVERSION_KIT);
        this.simple(ModItems.GOLD_TO_DIAMOND_CONVERSION_KIT);
        this.simple(ModItems.GOLD_TO_OBSIDIAN_CONVERSION_KIT);
        this.simple(ModItems.GOLD_TO_NETHERITE_CONVERSION_KIT);
        this.simple(ModItems.DIAMOND_TO_OBSIDIAN_CONVERSION_KIT);
        this.simple(ModItems.DIAMOND_TO_NETHERITE_CONVERSION_KIT);
        this.simple(ModItems.OBSIDIAN_TO_NETHERITE_CONVERSION_KIT);

        //this.chest(ModItems.WOOD_CHEST);
        //this.chest(ModItems.PUMPKIN_CHEST);
        //this.chest(ModItems.PRESENT);
        //this.chest(ModItems.IRON_CHEST);
        //this.chest(ModItems.GOLD_CHEST);
        //this.chest(ModItems.DIAMOND_CHEST);
        //this.chest(ModItems.OBSIDIAN_CHEST);
        //this.chest(ModItems.NETHERITE_CHEST);

        //this.oldChest(ModItems.OLD_WOOD_CHEST);
        //this.oldChest(ModItems.OLD_IRON_CHEST);
        //this.oldChest(ModItems.OLD_GOLD_CHEST);
        //this.oldChest(ModItems.OLD_DIAMOND_CHEST);
        //this.oldChest(ModItems.OLD_OBSIDIAN_CHEST);
        //this.oldChest(ModItems.OLD_NETHERITE_CHEST);

        //this.barrel(ModItems.IRON_BARREL);
        //this.barrel(ModItems.GOLD_BARREL);
        //this.barrel(ModItems.DIAMOND_BARREL);
        //this.barrel(ModItems.OBSIDIAN_BARREL);
        //this.barrel(ModItems.NETHERITE_BARREL);
    }

    @SuppressWarnings("ConstantConditions")
    private void simple(Item item) {
        String itemId = item.getRegistryName().getPath();
        this.withExistingParent(itemId, mcLoc("item/generated")).texture("layer0", "item/" + itemId);
    }

    @SuppressWarnings("ConstantConditions")
    private void chest(Item item) {
        this.withExistingParent(item.getRegistryName().getPath(), mcLoc("item/chest"));
    }

    @SuppressWarnings("ConstantConditions")
    private void oldChest(BlockItem item) {
        this.getBuilder(item.getRegistryName().getPath()).parent(this.getExistingFile(Utils.id("block/" + item.getBlock().getRegistryName().getPath() + "/single")));
    }

    @SuppressWarnings("ConstantConditions")
    private void barrel(BlockItem item) {
        this.getBuilder(item.getRegistryName().getPath()).parent(this.getExistingFile(Utils.id("block/" + item.getBlock().getRegistryName().getPath())));
    }

    @Override
    public String getName() {
        return "Expanded Storage - Item Models";
    }
}
