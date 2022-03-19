package org.duh102.mazegame.model.creation;

import org.duh102.mazegame.model.ExitDirection;

import java.util.Arrays;
import java.util.EnumSet;


public class ExitDirectionSet {
    private EnumSet<ExitDirection> exits;

    public ExitDirectionSet(EnumSet<ExitDirection> exits) {
        this.exits = exits;
    }
    public ExitDirectionSet() {
        this(EnumSet.noneOf(ExitDirection.class));
    }
    public ExitDirectionSet(ExitDirection ... exits) {
        this(EnumSet.copyOf(Arrays.asList(exits)));
    }
    public ExitDirectionSet(ExitDirectionSet exits) {
        this(exits.getExits());
    }

    public EnumSet<ExitDirection> getExits() {
        return exits;
    }

    public byte getTileIndex() {
        byte accumulator = 0;
        for( ExitDirection exit : exits ) {
            accumulator+=exit.getBitMask();
        }
        return accumulator;
    }
    public boolean connects(ExitDirection direction) {
        if(direction == null) {
            return false;
        }
        return exits.contains(direction.getOpposite());
    }
}
