package com.mapagratwa.simapag.exception;

public class DatabaseAksesException extends SimapagException {
    public DatabaseAksesException(String detail) {
        super("Terjadi gangguan saat mengakses basis data: " + detail);
    }
}