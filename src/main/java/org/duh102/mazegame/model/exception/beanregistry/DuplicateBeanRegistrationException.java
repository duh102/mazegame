package org.duh102.mazegame.model.exception.beanregistry;

public class DuplicateBeanRegistrationException extends BeanRegistryException {
    public DuplicateBeanRegistrationException(Class<?> type, String name) {
        super(String.format("Attempted to register duplicate bean %s for type %s", name, type.getName()));
    }
}
