package ellemes.expandedstorage.block.misc;

import java.util.Optional;

public final class SinglePropertyRetriever<A> implements PropertyRetriever<A> {
    private final A single;

    public SinglePropertyRetriever(A single) {
        this.single = single;
    }

    @Override
    public <B> Optional<B> get(Property<A, B> property) {
        return Optional.ofNullable(property.get(single));
    }
}
