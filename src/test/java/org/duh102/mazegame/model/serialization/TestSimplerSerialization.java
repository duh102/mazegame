package org.duh102.mazegame.model.serialization;

import com.google.gson.Gson;
import org.duh102.mazegame.model.maze.ExitDirection;
import org.duh102.mazegame.model.maze.ExitDirectionSet;
import org.duh102.mazegame.util.Pair;
import org.duh102.mazegame.util.Point2DInt;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TestSimplerSerialization {

    private Gson testJson = MazeCustomizedGSON.getGsonBase().setPrettyPrinting().serializeNulls().create();

    @Test
    public void testPairSerialization() throws IOException {
        Pair<Double, Double> aPair = Pair.of(1.2, 4.5);
        String json = testJson.toJson(aPair);
        assertThat(json).isEqualTo("{\n  \"first\": 1.2,\n  \"second\": 4.5\n}");
        Pair<Double, Double> twoPair = testJson.fromJson(json, Pair.class);
        assertThat(twoPair).isEqualTo(aPair);
    }

    @Test
    public void testPoint2DIntSerialization() throws IOException {
        Point2DInt pt = Point2DInt.of(12, 15);
        String json = testJson.toJson(pt);
        assertThat(json).isEqualTo("{\n  \"x\": 12,\n  \"y\": 15\n}");
        Point2DInt ptRead = testJson.fromJson(json, Point2DInt.class);
        assertThat(ptRead).isEqualTo(pt);
    }

    @Test
    public void testExitDirectionSetSerialization() throws IOException {
        ExitDirectionSet to = new ExitDirectionSet(ExitDirection.UP, ExitDirection.DOWN, ExitDirection.LEFT);
        String json = testJson.toJson(to);
        assertThat(json).isEqualTo("13");
        ExitDirectionSet from = testJson.fromJson(json, ExitDirectionSet.class);
        assertThat(from).isEqualTo(to);
    }
}
