// Atribusi AI: Kelas CsvExporterWorker dibuat dengan bantuan AI untuk memenuhi syarat multithreading SwingWorker pada ekspor laporan CSV.
package com.mapagratwa.simapag.util;

import com.mapagratwa.simapag.model.Exportable;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class CsvExporterWorker extends SwingWorker<Boolean, Void> {
    private final List<? extends Exportable> dataList;
    private final String header;
    private final String filePath;
    private final JFrame parentFrame;

    public CsvExporterWorker(List<? extends Exportable> dataList, String header, String filePath, JFrame parentFrame) {
        this.dataList = dataList;
        this.header = header;
        this.filePath = filePath;
        this.parentFrame = parentFrame;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(header);
            writer.newLine();
            for (Exportable item : dataList) {
                if (isCancelled()) {
                    return false;
                }
                writer.write(item.toCsvRow());
                writer.newLine();
            }
        }
        return true;
    }

    @Override
    protected void done() {
        try {
            if (get()) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Laporan berhasil diekspor ke:\n" + filePath,
                        "Ekspor Berhasil",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentFrame,
                    "Gagal mengekspor laporan: " + e.getMessage(),
                    "Error Ekspor",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}