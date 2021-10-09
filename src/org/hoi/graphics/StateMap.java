package org.hoi.graphics;

import org.hoi.element.history.Country;
import org.hoi.element.history.State;
import org.hoi.element.map.Province;
import org.hoi.graphics.various.ImagePanel;
import org.hoi.graphics.various.JExtendedList;
import org.hoi.system.data.ElementFetcher;
import org.hoi.various.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Function;

public class StateMap extends JFrame {
    final public ElementFetcher fetcher;

    private ImagePanel map;
    private JList<State> states;

    public StateMap (ElementFetcher fetcher, float size) throws HeadlessException {
        super("State Map");
        this.setLayout(new BorderLayout());
        this.setSize(Screen.getRelative(size));

        this.fetcher = fetcher;
        this.states = new JExtendedList<State>(this.fetcher.getHistory().getStates()) {
            @Override
            public String getTextFor (State value) {
                return fetcher.getLocalisation().get(value.getName());
            }
        };

        BufferedImage mapImage = this.fetcher.getMap().getMap((Function<Province, Color>)  x -> {
            State state = this.fetcher.getHistory().getState(x);
            if (state == null) {
                return new Color(0, true);
            }

            Country owner = this.fetcher.getHistory().getStateOwner(state);
            if (owner == null) {
                return new Color(0, true);
            }

            return owner.getColor();
        }).getLast();

        this.map = new ImagePanel(mapImage);
        this.add(this.map);
        this.add(new JScrollPane(this.states), BorderLayout.EAST);
    }
}
