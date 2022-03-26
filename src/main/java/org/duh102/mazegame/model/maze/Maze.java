package org.duh102.mazegame.model.maze;

import org.duh102.mazegame.util.Point2DInt;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Maze implements Serializable {
    private Tile[][] tiles;
    private Point2DInt entrance;
    private Point2DInt exit;

    public Maze(Tile[][] tiles, Point2DInt entrance, Point2DInt exit) {
        this.tiles = tiles;
        this.entrance = entrance;
        this.exit = exit;
    }
    public Maze(int xSize, int ySize, Point2DInt entrance, Point2DInt exit) {
        tiles = new Tile[xSize][ySize];
        for(int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                tiles[x][y] = new Tile();
            }
        }
        if(isIn(entrance)) {
            this.entrance = entrance;
        } else {
            this.entrance = Point2DInt.of(0, 0);
        }
        if(isIn(exit)) {
            this.exit = exit;
        } else {
            this.exit = Point2DInt.of(0,0);
        }
    }
    public Maze(int xSize, int ySize) {
        this(xSize, ySize, Point2DInt.of(0,0), Point2DInt.of(0,0));
    }
    public Maze(Maze copyFrom, int newX, int newY) {
        tiles = new Tile[newX][newY];
        for(int x = 0; x < newX; x++) {
            for(int y = 0; y < newY; y++) {
                Point2DInt loc = Point2DInt.of(x, y);
                Tile newTile = new Tile();
                if(copyFrom.isIn(loc)) {
                    newTile = new Tile(copyFrom.getTileAt(loc));
                }
                tiles[x][y] = newTile;
            }
        }
        if(isIn(copyFrom.getEntrance())) {
            this.entrance = copyFrom.getEntrance().copy();
        } else {
            this.entrance = Point2DInt.of(0,0);
        }
        if(isIn(copyFrom.getExit())) {
            this.exit = copyFrom.getExit().copy();
        } else {
            this.exit = Point2DInt.of(0,0);
        }
    }

    public boolean canMove(Point2DInt location, ExitDirection moving) {
        Tile tileAt = getTileAt(location);
        if(tileAt == null || !tileAt.canMoveTo(moving)) {
            return false;
        }
        Point2DInt newPos = location.add(moving.getMoveDirection());
        Tile nextTile = getTileAt(newPos);
        if(nextTile == null || !nextTile.canAcceptFrom(moving.getOpposite())) {
            return false;
        }
        return true;
    }
    public boolean canTeleport(Point2DInt target) {
        Tile tileAt = getTileAt(target);
        return tileAt != null;
    }
    public Tile getTileAt(Point2DInt location) {
        if(!isIn(location)) {
            return null;
        }
        return tiles[location.getX()][location.getY()];
    }

    public Tile[][] getTiles() {
        return tiles;
    }
    public Maze setTiles(Tile[][] tiles) {
        this.tiles = tiles;
        return this;
    }
    public Point2DInt getEntrance() {
        return entrance;
    }
    public Maze setEntrance(Point2DInt entrance) {
        this.entrance = entrance;
        return this;
    }
    public Point2DInt getExit() {
        return exit;
    }
    public Maze setExit(Point2DInt exit) {
        this.exit = exit;
        return this;
    }

    public boolean hasReachedExit(Point2DInt charLoc) {
        return exit.equals(charLoc);
    }
    public Point2DInt getSize() {
        int x = tiles.length, y = 0;
        for(int i = 0; i < tiles.length; i++) {
            if(tiles[i] != null && y < tiles[i].length) {
                y = tiles[i].length;
            }
        }
        return Point2DInt.of(x, y);
    }

    public boolean isIn(Point2DInt location) {
        Point2DInt maxSize = getSize();
        return tiles != null && tiles.length > 0
                && location.getX() >= 0 && location.getX() < maxSize.getX()
                && location.getY() >= 0 && location.getY() < maxSize.getY()
                && tiles[location.getX()].length > location.getY()
                && tiles[location.getX()][location.getY()] != null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(tiles), entrance, exit);
    }

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof Maze)) {
            return false;
        }
        Maze other = (Maze)obj;
        return Arrays.deepEquals(tiles, other.getTiles()) && Objects.equals(entrance, other.getEntrance()) && Objects.equals(exit, other.getExit());
    }

    @Override
    public String toString() {
        return String.format("Maze(Sz %s, Ent %s, Ex %s)", getSize(), getEntrance(), getExit());
    }
}
