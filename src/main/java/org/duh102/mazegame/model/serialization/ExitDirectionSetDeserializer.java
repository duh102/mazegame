package org.duh102.mazegame.model.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.duh102.mazegame.model.maze.ExitDirection;
import org.duh102.mazegame.model.maze.ExitDirectionSet;

import java.lang.reflect.Type;
import java.util.EnumSet;

public class ExitDirectionSetDeserializer implements JsonDeserializer<ExitDirectionSet> {
    @Override
    public ExitDirectionSet deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new ExitDirectionSet(jsonElement.getAsByte());
    }
}
