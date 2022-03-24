package org.duh102.mazegame.client;

import org.duh102.mazegame.graphics.*;
import org.duh102.mazegame.model.creation.MazeCarver;
import org.duh102.mazegame.model.exception.maze.MazeException;
import org.duh102.mazegame.model.maze.ExitDirection;
import org.duh102.mazegame.model.maze.GameBoard;
import org.duh102.mazegame.model.maze.Maze;
import org.duh102.mazegame.model.tileset.TileSet;
import org.duh102.mazegame.util.Provider;
import org.duh102.mazegame.util.beanreg.BeanRegistry;
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
        BeanRegistry registry = new BeanRegistry();
        Config config = new Config();
        File rootFolder = (new File(MazeGame.class.getProtectionDomain().getCodeSource().getLocation().getPath())).getParentFile();
        File configFile = new File(rootFolder, "gameconfig.json");
        try {
            Config temp = Config.loadFromFile(configFile);
            if(temp != null) {
                config = temp;
            }
        } catch (FileNotFoundException e) {
            System.err.printf("Couldn't find a config file at %s, generating one\n", configFile.getAbsolutePath());
            e.printStackTrace();
            try {
                config.writeToFile(configFile);
            } catch (IOException ioException) {
                System.err.printf("Couldn't output a config file to %s\n", configFile.getAbsolutePath());
                ioException.printStackTrace();
            }
        }
        registry.registerBean(config, "config");
        TileSetRegistry tileSetRegistry = new TileSetRegistry(rootFolder);
        tileSetRegistry.setSearchPaths(config.getTileSetSearchPaths());
        registry.registerBean(tileSetRegistry, "tilesetregistry");
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
        Provider<TileMap> tileMapProvider = new Provider<>(selectedTileMap);
        registry.registerBean(tileMapProvider, "tilemap");

        Maze maze = generateDemoMap();
        GameBoard board = new GameBoard(maze);
        registry.registerBean(board, "gameboard");


        AnimationController ac = new AnimationController(60, registry);
        MazeControlListener mcl = new MazeControlListener(registry);
        MazeResizeComponentListener mrcl = new MazeResizeComponentListener(registry);
        MazeActionListener mal = new MazeActionListener(registry);
        MazeDisplay display = new MazeDisplay(640, 480, registry);
        registry.registerBean(ac, "animcontroller")
                .registerBean(mcl, "controllistener")
                .registerBean(mrcl, "resizelistener")
                .registerBean(display, "mazedisplay")
                .registerBean(mal, "actionlistener");

        GameWindow window = new GameWindow(mcl, mal, mrcl, display, registry);
        registry.registerBean(window, "window");
        window.updateImage();
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
        carver.repositionKnife(Point2DInt.of(0,3))
                .carve(ExitDirection.RIGHT)
                .carve(ExitDirection.RIGHT)
                .carve(ExitDirection.RIGHT)
                .carve(ExitDirection.UP)
                .repositionKnife(Point2DInt.of(2,1))
                .carve(ExitDirection.LEFT)
                .carve(ExitDirection.UP)
                .carve(ExitDirection.RIGHT)
                .carve(ExitDirection.RIGHT);
        return carver.getMaze();
        } catch(MazeException me) {
            throw new RuntimeException(me);
        }
    }
}
