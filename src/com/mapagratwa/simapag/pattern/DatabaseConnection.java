// Atribusi AI: Implementasi pola Singleton dibuat dengan bantuan AI untuk manajemen koneksi JDBC MySQL.
package com.mapagratwa.simapag.pattern;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Pattern: Singleton.
 * Alasan:
 * 1. Memastikan hanya terdapat satu instans objek koneksi database MySQL yang aktif selama aplikasi berjalan.
 * 2. Menghindari pemborosan sumber daya memori dan latensi akibat membuka-tutup koneksi secara berurutan pada setiap operasi DAO.
 * 3. Memusatkan konfigurasi kredensial dan alamat basis data pada satu kelas tunggal agar mudah dikelola.
 *
 * Dua kemungkinan nyata yang membuatnya perlu:
 * 1. Pengurus logistik melakukan pencatatan peminjaman dan pengembalian puluhan alat secara berurutan dalam satu sesi kegiatan.
 * 2. Terjadi perpindahan server basis data dari komputer lokal ke server jaringan sekretariat UKM sehingga URL cukup diubah di satu tempat.
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/simapag_db";
    private final String user = "root";
    private final String password = "";

    private DatabaseConnection() throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
    }

    public static synchronized DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}