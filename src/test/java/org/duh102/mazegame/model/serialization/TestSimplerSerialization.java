package org.duh102.mazegame.model.serialization;

import com.fasterxml.jackson.jr.ob.JSON;
import org.duh102.mazegame.util.Pair;
import org.duh102.mazegame.util.Point2DInt;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TestSimplerSerialization {

    private JSON testJson = MazeCustomizedJSON.getJSON()
            .with(JSON.Feature.PRETTY_PRINT_OUTPUT)
            .with(JSON.Feature.WRITE_NULL_PROPERTIES);

    @Test
    public void testPairSerialization() throws IOException {
        Pair<Double, Double> aPair = Pair.of(1.2, 4.5);
        String json = testJson.asString(aPair);
        Pair<Double, Double> twoPair = testJson.beanFrom(Pair.class, json);
        assertThat(twoPair).isEqualTo(aPair);
    }

    @Test
    public void testLocationSerialization() throws IOException {
        Point2DInt pt = Point2DInt.of(12, 15);
        String json = testJson.asString(pt);
        Point2DInt ptRead = testJson.beanFrom(Point2DInt.class, json);
        assertThat(ptRead).isEqualTo(pt);
    }
}
