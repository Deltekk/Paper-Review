package com.paperreview.paperreview.gestioneConferenze.controls;

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
import com.paperreview.paperreview.MainControl;
import com.paperreview.paperreview.entities.ConferenzaEntity;
import com.paperreview.paperreview.entities.InvitoEntity;
import com.paperreview.paperreview.entities.Ruolo;
import com.paperreview.paperreview.gestioneConferenze.forms.InvitaChairFormModel;
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

public class InvitaChairControl implements ControlledScreen {

    @FXML
    private VBox formContainer;

    @FXML
    private Button addButton;

    @FXML
    private Button continueButton;

    @FXML
    private Label optionalLabel;

    @FXML
    private FlowPane chairContainer;

    @FXML
    private Label errorLabel;

    private Set<String> emails = new HashSet<>();


    private InvitaChairFormModel invitaChairForm = new InvitaChairFormModel();

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }


    public void initialize() {

        addButton.setDisable(true);
        errorLabel.setVisible(false);

        if(UserContext.isStandaloneInteraction())
            optionalLabel.setVisible(false);

        Form form = invitaChairForm.createForm();
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
        String nuovaEmail = invitaChairForm.getEmail().trim().toLowerCase();

        // Controllo se già esiste
        if (emails.contains(nuovaEmail) || nuovaEmail.equals(UserContext.getUtente().getEmail())) {
            errorLabel.setText("Errore: Hai già inserito quest’email!");
            errorLabel.setVisible(true);
            return;
        }

        // Aggiunta email
        emails.add(nuovaEmail);
        errorLabel.setVisible(false); // nascondi eventuale errore

        // Crea card visuale
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
            chairContainer.getChildren().remove(card);
            emails.remove(nuovaEmail);
        });

        card.getChildren().addAll(labelEmail, rimuoviBtn);
        chairContainer.getChildren().add(card);
    }


    @FXML
    private void handleContinueButton() {
        ConferenzaEntity conferenza = UserContext.getConferenzaAttuale();
        int idConferenza = conferenza.getId();
        int idMittente = UserContext.getUtente().getId();

        for (String email : emails) {
            try {
                InvitoDao invitoDao = new InvitoDao(DBMSBoundary.getConnection());
                RuoloConferenzaDao ruoloConferenzaDao = new RuoloConferenzaDao(DBMSBoundary.getConnection());
                UtenteDao utenteDao = new UtenteDao(DBMSBoundary.getConnection());

                Integer idDestinatario = utenteDao.getIdByEmail(email); // può essere null

                boolean giàPresente = idDestinatario != null &&
                        (ruoloConferenzaDao.getByRuoloUtenteAndConferenza(Ruolo.Chair,idDestinatario, idConferenza) != null);


                if (giàPresente) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Utente già presente");
                    alert.setHeaderText(null);
                    alert.setContentText("L'utente con email \"" + email + "\" fa già parte della conferenza!");
                    alert.showAndWait();
                    continue;
                }

                InvitoEntity invito = InvitoEntity.creaInvito(
                        email,
                        Ruolo.Chair,
                        idConferenza,
                        idMittente,
                        idDestinatario,
                        conferenza.getScadenzaSottomissione()
                );
                invitoDao.save(invito);

                System.out.println("Invito generato per: " + email);

                try {
                    String nomeConferenza = conferenza.getNome();
                    String ruolo = "Chair";

                    // Recupera nome utente se esiste
                    String nomeUtente = null;
                    if (idDestinatario != null) {
                        nomeUtente = utenteDao.getById(idDestinatario).getNome();
                    }

                    MailInvito mail = new MailInvito(
                            email,
                            ruolo,
                            nomeConferenza,
                            nomeUtente,             // null se non registrato
                            invito.getCodice()      // sempre incluso ma usato solo se utente non registrato
                    );

                    EmailSender.sendEmail(mail);
                    System.out.println("Invito mandato via email per: " + email);

                } catch (Exception ex) {
                    System.err.println("Errore durante l'invio dell'email a " + email);
                    ex.printStackTrace();
                }

                System.out.println("Invito mandato via email per: " + email);

            } catch (SQLException e) {
                e.printStackTrace(); // logga errori DB
            }
        }

        if(UserContext.isStandaloneInteraction()){
            mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/gestioneConferenze/gestioneConferenzeBoundary.fxml");
        }
        else{
            mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/invitaRevisori/invitaRevisoriBoundary.fxml");
        }
    }

}
