package org.duh102.mazegame.model.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.jr.ob.api.ValueReader;
import com.fasterxml.jackson.jr.ob.impl.JSONReader;
import org.duh102.mazegame.util.Point2DInt;

import java.io.IOException;
import java.util.Map;

public class Point2DIntValueReader extends ValueReader {
    protected Point2DIntValueReader() {
        super(Point2DInt.class);
    }

    @Override
    public Object read(JSONReader jsonReader, JsonParser jsonParser) throws IOException {
        Map<String, Object> pairMap = jsonReader.readMap();
        return Point2DInt.of((Integer)pairMap.get("first"), (Integer)pairMap.get("second"));
    }
}
