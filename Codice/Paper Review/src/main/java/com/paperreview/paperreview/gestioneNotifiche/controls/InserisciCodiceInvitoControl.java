package com.paperreview.paperreview.gestioneNotifiche.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.InvitoDao;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.StatusInvito;
import com.paperreview.paperreview.gestioneNotifiche.forms.CodiceInvitoForm;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class InserisciCodiceInvitoControl implements ControlledScreen {

    @FXML
    private VBox formContainer;
    @FXML
    private Label errorLabel;
    @FXML
    private Button confirmButton;

    private CodiceInvitoForm codiceInvitoForm = new CodiceInvitoForm();

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        errorLabel.setVisible(false);
        confirmButton.setDisable(true);

        Form form = codiceInvitoForm.createForm();
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
    public void handleConferma() {
        // 5.1 - Ottieni il codice inserito nel form
        String codice = codiceInvitoForm.getCodiceInvito().trim();

        // 5.1.1 - Campo vuoto
        if (codice.isEmpty()) {
            errorLabel.setText("Errore: bisogna compilare il campo del codice!");
            return;
        }

        // 5.2.1 - Formato non corretto (esempio: 10 caratteri alfanumerici)
        if (!codice.matches("^[A-Za-z0-9]{10}$")) {
            errorLabel.setText("Errore: il codice non è in un formato corretto!");
            return;
        }

        // 5.3 - Data corrente acquisita implicitamente nel DAO
        // 5.4 e 5.5 - Controllo nel DB tramite DAO
        try {
            InvitoDao invitoDao = new InvitoDao(DBMSBoundary.getConnection());
            String result = invitoDao.acceptOrRejectInvito(codice, true); // true → accetta

            // 5.6 - Controllo risultato
            switch (result) {
                case "Inesistente", "Scaduto" -> {
                    errorLabel.setText("Errore: il codice non esiste oppure è scaduto!");
                    return;
                }
                case "Accettato" -> {
                    // 6 - Stato modificato dal DAO
                    // 7 - Mostra messaggio
                    errorLabel.setText("INVITO ACCETTATO!");
                    // 8 - Rende il bottone "OK" cliccabile oppure naviga direttamente
                    confirmButton.setText("OK");
                    confirmButton.setOnAction(e -> mainControl.setView("/com/paperreview/paperreview/boundaries/home/homeBoundary.fxml"));
                }
                default -> errorLabel.setText("Errore imprevisto durante l'elaborazione.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
