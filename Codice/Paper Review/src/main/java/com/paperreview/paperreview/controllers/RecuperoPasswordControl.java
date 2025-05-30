package com.paperreview.paperreview.controllers;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.forms.RecuperoPasswordFormModel;
import com.paperreview.paperreview.interfaces.ControlledScreen;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


public class RecuperoPasswordControl implements ControlledScreen {

    @FXML
    private VBox formContainer;
    @FXML
    private Label errorLabel;
    @FXML
    private Button confirmButton;
    @FXML
    private ImageView logoImage;

    private RecuperoPasswordFormModel recuperoPasswordFormModel = new RecuperoPasswordFormModel();

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

        Form form = recuperoPasswordFormModel.createForm();
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

    }


    @FXML
    private void handleRecuperoPassword() {
        String email = recuperoPasswordFormModel.getEmail();

        // TODO: Da implementare il send dell'email.

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Recupero password");
        alert.setHeaderText(null);  // Nessun header

        String message = "Se " + email + " appartiene ad un account memorizzato nel sistema, ti arriverà una email con la tua password temporanea.";
        alert.setContentText(message);

        alert.getDialogPane().setPrefWidth(600);  // puoi aumentare o diminuire la larghezza
        alert.getDialogPane().setPrefHeight(100);  // puoi aumentare o diminuire la larghezza

        alert.showAndWait();

        mainControl.setView("/com/paperreview/paperreview/boundaries/login/loginBoundary.fxml");

    }

    @FXML
    private void handleBack() {
        mainControl.setView("/com/paperreview/paperreview/boundaries/login/loginBoundary.fxml");
    }
}
