package org.hoi.element.history;

import org.hoi.system.HoiLoader;
import org.hoi.system.HoiMap;
import org.hoi.various.collection.readonly.ReadOnlyList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class Country extends HoiMap {
    private static ReadOnlyList<Country> DEFAULTS;
    private String tag;

    public Country (String tag) {
        this.tag = tag;
    }

    public Country (File file, String tag) throws IOException {
        super(file);
        this.tag = tag;
    }

    public Country (Reader reader, String tag) throws IOException {
        super(reader);
        this.tag = tag;
    }

    // GETTERS
    public String getTag () {
        return tag;
    }

    final public int getCapital () {
        return this.getFirstInteger("capital");
    }

    final public String getOob () {
        return this.getFirst("oob").toString();
    }

    final public int getResearchSlots () {
        return this.getFirstInteger("set_research_slots");
    }

    final public float getStability () {
        return this.getFirstFloat("set_stability");
    }

    final public float getWarSupport () {
        return this.getFirstFloat("set_war_support");
    }

    /*
    public List<Idee> getIdeas () {
        return this.getFirstAs(HoiList.class, "add_ideas");
    }

    public List<Technology> getTechnologies () {
        return this.getAs("set_technology");
    }
    */

    final public int getPoliticalPower () {
        return this.getFirstIntegerOrElse("add_political_power", 0);
    }

    final public int getConvoys () {
        return this.getFirstIntegerOrElse("set_convoys", 0);
    }

    @Override
    public String toString() {
        return tag;
    }

    // STATIC
    public static ReadOnlyList<Country> getDefaults () {
        return DEFAULTS;
    }

    public static void loadDefaults () throws IOException {
        File[] files = HoiLoader.getFile("history/countries").listFiles();
        File countryTag = HoiLoader.getFile("common/country_tags/00_countries.txt");

        if (!countryTag.exists()) {
            throw new FileNotFoundException();
        } else if (!countryTag.isFile()) {
            throw new FileSystemException("File provided isn't a file");
        }

        String contents = Files.readString(countryTag.toPath());
        String[] rows = contents.split("\\n");

        ArrayList<Country> list = new ArrayList<>();
        for (String row : rows) {
            String tag = row.substring(0, 3);
            File historyFile = Arrays.stream(files).filter(x -> x.getName().startsWith(tag)).findFirst().get();

            list.add(new Country(historyFile, tag));
        }

        DEFAULTS = new ReadOnlyList<>(list);
    }
}
