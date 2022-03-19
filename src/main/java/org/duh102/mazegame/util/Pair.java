package org.duh102.mazegame.util;

public interface Pair<U,V> {
    U getFirst();
    V getSecond();
    static <U, V> Pair<U, V> of(U first, V second) {
        return new ImmutablePair<>(first, second);
    }
}
