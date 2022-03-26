package org.duh102.mazegame.client;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.duh102.mazegame.client.dialogs.MazeGenerationDialog;
import org.duh102.mazegame.graphics.FallbackTileMap;
import org.duh102.mazegame.graphics.FileBackedTileMap;
import org.duh102.mazegame.graphics.MazeDisplay;
import org.duh102.mazegame.graphics.TileMap;
import org.duh102.mazegame.model.creation.generator.MazeGenerator;
import org.duh102.mazegame.model.exception.beanregistry.NoBeanFoundException;
import org.duh102.mazegame.model.exception.maze.MazeException;
import org.duh102.mazegame.model.exception.maze.generator.MazeGeneratorException;
import org.duh102.mazegame.model.maze.Maze;
import org.duh102.mazegame.model.serialization.MazeCustomizedGSON;
import org.duh102.mazegame.model.tileset.TileSet;
import org.duh102.mazegame.util.ExtendedFileNameExtensionFilter;
import org.duh102.mazegame.util.Point2DInt;
import org.duh102.mazegame.util.Provider;
import org.duh102.mazegame.util.beanreg.BeanRegistry;
import org.duh102.mazegame.util.beanreg.CachedBeanRetriever;
import org.duh102.mazegame.util.TileSetRegistry;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MazeActionListener implements ActionListener {
    private BeanRegistry registry;
    private CachedBeanRetriever<TileSetRegistry> tileSetRegistry;
    private CachedBeanRetriever<Provider<TileMap>> tileMapProvider;
    private CachedBeanRetriever<GameWindow> gameWindow;
    private CachedBeanRetriever<MazeStateController> mazeStateController;
    private CachedBeanRetriever<File> rootFolder;
    private CachedBeanRetriever<MazeDisplay> display;

    public MazeActionListener(BeanRegistry registry) {
        this.registry = registry;
        tileSetRegistry = new CachedBeanRetriever<>(registry, TileSetRegistry.class);
        tileMapProvider = new CachedBeanRetriever<>(registry, (Class<Provider<TileMap>>)(new Provider<TileMap>(new FallbackTileMap())).getClass());
        gameWindow = new CachedBeanRetriever<>(registry, GameWindow.class);
        mazeStateController = new CachedBeanRetriever<>(registry, MazeStateController.class);
        rootFolder = new CachedBeanRetriever<>(registry, File.class, "root");
        display = new CachedBeanRetriever<>(registry, MazeDisplay.class);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String actionCommand = actionEvent.getActionCommand();
        if(actionCommand.equals("st")) {
            chooseExistingTileSet();
        } else if(actionCommand.equals("gm")) {
            Maze newMaze = generateMaze();
            if(newMaze != null) {
                replaceMaze(newMaze);
            }
        } else if(actionCommand.equals("lm")) {
            Maze newMaze = loadMaze();
            if(newMaze != null) {
                replaceMaze(newMaze);
            }
        } else if(actionCommand.equals("sm")) {
            saveMaze();
        }
    }

    private void replaceMaze(Maze newMaze) {
        try {
            MazeStateController brd = mazeStateController.get();
            brd.replaceMaze(newMaze);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(gameWindow.get(), "Couldn't replace the maze");
            e.printStackTrace();
        }
    }

    private TileMap chooseExistingTileSet() {
        try {
            List<TileSet> tileSets = tileSetRegistry.get().getDiscoveredTileSets();
            tileSets.sort(Comparator.comparing(TileSet::getTileSetName));
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
            if(s != null) {
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

    private Maze loadMaze() {
        try {
            GameWindow window = gameWindow.get();
            File rootDir = rootFolder.get();
            JFileChooser chooser = new JFileChooser();
            FileFilter filter = new ExtendedFileNameExtensionFilter("Maze files", ".maze.json");
            chooser.setFileFilter(filter);
            chooser.setCurrentDirectory(rootDir);
            int returnVal = chooser.showOpenDialog(window);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                Gson json = MazeCustomizedGSON.getGson();
                try {
                    FileReader reader = new FileReader(selectedFile);
                    Maze maze = json.fromJson(reader, Maze.class);
                    return maze;
                } catch(FileNotFoundException fnf) {
                    JOptionPane.showMessageDialog(window, String.format("Maze file %s not found", selectedFile.getPath()));
                } catch (JsonSyntaxException | JsonIOException e) {
                    JOptionPane.showMessageDialog(window, String.format("Maze file invalid: %s", e.getMessage()));
                    e.printStackTrace();
                }
            }
        } catch (NoBeanFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File saveMaze() {
        try {
            File rootDir = rootFolder.get();
            MazeStateController stateController = mazeStateController.get();
            GameWindow window = gameWindow.get();
            JFileChooser chooser = new JFileChooser();
            FileFilter filter = new ExtendedFileNameExtensionFilter("Maze files", ".maze.json");
            chooser.setFileFilter(filter);
            chooser.setCurrentDirectory(rootDir);
            int returnVal = chooser.showSaveDialog(window);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                if(!selectedFile.getPath().endsWith(".maze.json")) {
                    selectedFile = new File(selectedFile.getPath() + ".maze.json");
                }
                Gson json = MazeCustomizedGSON.getGsonBase().setPrettyPrinting().create();
                try {
                    System.err.printf("Saving maze to %s\n", selectedFile.getAbsolutePath());
                    FileWriter fileWriter = new FileWriter(selectedFile);
                    json.toJson(stateController.getMaze(), fileWriter);
                    fileWriter.close();
                    return selectedFile;
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(window, String.format("Unable to write file: %s", e.getMessage()));
                    e.printStackTrace();
                }
            }
        } catch (NoBeanFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
