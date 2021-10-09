package org.hoi.system.hoi;

import org.hoi.element.common.Autonomy;
import org.hoi.element.common.Building;
import org.hoi.element.common.Idea;
import org.hoi.element.common.Ideology;
import org.hoi.element.common.focus.FocusTree;
import org.hoi.element.history.Country;
import org.hoi.element.history.State;
import org.hoi.element.localisation.Localisation;
import org.hoi.element.map.Province;
import org.hoi.system.effects.types.EffectsLoader;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;

public class HoiLoader {
    final private static File FILE = new File("config/dir.txt");
    private static File DIRECTORY;

    static {
        readDirectory();
    }

    public static void readDirectory () {
        if (FILE.exists()) {
            try {
                setDirectory(new File(Files.readString(FILE.toPath())));
                loadAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            FILE.getParentFile().mkdirs();
        }
    }

    public static File getDirectory () {
        return DIRECTORY;
    }

    public static void setDirectory (File directory) throws IOException {
        if (!directory.exists()) {
            throw new FileNotFoundException();
        } else if (!directory.isDirectory()) {
            throw new FileSystemException("");
        }

        HoiLoader.DIRECTORY = directory;
        Files.writeString(FILE.toPath(), directory.getCanonicalPath());
    }

    public static File getFile (String path) {
        return new File(DIRECTORY, path);
    }

    // LOADERS
    public static void loadAll () throws IOException, ParseException {
        loadCommon();
        loadMap();
        loadHistory();

        EffectsLoader.loadDefaults();
        Localisation.loadDefaults();
    }

    public static void loadCommon () throws IOException {
        Autonomy.loadDefaults();
        Building.loadDefaults();
        FocusTree.loadDefaults();
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
