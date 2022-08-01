package ellemes.expandedstorage.fixer;

import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import net.minecraft.util.datafix.fixes.AddNewChoices;
import net.minecraft.util.datafix.fixes.BlockRenameFix;
import net.minecraft.util.datafix.fixes.ItemRenameFix;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.fixes.StatsRenameFix;

import java.util.Map;

public class DataFixerUtils {

    public static void register1_17DataFixer(DataFixerBuilder builder, int version, int subVersion) {
        Schema es1_17_1Schema = builder.addSchema(version, subVersion, ES1_17_1Schema2707v1::new);
        builder.addFixer(new AddNewChoices(es1_17_1Schema, "Add Expanded Storage BE", References.BLOCK_ENTITY));
    }

    public static void register1_18DataFixer(DataFixerBuilder builder, int version, int subVersion) {
        Schema es1_18_0Schema = builder.addSchema(version, subVersion, ES1_18_0Schema2851v1::new);
        builder.addFixer(new AddNewChoices(es1_18_0Schema, "Add renamed Expanded Storage BE", References.BLOCK_ENTITY));
        builder.addFixer(BlockRenameFix.create(es1_18_0Schema, "Rename ES Blocks: christmas_chest -> present", id -> {
            if (id.equals("expandedstorage:christmas_chest")) return "expandedstorage:present";
            return id;
        }));
        builder.addFixer(ItemRenameFix.create(es1_18_0Schema, "Rename ES Items: christmas_chest -> present, chest_mutator -> storage_mutator", id -> {
            if (id.equals("expandedstorage:christmas_chest")) return "expandedstorage:present";
            else if (id.equals("expandedstorage:chest_mutator")) return "expandedstorage:storage_mutator";
            return id;
        }));
        builder.addFixer(new StatsRenameFix(es1_18_0Schema, "Rename ES Stats: open_christmas_chest -> open_present", Map.of("expandedstorage:open_christmas_chest", "expandedstorage:open_present")));
    }
}
