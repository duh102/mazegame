package org.duh102.mazegame.client;

import org.duh102.mazegame.client.dialogs.MazeGenerationDialog;
import org.duh102.mazegame.graphics.FallbackTileMap;
import org.duh102.mazegame.graphics.FileBackedTileMap;
import org.duh102.mazegame.graphics.TileMap;
import org.duh102.mazegame.model.creation.generator.MazeGenerator;
import org.duh102.mazegame.model.exception.beanregistry.NoBeanFoundException;
import org.duh102.mazegame.model.exception.maze.MazeException;
import org.duh102.mazegame.model.exception.maze.generator.MazeGeneratorException;
import org.duh102.mazegame.model.maze.GameBoard;
import org.duh102.mazegame.model.maze.Maze;
import org.duh102.mazegame.model.tileset.TileSet;
import org.duh102.mazegame.util.Point2DInt;
import org.duh102.mazegame.util.Provider;
import org.duh102.mazegame.util.beanreg.BeanRegistry;
import org.duh102.mazegame.util.beanreg.CachedBeanRetriever;
import org.duh102.mazegame.util.TileSetRegistry;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MazeActionListener implements ActionListener {
    private BeanRegistry registry;
    private CachedBeanRetriever<TileSetRegistry> tileSetRegistry;
    private CachedBeanRetriever<Provider<TileMap>> tileMapProvider;
    private CachedBeanRetriever<GameWindow> gameWindow;
    private CachedBeanRetriever<GameBoard> board;

    public MazeActionListener(BeanRegistry registry) {
        this.registry = registry;
        tileSetRegistry = new CachedBeanRetriever<>(registry, TileSetRegistry.class);
        tileMapProvider = new CachedBeanRetriever<>(registry, (Class<Provider<TileMap>>)(new Provider<TileMap>(new FallbackTileMap())).getClass());
        gameWindow = new CachedBeanRetriever<>(registry, GameWindow.class);
        board = new CachedBeanRetriever<>(registry, GameBoard.class);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.printf("Heard action %s\n", actionEvent.getActionCommand());
        if(actionEvent.getActionCommand().equals("st")) {
            chooseExistingTileSet();
        } else if(actionEvent.getActionCommand().equals("gm")) {
            Maze newMaze = generateMaze();
            try {
                board.get().setMaze(newMaze);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(gameWindow.get(), "Couldn't replace the maze");
            }
        }
    }

    private TileMap chooseExistingTileSet() {
        try {
            List<TileSet> tileSets = tileSetRegistry.get().getDiscoveredTileSets();
            if(tileSets.size() == 0) {
                return null;
            }
            Provider<TileMap> tMapProvider = tileMapProvider.get();
            GameWindow window = gameWindow.get();
            List<String> tileSetNames = tileSets.stream().map(TileSet::getTileSetName).collect(Collectors.toList());
            Object[] possibilities = tileSetNames.toArray();
            String s = (String) JOptionPane.showInputDialog(
                    window,
                    "Choose from the loaded tilesets",
                    "TileSet selection",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    tileSetNames.get(0));
            TileSet selected = tileSets.get(tileSetNames.indexOf(s));
            try {
                TileMap newTMap = new FileBackedTileMap.Builder().loadFromTileSet(selected).build();
                tMapProvider.replace(newTMap);
                window.updateImage();
                return newTMap;
            } catch (MazeException | IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(window, "Unable to use that tileset, choose another or see log for details");
            }
        } catch (NoBeanFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Maze generateMaze() {
        try {
            List<MazeGenerator> mazeGenerators = new ArrayList<>(registry.getBeansOfType(MazeGenerator.class));
            mazeGenerators.sort(Comparator.comparing(MazeGenerator::getName));
            List<String> generatorNames = mazeGenerators.stream().map(MazeGenerator::getName).collect(Collectors.toList());
            MazeGenerationDialog mgd = new MazeGenerationDialog(gameWindow.get(), generatorNames);
            mgd.pack();
            mgd.setVisible(true);
            int selectedGen = mgd.getChosenGenerator();
            Point2DInt size = mgd.getChosenSize();
            long seed = mgd.getSeed();
            MazeGenerator selected = mazeGenerators.get(selectedGen);
            Maze generated = selected.seed(seed).generate(size.getX(), size.getY());
            return generated;
        } catch (NoBeanFoundException e) {
            JOptionPane.showMessageDialog(gameWindow.get(), "Didn't find any maze generators");
        } catch (MazeGeneratorException mge) {
            JOptionPane.showMessageDialog(gameWindow.get(), "Unable to generate maze; did you enter size less than 2x2?");
        }
        return null;
    }
}
