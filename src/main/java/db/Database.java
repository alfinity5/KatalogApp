package db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final Path FILE_DATABASE = Path.of(
            System.getProperty("user.dir"), "data", "katalog.db"
    ).toAbsolutePath().normalize();

    private static final Path FOLDER_GAMBAR = Path.of(
            System.getProperty("user.dir"), "data", "images"
    ).toAbsolutePath().normalize();

    public static Connection bukaKoneksi() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + FILE_DATABASE);
    }

    public static Path getFolderGambar() {
        return FOLDER_GAMBAR;
    }

    public static void prepareDatabase() throws IOException, SQLException {
        Files.createDirectories(FILE_DATABASE.getParent());
        Files.createDirectories(FOLDER_GAMBAR);
        System.out.println("DATABASE AKTIF: " + FILE_DATABASE);
        System.out.println("FOLDER GAMBAR MANUAL: " + FOLDER_GAMBAR);
        String sql = "CREATE TABLE IF NOT EXISTS produk ("
                + "id INTEGER PRIMARY KEY, "
                + "nama TEXT NOT NULL CHECK(length(trim(nama)) > 0), "
                + "harga INTEGER NOT NULL CHECK(harga > 0), "
                + "foto TEXT NOT NULL CHECK(length(trim(foto)) > 0))";

        try (
            Connection koneksi = bukaKoneksi();
            Statement perintah = koneksi.createStatement()) {
            perintah.executeUpdate(sql);
        }
    }
}
