package com.paperreview.paperreview.gestioneAccount.controls;

import com.paperreview.paperreview.gestioneAccount.forms.RegistrazioneFormModel;
import com.paperreview.paperreview.common.DBMSBoundary;
import com.paperreview.paperreview.common.dao.UtenteDao;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.UtenteEntity;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Border;

import java.sql.Connection;
import java.sql.SQLException;


public class RegistrazioneControl implements ControlledScreen {

    @FXML
    private VBox formContainer;
    @FXML
    private Label errorLabel;
    @FXML
    private Button confirmButton;
    @FXML
    private ImageView logoImage;

    private RegistrazioneFormModel registrazioneFormModel = new RegistrazioneFormModel();

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

        Form form = registrazioneFormModel.createForm();
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
    private void handleRegistrazione() {
        String nome = registrazioneFormModel.getNome();
        String cognome = registrazioneFormModel.getCognome();
        String email = registrazioneFormModel.getEmail();
        String password = registrazioneFormModel.getPassword();

        try {
            // Ottieni la connessione
            Connection conn = DBMSBoundary.getConnection();

            // Crea il dao
            UtenteDao dao = new UtenteDao(conn);

            // Crea l'entità
            UtenteEntity utente = new UtenteEntity(0, nome, cognome, email, password);

            // Tenta la registrazioneBoundary
            boolean success = dao.saveIfNotExistsByEmail(utente);

            if (!success) {
                errorLabel.setText("email già registrata");
                errorLabel.setVisible(true);
                return;
            }

            mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneAccount/loginBoundary/loginBoundary.fxml");
        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Errore durante la registrazioneBoundary");
            errorLabel.setVisible(true);
            // TODO: gestire errore SQL
        }

    }

    @FXML
    private void handleBack() {
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneAccount/loginBoundary/loginBoundary.fxml");
    }
}
