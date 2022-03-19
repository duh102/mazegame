package org.duh102.mazegame.model.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.jr.ob.api.ValueWriter;
import com.fasterxml.jackson.jr.ob.impl.JSONWriter;
import org.duh102.mazegame.model.creation.ExitDirectionSet;

import java.io.IOException;

public class ExitDirectionSetValueWriter implements ValueWriter {

    @Override
    public void writeValue(JSONWriter jsonWriter, JsonGenerator jsonGenerator, Object o) throws IOException {
        if(! (o instanceof ExitDirectionSet) ) {
            throw new IOException(String.format("Object is not an instance of ExitDirectionSet; %s: %s", o.getClass().getName(), o.toString()));
        }
        ExitDirectionSet dirSet = (ExitDirectionSet)o;
        jsonGenerator.writeNumber(dirSet.getTileIndex());
    }

    @Override
    public Class<?> valueType() {
        return ExitDirectionSet.class;
    }
}
