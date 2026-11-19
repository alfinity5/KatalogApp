# Katalog Produk — Rem Edition

Aplikasi desktop manajemen katalog produk berbasis **JavaFX** dan **SQLite** yang dirancang untuk mengelola daftar produk toko secara lokal. Proyek ini merupakan pengembangan aplikasi Katalog Produk untuk mata kuliah Pemrograman Lanjut dengan penambahan fitur CRUD lengkap, manajemen gambar manual, pencarian real-time, serta desain UI modern.

---

## Tentang Aplikasi

**Katalog Produk** memudahkan pengelolaan data barang (nama, harga, dan foto produk) dalam tampilan kartu katalog yang rapi dan responsif. Seluruh data tersimpan secara persisten menggunakan database lokal **SQLite** (`data/katalog.db`), sehingga tidak memerlukan konfigurasi server database terpisah.

### Fitur Utama

- **Tambah Produk (`Create`)** — Menambahkan produk baru dengan validasi nama, harga bulat positif, serta format otomatis mata uang Rupiah (`Rp 15.000`).
- **Edit Produk (`Update`)** — Memperbarui nama, harga, maupun gambar produk langsung dari form sidebar dengan indikator **Mode Edit**, highlight kartu aktif, dan tombol **Batal**.
- **Hapus Produk (`Delete`)** — Menghapus produk permanen dari database dilengkapi dialog konfirmasi sebelum eksekusi.
- **Upload Gambar Manual + Live Preview** — Mendukung foto bawaan (`roti.png`, `susu.png`, `kopi.png`) maupun unggah gambar lokal (`.png`, `.jpg`, `.jpeg`, `.gif`, `.bmp`) lewat `FileChooser`. File otomatis disalin ke folder `data/images/` agar tetap terbaca meskipun file sumber dipindahkan.
- **Hapus Gambar Produk** — Memungkinkan pengguna menghapus gambar yang terpasang agar produk tampil polos (**Tanpa Gambar**), sekaligus membersihkan file gambar custom yang sudah tidak digunakan.
- **Pencarian Real-Time** — Menyaring katalog produk secara langsung berdasarkan nama produk atau ID lengkap dengan penghitung total produk.

---

## Screenshot Aplikasi

### Tampilan Utama & Katalog Produk
![Tampilan Utama Katalog Produk]<img width="1423" height="982" alt="Screenshot 2026-11-19 173721" src="https://github.com/user-attachments/assets/69e6e1eb-edd3-4545-ad08-8698835569c7" />


### Mode Edit & Upload Gambar Manual
![Mode Edit Produk]<img width="1422" height="986" alt="Screenshot 2026-11-19 174524" src="https://github.com/user-attachments/assets/529c6dcd-a1af-429a-8000-14715c29e713" />

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

