package org.duh102.mazegame.model.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.duh102.mazegame.model.maze.ExitDirectionSet;

import java.lang.reflect.Type;

public class ExitDirectionSetSerializaer implements JsonSerializer<ExitDirectionSet> {
    @Override
    public JsonElement serialize(ExitDirectionSet exitDirectionSet, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(exitDirectionSet.getTileIndex());
    }
}
