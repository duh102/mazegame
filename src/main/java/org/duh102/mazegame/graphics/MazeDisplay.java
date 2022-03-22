package org.duh102.mazegame.graphics;

import org.duh102.mazegame.model.maze.GameBoard;
import org.duh102.mazegame.model.maze.Maze;
import org.duh102.mazegame.model.maze.Tile;
import org.duh102.mazegame.util.Point2DInt;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class MazeDisplay {
    private Image[] images = new Image[2];
    private int showImage = 0;
    private int xSize, ySize;
    private Point2D.Double incrementalCharPosition;
    private Point2DInt charPrev;
    private Point2DInt charCurr;
    private boolean readyForMovement = true;

    GameBoard board;
    TileMap tileMap;


    public MazeDisplay(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        images[0] = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB);
        images[1] = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB);
    }
    public MazeDisplay() {
        this(640, 480);
    }

    public synchronized Image getActiveImage() {
        return images[showImage];
    }
    public synchronized Image getInactiveImage() {
        return images[1-showImage];
    }
    public synchronized MazeDisplay flipActiveImage() {
        showImage = 1-showImage;
        return this;
    }

    public synchronized MazeDisplay redraw() {
        if( board == null || tileMap == null ) {
            getInactiveImage().getGraphics().clearRect(0, 0, xSize, ySize);
            return flipActiveImage();
        }
        Point2DInt charLoc = board.getCharacter().getPosition();
        Maze maze = board.getMaze();
        Point2DInt tileSize = tileMap.getTileSize();
        int xTiles = (int)Math.ceil((xSize+0.0) / tileSize.getX());
        int yTiles = (int)Math.ceil((ySize+0.0) / tileSize.getY());
        int xHalf = xTiles/2;
        int yHalf = yTiles/2;
        xTiles = (xHalf*2)+1;
        yTiles = (yHalf*2)+1;
        int halfTileX = tileSize.getX()/2;
        int halfTileY = tileSize.getY()/2;
        int halfImageWidth = xSize/2;
        int halfImageHeight = ySize/2;
        Graphics drawWith = getInactiveImage().getGraphics();
        for(int x = 0; x < xTiles; x++) {
            for(int y = 0; y < yTiles; y++) {
                Point2DInt checkLoc = Point2DInt.of(charLoc.getX() - (xHalf-x), charLoc.getY() - (yHalf-y));
                Point2DInt drawOffset = Point2DInt.of(halfImageWidth-((xHalf-x)*tileSize.getX())-halfTileX, halfImageHeight-((yHalf-y)*tileSize.getY())-halfTileY);
                Tile tileAt = maze.getTileAt(checkLoc);
                Image tileImage = tileMap.getTileFor(tileAt, checkLoc.getX(), checkLoc.getY()).getImage();
                drawWith.drawImage(tileImage, drawOffset.getX(), drawOffset.getY(), null);
            }
        }
        ImageWithOffset character = tileMap.getCharacterImage();
        Point2D.Double offset = character.getOffset();
        drawWith.drawImage(character.getImage(), (int)Math.round(halfImageWidth+incrementalCharPosition.getX()-offset.getX()), (int)Math.round(halfImageHeight+incrementalCharPosition.getY()-offset.getY()), null);
        return flipActiveImage();
    }
    public MazeDisplay setGameBoard(GameBoard board) {
        this.board = board;
        charCurr = board.getCharacter().getPosition();
        charPrev = charCurr;
        incrementalCharPosition = new Point2D.Double(0, 0);
        return this;
    }
    public GameBoard getBoard() {
        return board;
    }
    public MazeDisplay setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
        return this;
    }
    public TileMap getTileMap() {
        return tileMap;
    }

    public MazeDisplay setIncrementalMovement(double pixelsToMove) {
        synchronized (this) {
            if (readyForMovement) {
                return this;
            }
        }
        Point2DInt diff = charCurr.sub(charPrev);
        if(diff.getX() == 0 && incrementalCharPosition.getX() != 0) {
            incrementalCharPosition.setLocation(0, incrementalCharPosition.getY());
        }
        if (diff.getY() == 0 && incrementalCharPosition.getY() != 0) {
            incrementalCharPosition.setLocation(incrementalCharPosition.getX(), 0);
        }
        boolean gotThere = false;
        if(diff.getX() != 0) {
            if(incrementalCharPosition.getX() < 0 && incrementalCharPosition.getX()+pixelsToMove >= 0
                || incrementalCharPosition.getX() > 0 && incrementalCharPosition.getX()-pixelsToMove <= 0) {
                gotThere = true;
            }
            incrementalCharPosition.setLocation(incrementalCharPosition.getX()+ (diff.getX()*pixelsToMove), incrementalCharPosition.getY());
        }
        if(diff.getY() != 0) {
            if(incrementalCharPosition.getY() < 0 && incrementalCharPosition.getY()+pixelsToMove >= 0
                    || incrementalCharPosition.getY() > 0 && incrementalCharPosition.getY()-pixelsToMove <= 0) {
                gotThere = true;
            }
            incrementalCharPosition.setLocation(incrementalCharPosition.getX(), incrementalCharPosition.getY()+ (diff.getY()*pixelsToMove));
        }
        if(gotThere) {
            incrementalCharPosition.setLocation(0,0);
            charPrev = charCurr;
            synchronized (this) {
                readyForMovement = true;
            }
        }
        return this;
    }
    public synchronized MazeDisplay notifyMoved() {
        readyForMovement = false;
        charPrev = charCurr;
        charCurr = board.getCharacter().getPosition();
        Point2DInt diff = charCurr.sub(charPrev);
        Point2DInt tileSize = tileMap.getTileSize();
        incrementalCharPosition = new Point2D.Double(-diff.getX()*tileSize.getX(), -diff.getY()*tileSize.getY());
        return this;
    }
    public synchronized boolean readyForMovement() {
        return readyForMovement;
    }
    public MazeDisplay setImageDimensions(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        images[0] = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB);
        images[1] = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB);
        return this;
    }
}
