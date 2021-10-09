package org.hoi.graphics.various;

import org.hoi.various.Screen;

import javax.swing.*;
import java.awt.*;

public class Alert extends JFrame {
    public Alert (String title, String message) throws HeadlessException {
        super(title);
        this.setLayout(new GridLayout(2, 1));
        this.setSize(Screen.getRelative(0.1f));

        JLabel label = new JLabel(message);
        JButton ok = new JButton("OK");

        ok.addActionListener(e -> {
            Alert.this.dispose();
        });

        this.add(label);
        this.add(ok);
    }
}
