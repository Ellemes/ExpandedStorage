package ellemes.expandedstorage.registration;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public final class NamedValue<T> {
    private final ResourceLocation name;
    private final Supplier<T> valueSupplier;
    private T value;

    public NamedValue(ResourceLocation name, Supplier<T> valueSupplier) {
        this.name = name;
        this.valueSupplier = valueSupplier;
    }

    public ResourceLocation getName() {
        return name;
    }

    public T getValue() {
        if (value == null) {
            value = valueSupplier.get();
        }
        return value;
    }
}
