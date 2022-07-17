package ellemes.expandedstorage.datagen.providers;

import ellemes.expandedstorage.datagen.content.ModItems;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

public class ModelHelper {
    public static void registerItemModels(Consumer<Item> consumer) {
        consumer.accept(ModItems.STORAGE_MUTATOR);
        consumer.accept(ModItems.WOOD_TO_IRON_CONVERSION_KIT);
        consumer.accept(ModItems.WOOD_TO_GOLD_CONVERSION_KIT);
        consumer.accept(ModItems.WOOD_TO_DIAMOND_CONVERSION_KIT);
        consumer.accept(ModItems.WOOD_TO_OBSIDIAN_CONVERSION_KIT);
        consumer.accept(ModItems.WOOD_TO_NETHERITE_CONVERSION_KIT);
        consumer.accept(ModItems.IRON_TO_GOLD_CONVERSION_KIT);
        consumer.accept(ModItems.IRON_TO_DIAMOND_CONVERSION_KIT);
        consumer.accept(ModItems.IRON_TO_OBSIDIAN_CONVERSION_KIT);
        consumer.accept(ModItems.IRON_TO_NETHERITE_CONVERSION_KIT);
        consumer.accept(ModItems.GOLD_TO_DIAMOND_CONVERSION_KIT);
        consumer.accept(ModItems.GOLD_TO_OBSIDIAN_CONVERSION_KIT);
        consumer.accept(ModItems.GOLD_TO_NETHERITE_CONVERSION_KIT);
        consumer.accept(ModItems.DIAMOND_TO_OBSIDIAN_CONVERSION_KIT);
        consumer.accept(ModItems.DIAMOND_TO_NETHERITE_CONVERSION_KIT);
        consumer.accept(ModItems.OBSIDIAN_TO_NETHERITE_CONVERSION_KIT);

//        consumer.accept(ModItems.WOOD_CHEST_MINECART);
//        consumer.accept(ModItems.PUMPKIN_CHEST_MINECART);
//        consumer.accept(ModItems.PRESENT_MINECART);
//        consumer.accept(ModItems.BAMBOO_CHEST_MINECART);
//        consumer.accept(ModItems.IRON_CHEST_MINECART);
//        consumer.accept(ModItems.GOLD_CHEST_MINECART);
//        consumer.accept(ModItems.DIAMOND_CHEST_MINECART);
//        consumer.accept(ModItems.OBSIDIAN_CHEST_MINECART);
//        consumer.accept(ModItems.NETHERITE_CHEST_MINECART);
    }
}
