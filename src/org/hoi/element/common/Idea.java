package org.hoi.element.common;

import org.hoi.system.hoi.HoiList;
import org.hoi.system.hoi.HoiLoader;
import org.hoi.system.hoi.HoiMap;
import org.hoi.various.StringUtils;
import org.hoi.various.collection.readonly.ReadOnlyList;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class Idea extends HoiMap {
    private static ReadOnlyList<Idea> DEFAULTS;

    private String category, name;

    public Idea (HoiMap other, String category, String name) {
        super(other);
        this.category = category;
        this.name = name;
    }

    public Idea (File file, String category, String name) throws IOException {
        super(file);
        this.category = category;
        this.name = name;
    }

    public Idea (Reader reader, String category, String name) throws IOException {
        super(reader);
        this.category = category;
        this.name = name;
    }

    public String getName () {
        return name;
    }

    public String getCategory () {
        return category;
    }

    public Category getCategory (Collection<Category> options) {
        return options.stream().filter(x -> x.getName().equals(getCategory())).findFirst().orElse(null);
    }

    public int getCost () {
        return this.getFirstIntegerOrElse("cost", 0);
    }

    public int getRemovalCost () {
        return this.getFirstIntegerOrElse("removal_cost", 0);
    }

    @Override
    public String toString() {
        return StringUtils.toUpperCaseWords(getName(), "_", " ");
    }

    // STATIC
    public static ReadOnlyList<Idea> getDefaults () {
        return DEFAULTS;
    }

    public static Idea valueOf (String name) {
        return DEFAULTS.stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
    }

    public static void loadDefaults () throws IOException {
        File dir = HoiLoader.getFile("common/ideas");
        File[] files = dir.listFiles();

        ArrayList<Idea> ideas = new ArrayList<>();
        for (File file: files) {
            HoiMap map = new HoiMap(file).getFirstAs("ideas");

            for (Map.Entry<String, Object> entry: map) {
                String category = entry.getKey();
                if (entry.getValue() instanceof HoiList && ((HoiList) entry.getValue()).size() == 0) {
                    continue;
                }

                if (entry.getValue() instanceof HoiList) {
                    continue;
                }

                for (Map.Entry<String, Object> idea: (HoiMap) entry.getValue()) {
                    if (!(idea.getValue() instanceof HoiMap)) {
                        continue;
                    }

                    ideas.add(new Idea((HoiMap) idea.getValue(), category, idea.getKey()));
                }
            }
        }

        DEFAULTS = new ReadOnlyList<Idea>(ideas);
    }
    
    public static void loadAllDefaults () throws IOException {
        loadDefaults();
        Category.loadDefaults();
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

        // STATIC
        public static ReadOnlyList<Category> getDefaults () {
            return DEFAULTS;
        }

        public static Category valueOf (String name) {
            return DEFAULTS.stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
        }

        public static void loadDefaults () throws IOException {
            HoiMap map = new HoiMap(HoiLoader.getFile("common/idea_tags/00_idea.txt"));
            map = map.getFirstAs("idea_categories");

            ArrayList<Category> categories = new ArrayList<>();
            for (Map.Entry<String, Object> entry: map) {
                categories.add(new Category((HoiMap) entry.getValue(), entry.getKey()));
            }

            DEFAULTS = new ReadOnlyList<Category>(categories);
        }
    }
}
