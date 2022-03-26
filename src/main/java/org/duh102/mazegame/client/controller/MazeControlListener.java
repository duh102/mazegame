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
    private final AtomicBoolean heldModifier = new AtomicBoolean(false);

    public MazeControlListener(BeanRegistry registry) {
        mazeStateController = new CachedBeanRetriever<>(registry, MazeStateController.class);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        ExitDirection direction = decodeKey(keyEvent.getKeyCode());
        notifyMovement(direction);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        ExitDirection direction = decodeKey(keyEvent.getKeyCode());
        if(direction != null) {
            heldDirection = direction;
        }
        heldModifier.set(keyEvent.isControlDown());
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        ExitDirection direction = decodeKey(keyEvent.getKeyCode());
        if(Objects.equals(direction, heldDirection)) {
            heldDirection = null;
        }
        heldModifier.set(keyEvent.isControlDown());
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

    public MazeControlListener notifyMovement(ExitDirection direction) {
        boolean isModifierDown = heldModifier.get();
        try {
            MazeStateController stateController = mazeStateController.get();
            stateController.move(direction, isModifierDown);
        } catch (NoBeanFoundException | InvalidMoveException e) {
            // Don't move then!
        }
        return this;
    }
    public MazeControlListener notifyMovement() {
        notifyMovement(heldDirection);
        return this;
    }
}
