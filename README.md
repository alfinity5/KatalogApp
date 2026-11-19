# Katalog Produk

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

## Panduan Instalasi dan Menjalankan Aplikasi

Ikuti langkah-langkah berikut dari awal untuk menginstal perangkat lunak pendukung, memasang dependensi proyek, dan menjalankan aplikasi di komputer lokal.

### Tahap 1 — Instalasi Software Pendukung

Sebelum menjalankan proyek, pastikan perangkat sudah terpasang:

1. **Java Development Kit (JDK) 17 atau lebih baru**
   - Unduh dan instal JDK dari [Adoptium Temurin](https://adoptium.net/) atau [Oracle JDK](https://www.oracle.com/java/technologies/downloads/).
   - Pastikan variabel lingkungan `JAVA_HOME` dan `PATH` sudah mengarah ke folder instalasi JDK.
2. **Apache NetBeans IDE** *(Direkomendasikan — sudah dilengkapi Maven bawaan)*
   - Unduh dan instal dari situs resmi [Apache NetBeans](https://netbeans.apache.org/).
   - *(Opsional)* Jika ingin menjalankan proyek lewat Terminal/CMD tanpa NetBeans, instal [Apache Maven](https://maven.apache.org/download.cgi) secara terpisah dan tambahkan folder `bin`-nya ke `PATH`.
3. **Verifikasi Instalasi**
   Buka Terminal atau Command Prompt, lalu jalankan perintah berikut untuk memastikan Java (dan Maven jika memakai CLI) sudah terdeteksi:
   ```bash
   java -version
   mvn -version
   ```

---

### Tahap 2 — Mengunduh Proyek

Pilih salah satu cara berikut untuk mendapatkan kode sumber proyek:

- **Cara A — Clone via Git:**
  ```bash
  git clone [https://github.com/USERNAME/NAMA-REPO.git](https://github.com/USERNAME/NAMA-REPO.git)
  cd NAMA-REPO
  ```
- **Cara B — Download ZIP:**
  1. Klik tombol hijau **`<> Code`** di bagian atas halaman repositori GitHub ini, lalu pilih **Download ZIP**.
  2. Ekstrak file ZIP hasil unduhan ke folder pilihanmu (misalnya `Documents/NetBeansProjects/`).

---

### Tahap 3 — Instalasi Library & Menjalankan Aplikasi

Proyek ini menggunakan **Apache Maven** (`pom.xml`), sehingga seluruh library (`javafx-controls`, `javafx-fxml`, dan `sqlite-jdbc`) akan diunduh dan dipasang secara otomatis saat proses build pertama kali *(pastikan perangkat terhubung ke internet)*.

#### Opsi 1 — Menggunakan Apache NetBeans IDE *(Direkomendasikan)*
1. Buka aplikasi **Apache NetBeans IDE**.
2. Klik menu **File → Open Project...**, lalu arahkan ke folder **`KatalogProduk`** (folder yang memiliki ikon proyek Maven) dan klik **Open Project**.
3. Pada panel **Projects** di sebelah kiri, klik kanan pada **`KatalogProduk`** → pilih **Clean and Build**.
   - Tunggu hingga proses pengunduhan dependensi dan kompilasi selesai dengan pesan **`BUILD SUCCESS`** di panel Output.
4. Jalankan aplikasi dengan menekan tombol **Run (`F6`)** atau ikon segitiga hijau di toolbar.
   - *(Alternatif)*: Kamu juga bisa membuka panel **Navigator** di kiri bawah, lalu klik dua kali pada goal **`javafx:run`**.

#### Opsi 2 — Menggunakan Terminal / Command Prompt (Maven CLI)
1. Masuk ke folder utama proyek tempat file `pom.xml` berada:
   ```bash
   cd KatalogProduk
   ```
2. Unduh dependensi dan lakukan build proyek:
   ```bash
   mvn clean compile
   ```
3. Jalankan aplikasi menggunakan plugin Maven JavaFX:
   ```bash
   mvn javafx:run
   ```

---

### Tahap 4 — Inisialisasi Otomatis Saat Pertama Dijalankan

Saat aplikasi berhasil terbuka untuk pertama kali:
- Folder **`data/`** akan dibuat otomatis di direktori proyek.
- File database SQLite **`data/katalog.db`** beserta tabel `produk` akan dibuat secara otomatis (tidak perlu impor file `.sql` secara manual).
- Folder **`data/images/`** akan disiapkan sebagai tempat penyimpanan lokal untuk foto produk yang diunggah secara manual.

---

