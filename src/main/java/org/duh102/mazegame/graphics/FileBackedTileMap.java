package org.duh102.mazegame.graphics;

import org.duh102.mazegame.model.exception.maze.MazeException;
import org.duh102.mazegame.model.exception.maze.tileset.TileSizeException;
import org.duh102.mazegame.model.maze.Tile;
import org.duh102.mazegame.model.tileset.TileSet;
import org.duh102.mazegame.util.Pair;
import org.duh102.mazegame.util.Point2DInt;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class FileBackedTileMap implements TileMap {
    private ImageWithOffset characterImage;
    private ImageWithOffset entranceImage;
    private ImageWithOffset exitImage;
    private Map<Byte, List<ImageWithOffset>> tileImages;
    private TileSet tileset;

    Random random = new Random();


    public FileBackedTileMap(ImageWithOffset characterImage,
                             ImageWithOffset entranceImage,
                             ImageWithOffset exitImage,
                             Map<Byte, List<ImageWithOffset>> tileImages,
                             TileSet tileset) {
        this.characterImage = characterImage;
        this.entranceImage = entranceImage;
        this.exitImage = exitImage;
        this.tileImages = tileImages;
        this.tileset = tileset;
    }

    @Override
    public ImageWithOffset getTileFor(Tile tile, int x, int y) {
        byte idx = 0;
        long seed = generateSeedForBlank(x, y);
        if(tile != null) {
            idx = tile.getTileIndex();
            seed = convertVariantToSeed(x, y, tile.getVariantSeed());
        }
        random.setSeed(seed);
        List<ImageWithOffset> tilesForIndex = tileImages.get(idx);
        return tilesForIndex.get(random.nextInt(tilesForIndex.size()));
    }
    @Override
    public Point2DInt getTileSize() {
        return this.tileset.getTileSize();
    }
    @Override
    public ImageWithOffset getCharacterImage() {
        return characterImage;
    }
    @Override
    public ImageWithOffset getEntranceImage() {
        return entranceImage;
    }
    @Override
    public ImageWithOffset getExitImage() {
        return exitImage;
    }

    public TileSet getTileSet() {
        return tileset;
    }
    public static class Builder {
        private ImageWithOffset characterImage;
        private ImageWithOffset entranceImage;
        private ImageWithOffset exitImage;
        private Map<Byte, List<ImageWithOffset>> tileImages;
        private TileSet tileset;

        public Builder() {
        }

        public Builder loadFromTileSet(TileSet tileset) throws IOException, TileSizeException {
            this.tileset = tileset;
            characterImage = loadAndOffsetImage(tileset.getCharacterFile(), convertDoublePair(tileset.getCharacterImageOffset()));
            entranceImage = loadAndOffsetImage(tileset.getEntranceFile(), convertDoublePair(tileset.getEntranceImageOffset()));
            exitImage = loadAndOffsetImage(tileset.getExitFile(), convertDoublePair(tileset.getExitImageOffset()));
            tileImages = loadAndOffsetTiles(tileset);
            return this;
        }
        public FileBackedTileMap build() throws MazeException {
            if(tileset == null || characterImage == null || entranceImage == null
                    || exitImage == null || tileImages == null) {
                List<String> nulledItems = new ArrayList<>();
                if(tileset == null) {
                    nulledItems.add("tileset");
                }
                if(characterImage == null) {
                    nulledItems.add("character image");
                }
                if(entranceImage == null) {
                    nulledItems.add("entrance image");
                }
                if(exitImage == null) {
                    nulledItems.add("exit image");
                }
                if(tileImages == null) {
                    nulledItems.add("tile images");
                }
                throw new MazeException(String.format("Programmer error: %s are null; ensure you provided them to the builder", String.join(", ", nulledItems)));
            }
            return new FileBackedTileMap(characterImage, entranceImage, exitImage, tileImages, tileset);
        }

        private ImageWithOffset loadAndOffsetImage(String fileName, Point2D.Double offset) throws IOException {
            File characterFile = new File(fileName);
            if (!characterFile.exists()) {
                throw new FileNotFoundException(fileName);
            }
            Image imageData = ImageIO.read(characterFile);

            return new ImageWithOffset(imageData, offset);
        }
        private Map<Byte, List<ImageWithOffset>> loadAndOffsetTiles(TileSet tileset) throws IOException, TileSizeException {
            File tileFile = new File(tileset.getTileFile());
            if (!tileFile.exists()) {
                throw new FileNotFoundException(tileset.getTileFile());
            }
            BufferedImage imageData = ImageIO.read(tileFile);
            Point2DInt tileSize = tileset.getTileSize();
            Point2DInt tileOffset = tileset.getTileStartOffset();
            int variants = tileset.getVariants();
            int imageHeight = imageData.getHeight();
            int imageWidth = imageData.getWidth();
            int requiredHeight = tileOffset.getY() + (tileSize.getY() * variants);
            int requiredWidth = tileOffset.getX() + (tileSize.getX() * 16);
            if (imageHeight < requiredHeight) {
                throw new TileSizeException(String.format(
                        "Tile image height %d less than required offset %d + (variants %d * tile height %d) (%d)",
                        imageHeight, tileOffset.getY(), variants, tileSize.getY(), requiredHeight));
            }
            if (imageWidth < requiredWidth) {
                throw new TileSizeException(String.format(
                        "Tile image width %d less than required offset %d + (16 * tile width %d) (%d)",
                        imageWidth, tileOffset.getX(), tileSize.getX(), requiredWidth));
            }
            Map<Byte, List<ImageWithOffset>> tileLookupMap = new HashMap<>();
            for (byte x = 0; x < 16; x++) {
                int xOffset = tileOffset.getX() + (x * tileSize.getX());
                List<ImageWithOffset> variantsOfTile = new ArrayList<>(variants);
                for (int y = 0; y < variants; y++) {
                    int yOffset = tileOffset.getY() + (y * tileSize.getY());
                    Image tile = imageData.getSubimage(xOffset, yOffset, tileSize.getX(), tileSize.getY());
                    variantsOfTile.add(new ImageWithOffset(tile));
                }
                tileLookupMap.put(x, variantsOfTile);
            }
            return tileLookupMap;
        }
        private Point2D.Double convertDoublePair(Pair<Double, Double> pair) {
            return new Point2D.Double(pair.getFirst(), pair.getSecond());
        }
    }
    private long generateSeedForBlank(int x, int y) {
        long intX;
        long intY;
        if(x < 0) {
            intX = (((x+21)*5)<<45);
        } else {
            intX = (((x+45)*3)<<46);
        }
        if( y < 0 ) {
            intY = (((y+33)*7)<<46);
        } else {
            intY = (((y-12)*5)<<45);
        }
        return intX ^ intY;
    }
    private long convertVariantToSeed(int x, int y, int variant) {
        long intermediate = generateSeedForBlank(x, y);
        return variant<<46 ^ intermediate;
    }
}
