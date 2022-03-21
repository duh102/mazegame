package org.duh102.mazegame.util;

import java.util.Objects;

public class MutablePoint2DInt implements Point2DInt {
    private int x, y;

    public MutablePoint2DInt(int x, int y) {
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
    public Point2DInt sub(Point2DInt by) {
        if(by == null) {
            return this;
        }
        return Point2DInt.of(getX() - by.getX(), getY() - by.getY());
    }

    @Override
    public Point2DInt copy() {
        return new MutablePoint2DInt(x, y);
    }

    @Override
    public int getX() {
        return x;
    }
    @Override
    public int getY() {
        return y;
    }
    public Point2DInt setX(int x) {
        this.x = x;
        return this;
    }
    public Point2DInt setY(int y) {
        this.y = y;
        return this;
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
    public boolean equals(Object obj) {
        if(! (obj instanceof Point2DInt) ) {
            return false;
        }
        Point2DInt other = (Point2DInt)obj;
        return compareTo(other) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}
