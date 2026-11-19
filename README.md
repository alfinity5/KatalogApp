# Katalog Produk — Rem Edition

Aplikasi desktop manajemen katalog produk berbasis **JavaFX** dan **SQLite** yang dirancang untuk mengelola daftar produk toko secara lokal dengan antarmuka bertema **Rem (Re:Zero)**. Proyek ini merupakan pengembangan aplikasi Katalog Produk untuk mata kuliah Pemrograman Lanjut dengan penambahan fitur CRUD lengkap, manajemen gambar manual, pencarian real-time, serta desain UI modern.

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=flat-square&logo=openjdk)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-Project-C71A36?style=flat-square&logo=apachemaven)
![SQLite](https://img.shields.io/badge/Database-SQLite-003B57?style=flat-square&logo=sqlite)

---

## Tentang Aplikasi

**Katalog Produk — Rem Edition** memudahkan pengelolaan data barang (nama, harga, dan foto produk) dalam tampilan kartu katalog yang rapi dan responsif. Seluruh data tersimpan secara persisten menggunakan database lokal **SQLite** (`data/katalog.db`), sehingga tidak memerlukan konfigurasi server database terpisah.

### Fitur Utama

- **Tambah Produk (`Create`)** — Menambahkan produk baru dengan validasi nama, harga bulat positif, serta format otomatis mata uang Rupiah (`Rp 15.000`).
- **Edit Produk (`Update`)** — Memperbarui nama, harga, maupun gambar produk langsung dari form sidebar dengan indikator **Mode Edit**, highlight kartu aktif, dan tombol **Batal**.
- **Hapus Produk (`Delete`)** — Menghapus produk permanen dari database dilengkapi dialog konfirmasi sebelum eksekusi.
- **Upload Gambar Manual + Live Preview** — Mendukung foto bawaan (`roti.png`, `susu.png`, `kopi.png`) maupun unggah gambar lokal (`.png`, `.jpg`, `.jpeg`, `.gif`, `.bmp`) lewat `FileChooser`. File otomatis disalin ke folder `data/images/` agar tetap terbaca meskipun file sumber dipindahkan.
- **Hapus Gambar Produk** — Memungkinkan pengguna menghapus gambar yang terpasang agar produk tampil polos (**Tanpa Gambar**), sekaligus membersihkan file gambar custom yang sudah tidak digunakan.
- **Pencarian Real-Time** — Menyaring katalog produk secara langsung berdasarkan nama produk atau ID lengkap dengan penghitung total produk.
- **Desain UI Nuansa Rem (Re:Zero)** — Menggunakan kombinasi warna *midnight navy*, *Rem sky blue*, putih bersih, aksen *ribbon pink*, serta emblem kustom pada header dan ikon jendela.

---

## Screenshot Aplikasi

### Tampilan Utama & Katalog Produk
![Tampilan Utama Katalog Produk](screenshots/tampilan-utama.png)

### Mode Edit & Upload Gambar Manual
![Mode Edit Produk](screenshots/mode-edit.png)

### Dialog Konfirmasi Hapus Produk
![Konfirmasi Hapus Produk](screenshots/konfirmasi-hapus.png)

---

## Teknologi yang Digunakan

| Komponen | Teknologi / Versi |
| :--- | :--- |
| Bahasa Pemrograman | Java (Target Java 17, kompatibel hingga JDK 26) |
| GUI Framework | JavaFX 21 (`javafx-controls`, `javafx-fxml`) |
| Build Tool | Apache Maven + `javafx-maven-plugin` 0.0.8 |
| Database | SQLite (`sqlite-jdbc` 3.49.1.0) |
| Styling | JavaFX CSS (`app.css`) |
| IDE | Apache NetBeans IDE & Scene Builder |

---

## Struktur Proyek

```text
KatalogProduk/
├── pom.xml
├── data/
│   ├── katalog.db                  # Database SQLite (otomatis dibuat saat run)
│   └── images/                     # Penyimpanan lokal gambar upload manual
├── screenshots/                    # Folder gambar untuk tampilan README
└── src/main/
    ├── java/
    │   ├── controller/
    │   │   └── KatalogController.java
    │   ├── data/
    │   │   └── ProdukRepository.java
    │   ├── db/
    │   │   └── Database.java
    │   ├── main/
    │   │   ├── Launcher.java       # Entry point wrapper agar langsung jalan di IDE
    │   │   └── MainApp.java        # Kelas utama JavaFX Application
    │   └── model/
    │       └── Produk.java
    └── resources/
        ├── image/
        │   ├── kopi.png
        │   ├── rem_emblem.png
        │   ├── roti.png
        │   └── susu.png
        └── view/
            ├── katalog.fxml
            └── styles/
                └── app.css
