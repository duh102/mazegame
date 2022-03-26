package org.duh102.mazegame.graphics;

import org.duh102.mazegame.model.maze.ExitDirection;
import org.duh102.mazegame.model.maze.ExitDirectionSet;
import org.duh102.mazegame.model.maze.Tile;
import org.duh102.mazegame.util.Point2DInt;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class FallbackTileMap implements TileMap {
    public static final Point2DInt FALLBACK_TILE_SIZE = Point2DInt.of(32, 32);
    public static final Point2DInt FALLBACK_CHAR_SIZE = Point2DInt.of(24, 24);
    private ImageWithOffset blankTile;
    private ImageWithOffset characterImage;
    private ImageWithOffset entranceImage;
    private ImageWithOffset exitImage;
    private Map<Byte, ImageWithOffset> tileImages;

    public FallbackTileMap() {
        tileImages = new HashMap<>();
        for(byte i = 0; i < 16; i++) {
            tileImages.put(i, generateTile(new ExitDirectionSet(i))
            );
        }
        characterImage = generateCharacter();
        blankTile = generateBlankTile();
        entranceImage = generateEntranceImage();
        exitImage = generateExitImage();
    }
    public ImageWithOffset generateTile(ExitDirectionSet exits) {
        int xSize = FALLBACK_TILE_SIZE.getX();
        int ySize = FALLBACK_TILE_SIZE.getY();
        BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.BLACK);
        g.clearRect(0,0,xSize,ySize);
        g.setColor(Color.WHITE);
        g.fillRect(3,3,xSize-6, xSize-6);
        for(ExitDirection exit : exits.getExits()) {
            Point2DInt exitDir = exit.getMoveDirection();
            g.fillRect(3+(exitDir.getX()*3), 3+(exitDir.getY()*3), xSize-6, ySize-6);
        }
        return new ImageWithOffset(image);
    }
    public ImageWithOffset generateCharacter() {
        int xSize = FALLBACK_CHAR_SIZE.getX();
        int ySize = FALLBACK_CHAR_SIZE.getY();
        BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, xSize, ySize);
        g2d.setComposite(AlphaComposite.Src);
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(1, 1, xSize-2, ySize-2);
        g2d.setColor(Color.BLACK);
        g2d.drawLine(xSize/3, ySize/4, xSize/3, ySize/2);
        g2d.drawLine(2*xSize/3, ySize/4, 2*xSize/3, ySize/2);
        return new ImageWithOffset(image, new Point2D.Double(xSize/2.0, ySize/2.0));
    }
    public ImageWithOffset generateBlankTile() {
        int xSize = FALLBACK_TILE_SIZE.getX();
        int ySize = FALLBACK_TILE_SIZE.getY();
        BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.clearRect(0,0,xSize,ySize);
        return new ImageWithOffset(image);
    }
    public ImageWithOffset generateEntranceImage() {
        int xSize = FALLBACK_CHAR_SIZE.getX();
        int ySize = FALLBACK_CHAR_SIZE.getY();
        BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, xSize, ySize);
        g2d.setComposite(AlphaComposite.Src);
        g2d.setColor(Color.GREEN);
        g2d.drawRect(3, 3, xSize-6, ySize-6);
        return new ImageWithOffset(image, new Point2D.Double(xSize/2.0, ySize/2.0));
    }
    public ImageWithOffset generateExitImage() {
        int xSize = FALLBACK_CHAR_SIZE.getX();
        int ySize = FALLBACK_CHAR_SIZE.getY();
        BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, xSize, ySize);
        g2d.setComposite(AlphaComposite.Src);
        g2d.setColor(Color.RED);
        g2d.drawOval(3, 3, xSize-6, ySize-6);
        return new ImageWithOffset(image, new Point2D.Double(xSize/2.0, ySize/2.0));
    }

    @Override
    public ImageWithOffset getTileFor(Tile tile, int x, int y) {
        if(tile == null) {
            return blankTile;
        }
        return tileImages.get(tile.getTileIndex());
    }
    @Override
    public Point2DInt getTileSize() {
        return FALLBACK_TILE_SIZE;
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
}
