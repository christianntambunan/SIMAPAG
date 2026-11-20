CREATE DATABASE IF NOT EXISTS simapag_db;
USE simapag_db;

CREATE TABLE personel (
    id_personel VARCHAR(15) PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    nia VARCHAR(30) NOT NULL,
    jenis_personel ENUM('ANGGOTA', 'PENGURUS') NOT NULL,
    keterangan VARCHAR(50) NOT NULL
);

CREATE TABLE alat_outdoor (
    kode_alat VARCHAR(15) PRIMARY KEY,
    nama_alat VARCHAR(100) NOT NULL,
    kategori VARCHAR(50) NOT NULL,
    stok INT NOT NULL CHECK (stok >= 0),
    kondisi VARCHAR(30) NOT NULL
);

CREATE TABLE transaksi_peminjaman (
    id_transaksi INT AUTO_INCREMENT PRIMARY KEY,
    id_personel VARCHAR(15) NOT NULL,
    kode_alat VARCHAR(15) NOT NULL,
    jumlah INT NOT NULL,
    tanggal_pinjam DATE NOT NULL,
    status ENUM('DIPINJAM', 'DIKEMBALIKAN') DEFAULT 'DIPINJAM',
    FOREIGN KEY (id_personel) REFERENCES personel(id_personel),
    FOREIGN KEY (kode_alat) REFERENCES alat_outdoor(kode_alat)
);

-- Data Contoh
INSERT INTO personel VALUES 
('P01', 'Christian Tambunan', 'MPG-001', 'PENGURUS', 'Koordinator Logistik'),
('P02', 'Rina Siregar', 'MPG-015', 'ANGGOTA', 'Angkatan XIV'),
('P03', 'Budi Santoso', 'MPG-018', 'ANGGOTA', 'Angkatan XV');

INSERT INTO alat_outdoor VALUES 
('A01', 'Tenda Dome Kapasitas 4', 'Shelter', 5, 'Baik'),
('A02', 'Carrier 60L Consina', 'Tas', 8, 'Baik'),
('A03', 'Kompor Portable', 'Masak', 6, 'Baik'),
('A04', 'Nesting TNI', 'Masak', 10, 'Baik');