package ellemes.expandedstorage.common.mixin;

import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import ellemes.expandedstorage.fixer.ES1_17_1Schema2707v1;
import ellemes.expandedstorage.fixer.ES1_18_0Schema2851v1;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.AddNewChoices;
import net.minecraft.util.datafix.fixes.BlockRenameFix;
import net.minecraft.util.datafix.fixes.ItemRenameFix;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.fixes.StatsRenameFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(DataFixers.class)
public abstract class DataFixerEntrypoint {
    @Inject(
            method = "addFixers(Lcom/mojang/datafixers/DataFixerBuilder;)V",
            at = @At(value = "INVOKE",
                    target = "Lcom/mojang/datafixers/DataFixerBuilder;addSchema(ILjava/util/function/BiFunction;)Lcom/mojang/datafixers/schemas/Schema;",
                    ordinal = 131,
                    remap = false
            )
    )
    private static void expandedstorage_register1_17_1Schema(DataFixerBuilder builder, CallbackInfo ci) {
        Schema es1_17_1Schema = builder.addSchema(2707, 1, ES1_17_1Schema2707v1::new);
        builder.addFixer(new AddNewChoices(es1_17_1Schema, "Add Expanded Storage BE", References.BLOCK_ENTITY));
    }

    @Inject(
            method = "addFixers(Lcom/mojang/datafixers/DataFixerBuilder;)V",
            at = @At(value = "INVOKE",
                    target = "Lcom/mojang/datafixers/DataFixerBuilder;addFixer(Lcom/mojang/datafixers/DataFix;)V",
                    ordinal = 216,
                    shift = At.Shift.AFTER,
                    remap = false
            )
    )
    private static void expandedstorage_register1_18_0Schema(DataFixerBuilder builder, CallbackInfo ci) {
        Schema es1_18_0Schema = builder.addSchema(2852, 1, ES1_18_0Schema2851v1::new);
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
