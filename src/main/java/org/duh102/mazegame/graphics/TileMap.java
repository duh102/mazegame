package org.duh102.mazegame.graphics;

import org.duh102.mazegame.model.maze.Tile;
import org.duh102.mazegame.util.Point2DInt;

public interface TileMap {
    ImageWithOffset getTileFor(Tile tile, int x, int y);
    Point2DInt getTileSize();
    ImageWithOffset getCharacterImage();
    ImageWithOffset getEntranceImage();
    ImageWithOffset getExitImage();
}
