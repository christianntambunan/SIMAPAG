// Atribusi AI: Antarmuka Swing MainFrame dibuat dengan bantuan AI untuk mengintegrasikan seluruh operasi CRUD, transaksi, dan ekspor laporan.
package com.mapagratwa.simapag.view;

import com.mapagratwa.simapag.dao.*;
import com.mapagratwa.simapag.exception.SimapagException;
import com.mapagratwa.simapag.exception.ValidasiInputException;
import com.mapagratwa.simapag.model.*;
import com.mapagratwa.simapag.service.TransaksiService;
import com.mapagratwa.simapag.util.CsvExporterWorker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {
    private final AlatDAO alatDAO = new AlatDAO();
    private final PersonelDAO personelDAO = new PersonelDAO();
    private final TransaksiDAO transaksiDAO = new TransaksiDAO();
    private final TransaksiService transaksiService = new TransaksiService();

    // Komponen Tab Alat
    private JTextField txtKodeAlat, txtNamaAlat, txtKategori, txtStok, txtKondisi, txtCariAlat;
    private DefaultTableModel modelAlat;
    private JTable tblAlat;

    // Komponen Tab Personel
    private JTextField txtIdPersonel, txtNamaPersonel, txtNia, txtKeterangan;
    private JComboBox<String> cmbJenisPersonel;
    private DefaultTableModel modelPersonel;
    private JTable tblPersonel;

    // Komponen Tab Transaksi
    private JComboBox<Personel> cmbPeminjam;
    private JComboBox<AlatItemWrapper> cmbAlatPinjam;
    private JTextField txtJumlahPinjam;
    private DefaultTableModel modelTransaksi;
    private JTable tblTransaksi;

    public MainFrame() {
        setTitle("SIMAPAG - Sistem Informasi Manajemen Perlengkapan & Anggota Mapagratwa");
        setSize(950, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("1. Inventaris Alat Outdoor", createPanelAlat());
        tabbedPane.addTab("2. Data Anggota & Pengurus", createPanelPersonel());
        tabbedPane.addTab("3. Peminjaman & Pengembalian", createPanelTransaksi());

        add(tabbedPane);
        refreshAllData();
    }

    // ================= PANEL 1: INVENTARIS ALAT =================
    private JPanel createPanelAlat() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 4, 8, 8));
        txtKodeAlat = new JTextField();
        txtNamaAlat = new JTextField();
        txtKategori = new JTextField();
        txtStok = new JTextField();
        txtKondisi = new JTextField("Baik");

        form.add(new JLabel("Kode Alat:"));
        form.add(txtKodeAlat);
        form.add(new JLabel("Nama Alat:"));
        form.add(txtNamaAlat);
        form.add(new JLabel("Kategori:"));
        form.add(txtKategori);
        form.add(new JLabel("Stok:"));
        form.add(txtStok);
        form.add(new JLabel("Kondisi:"));
        form.add(txtKondisi);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTambah = new JButton("Tambah Alat");
        JButton btnUbah = new JButton("Ubah Alat");
        JButton btnHapus = new JButton("Hapus Alat");
        JButton btnUrutStok = new JButton("Urutkan Stok Sedikit");
        JButton btnEksporAlat = new JButton("Ekspor CSV Alat");

        txtCariAlat = new JTextField(12);
        JButton btnCari = new JButton("Cari");

        btnPanel.add(btnTambah);
        btnPanel.add(btnUbah);
        btnPanel.add(btnHapus);
        btnPanel.add(btnUrutStok);
        btnPanel.add(btnEksporAlat);
        btnPanel.add(new JLabel(" | Cari Nama:"));
        btnPanel.add(txtCariAlat);
        btnPanel.add(btnCari);

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(form, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        modelAlat = new DefaultTableModel(new String[]{"Kode", "Nama Alat", "Kategori", "Stok", "Kondisi"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblAlat = new JTable(modelAlat);

        tblAlat.getSelectionModel().addListSelectionListener(e -> {
            int row = tblAlat.getSelectedRow();
            if (row >= 0) {
                txtKodeAlat.setText(modelAlat.getValueAt(row, 0).toString());
                txtNamaAlat.setText(modelAlat.getValueAt(row, 1).toString());
                txtKategori.setText(modelAlat.getValueAt(row, 2).toString());
                txtStok.setText(modelAlat.getValueAt(row, 3).toString());
                txtKondisi.setText(modelAlat.getValueAt(row, 4).toString());
            }
        });

        btnTambah.addActionListener(e -> aksiTambahAlat());
        btnUbah.addActionListener(e -> aksiUbahAlat());
        btnHapus.addActionListener(e -> aksiHapusAlat());
        btnUrutStok.addActionListener(e -> loadTabelAlat(true, ""));
        btnCari.addActionListener(e -> loadTabelAlat(false, txtCariAlat.getText().trim()));
        btnEksporAlat.addActionListener(e -> aksiEksporAlat());

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblAlat), BorderLayout.CENTER);
        return panel;
    }

    // ================= PANEL 2: DATA PERSONEL =================
    private JPanel createPanelPersonel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 4, 8, 8));
        txtIdPersonel = new JTextField();
        txtNamaPersonel = new JTextField();
        txtNia = new JTextField();
        cmbJenisPersonel = new JComboBox<>(new String[]{"ANGGOTA", "PENGURUS"});
        txtKeterangan = new JTextField();

        form.add(new JLabel("ID Personel:"));
        form.add(txtIdPersonel);
        form.add(new JLabel("Nama Lengkap:"));
        form.add(txtNamaPersonel);
        form.add(new JLabel("NIA:"));
        form.add(txtNia);
        form.add(new JLabel("Status:"));
        form.add(cmbJenisPersonel);
        form.add(new JLabel("Angkatan / Jabatan:"));
        form.add(txtKeterangan);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTambah = new JButton("Simpan Personel");
        JButton btnHapus = new JButton("Hapus Personel");
        btnPanel.add(btnTambah);
        btnPanel.add(btnHapus);

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(form, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        modelPersonel = new DefaultTableModel(new String[]{"ID", "Nama", "NIA", "Status", "Keterangan", "Maks Pinjam", "Denda/Hari"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblPersonel = new JTable(modelPersonel);

        btnTambah.addActionListener(e -> aksiTambahPersonel());
        btnHapus.addActionListener(e -> aksiHapusPersonel());

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblPersonel), BorderLayout.CENTER);
        return panel;
    }

    // ================= PANEL 3: TRANSAKSI PEMINJAMAN =================
    private JPanel createPanelTransaksi() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(2, 4, 8, 8));
        cmbPeminjam = new JComboBox<>();
        cmbAlatPinjam = new JComboBox<>();
        txtJumlahPinjam = new JTextField("1");

        form.add(new JLabel("Pilih Peminjam:"));
        form.add(cmbPeminjam);
        form.add(new JLabel("Pilih Alat:"));
        form.add(cmbAlatPinjam);
        form.add(new JLabel("Jumlah Pinjam:"));
        form.add(txtJumlahPinjam);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnPinjam = new JButton("Proses Peminjaman");
        JButton btnKembali = new JButton("Tandai Dikembalikan");
        JButton btnEksporTrx = new JButton("Ekspor Laporan Transaksi (CSV)");

        btnPanel.add(btnPinjam);
        btnPanel.add(btnKembali);
        btnPanel.add(btnEksporTrx);

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(form, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        modelTransaksi = new DefaultTableModel(new String[]{"ID Trx", "Peminjam", "Alat", "Jumlah", "Tanggal Pinjam", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblTransaksi = new JTable(modelTransaksi);

        btnPinjam.addActionListener(e -> aksiProsesPinjam());
        btnKembali.addActionListener(e -> aksiProsesKembali());
        btnEksporTrx.addActionListener(e -> aksiEksporTransaksi());

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblTransaksi), BorderLayout.CENTER);
        return panel;
    }

    // ================= LOGIKA EVENT & VALIDASI =================
    private void aksiTambahAlat() {
        try {
            validasiTeks(txtKodeAlat.getText(), "Kode Alat");
            validasiTeks(txtNamaAlat.getText(), "Nama Alat");
            validasiTeks(txtKategori.getText(), "Kategori");
            int stok = Integer.parseInt(txtStok.getText().trim());
            if (stok < 0) throw new ValidasiInputException("Stok tidak boleh negatif");

            AlatOutdoor alat = new AlatOutdoor(
                    txtKodeAlat.getText().trim(), txtNamaAlat.getText().trim(),
                    txtKategori.getText().trim(), stok, txtKondisi.getText().trim()
            );
            alatDAO.insert(alat);
            refreshAllData();
            JOptionPane.showMessageDialog(this, "Alat berhasil ditambahkan.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Kolom Stok harus berupa angka.", "Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (SimapagException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aksiUbahAlat() {
        try {
            validasiTeks(txtKodeAlat.getText(), "Kode Alat");
            int stok = Integer.parseInt(txtStok.getText().trim());
            AlatOutdoor alat = new AlatOutdoor(
                    txtKodeAlat.getText().trim(), txtNamaAlat.getText().trim(),
                    txtKategori.getText().trim(), stok, txtKondisi.getText().trim()
            );
            alatDAO.update(alat);
            refreshAllData();
            JOptionPane.showMessageDialog(this, "Data alat berhasil diperbarui.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal mengubah alat: " + ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aksiHapusAlat() {
        int row = tblAlat.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris alat yang akan dihapus terlebih dahulu.");
            return;
        }
        String kode = modelAlat.getValueAt(row, 0).toString();
        try {
            alatDAO.delete(kode);
            refreshAllData();
            JOptionPane.showMessageDialog(this, "Alat berhasil dihapus.");
        } catch (SimapagException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aksiTambahPersonel() {
        try {
            validasiTeks(txtIdPersonel.getText(), "ID Personel");
            validasiTeks(txtNamaPersonel.getText(), "Nama Lengkap");
            validasiTeks(txtNia.getText(), "NIA");
            validasiTeks(txtKeterangan.getText(), "Angkatan / Jabatan");

            String jenis = cmbJenisPersonel.getSelectedItem().toString();
            Personel p = "PENGURUS".equals(jenis)
                    ? new PengurusInti(txtIdPersonel.getText().trim(), txtNamaPersonel.getText().trim(), txtNia.getText().trim(), txtKeterangan.getText().trim())
                    : new AnggotaBiasa(txtIdPersonel.getText().trim(), txtNamaPersonel.getText().trim(), txtNia.getText().trim(), txtKeterangan.getText().trim());

            personelDAO.insert(p);
            refreshAllData();
            JOptionPane.showMessageDialog(this, "Personel berhasil disimpan.");
        } catch (SimapagException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aksiHapusPersonel() {
        int row = tblPersonel.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris personel yang akan dihapus.");
            return;
        }
        String id = modelPersonel.getValueAt(row, 0).toString();
        try {
            personelDAO.delete(id);
            refreshAllData();
            JOptionPane.showMessageDialog(this, "Personel berhasil dihapus.");
        } catch (SimapagException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aksiProsesPinjam() {
        try {
            Personel peminjam = (Personel) cmbPeminjam.getSelectedItem();
            AlatItemWrapper wrapper = (AlatItemWrapper) cmbAlatPinjam.getSelectedItem();
            if (peminjam == null || wrapper == null) {
                throw new ValidasiInputException("Peminjam dan Alat");
            }
            int jumlah = Integer.parseInt(txtJumlahPinjam.getText().trim());
            transaksiService.prosesPeminjaman(peminjam, wrapper.alat, jumlah);
            refreshAllData();
            JOptionPane.showMessageDialog(this, "Peminjaman berhasil dicatat dan stok otomatis berkurang.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah pinjam harus berupa angka.", "Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (SimapagException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal Peminjaman", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aksiProsesKembali() {
        int row = tblTransaksi.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris transaksi yang akan dikembalikan.");
            return;
        }
        int idTrx = Integer.parseInt(modelTransaksi.getValueAt(row, 0).toString());
        try {
            List<TransaksiPeminjaman> semuaTrx = transaksiDAO.getAll();
            for (TransaksiPeminjaman trx : semuaTrx) {
                if (trx.getIdTransaksi() == idTrx) {
                    transaksiService.prosesPengembalian(trx);
                    refreshAllData();
                    JOptionPane.showMessageDialog(this, "Alat telah dikembalikan dan stok bertambah.");
                    return;
                }
            }
        } catch (SimapagException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aksiEksporAlat() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("laporan_inventaris_mapagratwa.csv"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                List<AlatOutdoor> list = alatDAO.getAll();
                new CsvExporterWorker(list, "Kode Alat,Nama Alat,Kategori,Stok,Kondisi",
                        chooser.getSelectedFile().getAbsolutePath(), this).execute();
            } catch (SimapagException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void aksiEksporTransaksi() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("laporan_transaksi_mapagratwa.csv"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                List<TransaksiPeminjaman> list = transaksiDAO.getAll();
                new CsvExporterWorker(list, "ID Transaksi,Peminjam,Alat,Jumlah,Tanggal Pinjam,Status",
                        chooser.getSelectedFile().getAbsolutePath(), this).execute();
            } catch (SimapagException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ================= PEMUATAN DATA TABEL =================
    private void refreshAllData() {
        loadTabelAlat(false, "");
        loadTabelPersonel();
        loadTabelTransaksi();
    }

    private void loadTabelAlat(boolean urutStok, String keyword) {
        try {
            List<AlatOutdoor> list = alatDAO.getAll();
            if (!keyword.isEmpty()) {
                list = list.stream()
                        .filter(a -> a.getNamaAlat().toLowerCase().contains(keyword.toLowerCase()))
                        .collect(Collectors.toList());
            }
            if (urutStok) {
                list.sort(Comparator.comparingInt(AlatOutdoor::getStok));
            }
            modelAlat.setRowCount(0);
            cmbAlatPinjam.removeAllItems();
            for (AlatOutdoor a : list) {
                modelAlat.addRow(new Object[]{a.getKodeAlat(), a.getNamaAlat(), a.getKategori(), a.getStok(), a.getKondisi()});
                cmbAlatPinjam.addItem(new AlatItemWrapper(a));
            }
        } catch (SimapagException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTabelPersonel() {
        try {
            List<Personel> list = personelDAO.getAll();
            modelPersonel.setRowCount(0);
            cmbPeminjam.removeAllItems();
            // Pemrosesan polimorfik tanpa instanceof
            for (Personel p : list) {
                String status = (p.getBatasPinjamAlat() > 3) ? "Pengurus Inti" : "Anggota Biasa";
                String ket = (p instanceof PengurusInti pi) ? pi.getJabatan() : ((AnggotaBiasa) p).getAngkatan();
                modelPersonel.addRow(new Object[]{
                        p.getIdPersonel(), p.getNama(), p.getNia(), status, ket,
                        p.getBatasPinjamAlat() + " unit", "Rp " + p.hitungDendaTerlambat(1)
                });
                cmbPeminjam.addItem(p);
            }
        } catch (SimapagException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTabelTransaksi() {
        try {
            List<TransaksiPeminjaman> list = transaksiDAO.getAll();
            modelTransaksi.setRowCount(0);
            for (TransaksiPeminjaman t : list) {
                modelTransaksi.addRow(new Object[]{
                        t.getIdTransaksi(), t.getPeminjam().getNama(), t.getAlat().getNamaAlat(),
                        t.getJumlah(), t.getTanggalPinjam(), t.getStatus()
                });
            }
        } catch (SimapagException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void validasiTeks(String nilai, String namaKolom) throws ValidasiInputException {
        if (nilai == null || nilai.trim().isEmpty()) {
            throw new ValidasiInputException(namaKolom);
        }
    }

    // Helper untuk menampilkan nama & stok alat pada JComboBox
    private static class AlatItemWrapper {
        final AlatOutdoor alat;
        AlatItemWrapper(AlatOutdoor alat) { this.alat = alat; }
        @Override
        public String toString() { return alat.getNamaAlat() + " (Stok: " + alat.getStok() + ")"; }
    }
}