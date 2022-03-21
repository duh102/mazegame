package org.duh102.mazegame.util;

public class ResourceFileLoader {
    ClassLoader classLoader;
    public ResourceFileLoader() {
        this.classLoader = getClass().getClassLoader();
    }
}
