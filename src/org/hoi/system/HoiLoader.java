package org.hoi.system;

import org.hoi.element.common.Autonomy;
import org.hoi.element.common.Building;
import org.hoi.element.common.Idea;
import org.hoi.element.common.Ideology;
import org.hoi.element.history.Country;
import org.hoi.element.history.State;
import org.hoi.element.map.Province;
import org.hoi.system.effects.types.EffectsLoader;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

public class HoiLoader {
    private static File DIRECTORY;

    public static File getDirectory () {
        return DIRECTORY;
    }

    public static void setDirectory (File directory) {
        HoiLoader.DIRECTORY = directory;
    }

    public static File getFile (String path) {
        return new File(DIRECTORY, path);
    }

    // LOADERS
    public static void loadAll () throws IOException, ParseException {
        loadEffects();
        loadCommon();
        loadMap();
        loadHistory();
    }

    public static void loadEffects () throws IOException, ParseException {
        EffectsLoader.loadDefaults();
    }

    public static void loadCommon () throws IOException {
        Autonomy.loadDefaults();
        Building.loadDefaults();
        Ideology.loadDefaults();
        Idea.loadAllDefaults();
    }

    public static void loadMap () throws IOException {
        Province.loadDefaults();
    }

    public static void loadHistory () throws IOException {
        Country.loadDefaults();
        State.loadAllDefaults();
    }
}
