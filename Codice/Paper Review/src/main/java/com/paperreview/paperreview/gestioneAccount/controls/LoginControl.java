package com.paperreview.paperreview.gestioneAccount.controls;

import com.paperreview.paperreview.gestioneAccount.forms.LoginFormModel;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.dao.UtenteDao;
import com.paperreview.paperreview.gestioneNotifiche.MainControl;
import com.paperreview.paperreview.entities.UtenteEntity;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.gestioneNotifiche.controls.NotificaPushControl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Border;

import java.sql.SQLException;


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

    }


    @FXML
    private void handleLogin() {
        String email = loginFormModel.getEmail();
        String password = loginFormModel.getPassword();

        try {
            UtenteDao dao = new UtenteDao(DBMSBoundary.getConnection());
            UtenteEntity utente = dao.login(email, password);

            if (utente == null) {
                errorLabel.setText("email o password errati");
                errorLabel.setVisible(true);
                return;
            }

            errorLabel.setVisible(false);
            System.out.println("Login riuscito: " + utente.getEmail());

            UserContext.login(utente);
            System.out.println("Login riuscito: " + utente.toString());

            mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneNotifiche/home/homeBoundary.fxml");

            NotificaPushControl.avviaNotifichePush();

        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Errore di sistema. Riprova più tardi.");
            errorLabel.setVisible(true);
        }

    }

    @FXML
    private void handleRegister() {
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneAccount/registrazioneBoundary/registrazioneBoundary.fxml");
    }

    @FXML
    private void handleRecover() {
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneAccount/recuperoPasswordBoundary/recuperoPasswordBoundary.fxml");
    }


}
