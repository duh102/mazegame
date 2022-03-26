package org.duh102.mazegame.model.tileset;

import org.duh102.mazegame.util.Point2DInt;
import org.duh102.mazegame.util.Pair;

import java.io.Serializable;

public class TileSet implements Serializable {
    String tileImages;
    String characterImage;
    String entranceImage;
    String exitImage;
    Point2DInt tileSize;
    Point2DInt tileStartOffset;
    int variants;
    Pair<Double, Double> characterImageOffset;
    Pair<Double, Double> entranceImageOffset;
    Pair<Double, Double> exitImageOffset;
    String tileSetName;
    // TODO: Make a no-tile image here too; maybe make it part of the tile map? image 17 perhaps

    public TileSet(String tileSetName,
                   String tileImages,
                   String entranceImage,
                   String exitImage,
                   String characterImage,
                   Point2DInt tileSize,
                   Point2DInt tileStartOffset,
                   int variants,
                   Pair<Double, Double> characterImageOffset,
                   Pair<Double, Double> entranceImageOffset,
                   Pair<Double, Double> exitImageOffset) {
        this.tileSetName = tileSetName;
        this.tileImages = tileImages;
        this.entranceImage = entranceImage;
        this.exitImage = exitImage;
        this.characterImage = characterImage;
        this.tileSize = tileSize;
        this.tileStartOffset = tileStartOffset;
        this.variants = variants;
        this.characterImageOffset = characterImageOffset;
        this.entranceImageOffset = entranceImageOffset;
        this.exitImageOffset = exitImageOffset;
    }

    public String getTileFile() {
        return tileImages;
    }
    public String getCharacterFile() {
        return characterImage;
    }
    public String getEntranceFile() {
        return entranceImage;
    }
    public String getExitFile() {
        return exitImage;
    }
    public Pair<Double, Double> getCharacterImageOffset() {
        return characterImageOffset;
    }
    public Pair<Double, Double> getEntranceImageOffset() {
        return entranceImageOffset;
    }
    public Pair<Double, Double> getExitImageOffset() {
        return exitImageOffset;
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
    public TileSet setEntranceFile(String entranceImage) {
        this.entranceImage = entranceImage;
        return this;
    }
    public TileSet setExitFile(String exitImage) {
        this.exitImage = exitImage;
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
    public boolean isFullyDefined() {
        return tileImages != null
                && characterImage != null
                && entranceImage != null
                && exitImage != null
                && tileSize != null
                && tileStartOffset != null
                && variants > 0
                && characterImageOffset != null
                && entranceImageOffset != null
                && exitImageOffset != null
                && tileSetName != null;
    }
}
