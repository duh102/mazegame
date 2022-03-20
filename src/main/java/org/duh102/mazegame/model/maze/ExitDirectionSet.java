package org.duh102.mazegame.model.maze;

import java.io.Serializable;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;


public class ExitDirectionSet implements Serializable {
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
    public boolean canAcceptFrom(ExitDirection direction) {
        if(direction == null) {
            return false;
        }
        return exits.contains(direction.getOpposite());
    }
    public boolean canMoveTo(ExitDirection direction) {
        if(direction == null) {
            return false;
        }
        return exits.contains(direction);
    }

    public ExitDirectionSet addExit(ExitDirection exit) {
        exits.add(exit);
        return this;
    }
    public ExitDirectionSet closeExit(ExitDirection exit) {
        exits.remove(exit);
        return this;
    }

    @Override
    public int hashCode() {
        return exits.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof ExitDirectionSet)) {
            return false;
        }
        ExitDirectionSet other = (ExitDirectionSet)obj;
        return Objects.equals(getExits(), other.getExits());
    }

    @Override
    public String toString() {
        if(exits.isEmpty()) {
            return "~";
        }
        StringBuilder sb = new StringBuilder();
        for(ExitDirection exit : exits) {
            sb.append(exit.toString());
        }
        return sb.toString();
    }
}
