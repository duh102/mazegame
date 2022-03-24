package org.duh102.mazegame.client;

import org.duh102.mazegame.graphics.FallbackTileMap;
import org.duh102.mazegame.graphics.FileBackedTileMap;
import org.duh102.mazegame.graphics.TileMap;
import org.duh102.mazegame.model.exception.beanregistry.NoBeanFoundException;
import org.duh102.mazegame.model.exception.maze.MazeException;
import org.duh102.mazegame.model.exception.maze.tileset.TileSizeException;
import org.duh102.mazegame.model.tileset.TileSet;
import org.duh102.mazegame.util.Provider;
import org.duh102.mazegame.util.beanreg.BeanRegistry;
import org.duh102.mazegame.util.beanreg.CachedBeanRetriever;
import org.duh102.mazegame.util.TileSetRegistry;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MazeActionListener implements ActionListener {
    private BeanRegistry registry;
    private CachedBeanRetriever<TileSetRegistry> tileSetRegistry;
    private CachedBeanRetriever<Provider<TileMap>> tileMapProvider;
    private CachedBeanRetriever<GameWindow> gameWindow;

    public MazeActionListener(BeanRegistry registry) {
        this.registry = registry;
        tileSetRegistry = new CachedBeanRetriever<>(registry, TileSetRegistry.class);
        tileMapProvider = new CachedBeanRetriever<>(registry, (Class<Provider<TileMap>>)(new Provider<TileMap>(new FallbackTileMap())).getClass());
        gameWindow = new CachedBeanRetriever<>(registry, GameWindow.class);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.printf("Heard action %s\n", actionEvent.getActionCommand());
        if(actionEvent.getActionCommand().equals("st")) {
            chooseExistingTileSet();
        }
    }

    public TileMap chooseExistingTileSet() {
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
}
