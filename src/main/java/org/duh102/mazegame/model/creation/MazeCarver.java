package org.duh102.mazegame.model.creation;

import org.duh102.mazegame.model.maze.ExitDirection;
import org.duh102.mazegame.model.maze.Maze;
import org.duh102.mazegame.model.maze.Tile;
import org.duh102.mazegame.model.exception.maze.NotInMazeException;
import org.duh102.mazegame.util.Point2DInt;

import java.util.Random;

public class MazeCarver {
    Maze maze;
    Point2DInt carveLocation;

    public MazeCarver(int xSize, int ySize) {
        this(new Maze(xSize, ySize));
    }
    public MazeCarver(Maze maze) {
        this.maze = maze;
        carveLocation = maze.getEntrance();
    }
    public MazeCarver() {
        this(2,2);
    }
    public MazeCarver repositionKnife(ExitDirection direction) throws NotInMazeException {
        Point2DInt newPos = carveLocation.add(direction.getMoveDirection());
        return repositionKnife(newPos);
    }
    public MazeCarver repositionKnife(Point2DInt newPos) throws NotInMazeException {
        if(!maze.isIn(newPos)) {
            throw new NotInMazeException();
        }
        carveLocation = newPos;
        return this;
    }
    public MazeCarver carve(ExitDirection direction) throws NotInMazeException {
        Point2DInt newPos = carveLocation.add(direction.getMoveDirection());
        if(!maze.isIn(newPos)) {
            throw new NotInMazeException();
        }
        Tile exiting = maze.getTileAt(carveLocation);
        Tile entering = maze.getTileAt(newPos);
        carveLocation = newPos;
        exiting.getExits().addExit(direction);
        entering.getExits().addExit(direction.getOpposite());
        return this;
    }
    public MazeCarver seal(ExitDirection direction) throws NotInMazeException {
        Point2DInt newPos = carveLocation.add(direction.getMoveDirection());
        if(!maze.isIn(newPos)) {
            throw new NotInMazeException();
        }
        Tile exiting = maze.getTileAt(carveLocation);
        Tile entering = maze.getTileAt(newPos);
        carveLocation = newPos;
        exiting.getExits().closeExit(direction);
        entering.getExits().closeExit(direction.getOpposite());
        return this;
    }
    public MazeCarver randomizeTileAppearance() {
        Tile here = maze.getTileAt(carveLocation);
        here.setVariantSeed(new Random().nextInt());
        return this;
    }

    public Maze getMaze() {
        return maze;
    }
    public MazeCarver setMaze(Maze maze) {
        this.maze = maze;
        carveLocation = maze.getEntrance();
        return this;
    }
    public Point2DInt getCarveLocation() {
        return carveLocation;
    }
    public MazeCarver setEntrance(Point2DInt entrance) throws NotInMazeException {
        if(!maze.isIn(entrance)) {
            throw new NotInMazeException();
        }
        maze.setEntrance(entrance);
        return this;
    }
    public MazeCarver setEntrance() throws NotInMazeException {
        return setEntrance(carveLocation);
    }
    public MazeCarver setExit(Point2DInt exit) throws NotInMazeException {
        if(!maze.isIn(exit)) {
            throw new NotInMazeException();
        }
        maze.setExit(exit);
        return this;
    }
    public MazeCarver setExit() throws NotInMazeException {
        return setExit(carveLocation);
    }
}
