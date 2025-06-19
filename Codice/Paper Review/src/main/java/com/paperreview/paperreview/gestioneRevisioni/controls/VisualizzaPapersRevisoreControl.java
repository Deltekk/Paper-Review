package com.paperreview.paperreview.gestioneRevisioni.controls;

import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.*;
import com.paperreview.paperreview.common.email.EmailSender;
import com.paperreview.paperreview.common.email.MailSegnalazione;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class VisualizzaPapersRevisoreControl implements ControlledScreen {

    @FXML
    private VBox paperContainer;

    @FXML
    private Label conferenceTitleLabel;

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        try(Connection connection = DBMSBoundary.getConnection()){

            conferenceTitleLabel.setText( String.format("Conferenza: \"%s\"", UserContext.getConferenzaAttuale().getNome()));

            PaperDao paperDao = new PaperDao(connection);

            // Prendiamo i paper che ha in revisione l'utente corrente
            List<PaperEntity> papersRevisore = paperDao.getPapersByRevisoreAndConferenza(UserContext.getUtente().getId(), UserContext.getConferenzaAttuale().getId());

            if(papersRevisore.isEmpty())
            {
                Label testo = new Label("Non è stato pubblicato ancora nessun paper!");
                testo.getStyleClass().addAll("font-bold", "text-rosso", "h5");
                testo.setWrapText(true);
                testo.setPrefWidth(500);
                testo.setAlignment(Pos.CENTER);
                testo.setTextAlignment(TextAlignment.CENTER);

                paperContainer.getChildren().add(testo);
            }
            else
            {
                for (PaperEntity paper : papersRevisore) {
                    Node card = creaPaperCard(paper);
                    paperContainer.getChildren().add(card);
                }
            }
        } catch (Exception e) {
            // TODO: Gestire questo errore
            throw new RuntimeException(e);
        }

    }

    private Node creaPaperCard(PaperEntity paper) throws SQLException {
        HBox card = new HBox(20); // Spazio tra gli elementi
        card.getStyleClass().addAll("paper-card", "bg-celeste");
        card.setPrefWidth(1000);
        card.setPrefHeight(400);
        card.setAlignment(Pos.CENTER);

        /* TESTO A SINISTRA */

        VBox textContainer = new VBox(10);
        textContainer.setAlignment(Pos.CENTER_LEFT);
        textContainer.setPrefWidth(700);
        textContainer.getStyleClass().addAll("text-container");

        // Nome paper

        Label labelNomeConferenza = new Label(String.format("\"%s\"", paper.getTitolo()));
        labelNomeConferenza.getStyleClass().addAll("font-bold", "text-bianco", "h5", "ombra");
        labelNomeConferenza.setWrapText(true);
        labelNomeConferenza.setPrefWidth(700);

        // -----  Autori paper ------

        HBox autoriBox = new HBox(10);

        // Autori icon

        FontIcon autoriIcon = new FontIcon("fas-users");
        autoriIcon.setIconSize(24);
        autoriIcon.setIconColor(Color.WHITE);
        autoriIcon.getStyleClass().add("ombra");

        // Prendiamo l'autore principale

        UtenteDao utenteDao = new UtenteDao(DBMSBoundary.getConnection());
        UtenteEntity autore = utenteDao.getById(paper.getRefUtente());

        // Prendiamo i coautori

        CoAutoriPaperDao coAutoriPaperDao = new CoAutoriPaperDao(DBMSBoundary.getConnection());
        Set<String> coautori = coAutoriPaperDao.getCoautoriForPaper(paper.getId());

        String testoLabelAutori = String.format("%s %s, ", autore.getNome(), autore.getCognome());

        int i = 0;
        for (String coautore : coautori) {
            if (i > 0) {
                testoLabelAutori += ", ";  // Aggiungi la virgola solo se non è il primo coautore
            }
            testoLabelAutori += coautore;
            i++;
        }

        // Label autori

        Label labelAutori = new Label(testoLabelAutori);
        labelAutori.getStyleClass().addAll("font-bold", "text-bianco", "h6", "ombra");
        labelAutori.setWrapText(true);
        labelAutori.setPrefWidth(700);

        // Layout autori

        autoriBox.getChildren().addAll(autoriIcon, labelAutori);

        textContainer.getChildren().setAll(labelNomeConferenza, autoriBox);

        /* SPACES (FLESSIBILI) */

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        /* BOTTONI A DESTRA */

        GridPane buttonGrid = new GridPane();
        buttonGrid.setVgap(10);
        buttonGrid.setHgap(10);
        buttonGrid.setPadding(new Insets(25));
        buttonGrid.setAlignment(Pos.CENTER);
        buttonGrid.setMaxWidth(500);

        Button buttonRevisionaPaper= new Button("Revisiona Paper");
        Button buttonModificaRevisione = new Button("Modifica Revisione");
        Button buttonScaricaPaper = new Button("Scarica Paper");
        Button buttonInvitaSottorevisore = new Button("Invita Sottorevisore");
        Button buttonVisualizzaRevisioni = new Button("Rimuovi Visualizza Revisioni");
        Button buttonSegnalaConflitto = new Button("Segnala Conflitto");
        Button buttonSegnalaPlagio = new Button("Segnala Plagio");

        buttonRevisionaPaper.getStyleClass().addAll("green-button", "grid-button-single");
        buttonModificaRevisione.getStyleClass().addAll("green-button", "grid-button-single");
        buttonScaricaPaper.getStyleClass().addAll("blue-button", "grid-button-double");
        buttonInvitaSottorevisore.getStyleClass().addAll("blue-button", "grid-button-single");
        buttonVisualizzaRevisioni.getStyleClass().addAll("blue-button", "grid-button-single");
        buttonSegnalaConflitto.getStyleClass().addAll("red-button", "grid-button-single");
        buttonSegnalaPlagio.getStyleClass().addAll("red-button", "grid-button-single");

        buttonRevisionaPaper.setPrefWidth(Double.MAX_VALUE);
        buttonModificaRevisione.setPrefWidth(Double.MAX_VALUE);
        buttonScaricaPaper.setPrefWidth(Double.MAX_VALUE);
        buttonInvitaSottorevisore.setPrefWidth(Double.MAX_VALUE);
        buttonVisualizzaRevisioni.setPrefWidth(Double.MAX_VALUE);
        buttonSegnalaConflitto.setPrefWidth(Double.MAX_VALUE);
        buttonSegnalaPlagio.setPrefWidth(Double.MAX_VALUE);

        buttonRevisionaPaper.setOnAction(event -> handleRevisionaPaper(paper));
        buttonModificaRevisione.setOnAction(event -> handleModificaRevisione(paper));
        buttonScaricaPaper.setOnAction(event -> handleScaricaPaper(paper));
        buttonInvitaSottorevisore.setOnAction(event -> handleInvitaSottoRevisore(paper));
        buttonVisualizzaRevisioni.setOnAction(event -> handleVisualizzaRevisioni(paper));
        buttonSegnalaConflitto.setOnAction(event -> handleSegnalaConflitto(paper));
        buttonSegnalaPlagio.setOnAction(event -> handleSegnalaPlagio(paper));

        buttonGrid.add(buttonRevisionaPaper, 0, 0);
        buttonGrid.add(buttonModificaRevisione, 1, 0);
        buttonGrid.add(buttonScaricaPaper, 0, 1);
        buttonGrid.add(buttonInvitaSottorevisore, 1, 1);
        buttonGrid.add(buttonVisualizzaRevisioni, 0, 2, 2, 1);

        /* COMPOSIZIONE: [Testo] [Spacer] [Bottoni] */

        card.getChildren().addAll(textContainer, spacer, buttonGrid);

        return card;
    }

    public void handleRevisionaPaper(PaperEntity paper) {
        System.out.println("Revisiona paper");
    }

    public void handleModificaRevisione(PaperEntity paper){
        System.out.println("Modifica Revisione");
    }

    public void handleScaricaPaper(PaperEntity paper) {
        try {
            // Recupera il file BLOB dal PaperEntity
            byte[] fileData = paper.getFile();
            String nomeFile = paper.getTitolo().replaceAll("[^a-zA-Z0-9-_\\.]", "_") + ".pdf"; // Fallback se non hai il nome originale

            // Mostra dialogo di salvataggio
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salva il paper");
            fileChooser.setInitialFileName(nomeFile);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            File fileToSave = fileChooser.showSaveDialog(paperContainer.getScene().getWindow());

            if (fileToSave != null) {
                try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                    fos.write(fileData);
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successo");
                alert.setHeaderText(null);
                alert.setContentText("Il paper è stato scaricato con successo.");
                alert.showAndWait();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Errore");
            errorAlert.setHeaderText("Errore durante il download del paper");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }

    public void handleInvitaSottoRevisore(PaperEntity paper) {
        ConferenzaEntity conferenza = UserContext.getConferenzaAttuale();

        // ✅ Controlla se la data odierna è minore della scadenza revisione
        if (LocalDateTime.now().isAfter(conferenza.getScadenzaRevisione())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Operazione non consentita");
            alert.setHeaderText("Scadenza revisione superata");
            alert.setContentText("Non è più possibile invitare un sotto-revisore perché la scadenza per le revisioni è già passata.");
            alert.showAndWait();
            return;
        }

        // ✅ Salva il paper e apri schermata invito
        UserContext.setPaperAttuale(paper);
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneRevisioni/invitaSottoRevisore/invitaSottoRevisoreBoundary.fxml");
    }


    public void handleVisualizzaRevisioni(PaperEntity paper) {
        try {
            RevisioneDao revisioneDao = new RevisioneDao(DBMSBoundary.getConnection());
            int idUtente = UserContext.getUtente().getId();

            // Ottieni tutte le revisioni per quel paper
            List<RevisioneEntity> revisioni = revisioneDao.getByPaper(paper.getId());

            boolean haSottomesso = revisioni.stream().anyMatch(rev ->
                    ((rev.getRefUtente() == idUtente ||
                            (rev.getRefSottorevisore() != null && rev.getRefSottorevisore() == idUtente))
                            && rev.getDataSottomissione() != null)
            );

            if (!haSottomesso) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Accesso negato");
                alert.setHeaderText("Revisione non ancora sottomessa");
                alert.setContentText("Puoi visualizzare le altre revisioni solo dopo aver pubblicato la tua.");
                alert.showAndWait();
                return;
            }

            // OK: salva paper e apri schermata
            UserContext.setPaperAttuale(paper);
            mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneRevisioni/visualizzaRevisioni/visualizzaRevisioniBoundary.fxml");

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Errore nel caricamento delle revisioni");
            alert.setContentText("Si è verificato un errore durante il controllo delle revisioni.");
            alert.showAndWait();
        }
    }

    public void handleSegnalaConflitto(PaperEntity paper) {
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
        conferma.setTitle("Conflitto di interesse");
        conferma.setHeaderText("Sei sicuro di voler segnalare un conflitto di interesse?");
        conferma.setContentText("Segnalare un conflitto impedisce di continuare la revisione.");

        Optional<ButtonType> result = conferma.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Connection conn = DBMSBoundary.getConnection();

                NotificaDao notificaDao = new NotificaDao(conn);
                RuoloConferenzaDao ruoloDao = new RuoloConferenzaDao(conn);
                RevisioneDao revisioneDao = new RevisioneDao(conn);
                ConferenzaDao conferenzaDao = new ConferenzaDao(conn);
                UtenteDao utenteDao = new UtenteDao(conn);

                UtenteEntity revisore = UserContext.getUtente();
                int idConferenza = paper.getRefConferenza();
                int idPaper = paper.getId();
                int idUtente = revisore.getId();

                ConferenzaEntity conferenza = conferenzaDao.getById(idConferenza);

                List<RuoloConferenzaEntity> ruoli = ruoloDao.getByConferenza(idConferenza);
                List<Integer> idChair = ruoli.stream()
                        .filter(r -> r.getRuolo() == Ruolo.Chair)
                        .map(RuoloConferenzaEntity::getRefUtente)
                        .toList();

                String testoNotifica = String.format(
                        "⚠️ Il revisore %s %s ha segnalato un conflitto di interesse per l’articolo \"%s\" (ID %d) nella conferenza %s.",
                        revisore.getNome(), revisore.getCognome(), paper.getTitolo(), idPaper, conferenza.getNome()
                );

                for (Integer idDestinatario : idChair) {
                    NotificaEntity notifica = new NotificaEntity(
                            0,
                            LocalDateTime.now(),       // data
                            testoNotifica,             // testo
                            false,                     // isLetta
                            idDestinatario,            // refUtente
                            idConferenza               // refConferenza
                    );
                    notificaDao.save(notifica);

                    UtenteEntity chair = utenteDao.getById(idDestinatario);
                    MailSegnalazione mail = new MailSegnalazione(
                            chair.getEmail(),         // to
                            revisore.getNome(),              // nomeSegnalante
                            revisore.getCognome(),           // cognomeSegnalante
                            conferenza.getNome(),       // nomeConferenza
                            "Conflitto di interesse",        // motivo
                            paper.getTitolo()                // titoloPaper
                    );

                    EmailSender emailSender = new EmailSender();
                    emailSender.sendEmail(mail);
                }

                // Trova ed elimina la revisione del revisore (o del sottorevisore)
                List<RevisioneEntity> revisioni = revisioneDao.getByPaper(idPaper);
                for (RevisioneEntity revisione : revisioni) {
                    if ((revisione.getRefUtente() == idUtente) ||
                            (revisione.getRefSottorevisore() != null && revisione.getRefSottorevisore() == idUtente)) {
                        revisioneDao.removeById(revisione.getId());  // rimuove la revisione dal DB
                        break;
                    }
                }

                Alert successo = new Alert(Alert.AlertType.INFORMATION);
                successo.setTitle("Conflitto segnalato");
                successo.setHeaderText("Segnalazione inviata con successo!");
                successo.setContentText("Il chair verrà informato tramite una notifica.");
                successo.showAndWait();

                mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneRevisioni/visualizzaPapersRevisore/visualizzaPapersRevisoreBoundary.fxml");

            } catch (SQLException e) {
                e.printStackTrace();
                Alert errore = new Alert(Alert.AlertType.ERROR);
                errore.setTitle("Errore");
                errore.setHeaderText("Errore durante l'invio della notifica.");
                errore.setContentText("Contattare l’amministratore.");
                errore.showAndWait();
            }
        }
    }

    public void handleSegnalaPlagio(PaperEntity paper) {
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
        conferma.setTitle("Conflitto di interesse");
        conferma.setHeaderText("Sei sicuro di voler segnalare un conflitto di interesse?");
        conferma.setContentText("Segnalare un conflitto impedisce di continuare la revisione.");

        Optional<ButtonType> result = conferma.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Connection conn = DBMSBoundary.getConnection();

                NotificaDao notificaDao = new NotificaDao(conn);
                RuoloConferenzaDao ruoloDao = new RuoloConferenzaDao(conn);
                RevisioneDao revisioneDao = new RevisioneDao(conn);
                ConferenzaDao conferenzaDao = new ConferenzaDao(conn);
                UtenteDao utenteDao = new UtenteDao(conn);

                UtenteEntity revisore = UserContext.getUtente();
                int idConferenza = paper.getRefConferenza();
                int idPaper = paper.getId();
                int idUtente = revisore.getId();

                ConferenzaEntity conferenza = conferenzaDao.getById(idConferenza);

                // 1. Trova la revisione del revisore (o del suo ruolo)
                List<RevisioneEntity> revisioni = revisioneDao.getByPaper(idPaper);
                for (RevisioneEntity revisione : revisioni) {
                    if ((revisione.getRefUtente() == idUtente) ||
                            (revisione.getRefSottorevisore() != null && revisione.getRefSottorevisore() == idUtente)) {
                        revisione.setCommentoChair("Conflitto di interesse");
                        revisioneDao.update(revisione);
                        break;
                    }
                }

                List<RuoloConferenzaEntity> ruoli = ruoloDao.getByConferenza(idConferenza);
                List<Integer> idChair = ruoli.stream()
                        .filter(r -> r.getRuolo() == Ruolo.Chair)
                        .map(RuoloConferenzaEntity::getRefUtente)
                        .toList();

                String testoNotifica = String.format(
                        "⚠️ Il revisore %s %s ha segnalato un plagio per l’articolo \"%s\" (ID %d) nella conferenza %s.",
                        revisore.getNome(), revisore.getCognome(), paper.getTitolo(), idPaper, conferenza.getNome()
                );

                for (Integer idDestinatario : idChair) {
                    NotificaEntity notifica = new NotificaEntity(
                            0,
                            LocalDateTime.now(),       // data
                            testoNotifica,             // testo
                            false,                     // isLetta
                            idDestinatario,            // refUtente
                            idConferenza               // refConferenza
                    );
                    notificaDao.save(notifica);

                    UtenteEntity chair = utenteDao.getById(idDestinatario);
                    MailSegnalazione mail = new MailSegnalazione(
                            chair.getEmail(),         // to
                            revisore.getNome(),              // nomeSegnalante
                            revisore.getCognome(),           // cognomeSegnalante
                            conferenza.getNome(),       // nomeConferenza
                            "Plagio",        // motivo
                            paper.getTitolo()                // titoloPaper
                    );

                    EmailSender emailSender = new EmailSender();
                    emailSender.sendEmail(mail);
                }

                Alert successo = new Alert(Alert.AlertType.INFORMATION);
                successo.setTitle("Conflitto segnalato");
                successo.setHeaderText("Segnalazione inviata con successo!");
                successo.setContentText("Il chair verrà informato tramite una notifica.");
                successo.showAndWait();

                mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneRevisioni/visualizzaPapersRevisore/visualizzaPapersRevisoreBoundary.fxml");

            } catch (SQLException e) {
                e.printStackTrace();
                Alert errore = new Alert(Alert.AlertType.ERROR);
                errore.setTitle("Errore");
                errore.setHeaderText("Errore durante l'invio della notifica.");
                errore.setContentText("Contattare l’amministratore.");
                errore.showAndWait();
            }
        }
    }
}