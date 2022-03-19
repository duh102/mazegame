package org.duh102.mazegame.util;

import java.util.Objects;

public class ImmutablePoint2DInt implements Point2DInt {
    private int x, y;

    public ImmutablePoint2DInt(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Point2DInt add(Point2DInt by) {
        if(by == null) {
            return this;
        }
        return Point2DInt.of(getX() + by.getX(), getY() + by.getY());
    }

    @Override
    public Point2DInt copy() {
        return new ImmutablePoint2DInt(x, y);
    }

    @Override
    public int getX() {
        return x;
    }
    @Override
    public Integer getFirst() {
        return x;
    }
    @Override
    public int getY() {
        return y;
    }
    @Override
    public Integer getSecond() {
        return y;
    }

    @Override
    public int compareTo(Point2DInt other) {
        if(other == null) {
            return -1;
        }
        if(other.getX() != getX()) {
            return Integer.compare(getX(), other.getX());
        }
        return Integer.compare(getY(), other.getY());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
