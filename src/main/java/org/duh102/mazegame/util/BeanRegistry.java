package org.duh102.mazegame.util;

import org.duh102.mazegame.model.exception.beanregistry.DuplicateBeanRegistrationException;
import org.duh102.mazegame.model.exception.beanregistry.NoBeanFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BeanRegistry {
    Map<Class<?>, Map<String, Object>> beanRegistry;

    public BeanRegistry() {
        beanRegistry = new HashMap<>();
    }
    public synchronized <T> BeanRegistry registerBean(T bean, String name) {
        Class<?> beanClass = bean.getClass();
        if(!beanRegistry.containsKey(beanClass)) {
            beanRegistry.put(beanClass, new HashMap<>());
        }
        Map<String, Object> beansOfClass = beanRegistry.get(beanClass);
        if(beansOfClass.containsKey(name)) {
            throw new DuplicateBeanRegistrationException(beanClass, name);
        }
        beansOfClass.put(name, bean);
        return this;
    }
    public synchronized <T> T getBean(Class<T> beanClass, String name) throws NoBeanFoundException {
        if(!beanRegistry.containsKey(beanClass) || beanRegistry.get(beanClass).size() < 1) {
            throw new NoBeanFoundException(beanClass);
        }
        Map<String, Object> beansOfType = beanRegistry.get(beanClass);
        if(name != null && !beansOfType.containsKey(name)) {
            throw new NoBeanFoundException(beanClass, name);
        }
        if(name == null) {
            return (T)(new ArrayList<>(beansOfType.values())).get(0);
        } else {
            return (T)beansOfType.get(name);
        }
    }
    public synchronized <T> T getBean(Class<T> beanClass) throws NoBeanFoundException {
        return getBean(beanClass, null);
    }
    public synchronized <T> Collection<T> getBeansOfType(Class<T> beanClass) throws NoBeanFoundException {
        if(!beanRegistry.containsKey(beanClass) || beanRegistry.get(beanClass).size() < 1) {
            throw new NoBeanFoundException(beanClass);
        }
        Map<String, Object> beansOfType = beanRegistry.get(beanClass);
        return (Collection<T>)beansOfType.values();
    }
}
