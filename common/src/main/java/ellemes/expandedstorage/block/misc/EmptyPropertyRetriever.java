package ellemes.expandedstorage.block.misc;

import java.util.Optional;

public final class EmptyPropertyRetriever<A> implements PropertyRetriever<A> {
    @Override
    public <B> Optional<B> get(Property<A, B> property) {
        return Optional.empty();
    }
}
