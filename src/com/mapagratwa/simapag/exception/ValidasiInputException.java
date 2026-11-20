package com.mapagratwa.simapag.exception;

public class ValidasiInputException extends SimapagException {
    public ValidasiInputException(String namaKolom) {
        super("Masukan pada kolom '" + namaKolom + "' tidak valid atau tidak boleh kosong.");
    }
}