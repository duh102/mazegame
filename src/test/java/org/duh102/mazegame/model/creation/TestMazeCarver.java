package org.duh102.mazegame.model.creation;

import org.duh102.mazegame.model.maze.ExitDirection;
import org.duh102.mazegame.model.maze.ExitDirectionSet;
import org.duh102.mazegame.model.maze.Maze;
import org.duh102.mazegame.model.maze.Tile;
import org.duh102.mazegame.model.exception.maze.NotInMazeException;
import org.duh102.mazegame.util.Point2DInt;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestMazeCarver {
    @Test
    public void testConstructor() {
        MazeCarver carver = new MazeCarver();
        assertThat(carver.getMaze().getSize()).isEqualTo(Point2DInt.of(2,2));
        Maze maze = carver.getMaze();
        for(int x = 0; x < 2; x++) {
            for(int y = 0; y < 2; y++) {
                assertThat(maze.getTileAt(Point2DInt.of(x,y))).isNotNull()
                        .extracting(Tile::getExits).isEqualTo(new ExitDirectionSet());
            }
        }
    }

    @Test
    public void testRepositionKnife() throws NotInMazeException {
        MazeCarver carver = new MazeCarver();
        carver.repositionKnife(Point2DInt.of(0,0));
        assertThat(carver.getCarveLocation()).isEqualTo(Point2DInt.of(0,0));
        assertThrows(NotInMazeException.class, () -> carver.repositionKnife(ExitDirection.LEFT));
        carver.repositionKnife(ExitDirection.DOWN);
        assertThat(carver.getCarveLocation()).isEqualTo(Point2DInt.of(0,1));
        assertThrows(NotInMazeException.class, () -> carver.repositionKnife(ExitDirection.DOWN));
        carver.repositionKnife(Point2DInt.of(1,0));
        assertThat(carver.getCarveLocation()).isEqualTo(Point2DInt.of(1, 0));
    }

    @Test
    public void testCarve() throws NotInMazeException {
        MazeCarver carver = new MazeCarver();
        carver.repositionKnife(Point2DInt.of(0,0))
                .setEntrance()
                .carve(ExitDirection.DOWN)
                .carve(ExitDirection.RIGHT)
                .carve(ExitDirection.UP)
                .setExit();
        assertThat(carver.getCarveLocation()).isEqualTo(Point2DInt.of(1,0));
        Maze maze = carver.getMaze();
        assertThat(maze.getEntrance()).isEqualTo(Point2DInt.of(0,0));
        assertThat(maze.getExit()).isEqualTo(Point2DInt.of(1,0));

        assertThat(maze.canMove(Point2DInt.of(0,0), ExitDirection.DOWN)).isTrue();
        assertThat(maze.canMove(Point2DInt.of(0,1), ExitDirection.RIGHT)).isTrue();
        assertThat(maze.canMove(Point2DInt.of(1,1), ExitDirection.UP)).isTrue();
        assertThat(maze.canMove(Point2DInt.of(1,0), ExitDirection.DOWN)).isTrue();
        assertThat(maze.canMove(Point2DInt.of(1,1), ExitDirection.LEFT)).isTrue();
        assertThat(maze.canMove(Point2DInt.of(0,1), ExitDirection.UP)).isTrue();

        assertThat(maze.canMove(Point2DInt.of(0,0), ExitDirection.LEFT)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(0,0), ExitDirection.UP)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(0,0), ExitDirection.RIGHT)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(0,1), ExitDirection.LEFT)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(0,1), ExitDirection.DOWN)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(1,1), ExitDirection.DOWN)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(1,1), ExitDirection.RIGHT)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(1,0), ExitDirection.LEFT)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(1,0), ExitDirection.UP)).isFalse();
        assertThat(maze.canMove(Point2DInt.of(1,0), ExitDirection.RIGHT)).isFalse();
    }
}
