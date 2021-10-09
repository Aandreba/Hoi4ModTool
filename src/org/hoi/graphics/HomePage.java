package org.hoi.graphics;

import org.hoi.graphics.various.Alert;
import org.hoi.system.data.ElementFetcher;
import org.hoi.system.hoi.HoiLoader;
import org.hoi.various.Screen;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {
    JLabel label;
    JButton folder, defaults;

    public HomePage (float size) {
        super("Hoi4ModTool");
        this.setLayout(new FlowLayout());
        this.setSize(Screen.getRelative(size));

        this.label = new JLabel("Hoi4ModTool");
        this.folder = new JButton("Select HOI 4 folder");
        this.defaults = new JButton("Open defaults");

        this.folder.addActionListener(e -> {
            JFileChooser dialog = new JFileChooser();
            dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int returnVal = dialog.showOpenDialog(HomePage.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    HoiLoader.setDirectory(dialog.getSelectedFile());
                    HoiLoader.loadAll();
                } catch (Exception exc) {
                    exc.printStackTrace();

                    Alert alert = new Alert("Error", exc.getMessage());
                    alert.setVisible(true);
                } finally {
                    Alert alert = new Alert("Success", "Directory changed correctly");
                    alert.setVisible(true);
                }
            }
        });

        this.defaults.addActionListener(e -> {
            StateMap map = new StateMap(ElementFetcher.DEFAULTS, size);
            map.setVisible(true);
        });

        this.add(label);
        this.add(folder);
        this.add(defaults);
    }

    public static void main (String... args) {
        HoiLoader.readDirectory();
        HomePage home = new HomePage(0.5f);
        home.setVisible(true);
    }
}
