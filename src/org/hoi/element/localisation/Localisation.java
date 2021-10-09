package org.hoi.element.localisation;

import org.hoi.system.hoi.HoiLoader;
import org.hoi.various.IteratorUtils;
import org.hoi.various.collection.readonly.ReadOnlyList;
import org.hoi.various.collection.readonly.ReadOnlySet;
import org.hoi.various.map.MapUtils;

import java.io.*;
import java.util.*;

public abstract class Localisation extends AbstractMap<String, String> {
    private static ReadOnlyList<Localisation> DEFAULTS;

    final public static Map<String, Locale> LOCALE_MAP = new HashMap<>(){{
        put("braz_por", new Locale("pt"));
        put("english", Locale.ENGLISH);
        put("french", Locale.FRENCH);
        put("german", Locale.GERMAN);
        put("polish", new Locale("pl"));
        put("russian", new Locale("ru"));
        put("spanish", new Locale("es"));
    }};

    public abstract Locale getLocale ();

    // STATIC
    public static ReadOnlyList<Localisation> getDefaults () {
        return DEFAULTS;
    }

    public static void loadDefaults () throws IOException {
        File dir = HoiLoader.getFile("localisation");
        File files[] = dir.listFiles();

        ArrayList<Localisation> localisations = new ArrayList<>();
        for (File file: files) {
            FileLocalisation loc = Localisation.ofFile(file);
            if (loc != null) {
                localisations.add(loc);
            }
        }

        DEFAULTS = new ReadOnlyList<>(localisations);
    }

    public static FileLocalisation ofFile (File file) throws IOException {
        String name = file.getName();
        String[] nameSplit = name.split("_l_");
        if (nameSplit.length < 2) {
            return null;
        }

        String lang = nameSplit[1];
        lang = lang.substring(0, lang.length() - 4);

        HashSet<Entry<String, String>> entries = new HashSet<>();
        Scanner scanner = new Scanner(new FileInputStream(file));
        scanner.nextLine();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split(":\\d*\\s", 2);

            if (split.length != 2) {
                continue;
            }

            String key = split[0];
            while (Character.isWhitespace(key.charAt(0))) {
                key = key.substring(1);
            }

            String value = split[1].split("#")[0];
            while (Character.isWhitespace(value.charAt(value.length() - 1))) {
                value = value.substring(0, value.length() - 1);
            }

            entries.add(MapUtils.entry(key, value.length() <= 2 ? "" : value.substring(1, value.length() - 1)));
        }

        return new FileLocalisation(lang, entries);
    }

    private static class FileLocalisation extends Localisation {
        final private String locale;
        final private ReadOnlySet<Entry<String, String>> set;

        private FileLocalisation (String locale, Set<Entry<String, String>> set) {
            this.locale = locale;
            this.set = new ReadOnlySet<>(set);
        }

        @Override
        public Locale getLocale() {
            return LOCALE_MAP.get(locale);
        }

        @Override
        public ReadOnlySet<Entry<String, String>> entrySet() {
            return this.set;
        }
    }
}
