package org.duh102.mazegame.client.state;

import org.duh102.mazegame.graphics.MazeDisplay;
import org.duh102.mazegame.model.creation.MazeCarver;
import org.duh102.mazegame.model.exception.client.InvalidGameStateTransition;
import org.duh102.mazegame.model.exception.maze.InvalidMoveException;
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
    public synchronized void startEditing() {
        gameStateContainer.transition(GameState.EDITING);
    }

    public void move(ExitDirection direction, boolean modifierDown) throws InvalidMoveException {
        GameState state = gameStateContainer.getState();
        try {
            MazeDisplay display = mazeDisplay.get();
            if(state != GameState.WON && direction != null && display.readyForMovement()) {
                try {
                    // if we're doing carving, do that too
                    board.move(direction);
                    display.notifyMoved();
                    if(state == GameState.PLAYING && board.hasWon()) {
                        gameStateContainer.transition(GameState.WON);
                    }
                } catch(InvalidMoveException ime) {
                    // Just don't move then!
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
