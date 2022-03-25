package org.duh102.mazegame.model.creation.generator;

import org.duh102.mazegame.model.exception.maze.generator.MazeGeneratorException;
import org.duh102.mazegame.model.maze.Maze;

public interface MazeGenerator {
    Maze generate(int xSize, int ySize) throws MazeGeneratorException;
    MazeGenerator seed(long seed);
}
