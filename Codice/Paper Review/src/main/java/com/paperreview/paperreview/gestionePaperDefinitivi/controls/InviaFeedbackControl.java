package com.paperreview.paperreview.gestionePaperDefinitivi.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.*;
import com.paperreview.paperreview.common.email.EmailSender;
import com.paperreview.paperreview.common.email.MailEditorFeedback;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.*;
import com.paperreview.paperreview.gestioneNotifiche.forms.CodiceInvitoForm;
import com.paperreview.paperreview.gestionePaperDefinitivi.forms.InviaFeedbackFormModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import java.time.LocalDateTime;

public class InviaFeedbackControl implements ControlledScreen {

    @FXML private VBox formContainer;
    @FXML private Label errorLabel, conferenzaLabel;
    @FXML private Button confirmButton;

    private InviaFeedbackFormModel inviaFeedbackForm = new InviaFeedbackFormModel();

    private MainControl mainControl;

    private ConferenzaEntity conferenza;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {

        conferenza = UserContext.getConferenzaAttuale();
        conferenzaLabel.setText(conferenza.getNome());
        conferenzaLabel.setVisible(true);

        errorLabel.setVisible(false);
        confirmButton.setDisable(true);

        Form form = inviaFeedbackForm.createForm();
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
        String feedback = inviaFeedbackForm.getFeedback().trim();
        final int NUMERO_CARATTERI_MASSIMO = 500;

        if (feedback.isEmpty()) {
            mostraErrore("Errore: i campi devono essere tutti compilati!");
            return;
        }

        if (feedback.length() > NUMERO_CARATTERI_MASSIMO) {
            mostraErrore("Errore: hai superato il limite di caratteri consentiti, non devi superare " + NUMERO_CARATTERI_MASSIMO + " caratteri!");
            return;
        }

        try {
            PaperEntity paper = UserContext.getPaperAttuale();
            if (paper == null) {
                mostraErrore("Errore: nessun paper selezionato.");
                return;
            }

            int idConferenza = paper.getRefConferenza();
            ConferenzaDao conferenzaDao = new ConferenzaDao(DBMSBoundary.getConnection());
            ConferenzaEntity conferenza = conferenzaDao.getById(idConferenza);

            if (conferenza == null) {
                mostraErrore("Errore: conferenza non trovata.");
                return;
            }

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime dataInizioFeedback = conferenza.getScadenzaSottomissione2(); // feedback dopo la seconda sottomissione
            LocalDateTime dataFineFeedback = conferenza.getScadenzaEditing();   // feedback pre editing

            if (now.isBefore(dataInizioFeedback)) {
                mostraErrore("Errore: non è ancora possibile inviare un feedback");
                return;
            }

            if (now.isAfter(dataFineFeedback)) {
                mostraErrore("Errore: non puoi più inviare un feedback");
                return;
            }

            // 5.6: ottieni autore
            int idAutore = paper.getRefUtente();
            UtenteDao utenteDao = new UtenteDao(DBMSBoundary.getConnection());
            UtenteEntity autore = utenteDao.getById(idAutore);

            if (autore == null) {
                mostraErrore("Errore: autore non trovato.");
                return;
            }

            // 5.7: notifica autore
            NotificaEntity notifica = new NotificaEntity(
                    0,
                    LocalDateTime.now(),
                    "Hai ricevuto un feedback sul tuo paper: \"" + paper.getTitolo() + "\".\nTesto: " + feedback,
                    false,
                    autore.getId(),
                    idConferenza
            );

            NotificaDao notificaDao = new NotificaDao(DBMSBoundary.getConnection());
            notificaDao.save(notifica);

            // 5.8: invia email all’autore
            MailEditorFeedback mail = new MailEditorFeedback(
                    autore.getEmail(),
                    autore.getNome(),                // oppure autore.getNome() + " " + autore.getCognome()
                    paper.getTitolo(),
                    conferenza.getNome(),
                    feedback
            );
            EmailSender.sendEmail(mail);

            // 5.9: mostra popup conferma
            mostraConferma("Feedback inviato con successo!", () -> {
                // 5.10: torna a schermata Visualizza Papers
                mainControl.setView("/com/paperreview/paperreview/boundaries/gestionePaperDefinitivi/visualizzaPapersEditore/visualizzaPapersEditoreBoundary.fxml");
            });

        } catch (Exception e) {
            e.printStackTrace();
            mostraErrore("Errore interno durante l'invio del feedback.");
        }
    }

    private void mostraErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    private void mostraConferma(String messaggio, Runnable azioneDopoOk) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Conferma");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
        azioneDopoOk.run();
    }
}
