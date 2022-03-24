package org.duh102.mazegame.graphics;

import org.duh102.mazegame.client.GameWindow;
import org.duh102.mazegame.model.exception.beanregistry.NoBeanFoundException;
import org.duh102.mazegame.util.beanreg.BeanRegistry;
import org.duh102.mazegame.util.beanreg.CachedBeanRetriever;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class MazeResizeComponentListener implements ComponentListener {
    BeanRegistry registry;
    CachedBeanRetriever<MazeDisplay> mazeDisplay;
    CachedBeanRetriever<GameWindow> gameWindow;

    public MazeResizeComponentListener(BeanRegistry registry) {
        this.registry = registry;
        mazeDisplay = new CachedBeanRetriever<>(registry, MazeDisplay.class);
        gameWindow = new CachedBeanRetriever<>(registry, GameWindow.class);
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {
        try {
            mazeDisplay.get();
            gameWindow.get();
        } catch(NoBeanFoundException nbfe) {
        }
        if( !(mazeDisplay.hasBean() && gameWindow.hasBean()) ) {
            return;
        }
        Component component = componentEvent.getComponent();
        mazeDisplay.get().setImageDimensions(component.getWidth(), component.getHeight());
        gameWindow.get().updateImage();
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }
}
