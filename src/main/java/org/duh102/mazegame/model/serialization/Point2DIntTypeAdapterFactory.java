package org.duh102.mazegame.model.serialization;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.duh102.mazegame.util.MutablePoint2DInt;
import org.duh102.mazegame.util.Point2DInt;

public class Point2DIntTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (!Point2DInt.class.equals(typeToken.getRawType())) return null;

        return (TypeAdapter<T>) gson.getAdapter(MutablePoint2DInt.class);
    }
}
