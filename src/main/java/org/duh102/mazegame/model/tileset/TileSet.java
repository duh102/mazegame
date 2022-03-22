package org.duh102.mazegame.model.tileset;

import org.duh102.mazegame.util.Point2DInt;
import org.duh102.mazegame.util.Pair;

import java.io.Serializable;

public class TileSet implements Serializable {
    String tileImages;
    String characterImage;
    Point2DInt tileSize;
    Point2DInt tileStartOffset;
    int variants;
    Pair<Double, Double> characterImageOffset;
    String tileSetName;
    // TODO: Make a no-tile image here too; maybe make it part of the tile map? image 17 perhaps

    public TileSet(String tileSetName, String tileImages, String characterImage, Point2DInt tileSize, Point2DInt tileStartOffset, int variants, Pair<Double, Double> characterImageOffset) {
        this.tileSetName = tileSetName;
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

    public String getTileSetName() {
        return tileSetName;
    }

    public TileSet setTileSetName(String tileSetName) {
        this.tileSetName = tileSetName;
        return this;
    }

    public TileSet setTileFile(String tileImages) {
        this.tileImages = tileImages;
        return this;
    }
    public TileSet setCharacterFile(String characterImage) {
        this.characterImage = characterImage;
        return this;
    }
    public TileSet setTileSize(Point2DInt tileSize) {
        this.tileSize = tileSize;
        return this;
    }
    public TileSet setTileStartOffset(Point2DInt tileStartOffset) {
        this.tileStartOffset = tileStartOffset;
        return this;
    }
    public TileSet setVariants(int variants) {
        this.variants = variants;
        return this;
    }
    public TileSet setCharacterImageOffset(Pair<Double, Double> characterImageOffset) {
        this.characterImageOffset = characterImageOffset;
        return this;
    }
}
