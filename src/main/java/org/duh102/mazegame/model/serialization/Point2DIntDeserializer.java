package org.duh102.mazegame.model.serialization;

import com.google.gson.*;
import org.duh102.mazegame.util.MutablePoint2DInt;
import org.duh102.mazegame.util.Point2DInt;

import java.lang.reflect.Type;

public class Point2DIntDeserializer implements JsonDeserializer<Point2DInt> {
    @Override
    public Point2DInt deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return jsonDeserializationContext.deserialize(jsonElement, MutablePoint2DInt.class);
    }
}
