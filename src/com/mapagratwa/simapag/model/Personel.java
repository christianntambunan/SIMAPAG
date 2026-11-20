// Atribusi AI: Kerangka abstract class Personel dibuat dengan bantuan AI sebagai kelas induk polimorfisme.
package com.mapagratwa.simapag.model;

public abstract class Personel {
    protected String idPersonel;
    protected String nama;
    protected String nia;

    public Personel(String idPersonel, String nama, String nia) {
        this.idPersonel = idPersonel;
        this.nama = nama;
        this.nia = nia;
    }

    public abstract int getBatasPinjamAlat();
    public abstract double hitungDendaTerlambat(int hariTerlambat);

    public String getIdPersonel() { return idPersonel; }
    public String getNama() { return nama; }
    public String getNia() { return nia; }

    @Override
    public String toString() {
        return nama + " (" + nia + ")";
    }
}