package com.paperreview.paperreview.gestioneConferenze.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.InvitoDao;
import com.paperreview.paperreview.common.dbms.dao.NotificaDao;
import com.paperreview.paperreview.common.dbms.dao.RuoloConferenzaDao;
import com.paperreview.paperreview.common.dbms.dao.UtenteDao;
import com.paperreview.paperreview.common.email.EmailSender;
import com.paperreview.paperreview.common.email.MailEditorInvito;
import com.paperreview.paperreview.common.email.MailInvito;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.*;
import com.paperreview.paperreview.gestioneConferenze.forms.InvitaEditoriFormModel;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class InvitaEditoriControl implements ControlledScreen {

    @FXML
    private VBox formContainer;

    @FXML
    private Button addButton;

    @FXML
    private Button continueButton;

    @FXML
    private FlowPane editorContainer;

    @FXML
    private Label errorLabel;

    private Set<String> emails = new HashSet<>();

    private InvitaEditoriFormModel invitaEditoriFormModel = new InvitaEditoriFormModel();

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {

        addButton.setDisable(true);
        errorLabel.setVisible(false);

        Form form = invitaEditoriFormModel.createForm();
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
            addButton.setDisable(!newVal);
        });

    }

    @FXML
    private void handleAddButton() {
        String nuovaEmail = invitaEditoriFormModel.getEmail().trim().toLowerCase();

        // Controllo email vuota
        if (nuovaEmail.isEmpty()) {
            errorLabel.setText("Errore: Inserisci un'email valida!");
            errorLabel.setVisible(true);
            return;
        }

        // Controllo limite di un solo editore
        if (emails.size() >= 1) {
            errorLabel.setText("Errore: Puoi inserire un solo editore! Rimuovi prima quello già presente.");
            errorLabel.setVisible(true);
            return;
        }

        // Aggiunta dell'email
        emails.add(nuovaEmail);
        errorLabel.setVisible(false); // Nasconde eventuali errori precedenti

        // Creazione della card visuale
        VBox card = new VBox();
        card.getStyleClass().addAll("chair-card", "bg-celeste");
        card.setSpacing(5);
        card.setAlignment(Pos.CENTER);
        card.setMinHeight(150);
        card.setMinWidth(350);

        Label labelEmail = new Label(nuovaEmail);
        labelEmail.getStyleClass().addAll("p", "bold", "text-bianco", "ombra");
        labelEmail.setWrapText(true);
        labelEmail.setAlignment(Pos.CENTER);
        labelEmail.setTextAlignment(TextAlignment.CENTER);

        Button rimuoviBtn = new Button("Rimuovi");
        rimuoviBtn.getStyleClass().add("red-button");

        rimuoviBtn.setOnAction(e -> {
            editorContainer.getChildren().remove(card);
            emails.remove(nuovaEmail);
        });

        card.getChildren().addAll(labelEmail, rimuoviBtn);
        editorContainer.getChildren().add(card);
    }


    @FXML
    private void handleContinueButton() {
        errorLabel.setVisible(false);

        if (emails.isEmpty()) {
            errorLabel.setText("Errore: Devi ancora inserire un editore!");
            errorLabel.setVisible(true);
            return;
        }

        ConferenzaEntity conferenza = UserContext.getConferenzaAttuale();
        int idConferenza = conferenza.getId();
        int idMittente = UserContext.getUtente().getId();

        try {
            RuoloConferenzaDao ruoloConferenzaDao = new RuoloConferenzaDao(DBMSBoundary.getConnection());
            UtenteDao utenteDao = new UtenteDao(DBMSBoundary.getConnection());

            for (String email : emails) {
                Integer idDestinatario = utenteDao.getIdByEmail(email);

                if (idDestinatario == null) {
                    // Se l'utente non è registrato, mostra errore (oppure crea flusso alternativo, se previsto)
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Utente non registrato");
                    alert.setHeaderText(null);
                    alert.setContentText("L'utente con email \"" + email + "\" non è registrato.");
                    alert.showAndWait();
                    continue;
                }

                boolean giàPresente = !ruoloConferenzaDao.getByUtenteAndConferenza(idDestinatario, idConferenza).isEmpty();

                if (giàPresente) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Utente già presente");
                    alert.setHeaderText(null);
                    alert.setContentText("L'utente con email \"" + email + "\" fa già parte della conferenza!");
                    alert.showAndWait();
                    continue;
                }

                RuoloConferenzaEntity ruolo = new RuoloConferenzaEntity(0, Ruolo.Editor, idDestinatario, idConferenza);
                ruoloConferenzaDao.save(ruolo);

                // Invio email informativa
                try {
                    String nomeUtente = utenteDao.getById(idDestinatario).getNome();
                    MailEditorInvito mail = new MailEditorInvito(
                            email,
                            conferenza.getNome(),
                            nomeUtente
                    );
                    EmailSender.sendEmail(mail);
                    System.out.println("Email informativa inviata a: " + email);
                } catch (Exception ex) {
                    System.err.println("Errore durante l'invio dell'email a " + email);
                    ex.printStackTrace();
                }
            }

            // Schermata finale
            mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/gestioneConferenze/gestioneConferenzeBoundary.fxml");

        } catch (SQLException e) {
            errorLabel.setText("Errore durante l'aggiunta degli editori.");
            errorLabel.setVisible(true);
            e.printStackTrace();
        }
    }
}
