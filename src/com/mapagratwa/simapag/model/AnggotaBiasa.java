// Atribusi AI: Kelas turunan AnggotaBiasa dibuat dengan bantuan AI untuk implementasi method polimorfik.
package com.mapagratwa.simapag.model;

public class AnggotaBiasa extends Personel {
    private String angkatan;

    public AnggotaBiasa(String idPersonel, String nama, String nia, String angkatan) {
        super(idPersonel, nama, nia);
        this.angkatan = angkatan;
    }

    @Override
    public int getBatasPinjamAlat() {
        return 3;
    }

    @Override
    public double hitungDendaTerlambat(int hariTerlambat) {
        return hariTerlambat * 5000.0;
    }

    public String getAngkatan() { return angkatan; }
}