package org.duh102.mazegame.graphics;

import org.duh102.mazegame.util.Pair;

import java.awt.*;
import java.awt.geom.Point2D;

public class ImageWithOffset {
    private Image image;
    private Point2D.Double offset;
    public ImageWithOffset(Image image, Point2D.Double offset) {
        this.image = image;
        this.offset = offset;
    }
    public ImageWithOffset(Image image, Pair<Double, Double> offset) {
        this(image, new Point2D.Double(offset.getFirst(), offset.getSecond()));
    }
    public ImageWithOffset(Image image) {
        this(image, new Point2D.Double(0,0));
    }

    public Image getImage() {
        return image;
    }
    public Point2D.Double getOffset() {
        return offset;
    }
}
