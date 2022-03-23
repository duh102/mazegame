package org.duh102.mazegame.model.exception.beanregistry;

public class BeanRegistryException extends RuntimeException {
    public BeanRegistryException(){ super(); }
    public BeanRegistryException(String message) {
        super(message);
    }
}
