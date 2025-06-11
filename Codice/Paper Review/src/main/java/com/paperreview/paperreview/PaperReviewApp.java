package com.paperreview.paperreview;

import com.paperreview.paperreview.common.DotenvUtil;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;

import com.paperreview.paperreview.common.dbms.DBMSBoundary;

public class PaperReviewApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        DotenvUtil.init(); // Carichiamo le variabili d'ambiente
        CSSFX.start(); // Inizializiamo l'hot-reload del CSS
        DBMSBoundary.init(); // Inizializziamo il DBMS

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/paperreview/paperreview/boundaries/main/main.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Paper Review");

        // Imposta dimensione minima della finestra
        primaryStage.setMinWidth(1480);
        primaryStage.setMinHeight(920);

        // Impostiamo full screen se massimizzato
        primaryStage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
            if (isNowMaximized) {
                primaryStage.setFullScreen(true);
            } else {
                primaryStage.setFullScreen(false);
            }
        });

        // Carichiamo l'icona dalla cartella resources/images (esempio: icon.png)
        InputStream iconStream = getClass().getResourceAsStream("/images/Icona.png");
        if (iconStream != null) {
            primaryStage.getIcons().add(new javafx.scene.image.Image(iconStream));
        } else {
            System.err.println("Icona app non trovata nel classpath!");
        }

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        DBMSBoundary.close();
    }
}
