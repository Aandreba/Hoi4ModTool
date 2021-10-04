import org.hoi.element.common.Building;
import org.hoi.element.history.State;
import org.hoi.system.HoiLoader;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main (String... args) throws IOException, ParseException {
        HoiLoader.setDirectory(new File("/Users/Adebas/Library/Application Support/Steam/steamapps/common/Hearts of Iron IV"));
        HoiLoader.loadAll();

        var test = State.getDefaults();
        System.out.println();
    }
}
