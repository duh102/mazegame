package org.duh102.mazegame.model.tileset;

import org.duh102.mazegame.model.maze.Tile;
import org.duh102.mazegame.util.Point2DInt;
import org.duh102.mazegame.util.Pair;

import java.io.Serializable;
import java.util.Random;

public class TileSet implements Serializable {
    String tileImages;
    String characterImage;
    Point2DInt tileSize;
    Point2DInt tileStartOffset;
    int variants;
    Pair<Double, Double> characterImageOffset;
    // TODO: Make a no-tile image here too; maybe make it part of the tile map? image 17 perhaps

    public TileSet(String tileImages, String characterImage, Point2DInt tileSize, Point2DInt tileStartOffset, int variants, Pair<Double, Double> characterImageOffset) {
        this.tileImages = tileImages;
        this.characterImage = characterImage;
        this.tileSize = tileSize;
        this.tileStartOffset = tileStartOffset;
        this.variants = variants;
        this.characterImageOffset = characterImageOffset;
    }

    public String getTileFile() {
        return tileImages;
    }
    public String getCharacterFile() {
        return characterImage;
    }
    public Pair<Double, Double> getCharacterImageOffset() {
        return characterImageOffset;
    }
    public Point2DInt getTileSize() {
        return tileSize;
    }
    public int getVariants() {
        return variants;
    }
    public Point2DInt getTileStartOffset() {
        return tileStartOffset;
    }
}
