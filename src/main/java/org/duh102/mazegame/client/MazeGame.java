package org.duh102.mazegame.client;

import org.duh102.mazegame.graphics.MazeDisplay;
import org.duh102.mazegame.graphics.MazeResizeComponentListener;
import org.duh102.mazegame.graphics.TileMap;
import org.duh102.mazegame.model.creation.MazeCarver;
import org.duh102.mazegame.model.exception.MazeException;
import org.duh102.mazegame.model.maze.ExitDirection;
import org.duh102.mazegame.model.maze.GameBoard;
import org.duh102.mazegame.model.maze.Maze;
import org.duh102.mazegame.model.tileset.TileSet;
import org.duh102.mazegame.util.Pair;
import org.duh102.mazegame.util.Point2DInt;

import java.io.IOException;

public class MazeGame {
    public static void main(String args[]) {
        AnimationController ac = new AnimationController(60);
        MazeControlListener mcl = new MazeControlListener();
        MazeResizeComponentListener mrcl = new MazeResizeComponentListener();
        MazeDisplay display = new MazeDisplay(640, 480);

        Pair<Maze, TileMap> demoMap = generateDemoMap();
        GameBoard board = new GameBoard(demoMap.getFirst());

        ac.setControlListener(mcl);
        ac.setMazeDisplay(display);

        mcl.setGameBoard(board);
        mcl.setMazeDisplay(display);

        display.setGameBoard(board);

        mrcl.setMazeDisplay(display);

        GameWindow window = new GameWindow(mcl, mrcl, display);
        mrcl.setGameWindow(window);
        ac.setGameWindow(window);
        window.setTileMap(demoMap.getSecond());
        window.packAndShow();
        ac.start();
    }

    private static TileSet dungeonTileset = new TileSet("/home/juggernaut/gits/mazegame/src/main/resources/tilesets/dungeon_2x/dungeon_tileset_2x_2.png",
            "/home/juggernaut/gits/mazegame/src/main/resources/tilesets/dungeon_2x/dungeon_character_2x.png",
            Point2DInt.of(128, 128),
            Point2DInt.of(0,0),
            2,
            Pair.of(17.0,30.0));
    private static TileSet grassTileset = new TileSet("/home/juggernaut/gits/mazegame/src/main/resources/tilesets/grass_2x/grass_tileset_2x_3.png",
            "/home/juggernaut/gits/mazegame/src/main/resources/tilesets/dungeon_2x/dungeon_character_2x.png",
            Point2DInt.of(64, 64),
            Point2DInt.of(0,0),
            2,
            Pair.of(17.0,30.0));

    public static Pair<Maze, TileMap> generateDemoMap() {
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
        TileSet temp = dungeonTileset;
        TileMap tileMap = new TileMap.Builder().loadFromTileset(temp).build();
        return Pair.of(carver.getMaze(), tileMap);
        } catch(MazeException | IOException me) {
            throw new RuntimeException(me);
        }
    }
}
