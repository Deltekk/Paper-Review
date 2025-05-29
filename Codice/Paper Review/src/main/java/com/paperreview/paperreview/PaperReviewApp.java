package com.paperreview.paperreview;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;

public class PaperReviewApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/paperreview/paperreview/views/login/login.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Paper Review - Login");

        // Imposta dimensione minima della finestra
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);

        primaryStage.show();

        InputStream is = getClass().getResourceAsStream("/images/logo.png");
        if (is == null) {
            System.out.println("Immagine non trovata nel classpath!");
        } else {
            System.out.println("Immagine trovata correttamente.");
        }


    }

    public static void main(String[] args) {
        launch(args);
    }
}
