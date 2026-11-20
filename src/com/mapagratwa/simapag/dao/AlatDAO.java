// Atribusi AI: Kelas AlatDAO dibuat dengan bantuan AI untuk operasi CRUD tabel alat_outdoor menggunakan PreparedStatement.
package com.mapagratwa.simapag.dao;

import com.mapagratwa.simapag.exception.DatabaseAksesException;
import com.mapagratwa.simapag.model.AlatOutdoor;
import com.mapagratwa.simapag.pattern.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlatDAO {

    public void insert(AlatOutdoor alat) throws DatabaseAksesException {
        String sql = "INSERT INTO alat_outdoor (kode_alat, nama_alat, kategori, stok, kondisi) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, alat.getKodeAlat());
                stmt.setString(2, alat.getNamaAlat());
                stmt.setString(3, alat.getKategori());
                stmt.setInt(4, alat.getStok());
                stmt.setString(5, alat.getKondisi());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseAksesException(e.getMessage());
        }
    }

    public List<AlatOutdoor> getAll() throws DatabaseAksesException {
        List<AlatOutdoor> list = new ArrayList<>();
        String sql = "SELECT * FROM alat_outdoor";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new AlatOutdoor(
                            rs.getString("kode_alat"),
                            rs.getString("nama_alat"),
                            rs.getString("kategori"),
                            rs.getInt("stok"),
                            rs.getString("kondisi")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseAksesException(e.getMessage());
        }
        return list;
    }

    public void update(AlatOutdoor alat) throws DatabaseAksesException {
        String sql = "UPDATE alat_outdoor SET nama_alat = ?, kategori = ?, stok = ?, kondisi = ? WHERE kode_alat = ?";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, alat.getNamaAlat());
                stmt.setString(2, alat.getKategori());
                stmt.setInt(3, alat.getStok());
                stmt.setString(4, alat.getKondisi());
                stmt.setString(5, alat.getKodeAlat());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseAksesException(e.getMessage());
        }
    }

    public void delete(String kodeAlat) throws DatabaseAksesException {
        String sql = "DELETE FROM alat_outdoor WHERE kode_alat = ?";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, kodeAlat);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseAksesException(e.getMessage());
        }
    }
}