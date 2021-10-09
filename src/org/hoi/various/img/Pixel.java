package org.hoi.various.img;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Pixel {
    public abstract Point getInitialPoint ();
    public abstract Point getFinalPoint ();
    public abstract Color getColor ();

    // STATIC
    public static class ColorPixel extends Pixel {
        final private Color color;
        final private Point startPoint;
        final private Point endPoint;

        public ColorPixel (Color color, Point startPoint, Point delta) {
            this.color = color;
            this.startPoint = startPoint;
            this.endPoint = new Point(startPoint.x + delta.x, startPoint.y + delta.y);
        }

        public ColorPixel (Color color, Point startPoint) {
            this.color = color;
            this.startPoint = startPoint;
            this.endPoint = startPoint;
        }

        public ColorPixel (Color color, int x, int y, Point delta) {
            this(color, new Point(x, y), delta);
        }

        public ColorPixel (Color color, Pixel other) {
            this.color = color;
            this.startPoint = other.getFinalPoint();
            this.endPoint = other.getFinalPoint();
        }

        @Override
        public Point getInitialPoint() {
            return startPoint;
        }

        @Override
        public Point getFinalPoint() {
            return endPoint;
        }

        @Override
        public Color getColor() {
            return color;
        }
    }

    public static class ImagePixel extends Pixel {
        final private BufferedImage image;
        final private Point startPoint;
        final private Point endPoint;

        public ImagePixel (BufferedImage image, Point startPoint, Point delta) {
            this.image = image;
            this.startPoint = startPoint;
            this.endPoint = new Point(startPoint.x + delta.x, startPoint.y + delta.y);
        }

        public ImagePixel (BufferedImage image, Point startPoint) {
            this.image = image;
            this.startPoint = startPoint;
            this.endPoint = startPoint;
        }

        public ImagePixel (BufferedImage image, int x, int y, Point delta) {
            this(image, new Point(x, y), delta);
        }

        public Point getInitialPoint() {
            return startPoint;
        }

        public Point getFinalPoint () {
            return endPoint;
        }

        public Color getColor () {
            return new Color(image.getRGB(startPoint.x, startPoint.y), true);
        }
    }
}
