// Atribusi AI: Kelas TransaksiDAO dibuat dengan bantuan AI untuk mencatat dan memuat riwayat peminjaman dari MySQL.
package com.mapagratwa.simapag.dao;

import com.mapagratwa.simapag.exception.DatabaseAksesException;
import com.mapagratwa.simapag.model.*;
import com.mapagratwa.simapag.pattern.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaksiDAO {

    public void insert(TransaksiPeminjaman trx) throws DatabaseAksesException {
        String sql = "INSERT INTO transaksi_peminjaman (id_personel, kode_alat, jumlah, tanggal_pinjam, status) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, trx.getPeminjam().getIdPersonel());
                stmt.setString(2, trx.getAlat().getKodeAlat());
                stmt.setInt(3, trx.getJumlah());
                stmt.setDate(4, Date.valueOf(trx.getTanggalPinjam()));
                stmt.setString(5, trx.getStatus());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseAksesException(e.getMessage());
        }
    }

    public List<TransaksiPeminjaman> getAll() throws DatabaseAksesException {
        List<TransaksiPeminjaman> list = new ArrayList<>();
        String sql = """
            SELECT t.id_transaksi, t.jumlah, t.tanggal_pinjam, t.status,
                   p.id_personel, p.nama, p.nia, p.jenis_personel, p.keterangan,
                   a.kode_alat, a.nama_alat, a.kategori, a.stok, a.kondisi
            FROM transaksi_peminjaman t
            JOIN personel p ON t.id_personel = p.id_personel
            JOIN alat_outdoor a ON t.kode_alat = a.kode_alat
            ORDER BY t.id_transaksi DESC
        """;
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Personel p = "PENGURUS".equalsIgnoreCase(rs.getString("jenis_personel"))
                            ? new PengurusInti(rs.getString("id_personel"), rs.getString("nama"), rs.getString("nia"), rs.getString("keterangan"))
                            : new AnggotaBiasa(rs.getString("id_personel"), rs.getString("nama"), rs.getString("nia"), rs.getString("keterangan"));

                    AlatOutdoor a = new AlatOutdoor(
                            rs.getString("kode_alat"), rs.getString("nama_alat"),
                            rs.getString("kategori"), rs.getInt("stok"), rs.getString("kondisi")
                    );

                    list.add(new TransaksiPeminjaman(
                            rs.getInt("id_transaksi"), p, a,
                            rs.getInt("jumlah"), rs.getDate("tanggal_pinjam").toLocalDate(),
                            rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseAksesException(e.getMessage());
        }
        return list;
    }

    public void updateStatus(int idTransaksi, String statusBaru) throws DatabaseAksesException {
        String sql = "UPDATE transaksi_peminjaman SET status = ? WHERE id_transaksi = ?";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, statusBaru);
                stmt.setInt(2, idTransaksi);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseAksesException(e.getMessage());
        }
    }
}