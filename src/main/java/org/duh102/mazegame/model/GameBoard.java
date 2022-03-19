package org.duh102.mazegame.model;

public class GameBoard {
    private Maze maze;
    private Character character;

    public GameBoard(Maze maze) {
        this.maze = maze;
        character = new Character(maze.getEntrance());
    }
    public boolean canMove(ExitDirection direction) {
        return maze.canMove(character.getPosition(), direction);
    }
    public GameBoard move(ExitDirection direction) {
        if(canMove(direction)) {
            character.move(direction);
        }
        return this;
    }
    public boolean hasWon() {
        return maze.hasReachedExit(character.getPosition());
    }
}
