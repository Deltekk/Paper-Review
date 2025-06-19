package com.paperreview.paperreview.gestioneRevisioni.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.InvitoDao;
import com.paperreview.paperreview.common.dbms.dao.RuoloConferenzaDao;
import com.paperreview.paperreview.common.dbms.dao.UtenteDao;
import com.paperreview.paperreview.common.email.EmailSender;
import com.paperreview.paperreview.common.email.MailInvito;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.gestioneNotifiche.MainControl;
import com.paperreview.paperreview.entities.*;
import com.paperreview.paperreview.gestioneRevisioni.forms.InvitaSottoRevisoreFormModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.SQLException;

public class InvitaSottoRevisoreControl implements ControlledScreen {

    @FXML
    private VBox formContainer;
    @FXML
    private Label errorLabel;
    @FXML
    private Button confirmButton;

    private InvitaSottoRevisoreFormModel invitaSottoRevisoreForm = new InvitaSottoRevisoreFormModel();

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        errorLabel.setVisible(false);
        confirmButton.setDisable(true);

        Form form = invitaSottoRevisoreForm.createForm();
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
        String email = invitaSottoRevisoreForm.getEmail().trim();
        errorLabel.setVisible(false);

        // 3 - Validazione email
        if (email.isEmpty()) {
            errorLabel.setText("Errore, bisogna compilare il campo dell’email!");
            errorLabel.setVisible(true);
            return;
        }

        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errorLabel.setText("Errore: l’email non è nel formato corretto! Es: nome@dominio.it");
            errorLabel.setVisible(true);
            return;
        }

        ConferenzaEntity conferenza = UserContext.getConferenzaAttuale();
        PaperEntity paper = UserContext.getPaperAttuale();
        UtenteEntity mittente = UserContext.getUtente();

        try {
            Connection conn = DBMSBoundary.getConnection();
            UtenteDao utenteDao = new UtenteDao(conn);
            InvitoDao invitoDao = new InvitoDao(conn);
            RuoloConferenzaDao ruoloDao = new RuoloConferenzaDao(conn);

            Integer idDestinatario = utenteDao.getIdByEmail(email);

            InvitoEntity invito;

            if (idDestinatario == null) {
                // Utente non registrato
                invito = InvitoEntity.creaInvitoConPaper(
                        email,
                        Ruolo.Sottorevisore,
                        conferenza.getId(),
                        mittente.getId(),
                        null,
                        paper.getId(),
                        conferenza.getScadenzaSottomissione()
                );
                invitoDao.save(invito);

                EmailSender.sendEmail(new MailInvito(
                        email,
                        Ruolo.Sottorevisore.name(),
                        conferenza.getNome(),
                        null,
                        invito.getCodice()
                ));

            } else {
                // Utente registrato
                if (ruoloDao.getByUtenteAndConferenza(idDestinatario, conferenza.getId()) != null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Errore");
                    alert.setHeaderText(email + " fa già parte della conferenza!");
                    alert.showAndWait();
                    return;
                }

                invito = InvitoEntity.creaInvitoConPaper(
                        email,
                        Ruolo.Sottorevisore,
                        conferenza.getId(),
                        mittente.getId(),
                        idDestinatario,
                        paper.getId(),
                        conferenza.getScadenzaSottomissione()
                );
                invitoDao.save(invito);

                UtenteEntity destinatario = utenteDao.getByEmail(email);

                EmailSender.sendEmail(new MailInvito(
                        email,
                        Ruolo.Sottorevisore.name(),
                        conferenza.getNome(),
                        destinatario.getNome() + " " + destinatario.getCognome(),
                        null
                ));
            }

            // Cambio schermata
            mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneRevisioni/visualizzaPapersRevisore/visualizzaPapersRevisoreBoundary.fxml");

        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Errore durante l'invio dell'invito.");
            errorLabel.setVisible(true);
        }
    }
}
