package org.hoi.system.data;

import org.hoi.element.history.State;
import org.hoi.element.map.Province;
import org.hoi.various.collection.tuple.Triple;
import org.hoi.various.img.ImageUtils;
import org.hoi.various.collection.tuple.Couple;
import org.hoi.various.img.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class MapFetcher {
    final public static DefaultsMapFetcher DEFAULTS = new DefaultsMapFetcher();

    public abstract List<Province> getProvinces ();

    public Couple<Point, BufferedImage> getMap (Predicate<Province> condition, Function<Province, Color> colorFunction) {
        List<Province> provinces = getProvinces();

        Stream<Pixel.ColorPixel> stream = provinces.stream()
                .map(x -> {
                    Province.ProvinceImage image = x.getImage();
                    return new Triple<>(x, image.getPixels(), image.getPosition());
                })
                .filter(x -> x.getMid() != null)
                .flatMap(x -> {
                    Color color = colorFunction.apply(x.getFirst());
                    return ImageUtils.stream(x.getMid(), x.getLast()).map(z -> {
                        Color og = z.getColor();
                        return new Pixel.ColorPixel(og.getAlpha() == 0 ? og : color, z);
                    });
                });

        return ImageUtils.fromStream(stream);
    }

    public Couple<Point, BufferedImage> getMap (Function<Province, Color> color) {
        return getMap(x -> true, color);
    }

    public Couple<Point, BufferedImage> getMap (Predicate<Province> condition) {
        return getMap(condition, Province::getColor);
    }

    public Couple<Point, BufferedImage> getStateImage (State state, Color color) {
        List<Province> provinces = getProvinces();
        Stream<Pixel.ColorPixel> stream = state.getProvinces().stream()
                .map(x -> provinces.stream().filter(z -> z.getId() == x).findFirst().orElse(null))
                .filter(Objects::nonNull)
                .map(Province::getImage)
                .flatMap(x -> ImageUtils.stream(x.getPixels(), x.getPosition()))
                .map(x -> {
                    Color pixel = x.getColor();
                    return new Pixel.ColorPixel(pixel.getAlpha() == 0 ? pixel : color, x);
                });

        return ImageUtils.fromStream(stream);
    }

    // STATIC
    private static class DefaultsMapFetcher extends MapFetcher {
        private DefaultsMapFetcher () {}

        @Override
        public List<Province> getProvinces() {
            return Province.getDefaults();
        }
    }
}
