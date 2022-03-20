package org.duh102.mazegame.model.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.duh102.mazegame.util.Point2DInt;

import java.lang.reflect.Type;

public class Point2DIntSerializer implements JsonSerializer<Point2DInt> {
    @Override
    public JsonElement serialize(Point2DInt point2DInt, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("x", point2DInt.getX());
        obj.addProperty("y", point2DInt.getY());
        return obj;
    }
}
