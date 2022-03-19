package org.duh102.mazegame.model;

import org.duh102.mazegame.model.creation.ExitDirectionSet;

public class Tile {
    private ExitDirectionSet exits;
    private int variantSeed;

    public Tile(ExitDirectionSet exits, int variantSeed) {
        this.exits = exits;
    }

    public Tile(ExitDirectionSet exits) {
        this(exits, 0);
    }
    public Tile() {
        this(new ExitDirectionSet());
    }
    public Tile(ExitDirection exit) {
        this(new ExitDirectionSet(exit));
    }
    public Tile(ExitDirection ... exits) {
        this(new ExitDirectionSet(exits));
    }

    public Tile(Tile toCopy) {
        this(toCopy.getExits(), toCopy.getVariantSeed());
    }

    public ExitDirectionSet getExits() {
        return exits;
    }

    public boolean connects(ExitDirection direction) {
        return exits.connects(direction);
    }

    public byte getTileIndex() {
        return getExits().getTileIndex();
    }

    public int getVariantSeed() {
        return variantSeed;
    }
    public Tile setVariantSeed(int variantSeed) {
        this.variantSeed = variantSeed;
        return this;
    }
}
