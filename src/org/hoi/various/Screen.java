package org.hoi.various;

import java.awt.*;

public class Screen {
    private static Toolkit toolkit = Toolkit.getDefaultToolkit();

    public static Dimension getSize () {
        return toolkit.getScreenSize();
    }

    public static int getWidth () {
        return getSize().width;
    }

    public static int getHeight () {
        return getSize().height;
    }

    public static Dimension getRelative (float scale) {
        Dimension size = getSize();
        return new Dimension(Math.round(size.width * scale), Math.round(size.height * scale));
    }
}
