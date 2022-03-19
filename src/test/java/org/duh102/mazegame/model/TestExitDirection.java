package org.duh102.mazegame.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.duh102.mazegame.model.ExitDirection.*;

public class TestExitDirection {
    @Test
    public void testGetOpposite() {
        assertThat(UP.getOpposite()).isEqualTo(DOWN);
        assertThat(DOWN.getOpposite()).isEqualTo(UP);
        assertThat(LEFT.getOpposite()).isEqualTo(RIGHT);
        assertThat(RIGHT.getOpposite()).isEqualTo(LEFT);
    }
    @Test
    public void testConnects() {
        assertThat(UP.connects(DOWN)).isTrue();
        assertThat(DOWN.connects(UP)).isTrue();
        assertThat(LEFT.connects(RIGHT)).isTrue();
        assertThat(RIGHT.connects(LEFT)).isTrue();

        assertThat(UP.connects(UP)).isFalse();
        assertThat(UP.connects(LEFT)).isFalse();
        assertThat(UP.connects(RIGHT)).isFalse();
        assertThat(DOWN.connects(DOWN)).isFalse();
        assertThat(DOWN.connects(LEFT)).isFalse();
        assertThat(DOWN.connects(RIGHT)).isFalse();
        assertThat(LEFT.connects(LEFT)).isFalse();
        assertThat(LEFT.connects(UP)).isFalse();
        assertThat(LEFT.connects(DOWN)).isFalse();
        assertThat(RIGHT.connects(RIGHT)).isFalse();
        assertThat(RIGHT.connects(UP)).isFalse();
        assertThat(RIGHT.connects(DOWN)).isFalse();
    }
}
