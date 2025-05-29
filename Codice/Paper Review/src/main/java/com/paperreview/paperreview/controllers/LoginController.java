package com.paperreview.paperreview.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private ImageView logoImage;

    @FXML
    public void initialize() {
        Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
        if (logo != null) {
            logoImage.setImage(logo);
        } else {
            System.err.println("Logo non trovato nel classpath!");
        }
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if(email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Errore: i campi devono essere tutti compilati!");
            errorLabel.setVisible(true);
            return;
        }

        // TODO: inserisci chiamata HTTP per login qui
        errorLabel.setVisible(false);
        System.out.println("Eseguo login con: " + email);
    }

    @FXML
    private void handleRegister() {
        System.out.println("Vai alla registrazione");
        // TODO: naviga a pagina registrazione
    }

    @FXML
    private void handleRecover() {
        System.out.println("Vai a recupero password");
        // TODO: naviga a pagina recupero password
    }
}
