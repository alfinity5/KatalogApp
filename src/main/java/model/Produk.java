package model;

public class Produk {

    public static final String NO_IMAGE = "none";

    private long id;
    private String nama;
    private long harga;
    private String foto;

    public Produk(long id, String nama, long harga, String foto) {
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama produk wajib diisi.");
        }
        if (harga <= 0) {
            throw new IllegalArgumentException("Harga harus lebih dari nol.");
        }
        this.id = id;
        this.nama = nama.trim();
        this.harga = harga;
        this.foto = (foto == null || foto.isBlank()) ? NO_IMAGE : foto.trim();
    }

    public Produk(String nama, long harga, String foto) {
        this(0, nama, harga, foto);
    }

    public long getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public long getHarga() {
        return harga;
    }

    public String getFoto() {
        return foto;
    }

    public boolean hasImage() {
        return foto != null && !foto.isBlank() && !NO_IMAGE.equalsIgnoreCase(foto);
    }
}
