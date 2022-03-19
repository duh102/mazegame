package org.duh102.mazegame.model;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;

public class TestTile {
    @Test
    public void testTileConstructor() {
        Tile tile = new Tile(ExitDirection.UP);
        assertThat(tile.getExits()).containsExactlyInAnyOrder(ExitDirection.UP);

        tile = new Tile(ExitDirection.UP, ExitDirection.DOWN);
        assertThat(tile.getExits()).containsExactlyInAnyOrder(ExitDirection.UP, ExitDirection.DOWN);

        tile = new Tile(EnumSet.of(ExitDirection.UP, ExitDirection.LEFT, ExitDirection.RIGHT));
        assertThat(tile.getExits()).containsExactlyInAnyOrder(ExitDirection.UP, ExitDirection.LEFT, ExitDirection.RIGHT);
    }

    @Test
    public void testTileConnects() {
        Tile straightUp = new Tile(ExitDirection.UP, ExitDirection.DOWN);
        assertThat(straightUp.connects(ExitDirection.UP)).isTrue();
        assertThat(straightUp.connects(ExitDirection.DOWN)).isTrue();
        assertThat(straightUp.connects(ExitDirection.LEFT)).isFalse();
        assertThat(straightUp.connects(ExitDirection.RIGHT)).isFalse();

        Tile upLeft = new Tile(ExitDirection.UP, ExitDirection.LEFT);
        assertThat(upLeft.connects(ExitDirection.UP)).isFalse();
        assertThat(upLeft.connects(ExitDirection.DOWN)).isTrue();
        assertThat(upLeft.connects(ExitDirection.LEFT)).isFalse();
        assertThat(upLeft.connects(ExitDirection.RIGHT)).isTrue();
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
