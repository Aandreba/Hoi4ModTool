package org.hoi.element.history;

import org.hoi.element.common.Idea;
import org.hoi.element.common.Ideology;
import org.hoi.various.img.TGA;
import org.hoi.system.hoi.HoiList;
import org.hoi.system.hoi.HoiLoader;
import org.hoi.system.hoi.HoiMap;
import org.hoi.various.collection.map.MappedList;
import org.hoi.various.collection.concat.ConcatList;
import org.hoi.various.collection.readonly.ReadOnlyList;
import org.hoi.various.collection.readonly.ReadOnlyMap;
import org.hoi.various.map.MappedMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

public class Country extends HoiMap {
    private static ReadOnlyList<Country> DEFAULTS;

    private String tag;
    private Color color;
    private String culture;
    private Flag flag;

    public Country (File file, String tag, Color color, String culture, File dir) throws IOException {
        super(file);
        this.tag = tag;
        this.color = color;
        this.culture = culture;
        this.flag = new Flag(dir);
    }

    // GETTERS
    public String getTag () {
        return tag;
    }

    public Color getColor () {
        return color;
    }

    public String getCulture () {
        return culture;
    }

    public Flag getFlag () {
        return flag;
    }

    final public int getCapital () {
        return this.getFirstInteger("capital");
    }

    final public State getCapital (Collection<State> options) {
        int capital = getCapital();
        return options.stream().filter(x -> x.getId() == capital).findFirst().orElse(null);
    }

    final public String getOob () {
        return this.getFirst("oob").toString();
    }

    final public int getResearchSlots () {
        return this.getFirstInteger("set_research_slots");
    }

    final public float getStability () {
        return this.getFirstFloatOrElse("set_stability", 0.5f);
    }

    final public float getWarSupport () {
        return this.getFirstFloatOrElse("set_war_support", 0.5f);
    }

    final public List<String> getIdeas () {
        List<Object> ideas = this.get("add_ideas");
        if (ideas.size() == 0) {
            return new ArrayList<>();
        }

        List<HoiList> mappedIdeas = new MappedList<>(ideas) {
            @Override
            protected HoiList map (Object input) {
                if (input instanceof HoiList) {
                    return (HoiList) input;
                }

                HoiList list = new HoiList();
                list.add(input);
                return list;
            }
        };

        List<Object> list = mappedIdeas.size() == 1 ? mappedIdeas.get(0) : ConcatList.of(mappedIdeas);
        return new MappedList<>(list) {
            @Override
            protected String map (Object input) {
                return input.toString();
            }
        };
    }

    final public List<Idea> getIdeas (Collection<Idea> options) {
        return new MappedList<>(getIdeas()) {
            @Override
            protected Idea map (String input) {
                return options.stream().filter(x -> x.getName().equals(input)).findFirst().orElse(null);
            }
        };
    }

    /*
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

    final public Map<String, Integer> getPopularity () {
        return new MappedMap.OfValue<>(this.getFirstAs(HoiMap.class, "set_popularities").toMap()) {
            @Override
            protected Integer mapValue (Object value) {
                return ((Number) value).intValue();
            }
        };
    }

    final public Map<Ideology, Integer> getPopularity (Collection<Ideology> options) {
        return new MappedMap<>(this.getFirstAs(HoiMap.class, "set_popularities").toMap()) {
            @Override
            protected Ideology mapKey (String key) {
                return options.stream().filter(x -> x.getName().equals(key)).findFirst().orElse(null);
            }

            @Override
            protected Integer mapValue (Object value) {
                return ((Number) value).intValue();
            }
        };
    }

    final public String getRulingParty () {
        return this.getFirstAs(HoiMap.class, "set_politics").getFirstString("ruling_party");
    }

    final public Ideology getRulingParty (Collection<Ideology> options) {
        String name = getRulingParty();
        return options.stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
    }

    final public List<String> isGuaranteeing () {
        return this.getString("give_guarantee");
    }

    final public List<Country> isGuaranteeing (Collection<Country> options) {
        return new MappedList<>(isGuaranteeing()) {
            @Override
            protected Country map (String input) {
                return options.stream().filter(x -> x.getTag().equals(input)).findFirst().orElse(null);
            }
        };
    }

    final public BufferedImage getCurrentFlag () {
        BufferedImage flag = this.flag.getOfIdeology(getRulingParty());
        return flag == null ? this.flag.getDefault() : flag;
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
        File common = HoiLoader.getFile("common");
        File gfx = HoiLoader.getFile("gfx/flags");

        File[] files = HoiLoader.getFile("history/countries").listFiles();
        File countryTag = new File(common, "country_tags/00_countries.txt");

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
            String commonPath = row.split("\\s*\\=\\s*")[1].split("#")[0];
            while (commonPath.charAt(commonPath.length() - 1) <= 32) {
                commonPath = commonPath.substring(0, commonPath.length() - 1);
            }

            commonPath = commonPath.substring(1, commonPath.length() - 1);
            File historyFile = Arrays.stream(files).filter(x -> x.getName().startsWith(tag)).findFirst().get();
            HoiMap commonContents = new HoiMap(new File(common, commonPath));

            String culture = commonContents.getFirstString("graphical_culture");
            culture = culture.substring(0, culture.length() - 4);

            list.add(new Country(historyFile, tag, commonContents.getFirstColor("color"), culture, gfx));
        }

        DEFAULTS = new ReadOnlyList<>(list);
    }

    public class Flag {
        final public Country parent;

        private BufferedImage defaultFlag;
        private Map<String, BufferedImage> byIdeology;

        public Flag (File dir) {
            this.parent = Country.this;

            try {
                defaultFlag = TGA.read(new File(dir, getTag() + ".tga").toPath());
            } catch (Exception e) {
                defaultFlag = null;
            }

            String match = getTag()+"_";
            File[] files = dir.listFiles(x -> x.getName().startsWith(match));

            HashMap<String, BufferedImage> byIdeology = new HashMap<>();
            for (File file: files) {
                String name = file.getName();

                String ideology = name.substring(match.length(), name.length() - 4);
                try {
                    BufferedImage image = TGA.read(file.toPath());
                    byIdeology.put(ideology, image);
                } catch (Exception ignore) {}
            }

            this.byIdeology = new ReadOnlyMap<>(byIdeology);
        }

        public BufferedImage getDefault () {
            return defaultFlag;
        }

        public BufferedImage getOfIdeology (String ideology) {
            return byIdeology.get(ideology);
        }

        public BufferedImage getOfIdeology (Ideology ideology) {
            return getOfIdeology(ideology.getName());
        }
    }
}
