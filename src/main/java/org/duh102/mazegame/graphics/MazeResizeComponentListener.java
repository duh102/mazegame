package org.duh102.mazegame.graphics;

import org.duh102.mazegame.client.GameWindow;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class MazeResizeComponentListener implements ComponentListener {
    MazeDisplay mazeDisplay = null;
    GameWindow gameWindow = null;

    public MazeResizeComponentListener() {
    }

    public MazeResizeComponentListener setMazeDisplay(MazeDisplay mazeDisplay) {
        this.mazeDisplay = mazeDisplay;
        return this;
    }

    public MazeResizeComponentListener setGameWindow(GameWindow toTriggerRedraw) {
        this.gameWindow = toTriggerRedraw;
        return this;
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {
        if(mazeDisplay == null || gameWindow == null) {
            return;
        }
        Component component = componentEvent.getComponent();
        mazeDisplay.setImageDimensions(component.getWidth(), component.getHeight());
        gameWindow.updateImage();
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
