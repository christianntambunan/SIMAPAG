// Atribusi AI: Kelas TransaksiService dibuat dengan bantuan AI untuk mengoordinasikan validasi batas pinjam dan pembaruan stok otomatis.
package com.mapagratwa.simapag.service;

import com.mapagratwa.simapag.dao.AlatDAO;
import com.mapagratwa.simapag.dao.TransaksiDAO;
import com.mapagratwa.simapag.exception.SimapagException;
import com.mapagratwa.simapag.exception.ValidasiInputException;
import com.mapagratwa.simapag.model.AlatOutdoor;
import com.mapagratwa.simapag.model.Personel;
import com.mapagratwa.simapag.model.TransaksiPeminjaman;

import java.time.LocalDate;

public class TransaksiService {
    private final AlatDAO alatDAO = new AlatDAO();
    private final TransaksiDAO transaksiDAO = new TransaksiDAO();

    /**
     * Memproses transaksi peminjaman alat, memvalidasi batas pinjam personel,
     * dan otomatis mengurangi stok alat pada basis data.
     */
    public void prosesPeminjaman(Personel peminjam, AlatOutdoor alat, int jumlah) throws SimapagException {
        if (jumlah <= 0) {
            throw new ValidasiInputException("Jumlah Pinjam (minimal 1)");
        }
        if (jumlah > peminjam.getBatasPinjamAlat()) {
            throw new ValidasiInputException("Jumlah melebihi batas pinjam maksimal (" + peminjam.getBatasPinjamAlat() + " unit)");
        }

        alat.kurangiStok(jumlah);
        alatDAO.update(alat);

        TransaksiPeminjaman trx = new TransaksiPeminjaman(0, peminjam, alat, jumlah, LocalDate.now(), "DIPINJAM");
        transaksiDAO.insert(trx);
    }

    /**
     * Menyelesaikan pengembalian alat dan menambahkan kembali stok ke basis data.
     */
    public void prosesPengembalian(TransaksiPeminjaman trx) throws SimapagException {
        if ("DIKEMBALIKAN".equalsIgnoreCase(trx.getStatus())) {
            throw new ValidasiInputException("Transaksi ini sudah berstatus DIKEMBALIKAN.");
        }
        trx.selesaikanPengembalian();
        alatDAO.update(trx.getAlat());
        transaksiDAO.updateStatus(trx.getIdTransaksi(), "DIKEMBALIKAN");
    }
}