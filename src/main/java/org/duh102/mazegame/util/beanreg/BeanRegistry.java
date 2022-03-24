package org.duh102.mazegame.util.beanreg;

import org.duh102.mazegame.model.exception.beanregistry.DuplicateBeanRegistrationException;
import org.duh102.mazegame.model.exception.beanregistry.NoBeanFoundException;

import java.util.*;

public class BeanRegistry {
    Map<Class<?>, Map<String, Object>> beanRegistry;
    Map<Object, Set<BeanChangeListener>> beanChangeListenerMap;

    public BeanRegistry() {
        beanRegistry = new HashMap<>();
        beanChangeListenerMap = new HashMap<>();
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
        Collection<Class<T>> classes = validKeys(beanClass);
        Class<T> foundClass = classes.stream().findFirst().orElse(null);
        if(foundClass == null || beanRegistry.get(foundClass).size() < 1) {
            throw new NoBeanFoundException(beanClass);
        }
        Map<String, Object> beansOfType = beanRegistry.get(foundClass);
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
        Collection<Class<T>> classes = validKeys(beanClass);
        if(classes.isEmpty()) {
            throw new NoBeanFoundException(beanClass);
        }
        Collection<T> beans = new ArrayList<>();
        for(Class<T> cls : classes) {
            if(beanRegistry.containsKey(cls)) {
                beans.addAll((Collection<T>) beanRegistry.get(cls).values());
            }
        }
        return beans;
    }
    private <T> Collection<Class<T>> validKeys(Class<T> beanClass) {
        List<Class<T>> classes = new ArrayList<>();
        for(Class<?> cls : beanRegistry.keySet()) {
            if(beanClass.isAssignableFrom(cls)) {
                classes.add((Class<T>)cls);
            }
        }
        return classes;
    }
}
