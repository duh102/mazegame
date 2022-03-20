package org.duh102.mazegame.model.serialization;

import com.google.gson.*;
import org.duh102.mazegame.util.MutablePair;
import org.duh102.mazegame.util.Pair;

import java.lang.reflect.Type;

public class PairDeserializer implements JsonDeserializer<Pair<?, ?>> {
    @Override
    public Pair<?, ?> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return jsonDeserializationContext.deserialize(jsonElement, MutablePair.class);
    }
}
