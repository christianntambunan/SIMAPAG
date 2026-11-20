// Atribusi AI: Kelas domain TransaksiPeminjaman dibuat dengan bantuan AI untuk relasi antar entitas.
package com.mapagratwa.simapag.model;

import java.time.LocalDate;

public class TransaksiPeminjaman implements Exportable {
    private int idTransaksi;
    private Personel peminjam;
    private AlatOutdoor alat;
    private int jumlah;
    private LocalDate tanggalPinjam;
    private String status;

    public TransaksiPeminjaman(int idTransaksi, Personel peminjam, AlatOutdoor alat, int jumlah, LocalDate tanggalPinjam, String status) {
        this.idTransaksi = idTransaksi;
        this.peminjam = peminjam;
        this.alat = alat;
        this.jumlah = jumlah;
        this.tanggalPinjam = tanggalPinjam;
        this.status = status;
    }

    public void selesaikanPengembalian() {
        if ("DIPINJAM".equalsIgnoreCase(this.status)) {
            this.status = "DIKEMBALIKAN";
            this.alat.tambahStok(this.jumlah);
        }
    }

    @Override
    public String toCsvRow() {
        return String.join(",",
                String.valueOf(idTransaksi),
                peminjam.getNama(),
                alat.getNamaAlat(),
                String.valueOf(jumlah),
                tanggalPinjam.toString(),
                status);
    }

    public int getIdTransaksi() { return idTransaksi; }
    public Personel getPeminjam() { return peminjam; }
    public AlatOutdoor getAlat() { return alat; }
    public int getJumlah() { return jumlah; }
    public LocalDate getTanggalPinjam() { return tanggalPinjam; }
    public String getStatus() { return status; }
}