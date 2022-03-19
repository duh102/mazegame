package org.duh102.mazegame.model;

import java.util.Arrays;
import java.util.EnumSet;

public class Tile {
    private EnumSet<ExitDirection> exits;
    private int variantSeed;

    public Tile(EnumSet<ExitDirection> exits, int variantSeed) {
        this.exits = exits;
    }

    public Tile(EnumSet<ExitDirection> exits) {
        this(exits, 0);
    }
    public Tile() {
        this(EnumSet.noneOf(ExitDirection.class));
    }
    public Tile(ExitDirection exit) {
        this(EnumSet.of(exit));
    }
    public Tile(ExitDirection ... exits) {
        this(EnumSet.copyOf(Arrays.asList(exits)));
    }

    public Tile(Tile toCopy) {
        this(toCopy.getExits(), toCopy.getVariantSeed());
    }

    public EnumSet<ExitDirection> getExits() {
        return exits;
    }

    public boolean connects(ExitDirection direction) {
        if(direction == null) {
            return false;
        }
        return exits.contains(direction.getOpposite());
    }

    public byte getTileIndex() {
        byte accumulator = 0;
        for( ExitDirection exit : exits ) {
            accumulator+=exit.getBitMask();
        }
        return accumulator;
    }

    public int getVariantSeed() {
        return variantSeed;
    }
    public Tile setVariantSeed(int variantSeed) {
        this.variantSeed = variantSeed;
        return this;
    }
}
