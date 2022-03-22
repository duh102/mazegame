package org.duh102.mazegame.client;

import org.duh102.mazegame.graphics.FallbackTileMap;
import org.duh102.mazegame.graphics.MazeDisplay;
import org.duh102.mazegame.graphics.MazeResizeComponentListener;
import org.duh102.mazegame.graphics.TileMap;
import org.duh102.mazegame.model.creation.MazeCarver;
import org.duh102.mazegame.model.exception.MazeException;
import org.duh102.mazegame.model.maze.ExitDirection;
import org.duh102.mazegame.model.maze.GameBoard;
import org.duh102.mazegame.model.maze.Maze;
import org.duh102.mazegame.util.Point2DInt;

public class MazeGame {
    public static void main(String args[]) {
        // Load config
        // Look for tilesets
        // Randomly pick one
        // If none are available, use the fallback tilemap
        TileMap selectedTileMap = new FallbackTileMap();


        AnimationController ac = new AnimationController(60);
        MazeControlListener mcl = new MazeControlListener();
        MazeResizeComponentListener mrcl = new MazeResizeComponentListener();
        MazeDisplay display = new MazeDisplay(640, 480);

        Maze maze = generateDemoMap();
        GameBoard board = new GameBoard(maze);

        ac.setControlListener(mcl);
        ac.setMazeDisplay(display);

        mcl.setGameBoard(board);
        mcl.setMazeDisplay(display);

        display.setGameBoard(board);

        mrcl.setMazeDisplay(display);

        GameWindow window = new GameWindow(mcl, mrcl, display);
        mrcl.setGameWindow(window);
        ac.setGameWindow(window);
        window.setTileMap(selectedTileMap);
        window.packAndShow();
        ac.start();
    }

    public static Maze generateDemoMap() {
        try {
        MazeCarver carver = new MazeCarver(4, 4);
        carver.repositionKnife(Point2DInt.of(0, 0))
                .carve(ExitDirection.DOWN)
                .setEntrance()
                .carve(ExitDirection.DOWN)
                .carve(ExitDirection.RIGHT)
                .carve(ExitDirection.DOWN)
                .carve(ExitDirection.RIGHT)
                .carve(ExitDirection.UP)
                .carve(ExitDirection.UP)
                .carve(ExitDirection.RIGHT)
                .setExit();
        return carver.getMaze();
        } catch(MazeException me) {
            throw new RuntimeException(me);
        }
    }
}
