import org.hoi.element.history.State;
import org.hoi.system.data.ElementFetcher;
import org.hoi.system.data.HistoryFetcher;
import org.hoi.system.hoi.HoiLoader;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main (String... args) throws IOException {
        HoiLoader.readDirectory();

        State test = HistoryFetcher.DEFAULTS.getState(805);
        System.out.println();
    }
}
