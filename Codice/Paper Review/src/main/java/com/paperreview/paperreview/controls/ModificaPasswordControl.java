package com.paperreview.paperreview.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.*;
import com.paperreview.paperreview.common.email.MailRecuperoAccount;
import com.paperreview.paperreview.entities.UtenteEntity;
import com.paperreview.paperreview.forms.ModificaPasswordModel;
import com.paperreview.paperreview.interfaces.ControlledScreen;
import jakarta.mail.MessagingException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class ModificaPasswordControl implements ControlledScreen {

    private MainControl mainControl;

    @FXML
    private VBox formContainer;
    @FXML
    private Label errorLabel;
    @FXML
    private Button confirmButton;

    private ModificaPasswordModel modificaPasswordModel = new ModificaPasswordModel();

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        errorLabel.setVisible(false);
        confirmButton.setDisable(true);

        Form form = modificaPasswordModel.createForm();
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
    private void handleModificaPassword() {
        String plainPassword = modificaPasswordModel.getPassword();

        try {

            UtenteDao utenteDao = new UtenteDao(DBMSBoundary.getConnection());

            UtenteEntity utente = UserContext.getUtente();

            String hashedPassword = PasswordUtil.hashPassword(plainPassword);

            utente.setPassword(hashedPassword);
            utenteDao.update(utente);

        }catch(SQLException e){
            e.printStackTrace();
            errorLabel.setVisible(true);
            errorLabel.setText("Errore interno! Riprova tra poco!");
            confirmButton.setText("Conferma");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Modifica password");
        alert.setHeaderText(null);  // Nessun header

        String message = "Password cambiata correttamente";
        alert.setContentText(message);

        alert.getDialogPane().setPrefWidth(600);  // puoi aumentare o diminuire la larghezza
        alert.getDialogPane().setPrefHeight(100);  // puoi aumentare o diminuire la larghezza

        alert.showAndWait();

        mainControl.setView("/com/paperreview/paperreview/boundaries/home/homeBoundary.fxml");
    }

}
