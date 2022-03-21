package org.duh102.mazegame.graphics;

import org.duh102.mazegame.model.exception.MazeException;
import org.duh102.mazegame.model.exception.TileSizeException;
import org.duh102.mazegame.model.maze.Tile;
import org.duh102.mazegame.model.tileset.TileSet;
import org.duh102.mazegame.util.Point2DInt;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class TileMap {
    private ImageWithOffset characterImage;
    private Map<Byte, List<ImageWithOffset>> tileImages;
    private TileSet tileset;

    Random random = new Random();


    public TileMap(ImageWithOffset characterImage, Map<Byte, List<ImageWithOffset>> tileImages, TileSet tileset) {
        this.characterImage = characterImage;
        this.tileImages = tileImages;
        this.tileset = tileset;
    }

    public ImageWithOffset getTileFor(Tile tile) {
        List<ImageWithOffset> tilesForIndex = tileImages.get(tile.getTileIndex());
        random.setSeed(tile.getVariantSeed());
        return tilesForIndex.get(random.nextInt(tilesForIndex.size()));
    }
    public TileSet getTileset() {
        return tileset;
    }
    public ImageWithOffset getCharacterImage() {
        return characterImage;
    }

    public static class Builder {
        private TileSet tileset;
        private ImageWithOffset characterImage;
        private Map<Byte, List<ImageWithOffset>> tileImages;

        public Builder() {
        }

        public Builder loadFromTileset(TileSet tileset) throws IOException, TileSizeException {
            this.tileset = tileset;
            characterImage = loadAndOffsetCharacter(tileset);
            tileImages = loadAndOffsetTiles(tileset);
            return this;
        }
        public TileMap build() throws MazeException {
            if(tileset == null || characterImage == null || tileImages == null) {
                throw new MazeException();
            }
            return new TileMap(characterImage, tileImages, tileset);
        }

        private ImageWithOffset loadAndOffsetCharacter(TileSet tileset) throws IOException {
            File characterFile = new File(tileset.getCharacterFile());
            if (!characterFile.exists()) {
                throw new FileNotFoundException(tileset.getCharacterFile());
            }
            Image imageData = ImageIO.read(characterFile);

            return new ImageWithOffset(imageData, tileset.getCharacterImageOffset());
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
            if (imageData.getHeight(null) < tileOffset.getY() + (tileSize.getY() * variants)) {
                throw new TileSizeException();
            }
            if (imageData.getWidth(null) < tileOffset.getX() + (tileSize.getX() * 16)) {
                throw new TileSizeException();
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
    }
}
