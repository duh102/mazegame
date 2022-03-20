package org.duh102.mazegame.model.maze;

import org.duh102.mazegame.util.Point2DInt;

import java.io.Serializable;

public enum ExitDirection implements Serializable {
    UP(1<<0, Point2DInt.of(0, -1)),
    RIGHT(1<<1, Point2DInt.of(1, 0)),
    DOWN(1<<2, Point2DInt.of(0, 1)),
    LEFT(1<<3, Point2DInt.of(-1, 0));

    private byte bitMask;
    private Point2DInt moveDirection;

    ExitDirection(int bitMask, Point2DInt moveDirection) {
        this.bitMask = (byte) bitMask;
        this.moveDirection = moveDirection;
    }

    public byte getBitMask() {
        return bitMask;
    }
    public Point2DInt getMoveDirection() { return moveDirection; }
    public boolean connects(ExitDirection other) {
        if( other == null ) {
            return false;
        }
        return getOpposite().equals(other);
    }
    public ExitDirection getOpposite() {
        switch(this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
            default:
                return LEFT;
        }
    }

    @Override
    public String toString() {
        return name().substring(0,1);
    }
}
