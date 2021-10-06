import org.hoi.element.common.Autonomy;
import org.hoi.element.common.Idea;
import org.hoi.element.history.Country;
import org.hoi.element.history.State;
import org.hoi.system.HoiLoader;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Main {
    public static void main (String... args) throws IOException, ParseException {
        HoiLoader.setDirectory(new File("/Users/Adebas/Library/Application Support/Steam/steamapps/common/Hearts of Iron IV"));
        HoiLoader.loadAll();

        var test = State.getDefaults();
        System.out.println();
    }
}
