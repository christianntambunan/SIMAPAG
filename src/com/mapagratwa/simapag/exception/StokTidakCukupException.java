package com.mapagratwa.simapag.exception;

public class StokTidakCukupException extends SimapagException {
    public StokTidakCukupException(String namaAlat, int sisaStok) {
        super("Stok alat '" + namaAlat + "' tidak mencukupi. Sisa tersedia: " + sisaStok + " unit.");
    }
}