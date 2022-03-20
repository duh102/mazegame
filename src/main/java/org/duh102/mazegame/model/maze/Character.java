package org.duh102.mazegame.model.maze;
import org.duh102.mazegame.util.Point2DInt;

import java.io.Serializable;
import java.util.Objects;

public class Character implements Serializable {
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
    public Character setPosition(Point2DInt newPos) {
        return teleport(newPos);
    }
    public Character teleport(Point2DInt newLoc) {
        this.position = newLoc;
        return this;
    }
    public Character move(ExitDirection direction) {
        this.position = this.position.add(direction.getMoveDirection());
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof Character)) {
            return false;
        }
        Character other = (Character)obj;
        return Objects.equals(position, other.getPosition());
    }

    @Override
    public String toString() {
        return String.format("Character(%s)", position.toString());
    }
}
