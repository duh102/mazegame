package org.duh102.mazegame.model.maze;

import org.duh102.mazegame.model.exception.InvalidMoveException;

import java.io.Serializable;

public class GameBoard implements Serializable {
    private Maze maze;
    private Character character;

    public GameBoard(Maze maze) {
        this.maze = maze;
        character = new Character(maze.getEntrance());
    }
    public boolean canMove(ExitDirection direction) {
        return maze.canMove(character.getPosition(), direction);
    }
    public GameBoard move(ExitDirection direction) throws InvalidMoveException {
        if(!canMove(direction)) {
            throw new InvalidMoveException();
        }
        character.move(direction);
        return this;
    }
    public boolean hasWon() {
        return maze.hasReachedExit(character.getPosition());
    }

    public Maze getMaze() {
        return maze;
    }
    public GameBoard setMaze(Maze maze) {
        this.maze = maze;
        return this;
    }

    public Character getCharacter() {
        return character;
    }
    public GameBoard setCharacter(Character character) {
        this.character = character;
        return this;
    }
}
