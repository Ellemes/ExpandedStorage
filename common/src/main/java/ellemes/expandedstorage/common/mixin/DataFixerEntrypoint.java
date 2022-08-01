package ellemes.expandedstorage.common.mixin;

import com.mojang.datafixers.DataFixerBuilder;
import ellemes.expandedstorage.fixer.DataFixerUtils;
import net.minecraft.util.datafix.DataFixers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
        DataFixerUtils.register1_17DataFixer(builder, 2707, 1);
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
        DataFixerUtils.register1_18DataFixer(builder, 2851, 1);
    }
}
