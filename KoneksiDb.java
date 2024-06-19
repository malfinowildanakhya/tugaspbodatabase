package javadb;

import java.sql.*;
import java.util.Scanner;

public class KoneksiDb {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://127.0.0.1/penjualan";
    static final String USER = "root";
    static final String PASS = "";

    static Connection conn;
    static Statement stmt;
    static ResultSet rs;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Tambah Data");
            System.out.println("2. Ubah Data");
            System.out.println("3. Hapus Data");
            System.out.println("4. Tampilkan Data");
            System.out.println("5. Keluar");
            System.out.print("Pilih opsi: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1:
                    tambah(scanner);
                    break;
                case 2:
                    ubah(scanner);
                    break;
                case 3:
                    hapus(scanner);
                    break;
                case 4:
                    tampilkan();
                    break;
                case 5:
                    System.out.println("Keluar...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opsi tidak valid. Silakan coba lagi.");
            }
        }
    }

    public static void tambah(Scanner scanner) {
        System.out.print("Masukkan Kode Barang: ");
        String kode_brg = scanner.nextLine();
        System.out.print("Masukkan Nama Barang: ");
        String nama_brg = scanner.nextLine();
        System.out.print("Masukkan Satuan: ");
        String satuan = scanner.nextLine();
        System.out.print("Masukkan Stok: ");
        int stok = scanner.nextInt();
        System.out.print("Masukkan Stok Minimal: ");
        int stok_min = scanner.nextInt();
        scanner.nextLine();

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "INSERT INTO barang (kd_brg, nm_brg, satuan, stok_brg, stok_min) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, kode_brg);
            ps.setString(2, nama_brg);
            ps.setString(3, satuan);
            ps.setInt(4, stok);
            ps.setInt(5, stok_min);

            ps.execute();
            System.out.println("Data berhasil ditambahkan!");

            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ubah(Scanner scanner) {
        System.out.print("Masukkan Kode Barang yang akan diubah: ");
        String kode_brg = scanner.nextLine();
        System.out.print("Masukkan Nama Barang baru: ");
        String nama_brg = scanner.nextLine();
        System.out.print("Masukkan Satuan baru: ");
        String satuan = scanner.nextLine();
        System.out.print("Masukkan Stok baru: ");
        int stok = scanner.nextInt();
        System.out.print("Masukkan Stok Minimal baru: ");
        int stok_min = scanner.nextInt();
        scanner.nextLine();

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Cek apakah data ada
            String checkSql = "SELECT * FROM barang WHERE kd_brg = ?";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setString(1, kode_brg);
            rs = checkPs.executeQuery();

            if (rs.next()) {
                String sql = "UPDATE barang SET nm_brg = ?, satuan = ?, stok_brg = ?, stok_min = ? WHERE kd_brg = ?";
                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, nama_brg);
                ps.setString(2, satuan);
                ps.setInt(3, stok);
                ps.setInt(4, stok_min);
                ps.setString(5, kode_brg);

                ps.executeUpdate();
                System.out.println("Data berhasil diubah!");

                ps.close();
            } else {
                System.out.println("Data tidak ditemukan.");
            }

            checkPs.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hapus(Scanner scanner) {
        System.out.print("Masukkan Kode Barang yang akan dihapus: ");
        String kode_brg = scanner.nextLine();

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String checkSql = "SELECT * FROM barang WHERE kd_brg = ?";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setString(1, kode_brg);
            rs = checkPs.executeQuery();

            if (rs.next()) {
                String sql = "DELETE FROM barang WHERE kd_brg = ?";
                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, kode_brg);

                ps.executeUpdate();
                System.out.println("Data berhasil dihapus!");

                ps.close();
            } else {
                System.out.println("Data tidak ditemukan.");
            }

            checkPs.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void tampilkan() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            rs = stmt.executeQuery("SELECT * FROM barang");
            int i = 1;
            boolean dataAda = false;
            while (rs.next()) {
                dataAda = true;
                System.out.println("Data ke-" + i);
                System.out.println("Kode Barang: " + rs.getString("kd_brg"));
                System.out.println("Nama Barang: " + rs.getString("nm_brg"));
                System.out.println("Satuan: " + rs.getString("satuan"));
                System.out.println("Stok: " + rs.getInt("stok_brg"));
                System.out.println("Stok minimal: " + rs.getInt("stok_min"));
                i++;
            }

            if (!dataAda) {
                System.out.println("Data tidak ditemukan.");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
