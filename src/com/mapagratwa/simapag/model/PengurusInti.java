// Atribusi AI: Kelas turunan PengurusInti dibuat dengan bantuan AI untuk implementasi method polimorfik.
package com.mapagratwa.simapag.model;

public class PengurusInti extends Personel {
    private String jabatan;

    public PengurusInti(String idPersonel, String nama, String nia, String jabatan) {
        super(idPersonel, nama, nia);
        this.jabatan = jabatan;
    }

    @Override
    public int getBatasPinjamAlat() {
        return 10;
    }

    @Override
    public double hitungDendaTerlambat(int hariTerlambat) {
        return hariTerlambat * 2000.0;
    }

    public String getJabatan() { return jabatan; }
}