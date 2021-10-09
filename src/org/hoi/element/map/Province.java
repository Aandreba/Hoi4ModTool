package org.hoi.element.map;

import org.hoi.system.hoi.HoiLoader;
import org.hoi.various.img.ImageUtils;
import org.hoi.various.collection.tuple.Couple;
import org.hoi.various.collection.readonly.ReadOnlyList;
import org.hoi.various.img.Pixel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

public abstract class Province {
    private static ReadOnlyList<Province> DEFAULTS;

    public abstract int getId ();
    public abstract Color getColor ();
    public abstract Type getType ();
    public abstract boolean isCoastal ();
    public abstract Terrain getTerrain ();
    public abstract ProvinceImage getImage ();

    // STATIC
    public static ReadOnlyList<Province> getDefaults () {
        return DEFAULTS;
    }

    public static void loadDefaults () throws IOException {
        // IMAGE INFO
        BufferedImage image = ImageIO.read(HoiLoader.getFile("map/provinces.bmp"));
        HashMap<Color, ArrayList<Point>> subimages = new HashMap<>();

        for (Pixel.ImagePixel pixel: ImageUtils.collection(image)) {
            ArrayList<Point> list = subimages.computeIfAbsent(pixel.getColor(), x -> new ArrayList<>());
            list.add(pixel.getInitialPoint());
        }

        // PROVINCE INFO
        String contents = Files.readString(HoiLoader.getFile("map/definition.csv").toPath());
        String[] rows = contents.split("\n");

        ArrayList<Province> provinces = new ArrayList<>();
        for (String row: rows) {
            String[] cols = row.split(";");

            int id = Integer.parseInt(cols[0]);
            int r = Integer.parseInt(cols[1]);
            int g = Integer.parseInt(cols[2]);
            int b = Integer.parseInt(cols[3]);
            String type = cols[4].toUpperCase();
            boolean coastal = Boolean.parseBoolean(cols[5]);
            String terrain = cols[6].toUpperCase();

            Color color = new Color(r, g, b);
            ArrayList<Point> pixels = subimages.get(color);

            if (pixels == null) {
                provinces.add(new Static(id, color, type, coastal, terrain, new Couple<>(null, null)));
            } else {
                Stream<Pixel.ImagePixel> subimage = pixels.stream().map(x -> new Pixel.ImagePixel(image, x));
                provinces.add(new Static(id, color, type, coastal, terrain, ImageUtils.fromStream(subimage)));
            }
        }

        DEFAULTS = new ReadOnlyList<>(provinces);
    }

    private static class Static extends Province {
        final private int id;
        final private Color color;
        final private Type type;
        final private boolean coastal;
        final private Terrain terrain;
        final private StaticImage image;

        public Static (int id, Color color, String type, boolean coastal, String terrain, Couple<Point, BufferedImage> image) {
            this.id = id;
            this.color = color;
            this.type = Type.valueOf(type);
            this.coastal = coastal;
            this.terrain = Terrain.valueOf(terrain);
            this.image = new StaticImage(image.getFirst(), image.getLast());
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public Color getColor() {
            return color;
        }

        @Override
        public Type getType() {
            return type;
        }

        @Override
        public boolean isCoastal() {
            return coastal;
        }

        @Override
        public Terrain getTerrain() {
            return terrain;
        }

        @Override
        public StaticImage getImage() {
            return image;
        }

        public class StaticImage extends ProvinceImage {
            final private Point point;
            final private BufferedImage pixels;

            public StaticImage (Point point, BufferedImage pixels) {
                super();
                this.point = point;
                this.pixels = pixels;
            }

            @Override
            public Point getPosition() {
                return point;
            }

            @Override
            public BufferedImage getPixels() {
                return pixels;
            }
        }
    }

    public enum Type {
        LAND,
        SEA,
        LAKE
    }

    public enum Terrain {
        DESERT,
        FOREST,
        HILLS,
        JUNGLE,
        MARSH,
        MOUNTAIN,
        PLAINS,
        URBAN,
        LAKES,
        OCEAN,
        UNKNOWN
    }

    public abstract class ProvinceImage {
        final public Province parent;

        public ProvinceImage () {
            this.parent = Province.this;
        }

        public abstract Point getPosition ();
        public abstract BufferedImage getPixels ();
    }
}
