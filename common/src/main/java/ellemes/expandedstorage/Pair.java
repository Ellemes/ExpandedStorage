package ellemes.expandedstorage;

import java.util.Objects;

@SuppressWarnings("ClassCanBeRecord")
public final class Pair<F, S> {
    private final F first;
    private final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Pair other)
            return first.equals(other.first) && second.equals(other.second);
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
}
