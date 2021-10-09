package org.hoi.element.common;

import org.hoi.system.hoi.HoiLoader;
import org.hoi.system.hoi.HoiMap;
import org.hoi.various.collection.readonly.ReadOnlyList;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;

public class Building extends HoiMap {
    protected String name;

    public Building(HoiMap other, String name) {
        super(other);
        this.name = name;
    }

    public Building(File file, String name) throws IOException {
        super(file);
        this.name = name;
    }

    public Building(Reader reader, String name) throws IOException {
        super(reader);
        this.name = name;
    }

    public String getName () {
        return name;
    }

    public static class OfState extends Building {
        private static ReadOnlyList<OfState> DEFAULTS;

        public OfState(HoiMap other, String name) {
            super(other, name);
        }

        public OfState(File file, String name) throws IOException {
            super(file, name);
        }

        public OfState(Reader reader, String name) throws IOException {
            super(reader, name);
        }

        public boolean isShared () {
            return this.getFirstBoolOrElse("shares_slots", false);
        }

        // STATIC
        public static ReadOnlyList<OfState> getDefaults () {
            return DEFAULTS;
        }
    }

    public static class OfProvince extends Building {
        private static ReadOnlyList<OfProvince> DEFAULTS;

        public OfProvince(HoiMap other, String name) {
            super(other, name);
        }

        public OfProvince(File file, String name) throws IOException {
            super(file, name);
        }

        public OfProvince(Reader reader, String name) throws IOException {
            super(reader, name);
        }

        public boolean isCoastal () {
            return this.getFirstBoolOrElse("only_coastal", false);
        }

        // STATIC
        public static ReadOnlyList<OfProvince> getDefaults () {
            return DEFAULTS;
        }
    }

    // STATIC
    public static void loadDefaults () throws IOException {
        HoiMap map = new HoiMap(HoiLoader.getFile("common/buildings/00_buildings.txt"));
        map = map.getFirstAs("buildings");

        ArrayList<OfState> states = new ArrayList<>();
        ArrayList<OfProvince> provinces = new ArrayList<>();

        for (Map.Entry<String, Object> entry: map) {
            HoiMap data = (HoiMap) entry.getValue();
            boolean province = data.getFirstBoolOrElse("provincial", false);

            if (province) {
                provinces.add(new OfProvince(data, entry.getKey()));
            } else {
                states.add(new OfState(data, entry.getKey()));
            }
        }

        OfState.DEFAULTS = new ReadOnlyList<>(states);
        OfProvince.DEFAULTS = new ReadOnlyList<OfProvince>(provinces);
    }
}
