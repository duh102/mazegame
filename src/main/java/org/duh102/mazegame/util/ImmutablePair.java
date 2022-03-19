package org.duh102.mazegame.util;

import java.util.Objects;

public class ImmutablePair<U, V> implements Pair<U, V> {
    private final U first;
    private final V second;

    public ImmutablePair(U first, V second) {
        this.first = first;
        this.second = second;
    }
    public U getFirst() {
        return first;
    }
    public V getSecond() {
        return second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof ImmutablePair) ) {
            return false;
        }
        ImmutablePair<?, ?> other = (ImmutablePair<?, ?>)obj;
        return Objects.equals(first, other.getFirst()) && Objects.equals(second, other.getSecond());
    }
}
