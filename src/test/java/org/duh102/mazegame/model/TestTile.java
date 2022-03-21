package org.duh102.mazegame.model;

import org.duh102.mazegame.model.maze.ExitDirection;
import org.duh102.mazegame.model.maze.ExitDirectionSet;
import org.duh102.mazegame.model.maze.Tile;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestTile {
    @Test
    public void testTileConstructor() {
        Tile tile = new Tile(ExitDirection.UP);
        assertThat(tile.getExits().getExits()).containsExactlyInAnyOrder(ExitDirection.UP);

        tile = new Tile(ExitDirection.UP, ExitDirection.DOWN);
        assertThat(tile.getExits().getExits()).containsExactlyInAnyOrder(ExitDirection.UP, ExitDirection.DOWN);

        tile = new Tile(new ExitDirectionSet(ExitDirection.UP, ExitDirection.LEFT, ExitDirection.RIGHT));
        assertThat(tile.getExits().getExits()).containsExactlyInAnyOrder(ExitDirection.UP, ExitDirection.LEFT, ExitDirection.RIGHT);
    }

    @Test
    public void testTileConnects() {
        Tile straightUp = new Tile(ExitDirection.UP, ExitDirection.DOWN);
        assertThat(straightUp.canAcceptFrom(ExitDirection.UP)).isTrue();
        assertThat(straightUp.canAcceptFrom(ExitDirection.DOWN)).isTrue();
        assertThat(straightUp.canAcceptFrom(ExitDirection.LEFT)).isFalse();
        assertThat(straightUp.canAcceptFrom(ExitDirection.RIGHT)).isFalse();

        Tile upLeft = new Tile(ExitDirection.UP, ExitDirection.LEFT);
        assertThat(upLeft.canAcceptFrom(ExitDirection.UP)).isTrue();
        assertThat(upLeft.canAcceptFrom(ExitDirection.DOWN)).isFalse();
        assertThat(upLeft.canAcceptFrom(ExitDirection.LEFT)).isTrue();
        assertThat(upLeft.canAcceptFrom(ExitDirection.RIGHT)).isFalse();
    }

    @Test
    public void testTileIndex() {
        Tile tile = new Tile();
        assertThat(tile.getTileIndex()).isEqualTo((byte)0);

        // Simple directions
        tile = new Tile(ExitDirection.UP);
        assertThat(tile.getTileIndex()).isEqualTo((byte)1);
        tile = new Tile(ExitDirection.RIGHT);
        assertThat(tile.getTileIndex()).isEqualTo((byte)2);
        tile = new Tile(ExitDirection.DOWN);
        assertThat(tile.getTileIndex()).isEqualTo((byte)4);
        tile = new Tile(ExitDirection.LEFT);
        assertThat(tile.getTileIndex()).isEqualTo((byte)8);

        // 2-combination directions
        tile = new Tile(ExitDirection.UP, ExitDirection.DOWN);
        assertThat(tile.getTileIndex()).isEqualTo((byte)5);
        tile = new Tile(ExitDirection.LEFT, ExitDirection.RIGHT);
        assertThat(tile.getTileIndex()).isEqualTo((byte)10);
    }
}
