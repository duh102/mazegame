package org.duh102.mazegame.util;

public interface Point2DInt extends Pair<Integer, Integer>, Comparable<Point2DInt> {
    Point2DInt add(Point2DInt by);
    Point2DInt copy();
    int getX();
    int getY();
    static Point2DInt of(int x, int y) {
        return new ImmutablePoint2DInt(x, y);
    }
}
