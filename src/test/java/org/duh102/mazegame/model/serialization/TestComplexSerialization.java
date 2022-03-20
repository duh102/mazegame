package org.duh102.mazegame.model.serialization;

import com.google.gson.Gson;
import org.duh102.mazegame.model.maze.ExitDirection;
import org.duh102.mazegame.model.maze.Maze;
import org.duh102.mazegame.model.maze.Tile;
import org.duh102.mazegame.model.maze.Character;
import org.duh102.mazegame.util.Point2DInt;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TestComplexSerialization {

    private Gson testJson = MazeCustomizedGSON.getGsonBase().setPrettyPrinting().serializeNulls().create();

    @Test
    public void testTileSerialization() throws IOException {
        Tile from = new Tile(123, ExitDirection.UP, ExitDirection.RIGHT);
        String json = testJson.toJson(from);
        assertThat(json).isEqualTo("{\n" +
                "  \"exits\": 3,\n" +
                "  \"variantSeed\": 0\n" +
                "}");
        Tile to = testJson.fromJson(json, Tile.class);
        assertThat(to).isEqualTo(from);
    }

    @Test
    public void testCharacterSerialization() throws IOException {
        Character from = new Character(Point2DInt.of(23, 45));
        String json = testJson.toJson(from);
        assertThat(json).isEqualTo("{\n" +
                "  \"position\": {\n" +
                "    \"x\": 23,\n" +
                "    \"y\": 45\n" +
                "  }\n" +
                "}");
        Character to = testJson.fromJson(json, Character.class);
        assertThat(to).isEqualTo(from);
    }

    @Test
    public void testMazeSerialization() throws IOException {
        Tile[][] tiles2x2 = new Tile[2][2];
        tiles2x2[0][0] = new Tile(ExitDirection.DOWN);
        tiles2x2[0][1] = new Tile(ExitDirection.UP, ExitDirection.RIGHT);
        tiles2x2[1][0] = new Tile(ExitDirection.DOWN);
        tiles2x2[1][1] = new Tile(ExitDirection.LEFT, ExitDirection.UP);
        Maze maze = new Maze(tiles2x2, Point2DInt.of(0,0), Point2DInt.of(1,0));

        String json = testJson.toJson(maze);

        assertThat(json).isEqualTo("{\n" +
                "  \"tiles\": [\n" +
                "    [\n" +
                "      {\n" +
                "        \"exits\": 4,\n" +
                "        \"variantSeed\": 0\n" +
                "      },\n" +
                "      {\n" +
                "        \"exits\": 3,\n" +
                "        \"variantSeed\": 0\n" +
                "      }\n" +
                "    ],\n" +
                "    [\n" +
                "      {\n" +
                "        \"exits\": 4,\n" +
                "        \"variantSeed\": 0\n" +
                "      },\n" +
                "      {\n" +
                "        \"exits\": 9,\n" +
                "        \"variantSeed\": 0\n" +
                "      }\n" +
                "    ]\n" +
                "  ],\n" +
                "  \"entrance\": {\n" +
                "    \"x\": 0,\n" +
                "    \"y\": 0\n" +
                "  },\n" +
                "  \"exit\": {\n" +
                "    \"x\": 1,\n" +
                "    \"y\": 0\n" +
                "  }\n" +
                "}");
        Maze read = testJson.fromJson(json, Maze.class);
        assertThat(read).isEqualTo(maze);
    }
}
