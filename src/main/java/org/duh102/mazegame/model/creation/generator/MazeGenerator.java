package org.duh102.mazegame.model.creation.generator;

import org.duh102.mazegame.model.Maze;

public interface MazeGenerator {
    Maze generate();
    MazeGenerator seed(Integer seed);
}
