package org.duh102.mazegame.client;

import org.duh102.mazegame.graphics.MazeDisplay;
import org.duh102.mazegame.model.exception.maze.InvalidMoveException;
import org.duh102.mazegame.model.maze.ExitDirection;
import org.duh102.mazegame.model.maze.GameBoard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

public class MazeControlListener implements KeyListener {
    private MazeDisplay mazeDisplay;
    private GameBoard gameBoard;
    private ExitDirection heldDirection = null;

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
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        ExitDirection direction = decodeKey(keyEvent.getKeyCode());
        if(Objects.equals(direction, heldDirection)) {
            heldDirection = null;
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

    public MazeControlListener notifyMovement(ExitDirection direction) {
        if(direction != null && mazeDisplay.readyForMovement()) {
            try {
                gameBoard.move(direction);
                mazeDisplay.notifyMoved();
            } catch(InvalidMoveException ime) {
                // Just don't move then!
            }
        }
        return this;
    }
    public MazeControlListener notifyMovement() {
        notifyMovement(heldDirection);
        return this;
    }

    public MazeControlListener setMazeDisplay(MazeDisplay mazeDisplay) {
        this.mazeDisplay = mazeDisplay;
        return this;
    }

    public MazeControlListener setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        return this;
    }
}
