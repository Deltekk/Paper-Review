package com.paperreview.paperreview.gestioneAccount.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.gestioneAccount.forms.RecuperoPasswordFormModel;
import com.paperreview.paperreview.common.*;
import com.paperreview.paperreview.common.dbms.dao.UtenteDao;
import com.paperreview.paperreview.common.email.EmailSender;
import com.paperreview.paperreview.common.email.MailRecuperoAccount;
import com.paperreview.paperreview.MainControl;
import com.paperreview.paperreview.entities.UtenteEntity;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.sql.SQLException;


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

        try {

            UtenteDao utenteDao = new UtenteDao(DBMSBoundary.getConnection());

            UtenteEntity utente = utenteDao.getByEmail(email);

            if (utente != null){
                String passwordProvvisoria = PasswordUtil.generateRandomPassword();
                String hashedPassword = PasswordUtil.hashPassword(passwordProvvisoria);

                MailRecuperoAccount mailRecuperoAccount = new MailRecuperoAccount(email, utente.getNomeUtente(), passwordProvvisoria);
                EmailSender.sendEmail(mailRecuperoAccount);

                utente.setPassword(hashedPassword);
                utenteDao.update(utente);
            }

        }catch(SQLException e){
            e.printStackTrace();
            errorLabel.setVisible(true);
            errorLabel.setText("Errore interno! Riprova tra poco!");
            confirmButton.setText("Conferma");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Recupero password");
        alert.setHeaderText(null);  // Nessun header

        String message = "Se " + email + " appartiene ad un account memorizzato nel sistema, ti arriverà una email con la tua password temporanea.";
        alert.setContentText(message);

        alert.getDialogPane().setPrefWidth(600);  // puoi aumentare o diminuire la larghezza
        alert.getDialogPane().setPrefHeight(100);  // puoi aumentare o diminuire la larghezza

        alert.showAndWait();

        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneAccount/loginBoundary/loginBoundary.fxml");

    }

    @FXML
    private void handleBack() {
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneAccount/loginBoundary/loginBoundary.fxml");
    }
}
