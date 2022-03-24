package org.duh102.mazegame.util.beanreg;

public class CachedBeanRetriever<T> {
    private BeanRegistry registry;
    private Class<T> classToRetrieve;
    private String nameToRetrieve;
    private T cachedBean;
    public CachedBeanRetriever(BeanRegistry registry, Class<T> classToRetrieve, String nameToRetrieve) {
        this.registry = registry;
        this.classToRetrieve = classToRetrieve;
        this.nameToRetrieve = nameToRetrieve;
    }
    public CachedBeanRetriever(BeanRegistry registry, Class<T> classToRetrieve) {
        this(registry, classToRetrieve, null);
    }

    public synchronized T get() {
        if(cachedBean == null) {
            cachedBean = registry.getBean(classToRetrieve, nameToRetrieve);
        }
        return cachedBean;
    }
    public synchronized boolean hasBean(){
        return cachedBean != null;
    }
}
