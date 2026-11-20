// Atribusi AI: Titik masuk aplikasi Main.java diperbarui dengan bantuan AI untuk meluncurkan antarmuka Swing di Event Dispatch Thread.
package com.mapagratwa.simapag;

import com.mapagratwa.simapag.view.MainFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}