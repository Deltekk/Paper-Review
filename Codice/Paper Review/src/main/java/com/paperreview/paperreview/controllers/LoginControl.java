package com.paperreview.paperreview.controllers;

import com.paperreview.paperreview.forms.LoginFormModel;
import com.paperreview.paperreview.interfaces.ControlledScreen;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Border;


public class LoginControl implements ControlledScreen {

    @FXML
    private VBox formContainer;
    @FXML
    private Label errorLabel;
    @FXML
    private Button confirmButton;
    @FXML
    private ImageView logoImage;

    private LoginFormModel loginFormModel = new LoginFormModel();

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        errorLabel.setVisible(false);
        confirmButton.setDisable(true);

        Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
        if (logo == null) {
            System.err.println("Logo non trovato nel classpath!");
        } else {
            logoImage.setImage(logo);
        }

        Form form = loginFormModel.createForm();
        FormRenderer formRenderer = new FormRenderer(form);
        formContainer.getChildren().add(formRenderer);
        formRenderer.setStyle("-fx-border-color: transparent; -fx-border-width: 0;\n");
        formContainer.setStyle("-fx-border-color: transparent; -fx-border-width: 0;\n");

        Border noBorder = Border.EMPTY;


        formRenderer.setBorder(noBorder);
        formContainer.setBorder(noBorder);

        formRenderer.lookupAll(".formsfx-group").forEach(node -> {
            if (node instanceof Region region) {
                region.setBorder(null);
            }
        });


        form.validProperty().addListener((obs, oldVal, newVal) -> {
            confirmButton.setDisable(!newVal);
        });

        loginFormModel.getEmailField().valueProperty().addListener((obs, oldVal, newVal) -> {
            if (errorLabel.isVisible()) {
                errorLabel.setVisible(false);
            }
        });

        loginFormModel.getPasswordField().valueProperty().addListener((obs, oldVal, newVal) -> {
            if (errorLabel.isVisible()) {
                errorLabel.setVisible(false);
            }
        });

    }


    @FXML
    private void handleLogin() {
        String email = loginFormModel.getEmail();
        String password = loginFormModel.getPassword();

        // Simulo un login fallito per esempio
        boolean loginSuccess = false; // TODO: qui metti la tua logica reale

        if (!loginSuccess) {
            errorLabel.setText("Email o password errati");
            errorLabel.setVisible(true);
            return;
        }

        errorLabel.setVisible(false);
        System.out.println("Eseguo login con: " + email + ":" + password);

        // TODO: logica login reale
    }

    @FXML
    private void handleRegister() {
        System.out.println("Registrazione");
        mainControl.setView("/com/paperreview/paperreview/boundaries/register/register.fxml");
    }

    @FXML
    private void handleRecover() {
        System.out.println("Recupero password");
        // TODO: logica login reale
    }


}
