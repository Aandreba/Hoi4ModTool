package org.hoi.various.img;

import org.hoi.various.CollectionUtils;
import org.hoi.various.collection.tuple.Couple;
import org.hoi.various.collection.map.MappedCollection;
import org.hoi.various.img.Pixel.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImageUtils {
    public static Collection<ImagePixel> collection (BufferedImage image, Point delta) {
        if (image == null) {
            return CollectionUtils.empty();
        }

        return new AbstractCollection<>() {
            @Override
            public Iterator<ImagePixel> iterator() {
                return new Iterator<>() {
                    int x, y;

                    @Override
                    public boolean hasNext() {
                        return x < image.getWidth();
                    }

                    @Override
                    public ImagePixel next() {
                        ImagePixel pixel = new ImagePixel(image, x, y, delta);
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

    public static Collection<ImagePixel> collection (BufferedImage image) {
        return collection(image, new Point(0, 0));
    }

    public static Stream<ImagePixel> stream (BufferedImage image, Point delta) {
        return collection(image, delta).stream();
    }

    public static Stream<ImagePixel> stream (BufferedImage image) {
        return stream(image, new Point(0, 0));
    }

    public static Couple<Point, BufferedImage> fromCollection (Collection<? extends Pixel> collection) {
        if (collection.size() == 0) {
            return new Couple<>(null, null);
        }

        int x0 = Integer.MAX_VALUE;
        int x1 = -1;
        int y0 = Integer.MAX_VALUE;
        int y1 = -1;

        for (Pixel pixel: collection) {
            Point point = pixel.getFinalPoint();

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
        for (Pixel pixel: collection) {
            Point point = pixel.getFinalPoint();
            int x = point.x - x0;
            int y = point.y - y0;

            Color from = new Color(image.getRGB(x, y), true);
            Color to = pixel.getColor();

            if (from.getAlpha() == 0 && to.getAlpha() > 0) {
                image.setRGB(x, y, to.getRGB());
            }
        }

        return new Couple<>(new Point(x0, y0), image);
    }

    public static Couple<Point, BufferedImage> fromStream (Stream<? extends Pixel> stream) {
        return fromCollection(stream.collect(Collectors.toList()));
    }

    public static BufferedImage replace (BufferedImage image, Function<Color, Color> function) {
        Collection<ColorPixel> collection = new MappedCollection<>(collection(image)) {
            @Override
            protected ColorPixel map (ImagePixel value) {
                return new ColorPixel(function.apply(value.getColor()), value);
            }
        };

        return fromCollection(collection).getLast();
    }

    public static BufferedImage transform (BufferedImage image, float scale, int dx, int dy) {
        if (scale == 1 && dx == 0 && dy == 0) {
            return image;
        }

        int w = (int) (image.getWidth() / scale);
        int h = (int) (image.getHeight() / scale);

        dx -= (image.getWidth() - w) / 2f;
        dy -= (image.getHeight() - h) / 2f;

        BufferedImage newImage = new BufferedImage(w, h, image.getType());
        Graphics2D graphics = newImage.createGraphics();

        graphics.drawImage(image, dx, dy, image.getWidth(), image.getHeight(), null);
        graphics.dispose();
        return newImage;
    }
}
