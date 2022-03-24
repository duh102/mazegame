package org.duh102.mazegame.util;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class TestBeanRegistry {
    private BeanRegistry beanRegistry = new BeanRegistry();

    @Test
    public void testSetAndRetrieveBean() {
        Double value = Double.valueOf(1.2);
        beanRegistry.registerBean(value, "special");
        assertThat(beanRegistry.getBean(Double.class, "special")).isEqualTo(value);
    }
    @Test
    public void testSetAndRetrieveAnyBean() {
        String aString = "This is a string";
        Double value = Double.valueOf(1.2);
        Double value2 = Double.valueOf(2.3);
        Double value3 = Double.valueOf(10);
        beanRegistry.registerBean(aString, "s1")
                .registerBean(value, "d1")
                .registerBean(value2, "d2")
                .registerBean(value3, "d3");
        assertThat(beanRegistry.getBean(Double.class)).isInstanceOf(Double.class);
    }

    @Test
    public void testSetAndRetrieveByName() {
        String aString = "This is a string";
        Double value = Double.valueOf(2.3);
        Double value2 = Double.valueOf(10);
        beanRegistry.registerBean(aString, "string")
                .registerBean(value, "d1")
                .registerBean(value2, "d2");
        assertThat(beanRegistry.getBean(Double.class, "d1")).isEqualTo(value);
    }

    @Test
    public void testGetAll() {
        String aString = "This is a string";
        Double value = Double.valueOf(2.3);
        Double value2 = Double.valueOf(10);
        beanRegistry.registerBean(aString, "string")
                .registerBean(value, "d1")
                .registerBean(value2, "d2");
        assertThat(beanRegistry.getBeansOfType(Double.class)).containsExactlyInAnyOrder(value, value2);
    }

    @Test
    public void testGetInheritors() {
        A a = new A();
        B b = new B();
        C c1 = new C();
        C c2 = new C();
        D d = new D();
        beanRegistry.registerBean(a, "a");
        beanRegistry.registerBean(b, "b");
        beanRegistry.registerBean(c1, "c1");
        beanRegistry.registerBean(c2, "c2");
        beanRegistry.registerBean(d, "d");
        Collection<C> cBeans = beanRegistry.getBeansOfType(C.class);
        assertThat(cBeans).containsExactlyInAnyOrder(c1, c2);
        Collection<D> dBeans = beanRegistry.getBeansOfType(D.class);
        assertThat(dBeans).containsExactlyInAnyOrder(d);
        Collection<B> bBeans = beanRegistry.getBeansOfType(B.class);
        assertThat(bBeans).containsExactlyInAnyOrder(b, (B)d);
        Collection<A> aBeans = beanRegistry.getBeansOfType(A.class);
        assertThat(aBeans).containsExactlyInAnyOrder(a, (A)b, (A)c1, (A)c2, (A)d);
    }

    public class A {
    }
    public class B extends A {
    }
    public class C extends A {
    }
    public class D extends B {
    }
}
