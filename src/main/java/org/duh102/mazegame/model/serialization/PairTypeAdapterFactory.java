package org.duh102.mazegame.model.serialization;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.duh102.mazegame.util.MutablePair;
import org.duh102.mazegame.util.Pair;

public class PairTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (!Pair.class.equals(typeToken.getRawType())) return null;

        return (TypeAdapter<T>) gson.getAdapter(MutablePair.class);
    }
}
