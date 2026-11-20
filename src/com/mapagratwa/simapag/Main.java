// Atribusi AI: Simulasi Main.java dibuat dengan bantuan AI untuk menguji polimorfisme, koleksi, dan eksepsi pada Checkpoint 1.
package com.mapagratwa.simapag;

import com.mapagratwa.simapag.exception.SimapagException;
import com.mapagratwa.simapag.model.*;

import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Map<String, Personel> daftarPersonel = new HashMap<>();
        daftarPersonel.put("P01", new PengurusInti("P01", "Christian Tambunan", "MPG-001", "Koordinator Logistik"));
        daftarPersonel.put("P02", new AnggotaBiasa("P02", "Rina Siregar", "MPG-015", "Angkatan XIV"));

        System.out.println("=== DATA PERSONEL & HAK PINJAM (POLIMORFISME) ===");
        for (Personel p : daftarPersonel.values()) {
            System.out.println(p.getNama() + " | Maks Pinjam: " + p.getBatasPinjamAlat()
                    + " alat | Denda 2 hari: Rp" + p.hitungDendaTerlambat(2));
        }

        List<AlatOutdoor> daftarAlat = new ArrayList<>();
        daftarAlat.add(new AlatOutdoor("A01", "Tenda Dome Kapasitas 4", "Shelter", 5, "Baik"));
        daftarAlat.add(new AlatOutdoor("A02", "Carrier 60L", "Tas", 2, "Baik"));
        daftarAlat.add(new AlatOutdoor("A03", "Kompor Portable", "Masak", 8, "Baik"));

        daftarAlat.sort(Comparator.comparingInt(AlatOutdoor::getStok));

        System.out.println("\n=== DAFTAR ALAT URUT STOK TERSEDIKIT ===");
        for (AlatOutdoor alat : daftarAlat) {
            System.out.println(alat.toCsvRow());
        }

        System.out.println("\n=== UJI TRANSAKSI & EKSEPSI ===");
        try {
            Personel peminjam = daftarPersonel.get("P02");
            AlatOutdoor carrier = daftarAlat.get(0);
            
            carrier.kurangiStok(5);
            TransaksiPeminjaman trx = new TransaksiPeminjaman(1, peminjam, carrier, 5, LocalDate.now(), "DIPINJAM");
            System.out.println("Transaksi berhasil: " + trx.toCsvRow());
        } catch (SimapagException e) {
            System.out.println("Peringatan Sistem: " + e.getMessage());
        }
    }
}