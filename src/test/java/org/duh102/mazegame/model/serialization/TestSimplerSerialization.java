package org.duh102.mazegame.model.serialization;

import com.fasterxml.jackson.jr.ob.JSON;
import org.duh102.mazegame.util.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TestSimplerSerialization {

    private JSON testJson = JSON.std.with(JSON.Feature.PRETTY_PRINT_OUTPUT).with(JSON.Feature.WRITE_NULL_PROPERTIES);

    @Test
    public void testPairSerialization() throws IOException {
        Pair<Double, Double> aPair = Pair.of(1.2, 4.5);
        String json = testJson.asString(aPair);
        Pair<Double, Double> twoPair = testJson.beanFrom(Pair.class, json);
        assertThat(twoPair).isEqualTo(aPair);
    }
}
