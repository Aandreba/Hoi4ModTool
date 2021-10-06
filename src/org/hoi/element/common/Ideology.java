package org.hoi.element.common;

import org.hoi.system.HoiList;
import org.hoi.system.HoiLoader;
import org.hoi.system.HoiMap;
import org.hoi.various.Stringx;
import org.hoi.various.collection.readonly.ReadOnlyList;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;

public class Ideology extends HoiMap {
    private static ReadOnlyList<Ideology> DEFAULTS;
    protected String name;

    public Ideology (String name) {
        this.name = name;
    }

    public Ideology (HoiMap other, String name) {
        super(other);
        this.name = name;
    }

    public Ideology (File file, String name) throws IOException {
        super(file);
        this.name = name;
    }

    public Ideology (Reader reader, String name) throws IOException {
        super(reader);
        this.name = name;
    }

    public String getName () {
        return name;
    }

    public Color getColor () {
        HoiList list = this.getFirstAs("color");
        return new Color(list.getInteger(0), list.getInteger(1), list.getInteger(2));
    }

    public boolean canHostGovernmentInExile () {
        return this.getFirstBoolOrElse("can_host_government_in_exile", false);
    }

    @Override
    public String toString() {
        return Stringx.toUpperCaseFirst(getName());
    }

    // STATIC
    public static ReadOnlyList<Ideology> getDefaults () {
        return DEFAULTS;
    }

    public static void loadDefaults () throws IOException {
        File file = HoiLoader.getFile("common/ideologies/00_ideologies.txt");
        HoiMap map = new HoiMap(file);
        map = map.getFirstAs("ideologies");

        ArrayList<Ideology> ideologies = new ArrayList<>();
        for (Map.Entry<String, Object> entry: map) {
            ideologies.add(new Ideology((HoiMap) entry.getValue(), entry.getKey()));
        }

        DEFAULTS = new ReadOnlyList<Ideology>(ideologies);
        System.out.println();
    }
}
