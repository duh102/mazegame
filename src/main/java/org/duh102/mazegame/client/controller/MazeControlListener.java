package org.duh102.mazegame.client.controller;

import org.duh102.mazegame.client.state.MazeStateController;
import org.duh102.mazegame.model.exception.beanregistry.NoBeanFoundException;
import org.duh102.mazegame.model.exception.maze.InvalidMoveException;
import org.duh102.mazegame.model.maze.ExitDirection;
import org.duh102.mazegame.util.beanreg.BeanRegistry;
import org.duh102.mazegame.util.beanreg.CachedBeanRetriever;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class MazeControlListener implements KeyListener {
    private final CachedBeanRetriever<MazeStateController> mazeStateController;
    private ExitDirection heldDirection = null;
    private final AtomicBoolean ctrlDown = new AtomicBoolean(false);
    private final AtomicBoolean altDown = new AtomicBoolean(false);

    public MazeControlListener(BeanRegistry registry) {
        mazeStateController = new CachedBeanRetriever<>(registry, MazeStateController.class);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        ExitDirection direction = decodeKey(keyCode);
        if(direction != null) {
            notifyMovement(direction);
        }
        if(keyCode == KeyEvent.VK_E || keyCode == KeyEvent.VK_F) {
            decodeSetLocation(keyCode);
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        ExitDirection direction = decodeKey(keyEvent.getKeyCode());
        if(direction != null) {
            heldDirection = direction;
        }
        ctrlDown.set(keyEvent.isControlDown());
        altDown.set(keyEvent.isAltDown());
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        ExitDirection direction = decodeKey(keyCode);
        if(Objects.equals(direction, heldDirection)) {
            heldDirection = null;
        }
        ctrlDown.set(keyEvent.isControlDown());
        altDown.set(keyEvent.isAltDown());
        if(keyCode == KeyEvent.VK_E || keyCode == KeyEvent.VK_F) {
            decodeSetLocation(keyCode);
        }
    }

    private ExitDirection decodeKey(int keyCode) {
        switch(keyCode) {
            case(KeyEvent.VK_UP):
            case(KeyEvent.VK_W):
                return ExitDirection.UP;
            case(KeyEvent.VK_RIGHT):
            case(KeyEvent.VK_D):
                return ExitDirection.RIGHT;
            case(KeyEvent.VK_DOWN):
            case(KeyEvent.VK_S):
                return ExitDirection.DOWN;
            case(KeyEvent.VK_LEFT):
            case(KeyEvent.VK_A):
                return ExitDirection.LEFT;
            default:
                return null;
        }
    }

    private MazeControlListener notifyMovement(ExitDirection direction) {
        boolean isCtrlDown = ctrlDown.get();
        boolean isAltDown = altDown.get();
        try {
            MazeStateController stateController = mazeStateController.get();
            stateController.move(direction, isCtrlDown, isAltDown);
        } catch (NoBeanFoundException e) {
            // Don't move then!
        }
        return this;
    }
    public MazeControlListener notifyMovement() {
        notifyMovement(heldDirection);
        return this;
    }
    private MazeControlListener decodeSetLocation(int keyCode) {
        if(keyCode != KeyEvent.VK_E && keyCode != KeyEvent.VK_F) {
            return this;
        }
        try {
            MazeStateController stateController = mazeStateController.get();
            // E == start, F == finish
            if(keyCode == KeyEvent.VK_E) {
                stateController.editSetEntrance();
            } else {
                stateController.editSetExit();
            }
        } catch (NoBeanFoundException e) {
            // Don't move then!
        }
        return this;
    }
}
