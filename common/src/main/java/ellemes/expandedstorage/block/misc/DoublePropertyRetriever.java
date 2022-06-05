package ellemes.expandedstorage.block.misc;

import java.util.Optional;

public final class DoublePropertyRetriever<A> implements PropertyRetriever<A> {
    private final A first;
    private final A second;

    public DoublePropertyRetriever(A first, A second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public <B> Optional<B> get(Property<A, B> property) {
        return Optional.ofNullable(property.get(first, second));
    }
}
