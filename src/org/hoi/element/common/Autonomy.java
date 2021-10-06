package org.hoi.element.common;

import org.hoi.system.HoiLoader;
import org.hoi.system.HoiMap;
import org.hoi.various.Stringx;
import org.hoi.various.collection.readonly.ReadOnlyList;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class Autonomy extends HoiMap {
    private static ReadOnlyList<Autonomy> DEFAULTS;

    public Autonomy (HoiMap other) {
        super(other);
    }

    public Autonomy (File file) throws IOException {
        super(file);
    }

    public Autonomy (Reader reader) throws IOException {
        super(reader);
    }

    final public String getId () {
        return this.getFirstString("id");
    }

    final public boolean isDefault () {
        return this.getFirstBoolOrElse("default", false);
    }

    final public boolean isPuppet () {
        return this.getFirstBoolOrElse("is_puppet", false);
    }

    final public boolean useOverlordColor () {
        return this.getFirstBoolOrElse("use_overlord_color", false);
    }

    final public float getMinFreedomLevel () {
        return this.getFirstFloat("min_freedom_level");
    }

    final public float getManpowerInfluence () {
        return this.getFirstFloat("manpower_influence");
    }

    @Override
    public String toString() {
        return "Autonomy{" +
                "id=" + Stringx.toUpperCaseWords(getId().substring(9), "_", " ") + ", " +
                "default=" + isDefault() + ", " +
                "is_puppet=" + isPuppet() + ", " +
                "use_overlord_color=" + useOverlordColor() + ", " +
                "min_freedom_level=" + getMinFreedomLevel() + ", " +
                "manpower_influence=" + getManpowerInfluence() +
                "}";
    }

    // STATIC
    public static ReadOnlyList<Autonomy> getDefaults () {
        return DEFAULTS;
    }

    public static Autonomy valueOf (String id) {
        return DEFAULTS.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }

    public static void loadDefaults () throws IOException {
        File dir = HoiLoader.getFile("common/autonomous_states");
        File[] files = dir.listFiles();

        ArrayList<Autonomy> autonomies = new ArrayList<>();
        for (File file: files) {
            autonomies.add(new Autonomy(new HoiMap(file).getFirstAs(HoiMap.class, "autonomy_state")));
        }

        DEFAULTS = new ReadOnlyList<Autonomy>(autonomies);
    }
}
