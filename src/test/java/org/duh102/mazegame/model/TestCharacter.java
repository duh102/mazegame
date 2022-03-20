package org.duh102.mazegame.model;

import org.duh102.mazegame.model.maze.Character;
import org.duh102.mazegame.model.maze.ExitDirection;
import org.duh102.mazegame.util.Point2DInt;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestCharacter {
    @Test
    public void testCharacterBasics() {
        Character character = new Character();
        character.teleport(Point2DInt.of(10,23));
        assertThat(character.getPosition()).isEqualTo(Point2DInt.of(10,23));
        character.teleport(Point2DInt.of(0,0));
        character.move(ExitDirection.UP);
        assertThat(character.getPosition()).isEqualTo(Point2DInt.of(0,-1));
    }
}
