package org.duh102.mazegame.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestBeanRegistry {
    private BeanRegistry beanRegistry = new BeanRegistry();

    @Test
    public void testGetAndRetrieveBean() {
        Double value = Double.valueOf(1.2);
        beanRegistry.registerBean(value, "special");
        assertThat(beanRegistry.getBean(Double.class, "special")).isEqualTo(value);
    }

    @Test
    public void testGetAndRetrieveByName() {
        String aString = "This is a string";
        Double value = Double.valueOf(2.3);
        Double value2 = Double.valueOf(10);
        beanRegistry.registerBean(aString, "string")
                .registerBean(value, "d1")
                .registerBean(value2, "d2");
        assertThat(beanRegistry.getBean(Double.class, "d1")).isEqualTo(value);
    }
}
