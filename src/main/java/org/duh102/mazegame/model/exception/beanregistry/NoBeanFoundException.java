package org.duh102.mazegame.model.exception.beanregistry;

public class NoBeanFoundException extends BeanRegistryException {
    Class<?> searchedType;
    String searchedName;
    public NoBeanFoundException(Class<?> type, String name) {
        super(String.format("No bean found of type %s and name %s", type.getName(), name));
        this.searchedName = name;
        this.searchedType = type;
    }
    public NoBeanFoundException(Class<?> type) {
        super(String.format("No beans found of type %s", type.getName()));
        this.searchedType = type;
    }

    public Class<?> getSearchedType() {
        return searchedType;
    }
    public String getSearchedName() {
        return searchedName;
    }
}
