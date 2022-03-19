package org.duh102.mazegame.model.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.jr.ob.api.ValueReader;
import com.fasterxml.jackson.jr.ob.impl.JSONReader;
import org.duh102.mazegame.model.ExitDirection;
import org.duh102.mazegame.model.creation.ExitDirectionSet;

import java.io.IOException;
import java.util.EnumSet;

public class ExitDirectionSetValueReader extends ValueReader {
    public ExitDirectionSetValueReader() {
        super(ExitDirectionSet.class);
    }
    @Override
    public Object read(JSONReader jsonReader, JsonParser jsonParser) throws IOException {
        byte exitDirections = (byte)(jsonParser.readValueAs(Byte.class) & 0xF);
        EnumSet<ExitDirection> directions = EnumSet.noneOf(ExitDirection.class);
        for(ExitDirection ed : ExitDirection.values()) {
            if((ed.getBitMask() & exitDirections) > 0) {
                directions.add(ed);
            }
        }
        return new ExitDirectionSet(directions);
    }
}
