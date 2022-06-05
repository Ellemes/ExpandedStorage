package ellemes.expandedstorage.fixer;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

import java.util.Map;
import java.util.function.Supplier;

public final class ES1_18_0Schema2851v1 extends NamespacedSchema {
    public ES1_18_0Schema2851v1(int versionKey, Schema parent) {
        super(versionKey, parent);
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
        Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
        schema.register(map, "expandedstorage:chest", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
        schema.register(map, "expandedstorage:old_chest", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
        return map;
    }
}
