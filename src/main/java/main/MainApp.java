package main;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import db.Database;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Database.prepareDatabase();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/katalog.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1140, 760);

        URL iconUrl = getClass().getResource("/image/rem_emblem.png");
        if (iconUrl != null) {
            stage.getIcons().add(new Image(iconUrl.toExternalForm()));
        }

        stage.setTitle("Katalog Produk Alpin Store");
        stage.setMinWidth(840);
        stage.setMinHeight(580);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }
}
