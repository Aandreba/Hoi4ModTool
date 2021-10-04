package org.hoi.element.common;

import org.hoi.system.HoiLoader;
import org.hoi.system.HoiMap;
import org.hoi.various.collection.MappedList;
import org.hoi.various.collection.concat.ConcatList;
import org.hoi.various.collection.readonly.ReadOnlyList;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Building extends HoiMap {
    public Building (File file) throws IOException {
        super (file);
    }

    public Building (Reader reader) throws IOException {
        super(reader);
    }

    public Building (HoiMap map) {
        super(map);
    }

    public abstract String getName ();

    public abstract static class OfState extends Building {
        private static ReadOnlyList<OfState> DEFAULTS;

        public OfState (File file) throws IOException {
            super(file);
        }

        public OfState (Reader reader) throws IOException {
            super(reader);
        }

        public OfState (HoiMap map) {
            super(map);
        }

        public boolean isShared () {
            return this.getFirstBoolOrElse("shares_slots", false);
        }

        // STATIC
        public static ReadOnlyList<OfState> getDefaults () {
            return DEFAULTS;
        }
    }

    public abstract static class OfProvince extends Building {
        private static ReadOnlyList<OfProvince> DEFAULTS;

        public OfProvince (File file) throws IOException {
            super(file);
        }

        public OfProvince (Reader reader) throws IOException {
            super(reader);
        }

        public OfProvince (HoiMap map) {
            super(map);
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
                provinces.add(new OfProvince(data) {
                    @Override
                    public String getName() {
                        return entry.getKey();
                    }
                });
            } else {
                states.add(new OfState(data) {
                    @Override
                    public String getName() {
                        return entry.getKey();
                    }
                });
            }
        }

        OfState.DEFAULTS = new ReadOnlyList<>(states);
        OfProvince.DEFAULTS = new ReadOnlyList<OfProvince>(provinces);
    }
}
