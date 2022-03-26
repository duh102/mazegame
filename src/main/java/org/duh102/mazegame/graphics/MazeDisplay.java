package org.duh102.mazegame.graphics;

import org.duh102.mazegame.model.exception.beanregistry.NoBeanFoundException;
import org.duh102.mazegame.model.maze.GameBoard;
import org.duh102.mazegame.model.maze.Maze;
import org.duh102.mazegame.model.maze.Tile;
import org.duh102.mazegame.util.Provider;
import org.duh102.mazegame.util.beanreg.BeanRegistry;
import org.duh102.mazegame.util.beanreg.CachedBeanRetriever;
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

    CachedBeanRetriever<GameBoard> board;
    CachedBeanRetriever<Provider<TileMap>> tileMap;

    public MazeDisplay(int xSize, int ySize, BeanRegistry registry) {
        this.xSize = xSize;
        this.ySize = ySize;
        images[0] = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB);
        images[1] = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB);
        board = new CachedBeanRetriever<>(registry, GameBoard.class);
        tileMap = new CachedBeanRetriever<>(registry, (Class<Provider<TileMap>>)(new Provider<TileMap>(new FallbackTileMap())).getClass());
    }
    public MazeDisplay(BeanRegistry registry) {
        this(640, 480, registry);
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
        GameBoard gb = null;
        TileMap tm = null;
        try {
            gb = board.get();
            tm = tileMap.get().get();
        } catch(NoBeanFoundException nbfe) {
            System.out.printf("Don't have %s\n", board.hasBean()? "tileMap" : "board");
        }
        if( gb == null || tm == null ) {
            getInactiveImage().getGraphics().clearRect(0, 0, xSize, ySize);
            return flipActiveImage();
        }
        Point2DInt entrance = gb.getMaze().getEntrance();
        Point2DInt exit = gb.getMaze().getExit();
        if(charCurr == null) {
            charCurr = gb.getCharacter().getPosition();
            charPrev = charCurr;
            incrementalCharPosition = new Point2D.Double(0, 0);
        }
        Point2DInt charLoc = gb.getCharacter().getPosition();
        Maze maze = gb.getMaze();
        Point2DInt tileSize = tm.getTileSize();
        int xTiles = (int)Math.ceil((xSize+0.0) / tileSize.getX())+2;
        int yTiles = (int)Math.ceil((ySize+0.0) / tileSize.getY())+2;
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
                Point2DInt drawOffset = Point2DInt.of(
                        (int)Math.round(halfImageWidth-((xHalf-x)*tileSize.getX())-halfTileX-incrementalCharPosition.getX()),
                        (int)Math.round(halfImageHeight-((yHalf-y)*tileSize.getY())-halfTileY-incrementalCharPosition.getY())
                );
                Tile tileAt = maze.getTileAt(checkLoc);
                Image tileImage = tm.getTileFor(tileAt, checkLoc.getX(), checkLoc.getY()).getImage();
                drawWith.drawImage(tileImage, drawOffset.getX(), drawOffset.getY(), null);
                if (checkLoc.equals(entrance)) {
                    ImageWithOffset entImage = tm.getEntranceImage();
                    Point2D.Double entOff = entImage.getOffset();
                    drawWith.drawImage(entImage.getImage(),
                            (int)Math.round(drawOffset.getX()+halfTileX-entOff.getX()),
                            (int)Math.round(drawOffset.getY()+halfTileY-entOff.getY()),
                            null);
                }
                if (checkLoc.equals(exit)) {
                    ImageWithOffset exImage = tm.getExitImage();
                    Point2D.Double entOff = exImage.getOffset();
                    drawWith.drawImage(exImage.getImage(),
                            (int)Math.round(drawOffset.getX()+halfTileX-entOff.getX()),
                            (int)Math.round(drawOffset.getY()+halfTileY-entOff.getY()),
                            null);
                }
            }
        }
        ImageWithOffset character = tm.getCharacterImage();
        Point2D.Double offset = character.getOffset();
        drawWith.drawImage(character.getImage(), (int)Math.round(halfImageWidth-offset.getX()), (int)Math.round(halfImageHeight-offset.getY()), null);
        return flipActiveImage();
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
        charCurr = board.get().getCharacter().getPosition();
        Point2DInt diff = charCurr.sub(charPrev);
        Point2DInt tileSize = tileMap.get().get().getTileSize();
        incrementalCharPosition = new Point2D.Double(-diff.getX()*tileSize.getX(), -diff.getY()*tileSize.getY());
        return this;
    }
    public synchronized MazeDisplay resetMovement() {
        readyForMovement = true;
        incrementalCharPosition = new Point2D.Double(0,0);
        charCurr = board.get().getCharacter().getPosition();
        charPrev = charCurr;
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
