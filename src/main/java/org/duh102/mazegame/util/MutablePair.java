package org.duh102.mazegame.util;

import java.util.Objects;

public class MutablePair<U, V> implements Pair<U, V> {
    private U first;
    private V second;

    public MutablePair(U first, V second) {
        this.first = first;
        this.second = second;
    }
    public U getFirst() {
        return first;
    }
    public V getSecond() {
        return second;
    }
    public MutablePair<U, V> setFirst(U first) {
        this.first = first;
        return this;
    }
    public MutablePair<U, V> setSecond(V second) {
        this.second = second;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof MutablePair) ) {
            return false;
        }
        MutablePair<?, ?> other = (MutablePair<?, ?>)obj;
        return Objects.equals(first, other.getFirst()) && Objects.equals(second, other.getSecond());
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", first, second);
    }
}
