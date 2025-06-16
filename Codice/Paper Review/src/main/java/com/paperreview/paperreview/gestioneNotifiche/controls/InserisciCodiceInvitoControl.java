package com.paperreview.paperreview.gestioneNotifiche.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.InvitoDao;
import com.paperreview.paperreview.common.dbms.dao.RuoloConferenzaDao;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.InvitoEntity;
import com.paperreview.paperreview.entities.Ruolo;
import com.paperreview.paperreview.entities.RuoloConferenzaEntity;
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

        // 5.2 - Verifica che il codice non sia vuoto
        if (codice.isEmpty()) {
            errorLabel.setText("Errore: bisogna compilare il campo del codice!");
            return;
        }

        // 5.3 - Verifica che rispetti il formato "123-456"
        if (!codice.matches("\\d{3}-\\d{3}")) {
            errorLabel.setText("Errore: il codice non è in un formato corretto!");
            return;
        }

        // 5.4 e 5.5 - Controllo nel DB tramite DAO
        try {
            InvitoDao invitoDao = new InvitoDao(DBMSBoundary.getConnection());
            RuoloConferenzaDao ruoloConferenzaDao= new RuoloConferenzaDao(DBMSBoundary.getConnection());
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
                    InvitoEntity invito = invitoDao.getInvitoByCodice(codice);
                    if(invito == null) throw  new RuntimeException("Errore: invito non trovato!");
                    Ruolo ruolo = Ruolo.fromString(invito.getRuolo());
                    int idUtente = UserContext.getUtente().getId();
                    RuoloConferenzaEntity ruoloConferenzaEntity = new RuoloConferenzaEntity(ruolo, idUtente, invito.getRefConferenza());
                    ruoloConferenzaDao.save(ruoloConferenzaEntity);
                    errorLabel.setText("INVITO ACCETTATO!");
                    // 8 - Rende il bottone "OK" cliccabile oppure naviga direttamente
                    mainControl.setView("/com/paperreview/paperreview/boundaries/home/homeBoundary.fxml");
                }
                default -> errorLabel.setText("Errore imprevisto durante l'elaborazione.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
