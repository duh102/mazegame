package org.duh102.mazegame.model.creation.generator;

import org.duh102.mazegame.model.creation.MazeCarver;
import org.duh102.mazegame.model.exception.maze.NotInMazeException;
import org.duh102.mazegame.model.exception.maze.generator.MazeGeneratorException;
import org.duh102.mazegame.model.maze.ExitDirection;
import org.duh102.mazegame.model.maze.Maze;
import org.duh102.mazegame.model.maze.Tile;
import org.duh102.mazegame.util.Point2DInt;

import java.util.Random;

public class AldousBroderAlg implements MazeGenerator {
    private Random random;

    public AldousBroderAlg() {
        random = new Random();
    }

    @Override
    public String getName() {
        return "Aldous-Broder";
    }

    @Override
    public Maze generate(int xSize, int ySize) throws MazeGeneratorException {
        if(xSize < 2 || ySize < 2) {
            throw new MazeGeneratorException(String.format("Requested size (%d,%d) too small", xSize, ySize));
        }
        MazeCarver carver = new MazeCarver(xSize, ySize);
        Maze maze = carver.getMaze();
        // Place the entrance somewhere in the middle
        Point2DInt mazeCenter = Point2DInt.of(xSize/2, ySize/2);
        Point2DInt entrance = Point2DInt.of(random.nextInt(2)+mazeCenter.getX()-1, random.nextInt(2)+mazeCenter.getY()-1);
        maze.setEntrance(entrance);
        Point2DInt exit;
        do {
            int side = random.nextInt(4);
            switch(side) {
                case 0: // top
                    exit = Point2DInt.of(random.nextInt(xSize), 0);
                    break;
                case 1: // right
                    exit = Point2DInt.of(xSize-1, random.nextInt(ySize));
                    break;
                case 2: // bottom
                    exit = Point2DInt.of(random.nextInt(xSize), ySize-1);
                    break;
                case 3: // left
                default:
                    exit = Point2DInt.of(0, random.nextInt(ySize));
            }
        } while(exit.equals(entrance));
        maze.setExit(exit);
        Point2DInt current = entrance.copy();
        Point2DInt next;
        ExitDirection[] directions = ExitDirection.values();
        while(!allCellsVisited(maze)) {
            ExitDirection dir = directions[random.nextInt(4)];
            next = current.add(dir.getMoveDirection());
            while(!maze.isIn(next)) {
                dir = directions[random.nextInt(4)];
                next = current.add(dir.getMoveDirection());
            }
            Tile nextTile = maze.getTileAt(next);
            if(nextTile.getTileIndex() == 0) {
                try {
                    carver.repositionKnife(current);
                    carver.carve(dir);
                } catch (NotInMazeException e) {
                    e.printStackTrace();
                }
                nextTile.setVariantSeed(random.nextInt());
            }
            current = next;
        }
        return maze;
    }
    private boolean allCellsVisited(Maze maze) {
        Tile[][] tiles = maze.getTiles();
        Point2DInt size = maze.getSize();
        for(int x = 0; x < size.getX(); x++) {
            for(int y = 0; y < size.getY(); y++) {
                if(tiles[x][y].getTileIndex() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public MazeGenerator seed(long seed) {
        random.setSeed(seed);
        return this;
    }
}
