package org.duh102.mazegame.client.state;

import org.duh102.mazegame.graphics.MazeDisplay;
import org.duh102.mazegame.model.creation.MazeCarver;
import org.duh102.mazegame.model.exception.client.InvalidGameStateTransition;
import org.duh102.mazegame.model.exception.maze.InvalidMoveException;
import org.duh102.mazegame.model.exception.maze.NotInMazeException;
import org.duh102.mazegame.model.maze.ExitDirection;
import org.duh102.mazegame.model.maze.GameBoard;
import org.duh102.mazegame.model.maze.Maze;
import org.duh102.mazegame.util.beanreg.BeanRegistry;
import org.duh102.mazegame.util.beanreg.CachedBeanRetriever;

public class MazeStateController {
    private GameBoard board;
    private MazeCarver carver;
    private final GameStateContainer gameStateContainer;
    private final CachedBeanRetriever<MazeDisplay> mazeDisplay;

    public MazeStateController(BeanRegistry registry) {
        mazeDisplay = new CachedBeanRetriever<>(registry, MazeDisplay.class);
        gameStateContainer = new GameStateContainer();
    }
    public GameStateContainer getGameStateContainer() {
        return gameStateContainer;
    }
    public synchronized MazeStateController replaceMaze(Maze maze) {
        board = new GameBoard(maze);
        carver = new MazeCarver(board.getMaze());
        try {
            MazeDisplay display = mazeDisplay.get();
            display.resetMovement();
            if(gameStateContainer.getState() == GameState.WON) {
                gameStateContainer.transition(GameState.PLAYING);
            }
        } catch (InvalidGameStateTransition igst) {
            igst.printStackTrace();
        }
        return this;
    }

    public synchronized GameBoard getBoard() {
        return board;
    }
    public synchronized Maze getMaze() {
        if(board == null) {
            return null;
        }
        return board.getMaze();
    }
    public synchronized void toggleEditing() {
        GameState state = gameStateContainer.getState();
        if(state == GameState.PLAYING || state == GameState.WON) {
            gameStateContainer.transition(GameState.EDITING);
        } else {
            gameStateContainer.transition(GameState.PLAYING);
            board.getCharacter().teleport(getBoard().getMaze().getEntrance());
            try {
                MazeDisplay display = mazeDisplay.get();
                display.resetMovement();
            } catch (Exception e) {
                // If we don't have a display, then we don't need to reset the movement!
                e.printStackTrace();
            }
        }
    }

    private void editSetLocation(boolean isEntrance) {
        GameState state = gameStateContainer.getState();
        if(state != GameState.EDITING) {
            return;
        }
        try {
            carver.repositionKnife(board.getCharacter().getPosition());
            if(isEntrance) {
                carver.setEntrance();
            } else {
                carver.setExit();
            }
        } catch (NotInMazeException e) {
            e.printStackTrace();
        }
    }
    public void editSetEntrance() {
        editSetLocation(true);
    }
    public void editSetExit() {
        editSetLocation(false);
    }
    public void move(ExitDirection direction, boolean carveModifier, boolean sealModifier) {
        GameState state = gameStateContainer.getState();
        try {
            MazeDisplay display = mazeDisplay.get();
            if(state != GameState.WON && direction != null && display.readyForMovement()) {
                try {
                    carver.repositionKnife(board.getCharacter().getPosition());
                    if(state == GameState.EDITING) {
                        if(!(carveModifier || sealModifier)) {
                            board.teleport(direction);
                        } else {
                            if(sealModifier) {
                                board.move(direction);
                                carver.seal(direction);
                            } else {
                                carver.carve(direction);
                                board.move(direction);
                            }
                        }
                    } else if (state == GameState.PLAYING) {
                        board.move(direction);
                        if(board.hasWon()) {
                            gameStateContainer.transition(GameState.WON);
                        }
                    }
                    display.notifyMoved();
                } catch(InvalidMoveException | NotInMazeException ime) {
                    // Just don't move then!
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
