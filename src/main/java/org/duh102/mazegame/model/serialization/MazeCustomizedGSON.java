package org.duh102.mazegame.model.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.duh102.mazegame.model.maze.ExitDirectionSet;
import org.duh102.mazegame.util.Pair;
import org.duh102.mazegame.util.Point2DInt;

public class MazeCustomizedGSON {
    public static Gson getGson() {
        return getGsonBase().create();
    }
    public static GsonBuilder getGsonBase() {
        return new GsonBuilder()
                .registerTypeAdapter(ExitDirectionSet.class, new ExitDirectionSetDeserializer())
                .registerTypeAdapter(ExitDirectionSet.class, new ExitDirectionSetSerializaer())
                .registerTypeAdapter(Point2DInt.class, new Point2DIntDeserializer())
                .registerTypeAdapter(Point2DInt.class, new Point2DIntSerializer())
                .registerTypeAdapter(Pair.class, new PairDeserializer())
                ;
    }
}
