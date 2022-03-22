package org.duh102.mazegame.client;

import org.duh102.mazegame.graphics.*;
import org.duh102.mazegame.model.creation.MazeCarver;
import org.duh102.mazegame.model.exception.MazeException;
import org.duh102.mazegame.model.exception.TileSizeException;
import org.duh102.mazegame.model.maze.ExitDirection;
import org.duh102.mazegame.model.maze.GameBoard;
import org.duh102.mazegame.model.maze.Maze;
import org.duh102.mazegame.model.tileset.TileSet;
import org.duh102.mazegame.util.Config;
import org.duh102.mazegame.util.Point2DInt;
import org.duh102.mazegame.util.TileSetRegistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MazeGame {
    public static void main(String args[]) {
        Config config = new Config();
        File configFile = new File("gameconfig.json");
        try {
            Config temp = Config.loadFromFile(configFile);
            if(temp != null) {
                config = temp;
            }
        } catch (FileNotFoundException e) {
            System.err.printf("Couldn't find a file at %s\n", configFile.getAbsolutePath());
            e.printStackTrace();
        }
        TileSetRegistry tileSetRegistry = new TileSetRegistry(config.getTileSetSearchPaths());
        TileMap selectedTileMap = null;
        tileSetRegistry.rescan();
        if(tileSetRegistry.getDiscoveredTileSets().size() > 0) {
            try {
                Random random = new Random();
                List<TileSet> usableSets = tileSetRegistry.getDiscoveredTileSets();
                System.out.printf("DEBUG Found %d tilesets: %s\n", usableSets.size(),
                        usableSets.stream().map(TileSet::getTileSetName).collect(Collectors.joining(", ")));
                TileSet selectedTileSet = usableSets.get(random.nextInt(usableSets.size()));
                selectedTileMap = new FileBackedTileMap.Builder().loadFromTileSet(selectedTileSet).build();
                System.out.printf("DEBUG using tileset %s\n", selectedTileSet.getTileSetName());
            } catch (MazeException | IOException e) {
                e.printStackTrace();
            }
        }
        if(selectedTileMap == null) {
            selectedTileMap = new FallbackTileMap();
        }


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
