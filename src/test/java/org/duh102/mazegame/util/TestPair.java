package org.duh102.mazegame.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestPair {
    @Test
    public void testPairConstructor() {
        Pair<Integer, Integer> pair = new ImmutablePair<>(2, 4);
        assertThat(pair.getFirst()).isEqualTo(2);
        assertThat(pair.getSecond()).isEqualTo(4);
    }

    @Test
    public void testPairStaticGet() {
        Pair<Integer, Integer> pair = Pair.of(2, 4);
        assertThat(pair.getFirst()).isEqualTo(2);
        assertThat(pair.getSecond()).isEqualTo(4);
    }
}
