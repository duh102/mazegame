package org.duh102.mazegame.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestPoint2DInt {
    @Test
    public void testAdd() {
        Point2DInt a = Point2DInt.of(1,2);
        Point2DInt b = Point2DInt.of(1,1);
        assertThat(a.add(b)).isEqualTo(Point2DInt.of(2,3));
        a = Point2DInt.of(0,0);
        b = Point2DInt.of(-1, 23);
        assertThat(a.add(b)).isEqualTo(Point2DInt.of(-1, 23));
    }

    @Test
    public void testSub() {
        Point2DInt a = Point2DInt.of(1,2);
        Point2DInt b = Point2DInt.of(1,1);
        assertThat(a.sub(b)).isEqualTo(Point2DInt.of(0,1));
        assertThat(b.sub(a)).isEqualTo(Point2DInt.of(0,-1));
        a = Point2DInt.of(0,0);
        b = Point2DInt.of(-1, 23);
        assertThat(a.sub(b)).isEqualTo(Point2DInt.of(1,-23));
        assertThat(b.sub(a)).isEqualTo(Point2DInt.of(-1,23));
    }
}
