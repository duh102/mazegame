package org.duh102.mazegame.model.maze;

import org.duh102.mazegame.model.exception.maze.InvalidMoveException;

import java.io.Serializable;

public class GameBoard implements Serializable {
    private Maze maze;
    private Character character;

    public GameBoard(Maze maze) {
        this.maze = maze;
        character = new Character(maze.getEntrance());
    }
    public synchronized boolean canMove(ExitDirection direction) {
        return maze.canMove(character.getPosition(), direction);
    }
    public synchronized GameBoard move(ExitDirection direction) throws InvalidMoveException {
        if(!canMove(direction)) {
            throw new InvalidMoveException();
        }
        character.move(direction);
        return this;
    }
    public synchronized boolean hasWon() {
        return maze.hasReachedExit(character.getPosition());
    }

    public synchronized Maze getMaze() {
        return maze;
    }
    public synchronized GameBoard setMaze(Maze maze) {
        this.maze = maze;
        this.character.setPosition(maze.getEntrance());
        return this;
    }

    public synchronized Character getCharacter() {
        return character;
    }
    public synchronized GameBoard setCharacter(Character character) {
        this.character = character;
        return this;
    }
}