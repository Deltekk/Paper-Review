package com.paperreview.paperreview.controllers;

import com.paperreview.paperreview.interfaces.ControlledScreen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainControl {

    @FXML
    private StackPane rootPane;

    @FXML
    public void initialize() {
        // Carica la schermata di login all’avvio
        setView("/com/paperreview/paperreview/boundaries/login/loginBoundary.fxml");
    }

    public void setView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ControlledScreen controlledScreen) {
                controlledScreen.setMainController(this);
            }

            rootPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
