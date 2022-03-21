package org.duh102.mazegame.model;

import org.duh102.mazegame.model.maze.ExitDirection;
import org.duh102.mazegame.model.maze.Maze;
import org.duh102.mazegame.model.maze.Tile;
import org.duh102.mazegame.util.Point2DInt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestMaze {
    private Tile[][] tiles2x2 = new Tile[2][2];
    private Tile[][] sparseTiles = new Tile[3][2];
    private Tile blankTile = new Tile();

    @BeforeEach
    public void setup() {
        tiles2x2[0][0] = new Tile(ExitDirection.DOWN);
        tiles2x2[0][1] = new Tile(ExitDirection.UP, ExitDirection.RIGHT);
        tiles2x2[1][0] = new Tile(ExitDirection.DOWN);
        tiles2x2[1][1] = new Tile(ExitDirection.LEFT, ExitDirection.UP);
        sparseTiles[0][0] = new Tile(ExitDirection.DOWN);
        sparseTiles[0][1] = new Tile(ExitDirection.UP, ExitDirection.RIGHT);
        sparseTiles[1][1] = new Tile(ExitDirection.LEFT, ExitDirection.RIGHT);
        sparseTiles[2][0] = new Tile(ExitDirection.DOWN);
        sparseTiles[2][1] = new Tile(ExitDirection.LEFT, ExitDirection.UP);
    }

    @Test
    public void testMazeConstruction() {
        Maze maze = new Maze(tiles2x2, Point2DInt.of(0,0), Point2DInt.of(1,0));
        assertThat(maze.getTileAt(Point2DInt.of(0,0))).isEqualTo(tiles2x2[0][0]);
        assertThat(maze.getTileAt(Point2DInt.of(0,1))).isEqualTo(tiles2x2[0][1]);
        assertThat(maze.getTileAt(Point2DInt.of(1,0))).isEqualTo(tiles2x2[1][0]);
        assertThat(maze.getTileAt(Point2DInt.of(1,1))).isEqualTo(tiles2x2[1][1]);
        assertThat(maze.getEntrance()).isEqualTo(Point2DInt.of(0,0));
        assertThat(maze.getExit()).isEqualTo(Point2DInt.of(1,0));

        maze = new Maze(2,2);
        assertThat(maze.getTileAt(Point2DInt.of(0,0))).isEqualTo(blankTile);
        assertThat(maze.getTileAt(Point2DInt.of(0,1))).isEqualTo(blankTile);
        assertThat(maze.getTileAt(Point2DInt.of(1,0))).isEqualTo(blankTile);
        assertThat(maze.getTileAt(Point2DInt.of(1,1))).isEqualTo(blankTile);
        assertThat(maze.getEntrance()).isEqualTo(Point2DInt.of(0,0));
        assertThat(maze.getExit()).isEqualTo(Point2DInt.of(0,0));
    }

    @Test
    public void testIsIn() {
        Maze maze = new Maze(tiles2x2, Point2DInt.of(0,0), Point2DInt.of(1,0));
        assertThat(maze.isIn(Point2DInt.of(0,0))).isTrue();
        assertThat(maze.isIn(Point2DInt.of(0,1))).isTrue();
        assertThat(maze.isIn(Point2DInt.of(1,0))).isTrue();
        assertThat(maze.isIn(Point2DInt.of(1,1))).isTrue();

        assertThat(maze.isIn(Point2DInt.of(-1,0))).isFalse();
        assertThat(maze.isIn(Point2DInt.of(0,-1))).isFalse();
        assertThat(maze.isIn(Point2DInt.of(-1,-1))).isFalse();
        assertThat(maze.isIn(Point2DInt.of(2,0))).isFalse();
        assertThat(maze.isIn(Point2DInt.of(0,2))).isFalse();
        assertThat(maze.isIn(Point2DInt.of(2,2))).isFalse();
    }

    @Test
    public void testGetTile() {
        Maze maze = new Maze(sparseTiles, Point2DInt.of(0,0), Point2DInt.of(2,0));
        assertThat(maze.getTileAt(Point2DInt.of(0,0))).isEqualTo(sparseTiles[0][0]);
        assertThat(maze.getTileAt(Point2DInt.of(0,1))).isEqualTo(sparseTiles[0][1]);
        assertThat(maze.getTileAt(Point2DInt.of(1,0))).isNull();
        assertThat(maze.getTileAt(Point2DInt.of(1,1))).isEqualTo(sparseTiles[1][1]);
        assertThat(maze.getTileAt(Point2DInt.of(2,0))).isEqualTo(sparseTiles[2][0]);
        assertThat(maze.getTileAt(Point2DInt.of(2,1))).isEqualTo(sparseTiles[2][1]);
        assertThat(maze.getTileAt(Point2DInt.of(-1,0))).isNull();
        assertThat(maze.getTileAt(Point2DInt.of(0,-1))).isNull();
        assertThat(maze.getTileAt(Point2DInt.of(0,2))).isNull();
        assertThat(maze.getTileAt(Point2DInt.of(3,0))).isNull();
    }

    @Test
    public void testCanMove() {
        Maze maze = new Maze(sparseTiles, Point2DInt.of(0,0), Point2DInt.of(1,0));
        assertThat(maze.canMove(Point2DInt.of(0,0), ExitDirection.DOWN)).isTrue();
        assertThat(maze.canMove(Point2DInt.of(0,1), ExitDirection.RIGHT)).isTrue();
        assertThat(maze.canMove(Point2DInt.of(1,1), ExitDirection.RIGHT)).isTrue();
        assertThat(maze.canMove(Point2DInt.of(2,1), ExitDirection.UP)).isTrue();
        assertThat(maze.canMove(Point2DInt.of(2,0), ExitDirection.DOWN)).isTrue();
        assertThat(maze.canMove(Point2DInt.of(2,1), ExitDirection.LEFT)).isTrue();
        assertThat(maze.canMove(Point2DInt.of(1,1), ExitDirection.LEFT)).isTrue();
        assertThat(maze.canMove(Point2DInt.of(0,1), ExitDirection.UP)).isTrue();

        assertThat(maze.canMove(Point2DInt.of(0,0), ExitDirection.LEFT)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(0,0), ExitDirection.UP)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(0,0), ExitDirection.RIGHT)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(0,1), ExitDirection.LEFT)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(0,1), ExitDirection.DOWN)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(2,1), ExitDirection.DOWN)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(2,1), ExitDirection.RIGHT)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(2,0), ExitDirection.LEFT)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(2,0), ExitDirection.UP)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(2,0), ExitDirection.RIGHT)).isFalse();
    }

    @Test
    public void testWinCondition() {
        Maze maze = new Maze(tiles2x2, Point2DInt.of(0,0), Point2DInt.of(1,0));
        assertThat(maze.hasReachedExit(Point2DInt.of(0,0))).isFalse();
        assertThat(maze.hasReachedExit(Point2DInt.of(0,1))).isFalse();
        assertThat(maze.hasReachedExit(Point2DInt.of(1,1))).isFalse();
        assertThat(maze.hasReachedExit(Point2DInt.of(1,0))).isTrue();
    }
}
