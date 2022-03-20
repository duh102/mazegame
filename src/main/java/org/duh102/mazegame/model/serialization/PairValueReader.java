package org.duh102.mazegame.model.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.jr.ob.api.ValueReader;
import com.fasterxml.jackson.jr.ob.impl.JSONReader;
import org.duh102.mazegame.util.Pair;

import java.io.IOException;
import java.util.Map;

public class PairValueReader extends ValueReader {
    public PairValueReader() {
        super(Pair.class);
    }

    @Override
    public Object read(JSONReader jsonReader, JsonParser jsonParser) throws IOException {
        Map<String, Object> pairMap = jsonReader.readMap();
        return Pair.of(pairMap.get("first"), pairMap.get("second"));
    }
}
