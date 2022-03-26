package org.duh102.mazegame.graphics;

import org.duh102.mazegame.client.state.GameState;
import org.duh102.mazegame.client.state.MazeStateController;
import org.duh102.mazegame.model.exception.beanregistry.NoBeanFoundException;
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

    private final CachedBeanRetriever<MazeStateController> mazeStateController;
    private final CachedBeanRetriever<Provider<TileMap>> tileMap;

    public MazeDisplay(int xSize, int ySize, BeanRegistry registry) {
        this.xSize = xSize;
        this.ySize = ySize;
        images[0] = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB);
        images[1] = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB);
        mazeStateController = new CachedBeanRetriever<>(registry, MazeStateController.class);
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
        MazeStateController sc = null;
        TileMap tm = null;
        try {
            sc = mazeStateController.get();
            tm = tileMap.get().get();
        } catch(NoBeanFoundException nbfe) {
            System.out.printf("Don't have %s\n", mazeStateController.hasBean()? "tileMap" : "board");
        }
        if( sc == null || tm == null ) {
            getInactiveImage().getGraphics().clearRect(0, 0, xSize, ySize);
            return flipActiveImage();
        }
        Point2DInt entrance = sc.getMaze().getEntrance();
        Point2DInt exit = sc.getMaze().getExit();
        if(charCurr == null) {
            charCurr = sc.getBoard().getCharacter().getPosition();
            charPrev = charCurr;
            incrementalCharPosition = new Point2D.Double(0, 0);
        }
        Point2DInt charLoc = sc.getBoard().getCharacter().getPosition();
        Maze maze = sc.getMaze();
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
        GameState gameState = sc.getGameStateContainer().getState();
        if(gameState == GameState.WON) {
            drawStringWithBackground("You win!", drawWith,
                    Point2DInt.of(3,3), Point2DInt.of(halfImageWidth, halfImageHeight),
                    Color.BLACK, Color.WHITE);
        } else if(gameState == GameState.EDITING) {
            drawStringWithBackground("EDITING", drawWith,
                    Point2DInt.of(3,3), Point2DInt.of(halfImageWidth, halfImageHeight),
                    null, Color.RED);
        }
        return flipActiveImage();
    }
    private void drawStringWithBackground(String string, Graphics graphics, Point2DInt padding, Point2DInt center, Color bgColor, Color fgColor) {
        FontMetrics fontMetrics = graphics.getFontMetrics();
        Point2DInt stringSize = Point2DInt.of(fontMetrics.stringWidth(string), fontMetrics.getHeight());
        Point2DInt halfStringSize = Point2DInt.of(stringSize.getX()/2, stringSize.getY()/2);
        if(bgColor != null) {
            graphics.setColor(bgColor);
            graphics.fillRect(center.getX()-halfStringSize.getX()-padding.getX(),
                    center.getY()-halfStringSize.getY()-padding.getY(),
                    stringSize.getX() + padding.getX()*2, stringSize.getY() + padding.getY()*2);
        }
        graphics.setColor(fgColor);
        graphics.drawString(string, center.getX()-halfStringSize.getX(), center.getY()+halfStringSize.getY()-padding.getY());
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
        charCurr = mazeStateController.get().getBoard().getCharacter().getPosition();
        Point2DInt diff = charCurr.sub(charPrev);
        Point2DInt tileSize = tileMap.get().get().getTileSize();
        incrementalCharPosition = new Point2D.Double(-diff.getX()*tileSize.getX(), -diff.getY()*tileSize.getY());
        return this;
    }
    public synchronized MazeDisplay resetMovement() {
        readyForMovement = true;
        incrementalCharPosition = new Point2D.Double(0,0);
        charCurr = mazeStateController.get().getBoard().getCharacter().getPosition();
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
