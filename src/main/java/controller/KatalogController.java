package controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import data.ProdukRepository;
import db.Database;
import model.Produk;

public class KatalogController {

    private static final String CUSTOM_PREFIX = "custom:";

    @FXML
    private ImageView remEmblemView;
    @FXML
    private TextField searchField;
    @FXML
    private Label jumlahLabel;

    @FXML
    private Label modeBadgeLabel;
    @FXML
    private ImageView previewImageView;
    @FXML
    private Label previewFallbackLabel;
    @FXML
    private Label selectedFotoLabel;
    @FXML
    private TextField namaField;
    @FXML
    private TextField hargaField;
    @FXML
    private ComboBox<String> fotoBox;
    @FXML
    private Button removeImageButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelEditButton;
    @FXML
    private Label pesanLabel;

    @FXML
    private TilePane katalogPane;

    private final ProdukRepository repository = new ProdukRepository();
    private final NumberFormat rupiahFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"));

    private ArrayList<Produk> allProducts = new ArrayList<>();
    private Long editingProductId = null;
    private String editingOriginalFoto = null;
    private File selectedManualImageFile = null;
    private String activeFotoValue = "roti.png";
    private boolean isPopulatingForm = false;

    @FXML
    private void initialize() {
        loadHeaderEmblem();

        fotoBox.getItems().addAll("roti.png", "susu.png", "kopi.png");
        fotoBox.setValue("roti.png");
        activeFotoValue = "roti.png";

        fotoBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (isPopulatingForm || newVal == null) {
                return;
            }
            selectedManualImageFile = null;
            activeFotoValue = newVal;
            updateImagePreview(loadImage(newVal, 240, 145), "Foto bawaan: " + newVal, true);
        });

        searchField.clear();
        searchField.textProperty().addListener((obs, oldText, newText) -> renderCards());

        setEditModeUI(false, null);
        updateImagePreview(loadImage("roti.png", 240, 145), "Foto bawaan: roti.png", true);
        setStatusMessage("Siap mengelola katalog produk.", "info");

        try {
            showData();
        } catch (SQLException e) {
            setStatusMessage("Gagal membaca database. Lihat panel Output.", "error");
            e.printStackTrace();
        }
    }

    private void loadHeaderEmblem() {
        URL emblemUrl = getClass().getResource("/image/rem_emblem.png");
        if (emblemUrl != null && remEmblemView != null) {
            Image emblem = new Image(emblemUrl.toExternalForm(), 60, 60, true, true);
            if (!emblem.isError()) {
                remEmblemView.setImage(emblem);
            }
        }
    }

    private void showData() throws SQLException {
        allProducts = repository.getAll();
        renderCards();
    }

    private void renderCards() {
        String keyword = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);
        ArrayList<Produk> filtered = new ArrayList<>();
        for (Produk produk : allProducts) {
            if (keyword.isEmpty()
                    || produk.getNama().toLowerCase(Locale.ROOT).contains(keyword)
                    || String.valueOf(produk.getId()).contains(keyword)) {
                filtered.add(produk);
            }
        }

        katalogPane.getChildren().clear();

        if (filtered.isEmpty()) {
            katalogPane.setAlignment(Pos.CENTER);

            VBox emptyBox = new VBox(8);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPrefWidth(420);
            emptyBox.setMaxWidth(420);
            emptyBox.getStyleClass().add("empty-state");

            Label emptyTitle = new Label(allProducts.isEmpty()
                    ? "Belum ada produk di katalog"
                    : "Produk tidak ditemukan");
            emptyTitle.getStyleClass().add("empty-title");

            Label emptySub = new Label(allProducts.isEmpty()
                    ? "Tambahkan produk pertama di panel kiri, atau unggah gambar produk sendiri."
                    : "Coba ubah kata kunci pencarianmu.");
            emptySub.setWrapText(true);
            emptySub.setAlignment(Pos.CENTER);
            emptySub.getStyleClass().add("muted");

            emptyBox.getChildren().addAll(emptyTitle, emptySub);
            katalogPane.getChildren().add(emptyBox);
        } else {
            katalogPane.setAlignment(Pos.TOP_LEFT);
            for (Produk produk : filtered) {
                VBox kartu = createProductCard(produk);
                katalogPane.getChildren().add(kartu);
            }
        }

        if (keyword.isEmpty()) {
            jumlahLabel.setText("Total: " + allProducts.size() + " Produk");
        } else {
            jumlahLabel.setText("Tampil: " + filtered.size() + " / " + allProducts.size() + " Produk");
        }
    }

    private VBox createProductCard(Produk produk) {
        ImageView fotoView = new ImageView();
        fotoView.setFitWidth(196);
        fotoView.setFitHeight(124);
        fotoView.setPreserveRatio(true);
        fotoView.setSmooth(true);

        StackPane bingkai = new StackPane();
        bingkai.setPrefSize(208, 134);
        bingkai.setMinSize(208, 134);
        bingkai.setMaxSize(208, 134);
        bingkai.getStyleClass().add("bingkai-foto");

        Label sourceBadge;
        if (!produk.hasImage()) {
            Label noImgLabel = new Label("Tanpa Gambar");
            noImgLabel.getStyleClass().add("no-image-text");
            bingkai.getChildren().add(noImgLabel);

            sourceBadge = new Label("Polos");
            sourceBadge.getStyleClass().add("badge-no-image");
        } else {
            Image gambar = loadImage(produk.getFoto(), 196, 124);
            if (gambar != null && !gambar.isError()) {
                fotoView.setImage(gambar);
                bingkai.getChildren().add(fotoView);
            } else {
                Label fallback = new Label("Gambar tidak tersedia");
                fallback.getStyleClass().add("muted");
                bingkai.getChildren().add(fallback);
            }

            if (produk.getFoto().startsWith(CUSTOM_PREFIX)) {
                sourceBadge = new Label("Custom");
                sourceBadge.getStyleClass().add("badge-custom");
            } else {
                sourceBadge = new Label("Bawaan");
                sourceBadge.getStyleClass().add("badge-preset");
            }
        }

        Label idBadge = new Label("#" + produk.getId());
        idBadge.getStyleClass().add("id-badge");

        Region badgeSpacer = new Region();
        HBox.setHgrow(badgeSpacer, Priority.ALWAYS);
        HBox topMetaRow = new HBox(6, idBadge, badgeSpacer, sourceBadge);
        topMetaRow.setAlignment(Pos.CENTER_LEFT);

        Label namaLabel = new Label(produk.getNama());
        namaLabel.setWrapText(true);
        namaLabel.setMinHeight(38);
        namaLabel.getStyleClass().add("nama-produk");

        Label hargaLabel = new Label(formatRupiah(produk.getHarga()));
        hargaLabel.getStyleClass().add("harga-produk");

        Button editBtn = new Button("Edit");
        editBtn.getStyleClass().add("button-edit");
        editBtn.setMaxWidth(Double.MAX_VALUE);
        editBtn.setOnAction(event -> startEdit(produk));
        HBox.setHgrow(editBtn, Priority.ALWAYS);

        Button deleteBtn = new Button("Hapus");
        deleteBtn.getStyleClass().add("button-delete");
        deleteBtn.setMaxWidth(Double.MAX_VALUE);
        deleteBtn.setOnAction(event -> confirmAndDelete(produk));
        HBox.setHgrow(deleteBtn, Priority.ALWAYS);

        HBox actionRow = new HBox(8, editBtn, deleteBtn);
        actionRow.setAlignment(Pos.CENTER);

        VBox kartu = new VBox(10);
        kartu.setMinWidth(240);
        kartu.setPrefWidth(240);
        kartu.setMaxWidth(240);
        kartu.getStyleClass().add("kartu-produk");
        if (editingProductId != null && editingProductId.equals(produk.getId())) {
            kartu.getStyleClass().add("kartu-produk-editing");
        }

        kartu.getChildren().addAll(topMetaRow, bingkai, namaLabel, hargaLabel, actionRow);
        return kartu;
    }

    @FXML
    private void chooseManualImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih Gambar Produk");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "File Gambar (*.png, *.jpg, *.jpeg, *.gif, *.bmp)",
                        "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"
                )
        );

        Window owner = namaField.getScene() != null ? namaField.getScene().getWindow() : null;
        File chosenFile = chooser.showOpenDialog(owner);
        if (chosenFile == null) {
            return;
        }

        Image previewImg = loadImageFromFile(chosenFile.toPath(), 240, 145);
        if (previewImg == null || previewImg.isError()) {
            setStatusMessage("File gambar tidak valid atau gagal dibaca.", "error");
            return;
        }

        selectedManualImageFile = chosenFile;
        activeFotoValue = CUSTOM_PREFIX + chosenFile.getName();
        isPopulatingForm = true;
        fotoBox.setValue(null);
        isPopulatingForm = false;

        updateImagePreview(previewImg, "Manual: " + chosenFile.getName(), true);
        setStatusMessage("Gambar manual dipilih: " + chosenFile.getName(), "info");
    }

    @FXML
    private void removeSelectedImage() {
        isPopulatingForm = true;
        fotoBox.setValue(null);
        isPopulatingForm = false;

        selectedManualImageFile = null;
        activeFotoValue = Produk.NO_IMAGE;

        updateImagePreview(null, "Tanpa gambar", false);
        setStatusMessage("Gambar dihapus dari pilihan. Produk akan disimpan tanpa gambar.", "info");
    }

    private void startEdit(Produk produk) {
        isPopulatingForm = true;
        editingProductId = produk.getId();
        editingOriginalFoto = produk.getFoto();
        selectedManualImageFile = null;
        activeFotoValue = produk.getFoto();

        namaField.setText(produk.getNama());
        hargaField.setText(String.valueOf(produk.getHarga()));

        if (!produk.hasImage()) {
            fotoBox.setValue(null);
            updateImagePreview(null, "Tanpa gambar", false);
        } else if (fotoBox.getItems().contains(produk.getFoto())) {
            fotoBox.setValue(produk.getFoto());
            updateImagePreview(loadImage(produk.getFoto(), 240, 145), "Foto bawaan: " + produk.getFoto(), true);
        } else {
            fotoBox.setValue(null);
            updateImagePreview(
                    loadImage(produk.getFoto(), 240, 145),
                    "Foto manual: " + extractDisplayPhotoName(produk.getFoto()),
                    true
            );
        }
        isPopulatingForm = false;

        setEditModeUI(true, produk.getId());
        setStatusMessage("Mode Edit aktif untuk \"" + produk.getNama() + "\".", "info");
        namaField.requestFocus();
        namaField.positionCaret(namaField.getText().length());
        renderCards();
    }

    @FXML
    private void cancelEdit() {
        resetFormToAddMode();
        setStatusMessage("Mode Edit dibatalkan. Kembali ke Mode Tambah.", "info");
        renderCards();
    }

    private void resetFormToAddMode() {
        isPopulatingForm = true;
        editingProductId = null;
        editingOriginalFoto = null;
        selectedManualImageFile = null;
        activeFotoValue = "roti.png";

        namaField.clear();
        hargaField.clear();
        fotoBox.setValue("roti.png");
        isPopulatingForm = false;

        setEditModeUI(false, null);
        updateImagePreview(loadImage("roti.png", 240, 145), "Foto bawaan: roti.png", true);
    }

    private void setEditModeUI(boolean editing, Long productId) {
        modeBadgeLabel.getStyleClass().removeAll("mode-badge-add", "mode-badge-edit");
        if (editing && productId != null) {
            modeBadgeLabel.setText("✎ Mode Edit • Produk #" + productId);
            modeBadgeLabel.getStyleClass().add("mode-badge-edit");
            saveButton.setText("Simpan Perubahan");
            cancelEditButton.setVisible(true);
            cancelEditButton.setManaged(true);
        } else {
            modeBadgeLabel.setText("Tambah Produk");
            modeBadgeLabel.getStyleClass().add("mode-badge-add");
            saveButton.setText("Simpan Produk");
            cancelEditButton.setVisible(false);
            cancelEditButton.setManaged(false);
        }
    }

    @FXML
    private void saveProduct() {
        String nama = namaField.getText();
        long harga;
        try {
            harga = Long.parseLong(hargaField.getText().trim());
        } catch (NumberFormatException e) {
            setStatusMessage("Harga harus berupa angka bulat, contoh: 12000.", "error");
            return;
        }

        String fotoToSave;
        if (selectedManualImageFile != null) {
            try {
                fotoToSave = copyManualImageToStorage(selectedManualImageFile);
            } catch (IOException e) {
                setStatusMessage("Gagal menyalin file gambar manual.", "error");
                e.printStackTrace();
                return;
            }
        } else if (activeFotoValue != null && !activeFotoValue.isBlank()) {
            fotoToSave = activeFotoValue;
        } else if (fotoBox.getValue() != null) {
            fotoToSave = fotoBox.getValue();
        } else {
            fotoToSave = Produk.NO_IMAGE;
        }

        boolean isEdit = (editingProductId != null);
        String oldFotoBeforeEdit = editingOriginalFoto;
        Produk produk;
        try {
            if (isEdit) {
                produk = new Produk(editingProductId, nama, harga, fotoToSave);
            } else {
                produk = new Produk(nama, harga, fotoToSave);
            }
        } catch (IllegalArgumentException e) {
            setStatusMessage(e.getMessage(), "error");
            return;
        }

        try {
            if (isEdit) {
                repository.update(produk);
            } else {
                repository.add(produk);
            }
        } catch (SQLException e) {
            setStatusMessage("Gagal menyimpan ke database. Lihat panel Output.", "error");
            e.printStackTrace();
            return;
        }

        resetFormToAddMode();
        try {
            showData();
            if (isEdit && oldFotoBeforeEdit != null && !oldFotoBeforeEdit.equals(fotoToSave)) {
                cleanupUnusedCustomImage(oldFotoBeforeEdit);
            }
            if (isEdit) {
                setStatusMessage("Perubahan produk \"" + produk.getNama() + "\" berhasil disimpan!", "success");
            } else {
                setStatusMessage("Produk \"" + produk.getNama() + "\" berhasil ditambahkan!", "success");
            }
        } catch (SQLException e) {
            setStatusMessage("Data tersimpan, tetapi katalog gagal dimuat ulang.", "error");
            e.printStackTrace();
        }
    }

    private void confirmAndDelete(Produk produk) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus Produk");
        alert.setHeaderText("Hapus \"" + produk.getNama() + "\"?");
        alert.setContentText(
                "Produk ID #" + produk.getId() + " (" + formatRupiah(produk.getHarga())
                + ") akan dihapus permanen dari database katalog."
        );

        ButtonType btnHapus = new ButtonType("Ya, Hapus", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnBatal = new ButtonType("Batal", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnHapus, btnBatal);

        DialogPane dialogPane = alert.getDialogPane();
        URL cssUrl = getClass().getResource("/view/styles/app.css");
        if (cssUrl != null) {
            dialogPane.getStylesheets().add(cssUrl.toExternalForm());
            dialogPane.getStyleClass().add("rem-dialog");
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btnHapus) {
            String deletedFoto = produk.getFoto();
            try {
                repository.delete(produk.getId());
                if (editingProductId != null && editingProductId.equals(produk.getId())) {
                    resetFormToAddMode();
                }
                showData();
                cleanupUnusedCustomImage(deletedFoto);
                setStatusMessage("Produk \"" + produk.getNama() + "\" berhasil dihapus.", "success");
            } catch (SQLException e) {
                setStatusMessage("Gagal menghapus produk. Lihat panel Output.", "error");
                e.printStackTrace();
            }
        }
    }

    private void cleanupUnusedCustomImage(String fotoRef) {
        if (fotoRef == null || !fotoRef.startsWith(CUSTOM_PREFIX)) {
            return;
        }
        for (Produk p : allProducts) {
            if (fotoRef.equals(p.getFoto())) {
                return;
            }
        }
        String fileName = fotoRef.substring(CUSTOM_PREFIX.length());
        Path filePath = Database.getFolderGambar().resolve(fileName);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException ignored) {
        }
    }

    private String copyManualImageToStorage(File sourceFile) throws IOException {
        Path imageDir = Database.getFolderGambar();
        Files.createDirectories(imageDir);

        String originalName = sourceFile.getName();
        String extension = ".png";
        String baseName = originalName;
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < originalName.length() - 1) {
            baseName = originalName.substring(0, dotIndex);
            extension = originalName.substring(dotIndex).toLowerCase(Locale.ROOT);
        }

        String safeBase = baseName.replaceAll("[^a-zA-Z0-9_-]", "_");
        if (safeBase.length() > 36) {
            safeBase = safeBase.substring(0, 36);
        }
        if (safeBase.isBlank()) {
            safeBase = "produk";
        }

        String uniqueName = "img_" + System.currentTimeMillis() + "_" + safeBase + extension;
        Path targetPath = imageDir.resolve(uniqueName);
        Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return CUSTOM_PREFIX + uniqueName;
    }

    private Image loadImage(String fotoRef, double maxWidth, double maxHeight) {
        if (fotoRef == null || fotoRef.isBlank() || Produk.NO_IMAGE.equalsIgnoreCase(fotoRef)) {
            return null;
        }

        if (fotoRef.startsWith(CUSTOM_PREFIX)) {
            String fileName = fotoRef.substring(CUSTOM_PREFIX.length());
            Path customPath = Database.getFolderGambar().resolve(fileName);
            return loadImageFromFile(customPath, maxWidth, maxHeight);
        }

        Path storedPath = Database.getFolderGambar().resolve(fotoRef);
        if (Files.exists(storedPath)) {
            return loadImageFromFile(storedPath, maxWidth, maxHeight);
        }

        URL resourceUrl = getClass().getResource("/image/" + fotoRef);
        if (resourceUrl != null) {
            return new Image(resourceUrl.toExternalForm(), maxWidth, maxHeight, true, true);
        }

        try {
            Path directPath = Path.of(fotoRef);
            if (Files.exists(directPath)) {
                return loadImageFromFile(directPath, maxWidth, maxHeight);
            }
        } catch (Exception ignored) {
        }

        return null;
    }

    private Image loadImageFromFile(Path path, double maxWidth, double maxHeight) {
        if (path == null || !Files.exists(path)) {
            return null;
        }
        try (InputStream in = Files.newInputStream(path)) {
            return new Image(in, maxWidth, maxHeight, true, true);
        } catch (IOException e) {
            return null;
        }
    }

    private void updateImagePreview(Image image, String caption, boolean hasActiveImage) {
        if (image != null && !image.isError()) {
            previewImageView.setImage(image);
            previewImageView.setVisible(true);
            previewFallbackLabel.setVisible(false);
        } else {
            previewImageView.setImage(null);
            previewImageView.setVisible(false);
            previewFallbackLabel.setText(hasActiveImage ? "Pratinjau tidak tersedia" : "Tanpa Gambar");
            previewFallbackLabel.setVisible(true);
        }
        selectedFotoLabel.setText(caption);
        if (removeImageButton != null) {
            removeImageButton.setDisable(!hasActiveImage);
        }
    }

    private String extractDisplayPhotoName(String fotoRef) {
        if (fotoRef == null || Produk.NO_IMAGE.equalsIgnoreCase(fotoRef)) {
            return "Tanpa gambar";
        }
        String name = fotoRef.startsWith(CUSTOM_PREFIX)
                ? fotoRef.substring(CUSTOM_PREFIX.length())
                : fotoRef;
        if (name.startsWith("img_")) {
            int secondUnderscore = name.indexOf('_', 4);
            if (secondUnderscore > 0 && secondUnderscore < name.length() - 1) {
                return name.substring(secondUnderscore + 1);
            }
        }
        return name;
    }

    private String formatRupiah(long harga) {
        return "Rp " + rupiahFormat.format(harga);
    }

    private void setStatusMessage(String message, String type) {
        pesanLabel.setText(message);
        pesanLabel.getStyleClass().removeAll("status-success", "status-error", "status-info");
        if ("success".equals(type)) {
            pesanLabel.getStyleClass().add("status-success");
        } else if ("error".equals(type)) {
            pesanLabel.getStyleClass().add("status-error");
        } else {
            pesanLabel.getStyleClass().add("status-info");
        }
    }
}
