// Atribusi AI: Kelas PersonelDAO dibuat dengan bantuan AI untuk operasi CRUD tabel personel menggunakan PreparedStatement.
package com.mapagratwa.simapag.dao;

import com.mapagratwa.simapag.exception.DatabaseAksesException;
import com.mapagratwa.simapag.model.*;
import com.mapagratwa.simapag.pattern.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonelDAO {

    public void insert(Personel p) throws DatabaseAksesException {
        String sql = "INSERT INTO personel (id_personel, nama, nia, jenis_personel, keterangan) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, p.getIdPersonel());
                stmt.setString(2, p.getNama());
                stmt.setString(3, p.getNia());
                if (p instanceof PengurusInti pengurus) {
                    stmt.setString(4, "PENGURUS");
                    stmt.setString(5, pengurus.getJabatan());
                } else if (p instanceof AnggotaBiasa anggota) {
                    stmt.setString(4, "ANGGOTA");
                    stmt.setString(5, anggota.getAngkatan());
                }
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseAksesException(e.getMessage());
        }
    }

    public List<Personel> getAll() throws DatabaseAksesException {
        List<Personel> list = new ArrayList<>();
        String sql = "SELECT * FROM personel";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id_personel");
                    String nama = rs.getString("nama");
                    String nia = rs.getString("nia");
                    String jenis = rs.getString("jenis_personel");
                    String ket = rs.getString("keterangan");

                    if ("PENGURUS".equalsIgnoreCase(jenis)) {
                        list.add(new PengurusInti(id, nama, nia, ket));
                    } else {
                        list.add(new AnggotaBiasa(id, nama, nia, ket));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseAksesException(e.getMessage());
        }
        return list;
    }

    public void delete(String idPersonel) throws DatabaseAksesException {
        String sql = "DELETE FROM personel WHERE id_personel = ?";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, idPersonel);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseAksesException(e.getMessage());
        }
    }
}