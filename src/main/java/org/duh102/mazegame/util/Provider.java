package org.duh102.mazegame.util;

public class Provider<T> {
    private T toProvide;
    public Provider(T provided) {
        this.toProvide = provided;
    }
    public synchronized T get() {
        return toProvide;
    }
    public synchronized Provider<T> replace(T toProvide) {
        this.toProvide = toProvide;
        return this;
    }
}
