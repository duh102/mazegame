package org.duh102.mazegame.util;

import java.io.Serializable;

public interface Point2DInt extends Comparable<Point2DInt>, Serializable {
    Point2DInt add(Point2DInt by);
    Point2DInt sub(Point2DInt by);
    Point2DInt copy();
    int getX();
    int getY();
    static Point2DInt of(int x, int y) {
        return new MutablePoint2DInt(x, y);
    }
}
