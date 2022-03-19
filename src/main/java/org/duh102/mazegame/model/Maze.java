package org.duh102.mazegame.model;

import org.duh102.mazegame.util.Point2DInt;

public class Maze {
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
        if(tileAt == null || !tileAt.connects(moving)) {
            return false;
        }
        Point2DInt newPos = location.add(moving.getMoveDirection());
        Tile nextTile = getTileAt(newPos);
        if(nextTile == null || nextTile.connects(moving.getOpposite())) {
            return false;
        }
        return true;
    }
    public Tile getTileAt(Point2DInt location) {
        if(!isIn(location)) {
            return null;
        }
        return tiles[location.getX()][location.getY()];
    }
    public Point2DInt getEntrance() {
        return entrance;
    }
    public Point2DInt getExit() {
        return exit;
    }
    public boolean hasReachedExit(Point2DInt charLoc) {
        return exit.equals(charLoc);
    }

    public boolean isIn(Point2DInt location) {
        return tiles != null && tiles.length > 0
                && location.getX()>= 0 && location.getX() < tiles.length
                && location.getY() >=0 && location.getY() < tiles[0].length
                && tiles[location.getX()][location.getY()] != null;
    }
}
