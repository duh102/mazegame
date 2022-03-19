package org.duh102.mazegame.graphics.tileset;

import org.duh102.mazegame.model.Tile;
import org.duh102.mazegame.util.Point2DInt;
import org.duh102.mazegame.util.Pair;

import java.util.Random;

public class Tileset {
    String tileImages;
    String characterImage;
    Point2DInt tileSize;
    Point2DInt tileStartOffset;
    int variants;
    Pair<Double, Double> characterImageOffset;

    Random random = new Random();

    public Tileset(String tileImages, String characterImage, Point2DInt tileSize, Point2DInt tileStartOffset, int variants, Pair<Double, Double> characterImageOffset) {
        this.tileImages = tileImages;
        this.characterImage = characterImage;
        this.tileSize = tileSize;
        this.tileStartOffset = tileStartOffset;
        this.variants = variants;
        this.characterImageOffset = characterImageOffset;
    }

    public Pair<Point2DInt, Point2DInt> getTileCoordinatesFor(Tile tile) {
        random.setSeed(tile.getVariantSeed());
        int xIndex = tileSize.getX()*tile.getTileIndex();
        int yIndex = random.nextInt(variants)*tileSize.getY();
        Point2DInt startCut = tileStartOffset.add(Point2DInt.of(xIndex, yIndex));
        return Pair.of(startCut, tileSize);
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
}
