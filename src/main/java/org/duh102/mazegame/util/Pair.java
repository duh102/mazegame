package org.duh102.mazegame.util;

import java.io.Serializable;

public interface Pair<U,V> extends Serializable {
    U getFirst();
    V getSecond();
    static <U, V> Pair<U, V> of(U first, V second) {
        return new MutablePair<>(first, second);
    }
}
