package org.hoi.element.history;

import org.hoi.element.map.Province;
import org.hoi.system.hoi.HoiList;
import org.hoi.system.hoi.HoiLoader;
import org.hoi.system.hoi.HoiMap;
import org.hoi.system.hoi.HoiTag;
import org.hoi.various.ListUtils;
import org.hoi.various.collection.map.MappedList;
import org.hoi.various.collection.readonly.ReadOnlyList;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.List;

public class State extends HoiMap {
    private static ReadOnlyList<State> DEFAULTS;

    public State (File file) throws IOException {
        super(file);
    }

    public State (Reader reader) throws IOException {
        super(reader);
    }

    public State (HoiMap other) {
        super(other);
    }

    // PROPERTIES
    final public int getId () {
        return this.getFirstInteger("id");
    }

    final public String getName () {
        return this.getFirstString("name");
    }

    final public int getManpower () {
        return this.getFirstInteger("manpower");
    }

    final public String getCategory () {
        return this.getFirstString("state_category");
    }

    final public Category getCategory (Collection<Category> options) {
        String category = getCategory();
        return options.stream().filter(x -> x.getName().equals(category)).findFirst().orElse(null);
    }

    final public float getBuildingsMaxLevelFactor () {
        return this.getFirstFloatOrElse("buildings_max_level_factor", 1f);
    }

    final public boolean isImpassable () {
        return this.getFirstBoolOrElse("impassable", false);
    }

    final public List<Integer> getProvinces () {
        HoiList list = this.getFirstAs(HoiList.class, "provinces");
        if (list == null) {
            return ListUtils.empty();
        }
        return new MappedList<>(list) {
            @Override
            protected Integer map (Object input) {
                return ((Number) input).intValue();
            }
        };
    }

    final public List<Province> getProvinces (Collection<Province> options) {
        return new MappedList<>(this.get("provinces")) {
            @Override
            protected Province map (Object input) {
                int i = ((Number) input).intValue();
                return options.stream().filter(x -> x.getId() == i).findFirst().orElse(null);
            }
        };
    }

    final public History getHistory () {
        return new History();
    }

    @Override
    public String toString() {
        return Integer.toString(getId());
    }

    // STATIC
    public static ReadOnlyList<State> getDefaults () {
        return DEFAULTS;
    }

    public static void loadAllDefaults () throws IOException {
        loadDefaults();
        Category.loadDefaults();
    }

    public static void loadDefaults () throws IOException {
        File[] files = HoiLoader.getFile("/history/states").listFiles();
        ArrayList<State> states = new ArrayList<>();

        for (File file: files) {
            HoiMap map = new HoiMap(file).getFirstAs("state");
            states.add(new State(map));
        }

        DEFAULTS = new ReadOnlyList<State>(states);
    }

    public class History extends HoiMap {
        final public State parent;

        public History () {
            super(State.this.getFirstAs(HoiMap.class, "history"));
            this.parent = State.this;
        }

        final public String getOwner () {
            return this.getFirstString("owner");
        }

        final public Country getOwner (Collection<Country> options) {
            String owner = getOwner();
            return options.stream().filter(x -> x.getTag().equals(owner)).findFirst().orElse(null);
        }

        final public String getController () {
            try {
                return this.getFirstAs(HoiTag.class, "controller").toString();
            } catch (Exception ignore) {}

            return this.getFirstAs(HoiTag.class, "owner").toString();
        }

        final public Country getController (Collection<Country> options) {
            String owner = getController();
            return options.stream().filter(x -> x.getTag().equals(owner)).findFirst().orElse(null);
        }

        final public List<String> coreOf () {
            return this.getString("add_core_of");
        }

        final public List<Country> coreOf (Collection<Country> options) {
            return new MappedList<>(coreOf()) {
                @Override
                protected Country map (String input) {
                    return options.stream().filter(x -> x.getTag().equals(input)).findFirst().orElse(null);
                }
            };
        }

        final public List<String> claimedBy () {
            return this.getString("add_claim_by");
        }

        final public List<Country> claimedBy (Collection<Country> options) {
            return new MappedList<>(claimedBy()) {
                @Override
                protected Country map (String input) {
                    return options.stream().filter(x -> x.getTag().equals(input)).findFirst().orElse(null);
                }
            };
        }

        final public Map<Integer, Integer> getVictoryPoints () {
            List<Integer> list = new MappedList<>(this.getFirstAs(HoiList.class, "victory_points")) {
                @Override
                protected Integer map (Object input) {
                    return ((Number) input).intValue();
                }
            };

            Iterator<Integer> iter = list.iterator();
            HashMap<Integer, Integer> vp = new HashMap<>();

            while (iter.hasNext()) {
                vp.put(iter.next(), iter.next());
            }

            return vp;
        }

        @Override
        public String toString() {
            return "History {" +
                    "owner=" + getOwner() + ", " +
                    "controller=" + getController() + ", " +
                    "coreOf=" + coreOf() + ", " +
                    "claimedBy=" + claimedBy() + ", " +
                    "victoryPoints=" + getVictoryPoints() +
                    '}';
        }
    }

    public static class Category extends HoiMap {
        private static ReadOnlyList<Category> DEFAULTS;

        private String name;

        public Category (HoiMap other, String name) {
            super(other);
            this.name = name;
        }

        public Category (File file, String name) throws IOException {
            super(file);
            this.name = name;
        }

        public Category (Reader reader, String name) throws IOException {
            super(reader);
            this.name = name;
        }

        public String getName () {
            return name;
        }

        public int getBuildingSlots () {
            return this.getFirstInteger("local_building_slots");
        }

        public Color getColor () {
            return this.getFirstColor("color");
        }

        // STATIC
        public static ReadOnlyList<Category> getDefaults () {
            return DEFAULTS;
        }

        public static void loadDefaults () throws IOException {
            File dir = HoiLoader.getFile("common/state_category");
            File[] files = dir.listFiles();

            ArrayList<Category> categories = new ArrayList<>();
            for (File file: files) {
                HoiMap map = new HoiMap(file).getFirstAs("state_categories");

                for (Map.Entry<String, Object> entry: map) {
                    categories.add(new Category((HoiMap) entry.getValue(), entry.getKey()));
                }
            }

            DEFAULTS = new ReadOnlyList<Category>(categories);
        }
    }
}
