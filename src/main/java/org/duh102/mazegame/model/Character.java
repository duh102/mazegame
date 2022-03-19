package org.duh102.mazegame.model;

import org.duh102.mazegame.util.Point2DInt;

public class Character {
    private Point2DInt position;

    public Character(Point2DInt location) {
        this.position = location;
    }
    public Character() {
        this(Point2DInt.of(0,0));
    }

    public Point2DInt getPosition() {
        return position;
    }
    public Character teleport(Point2DInt newLoc) {
        this.position = newLoc;
        return this;
    }
    public Character move(ExitDirection direction) {
        this.position = this.position.add(direction.getMoveDirection());
        return this;
    }
}
