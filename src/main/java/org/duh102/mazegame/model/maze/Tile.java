package org.duh102.mazegame.model.maze;

import java.io.Serializable;
import java.util.Objects;

public class Tile implements Serializable {
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
    public Tile(int variantSeed, ExitDirection exit) {
        this(new ExitDirectionSet(exit), variantSeed);
    }
    public Tile(ExitDirection ... exits) {
        this(new ExitDirectionSet(exits));
    }
    public Tile(int variantSeed, ExitDirection ... exits) {
        this(new ExitDirectionSet(exits), variantSeed);
    }

    public Tile(Tile toCopy) {
        this(toCopy.getExits(), toCopy.getVariantSeed());
    }

    public ExitDirectionSet getExits() {
        return exits;
    }
    public Tile setExits(ExitDirectionSet exits) {
        this.exits = exits;
        return this;
    }

    public boolean canAcceptFrom(ExitDirection direction) {
        return exits.canAcceptFrom(direction);
    }
    public boolean canMoveTo(ExitDirection direction) { return exits.canMoveTo(direction); }

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

    @Override
    public int hashCode() {
        return Objects.hash(variantSeed, exits);
    }

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof Tile)) {
            return false;
        }
        Tile other = (Tile) obj;
        return Objects.equals(variantSeed, other.getVariantSeed()) && Objects.equals(exits, other.getExits());
    }

    @Override
    public String toString() {
        return String.format("Tile(%s, %d)", exits.toString(), variantSeed);
    }
}
