package org.duh102.mazegame.model.serialization;

import com.fasterxml.jackson.jr.ob.api.ReaderWriterProvider;
import com.fasterxml.jackson.jr.ob.api.ValueReader;
import com.fasterxml.jackson.jr.ob.api.ValueWriter;
import com.fasterxml.jackson.jr.ob.impl.JSONReader;
import com.fasterxml.jackson.jr.ob.impl.JSONWriter;
import org.duh102.mazegame.model.creation.ExitDirectionSet;
import org.duh102.mazegame.util.Pair;
import org.duh102.mazegame.util.Point2DInt;

public class MazeRWProvider extends ReaderWriterProvider {
    @Override
    public ValueReader findValueReader(JSONReader jsonReader, Class<?> aClass) {
        if(aClass == ExitDirectionSet.class) {
            return new ExitDirectionSetValueReader();
        } else if(aClass == org.duh102.mazegame.util.Pair.class) {
            return new PairValueReader();
        } else if(aClass == Point2DInt.class) {
            return new Point2DIntValueReader();
        }
        return super.findValueReader(jsonReader, aClass);
    }

    @Override
    public ValueWriter findValueWriter(JSONWriter jsonWriter, Class<?> aClass) {
        if(aClass == ExitDirectionSet.class) {
            return new ExitDirectionSetValueWriter();
        }
        return super.findValueWriter(jsonWriter, aClass);
    }
}
