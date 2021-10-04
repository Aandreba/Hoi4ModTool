package org.hoi.element.history;

import org.hoi.element.map.Province;
import org.hoi.system.HoiList;
import org.hoi.system.HoiLoader;
import org.hoi.system.HoiMap;
import org.hoi.system.HoiTag;
import org.hoi.various.collection.MappedList;
import org.hoi.various.collection.readonly.ReadOnlyList;
import org.hoi.various.collection.readonly.ReadOnlyMap;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class State extends HoiMap {
    private static ReadOnlyList<State> DEFAULTS;

    public State (File file) throws IOException {
        super(file);
    }

    public State (Reader reader) throws IOException {
        super(reader);
    }

    // PROPERTIES
    private HoiMap getMap () {
        return this.getFirstAs("state");
    }

    public int getId () {
        return getMap().getFirstInteger("id");
    }

    public int getManpower () {
        return getMap().getFirstInteger("manpower");
    }

    public float getBuildingsMaxLevelFactor () {
        return getMap().getFirstFloatOrElse("buildings_max_level_factor", 1f);
    }

    public boolean isImpassable () {
        return getMap().getFirstBoolOrElse("impassable", false);
    }

    public List<Integer> getProvinces () {
        return getMap().getAs("provinces");
    }

    public List<Province> getProvinces (Map<Integer, Province> provinces) {
        return new MappedList<>(getMap().get("provinces")) {
            @Override
            protected Province map (Object input) {
                return provinces.get(input);
            }
        };
    }

    public History getHistory () {
        return new History();
    }

    @Override
    public String toString() {
        return "State{" +
                "id=" + getId() + ", " +
                "manpower=" + getManpower() + ", " +
                "buildings_max_level_factor=" + getBuildingsMaxLevelFactor() + ", " +
                "impassable=" + isImpassable() + ", " +
                "provinces=" + getProvinces() + ", " +
                "history=" + getHistory() +
                "}";
    }

    // STATIC
    public static ReadOnlyList<State> getDefaults () {
        return DEFAULTS;
    }

    public static void loadDefaults () throws IOException {
        File[] files = HoiLoader.getFile("/history/states").listFiles();
        ArrayList<State> states = new ArrayList<>();

        for (File file: files) {
            states.add(new State(file));
        }

        DEFAULTS = new ReadOnlyList<State>(states);
    }

    public class History {
        final public State parent;

        public History () {
            this.parent = State.this;
        }

        private HoiMap getMap () {
            return State.this.getMap().getFirstAs("history");
        }

        public String getOwner () {
            return getMap().getFirstAs(HoiTag.class, "owner").toString();
        }

        public String getController () {
            HoiMap map = getMap();

            try {
                return map.getFirstAs(HoiTag.class, "controller").toString();
            } catch (Exception ignore) {}

            return map.getFirstAs(HoiTag.class, "owner").toString();
        }

        public List<String> coreOf () {
            return getMap().getString("add_core_off");
        }

        public List<String> claimedBy () {
            return getMap().getString("add_core_off");
        }

        public Map<Integer, Integer> getVictoryPoints () {
            List<Integer> list = new MappedList<>(getMap().getFirstAs(HoiList.class, "victory_points")) {
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
}
