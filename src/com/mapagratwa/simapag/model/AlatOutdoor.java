// Atribusi AI: Kelas domain AlatOutdoor dibuat dengan bantuan AI untuk mengelola data dan stok peralatan.
package com.mapagratwa.simapag.model;

import com.mapagratwa.simapag.exception.StokTidakCukupException;

public class AlatOutdoor implements Exportable {
    private String kodeAlat;
    private String namaAlat;
    private String kategori;
    private int stok;
    private String kondisi;

    public AlatOutdoor(String kodeAlat, String namaAlat, String kategori, int stok, String kondisi) {
        this.kodeAlat = kodeAlat;
        this.namaAlat = namaAlat;
        this.kategori = kategori;
        this.stok = stok;
        this.kondisi = kondisi;
    }

    public void kurangiStok(int jumlah) throws StokTidakCukupException {
        if (jumlah > this.stok) {
            throw new StokTidakCukupException(this.namaAlat, this.stok);
        }
        this.stok -= jumlah;
    }

    public void tambahStok(int jumlah) {
        this.stok += jumlah;
    }

    @Override
    public String toCsvRow() {
        return String.join(",", kodeAlat, namaAlat, kategori, String.valueOf(stok), kondisi);
    }

    public String getKodeAlat() { return kodeAlat; }
    public String getNamaAlat() { return namaAlat; }
    public String getKategori() { return kategori; }
    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
    public String getKondisi() { return kondisi; }
}