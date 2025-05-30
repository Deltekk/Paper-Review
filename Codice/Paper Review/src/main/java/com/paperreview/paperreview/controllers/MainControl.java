package com.paperreview.paperreview.controllers;

import com.paperreview.paperreview.interfaces.ControlledScreen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import java.io.IOException;

public class MainControl {

    @FXML
    private StackPane rootPane;

    @FXML
    private HBox header;

    @FXML
    private RowConstraints headerRow, contentRow;

    @FXML
    private ImageView logoImage;

    @FXML
    private Button logoButton;

    @FXML
    public void initialize() {
        // Carica la schermata di login all’avvio
        setView("/com/paperreview/paperreview/boundaries/login/loginBoundary.fxml");
        Image logo = new Image(getClass().getResourceAsStream("/images/logoBianco.png"));
        Image hoverLogo = new Image(getClass().getResourceAsStream("/images/logoHover.png"));
        Image pressedLogo = new Image(getClass().getResourceAsStream("/images/logoPressed.png"));

        logoButton.setOnMouseEntered(event -> {
            logoImage.setImage(hoverLogo);
        });

        logoButton.setOnMousePressed(event -> {
            logoImage.setImage(pressedLogo);
        });

        logoButton.setOnMouseReleased(event -> {
            logoImage.setImage(hoverLogo);
        });

        logoButton.setOnMouseExited(event -> {logoImage.setImage(logo); System.out.println("Nando");});

        logoImage.setImage(logo);
    }

    public void showHeader() {
        header.setVisible(true);
        header.setManaged(true);
        headerRow.setPercentHeight(8);
        contentRow.setPercentHeight(92);
    }

    public void hideHeader() {
        header.setVisible(false);
        header.setManaged(false);
        headerRow.setPercentHeight(0);
        contentRow.setPercentHeight(100);
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

            if (fxmlPath.contains("login") || fxmlPath.contains("registrazione") || fxmlPath.contains("recupero"))
            {
                hideHeader();
            } else {
                showHeader();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleHome(){
        System.out.println("Handle home");
    }

    public void handleProfile(){
        System.out.println("Handle profile");
    }

}
