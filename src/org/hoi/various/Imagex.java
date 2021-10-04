package org.hoi.various;

import org.hoi.various.collection.Couple;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Imagex {
    public static class ImagePixel {
        final private BufferedImage image;
        final private Point point;

        public ImagePixel (BufferedImage image, Point point) {
            this.image = image;
            this.point = point;
        }

        public ImagePixel (BufferedImage image, int x, int y) {
            this(image, new Point(x, y));
        }

        public Point getPoint () {
            return point;
        }

        public int getPixel () {
            return image.getRGB(point.x, point.y);
        }

        public Color getColor () {
            return new Color(getPixel(), true);
        }

        @Override
        public String toString() {
            return "ImagePixel{" +
                    "point=" + point +
                    ", color=" + getColor() +
                    '}';
        }
    }

    public static Collection<ImagePixel> collection (BufferedImage image) {
        return new AbstractCollection<ImagePixel>() {
            @Override
            public Iterator<ImagePixel> iterator() {
                return new Iterator<ImagePixel>() {
                    int x, y;

                    @Override
                    public boolean hasNext() {
                        return x < image.getWidth();
                    }

                    @Override
                    public ImagePixel next() {
                        ImagePixel pixel = new ImagePixel(image, x, y);
                        int ny = y + 1;
                        if (ny >= image.getHeight()) {
                            x++;
                            y = 0;
                        } else {
                            y = ny;
                        }

                        return pixel;
                    }
                };
            }

            @Override
            public int size() {
                return image.getWidth() * image.getHeight();
            }
        };
    }

    public static Stream<ImagePixel> stream (BufferedImage image) {
        return collection(image).stream();
    }

    public static Couple<Point, BufferedImage> fromCollection (Collection<ImagePixel> collection) {
        if (collection.size() == 0) {
            return new Couple<>(null, null);
        }

        int x0 = Integer.MAX_VALUE;
        int x1 = -1;
        int y0 = Integer.MAX_VALUE;
        int y1 = -1;

        for (ImagePixel pixel: collection) {
            Point point = pixel.point;

            if (point.x < x0) {
                x0 = point.x;
            } if (point.x > x1) {
                x1 = point.x;
            }

            if (point.y < y0) {
                y0 = point.y;
            } if (point.y > y1) {
                y1 = point.y;
            }
        }

        int width = x1 - x0 + 1;
        int height = y1 - y0 + 1;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (ImagePixel pixel: collection) {
            image.setRGB(pixel.point.x - x0, pixel.point.y - y0, pixel.getPixel());
        }

        return new Couple<>(new Point(x0, y0), image);
    }

    public static Couple<Point, BufferedImage> fromStream (Stream<ImagePixel> stream) {
        return fromCollection(stream.collect(Collectors.toList()));
    }
}
