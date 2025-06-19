package com.paperreview.paperreview.gestioneConferenze.controls;

import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.*;
import com.paperreview.paperreview.common.email.EmailSender;
import com.paperreview.paperreview.common.email.MailSegnalazione;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.MainControl;
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
import org.kordamp.ikonli.javafx.FontIcon;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class VisualizzaDettagliPaperControl implements ControlledScreen {

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
        try (Connection connection = DBMSBoundary.getConnection()) {

            conferenceTitleLabel.setText(String.format("Conferenza: \"%s\"", UserContext.getConferenzaAttuale().getNome()));

            RevisioneDao revisioneDao = new RevisioneDao(connection);
            UtenteDao utenteDao = new UtenteDao(connection);
            PaperDao paperDao = new PaperDao(connection);
            CoAutoriPaperDao coAutoriDao = new CoAutoriPaperDao(connection);

            List<RevisioneEntity> revisioni = revisioneDao.getByConferenza(UserContext.getConferenzaAttuale().getId());

            if (revisioni.isEmpty()) {
                Label testo = new Label("Non ci sono revisioni disponibili per questa conferenza.");
                testo.getStyleClass().addAll("font-bold", "text-rosso", "h5");
                testo.setWrapText(true);
                testo.setPrefWidth(500);
                testo.setAlignment(Pos.CENTER);
                testo.setTextAlignment(TextAlignment.CENTER);
                paperContainer.getChildren().add(testo);
                return;
            }

            for (RevisioneEntity revisione : revisioni) {

                UtenteEntity revisore = utenteDao.getById(revisione.getRefUtente());
                PaperEntity paper = paperDao.getById(revisione.getRefPaper());

                if (paper == null) continue;

                UtenteEntity autore = utenteDao.getById(paper.getRefUtente());
                Set<String> coautori = coAutoriDao.getCoautoriForPaper(paper.getId());

                StringBuilder autori = new StringBuilder();
                autori.append(autore.getNome()).append(" ").append(autore.getCognome());
                for (String co : coautori) autori.append(", ").append(co);

                double averageScore = revisioneDao.getAverageScore(paper.getId());

                if(revisione.getTesto() != null)
                {
                    if(!revisione.getTesto().isBlank())
                    {
                        Node card = creaCardRevisione(revisione, revisore, autori.toString(), averageScore);
                        paperContainer.getChildren().add(card);
                    }
                }

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Node creaCardRevisione(RevisioneEntity revisione, UtenteEntity revisore, String autori, double averageScore) {
        VBox card = new VBox(20);
        card.getStyleClass().addAll("paper-card", "bg-celeste");
        card.setPadding(new Insets(25));
        card.setPrefWidth(1000);

        /* REVISIONE */

        Label titolo = new Label("Revisione");
        titolo.getStyleClass().addAll("font-bold", "text-bianco", "h4", "ombra");

        Label testo = new Label(revisione.getTesto());
        testo.getStyleClass().addAll("p", "text-bianco", "ombra", "text-bold");
        testo.setWrapText(true);

        /* COMMENTO PRIVATO */

        Label titoloPrivato = new Label("Commento privato");
        titoloPrivato.getStyleClass().addAll("font-bold", "text-bianco", "h5", "ombra");

        Label commento = new Label();

        if(revisione.getCommentoChair() != null) {
            commento.setText(revisione.getCommentoChair().isBlank() ? "Non è presente un commento privato." : revisione.getCommentoChair() );
        }

        commento.getStyleClass().addAll("p", "text-bianco", "ombra", "text-bold");
        commento.setWrapText(true);

        /* PUNTI DI FORZA */

        Label puntiDiForza = new Label("Punti di forza");
        puntiDiForza.getStyleClass().addAll("font-bold", "text-bianco", "h5", "ombra");

        Label puntiDiForzaLabel = new Label(revisione.getPuntiForza());
        puntiDiForzaLabel.getStyleClass().addAll("p", "text-bianco", "ombra", "text-bold");
        puntiDiForzaLabel.setWrapText(true);

        /* PUNTI DI DEBOLEZZA */

        Label puntiDiDebolezza = new Label("Punti di debolezza");
        puntiDiDebolezza.getStyleClass().addAll("font-bold", "text-bianco", "h5", "ombra");

        Label puntiDiDebolezzaLabel = new Label(revisione.getPuntiDebolezza());
        puntiDiDebolezzaLabel.getStyleClass().addAll("p", "text-bianco", "ombra", "text-bold");
        puntiDiDebolezzaLabel.setWrapText(true);

        /* REVISORE */

        FontIcon revisoreIcon = new FontIcon("fas-user-secret");
        revisoreIcon.setIconColor(Color.WHITE);
        revisoreIcon.setIconSize(24);
        revisoreIcon.getStyleClass().addAll("text-bianco", "ombra");

        Label revisoreLabel = new Label(revisore.getNome() + " " + revisore.getCognome());
        revisoreLabel.getStyleClass().addAll("font-bold", "text-bianco", "ombra");
        HBox autoreBox = new HBox(10, revisoreIcon, revisoreLabel);
        autoreBox.setAlignment(Pos.CENTER_LEFT);

        /* SCORE */

        HBox scoreBox = new HBox(10);
        FontIcon scoreIcon = new FontIcon("fas-star");
        scoreIcon.setIconSize(24);
        scoreIcon.setIconColor(Color.WHITE);
        scoreIcon.getStyleClass().setAll("text-bianco", "ombra");

        Integer maxScore = Integer.valueOf(UserContext.getConferenzaAttuale().getMetodoValutazione().getValoreDb());
        Label labelScore = new Label(String.format("%.2f/%.2f", averageScore, (float) maxScore));
        labelScore.getStyleClass().addAll("h6", "text-bianco", "ombra", "font-light");
        labelScore.setWrapText(true);
        scoreBox.setAlignment(Pos.CENTER_LEFT);
        scoreBox.getChildren().addAll(scoreIcon, labelScore);

        /* AUTORI */

        FontIcon autoriIcon = new FontIcon("fas-users");
        autoriIcon.setIconColor(Color.WHITE);
        autoriIcon.setIconSize(24);
        autoriIcon.getStyleClass().addAll("text-bianco", "ombra");

        Label autoriLabel = new Label(autori);
        autoriLabel.getStyleClass().addAll("font-bold", "text-bianco", "ombra");
        HBox autoriBox = new HBox(10, autoriIcon, autoriLabel);
        autoriBox.setAlignment(Pos.CENTER_LEFT);

        /* PULSANTI */

        Button btnConflitto = new Button("Segnala conflitto");
        btnConflitto.getStyleClass().addAll("red-button");
        btnConflitto.setOnAction(e -> handleConflitto(revisione));

        Button btnPlagio = new Button("Segnala plagio");
        btnPlagio.getStyleClass().addAll("red-button");
        btnPlagio.setOnAction(e -> handlePlagio(revisione));

        VBox leftBox = new VBox(10, autoriBox, autoreBox, scoreBox);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        VBox rightBox = new VBox(10, btnConflitto, btnPlagio);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox bottomRow = new HBox(30, leftBox, spacer, rightBox);
        bottomRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(leftBox, Priority.ALWAYS);

        card.getChildren().addAll(titolo, testo, titoloPrivato, commento, puntiDiForza, puntiDiForzaLabel, puntiDiDebolezza, puntiDiDebolezzaLabel, bottomRow);
        return card;
    }

    public void handleConflitto(RevisioneEntity revisione) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conflitto di Interesse");
        alert.setHeaderText("Vuoi davvero eliminare questa revisione per conflitto di interesse?");
        alert.setContentText("L'azione notificherà il revisore coinvolto e tutti gli altri Chair.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        int idChairAttuale = UserContext.getUtente().getId();

        try {
            // DAO
            Connection conn = DBMSBoundary.getConnection();
            RevisioneDao revisioneDao = new RevisioneDao(conn);
            PaperDao paperDao = new PaperDao(conn);
            NotificaDao notificaDao = new NotificaDao(conn);
            RuoloConferenzaDao ruoloDao = new RuoloConferenzaDao(conn);
            UtenteDao utenteDao = new UtenteDao(conn);
            ConferenzaDao conferenzaDao = new ConferenzaDao(conn);

            // Recupera info paper e conferenza
            PaperEntity paper = paperDao.getById(revisione.getRefPaper());
            if (paper == null) {
                System.err.println("Errore: paper non trovato");
                return;
            }

            int idPaper = paper.getId();
            int idConferenza = paper.getRefConferenza();
            ConferenzaEntity conferenza = conferenzaDao.getById(idConferenza);
            String nomePaper = paper.getTitolo();
            UtenteEntity utenteChair = UserContext.getUtente();

            // Recupera revisore
            UtenteEntity revisore = utenteDao.getById(revisione.getRefUtente());
            if (revisore == null) {
                System.err.println("Errore: revisore non trovato");
                return;
            }

            // Messaggio notifica completo
            String notifica = String.format(
                    "Il Chair %s %s ha eliminato la revisione del revisore %s %s per conflitto di interesse sul paper \"%s\" nella conferenza \"%s\".",
                    utenteChair.getNome(), utenteChair.getCognome(),
                    revisore.getNome(), revisore.getCognome(),
                    nomePaper, conferenza.getNome()
            );

            // Elimina revisione
            revisioneDao.removeById(revisione.getId());

            // Notifica altri Chair
            Set<Integer> idsChair = ruoloDao.getIdUtentiByRuoloAndConferenza(Ruolo.Chair, idConferenza);
            for (Integer idChair : idsChair) {
                if (idChair.equals(idChairAttuale)) continue;

                UtenteEntity altroChair = utenteDao.getById(idChair);
                notificaDao.save(new NotificaEntity(
                        0,
                        LocalDateTime.now(),
                        notifica,
                        false,
                        altroChair.getId(),
                        idConferenza
                ));

                MailSegnalazione mailChair = new MailSegnalazione(
                        altroChair.getEmail(),
                        utenteChair.getNome(),
                        utenteChair.getCognome(),
                        conferenza.getNome(),
                        "Conflitto di interesse (revisione eliminata)",
                        nomePaper
                );
                new EmailSender().sendEmail(mailChair);
            }

            // Notifica revisore stesso
            notificaDao.save(new NotificaEntity(
                    0,
                    LocalDateTime.now(),
                    notifica,
                    false,
                    revisore.getId(),
                    idConferenza
            ));

            MailSegnalazione mailRev = new MailSegnalazione(
                    revisore.getEmail(),
                    utenteChair.getNome(),
                    utenteChair.getCognome(),
                    conferenza.getNome(),
                    "Conflitto di interesse (revisione eliminata)",
                    nomePaper
            );
            new EmailSender().sendEmail(mailRev);

            // Aggiorna schermata
            mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/visualizzaPapersChair/visualizzaPapersChairBoundary.fxml");

        } catch (SQLException e) {
            e.printStackTrace();
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Errore");
            err.setHeaderText("Errore durante la gestione del conflitto");
            err.setContentText("Si è verificato un errore durante l'eliminazione della revisione.");
            err.showAndWait();
        }
    }

    public void handlePlagio(RevisioneEntity revisione) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Eliminazione Paper");
        alert.setHeaderText("Vuoi davvero eliminare questo paper per sospetto plagio?");
        alert.setContentText("L'azione notificherà tutti gli utenti coinvolti e non sarà reversibile.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return; // l'utente ha annullato
        }

        int idChairAttuale = UserContext.getUtente().getId();

        try {
            // DAO inizializzazioni
            Connection conn = DBMSBoundary.getConnection();
            PaperDao paperDao = new PaperDao(conn);
            NotificaDao notificaDao = new NotificaDao(conn);
            RuoloConferenzaDao ruoloDao = new RuoloConferenzaDao(conn);
            UtenteDao utenteDao = new UtenteDao(conn);
            RevisioneDao revisioneDao = new RevisioneDao(conn);
            ConferenzaDao conferenzaDao = new ConferenzaDao(conn);
            InvitoDao invitoDao = new InvitoDao(conn);
            TopicPaperDao topicPaperDao = new TopicPaperDao(conn);
            CoAutoriPaperDao coAutoriDao = new CoAutoriPaperDao(conn);

            // 1. Ottieni il paper associato alla revisione
            PaperEntity paper = paperDao.getById(revisione.getRefPaper());

            if (paper == null) {
                System.err.println("Errore: Paper non trovato per revisione " + revisione.getId());
                return;
            }

            int idConferenza = paper.getRefConferenza();
            ConferenzaEntity conferenza = conferenzaDao.getById(idConferenza);
            int idAutore = paper.getRefUtente();
            int idPaper = paper.getId();
            String nomePaper = paper.getTitolo();

            String notifica = String.format(
                    "Il Chair %s %s ha eliminato il paper \"%s\" per sospetto plagio nella conferenza \"%s\".",
                    UserContext.getUtente().getNome(), UserContext.getUtente().getCognome(), paper.getTitolo(), conferenza.getNome()
            );

            // 2. Elimina il paper
            invitoDao.removeByPaperId(paper.getId());
            revisioneDao.removeByPaperId(paper.getId());
            topicPaperDao.removeByPaperId(paper.getId());
            coAutoriDao.removeByPaperId(paper.getId());
            paperDao.removeById(idPaper);

            // 3. Notifica agli altri Chair
            Set<Integer> idsChair = ruoloDao.getIdUtentiByRuoloAndConferenza(Ruolo.Chair, idConferenza);
            for (Integer idChair : idsChair) {
                if(idChairAttuale == idChair) continue;

                UtenteEntity chair = utenteDao.getById(idChair);

                notificaDao.save(new NotificaEntity(
                        0,
                        LocalDateTime.now(),
                        notifica,
                        false, chair.getId(),
                        idConferenza
                ));

                MailSegnalazione mail = new MailSegnalazione(
                        chair.getEmail(),         // to
                        UserContext.getUtente().getNome(),              // nomeSegnalante
                        UserContext.getUtente().getCognome(),           // cognomeSegnalante
                        conferenza.getNome(),       // nomeConferenza
                        "Eliminato per plagio",        // motivo
                        nomePaper                // titoloPaper
                );

                EmailSender emailSender = new EmailSender();
                emailSender.sendEmail(mail);
            }

            // 4. Notifica al Revisore
            List<RevisioneEntity> revisore = revisioneDao.getByPaper(idPaper);
            for(RevisioneEntity rev : revisore) {
                notificaDao.save(new NotificaEntity(
                        0,
                        LocalDateTime.now(),
                        notifica,
                        false, rev.getId(),
                        idConferenza
                ));

                UtenteEntity utente = utenteDao.getById(rev.getRefUtente());

                MailSegnalazione mail = new MailSegnalazione(
                        utente.getEmail(),         // to
                        UserContext.getUtente().getNome(),              // nomeSegnalante
                        UserContext.getUtente().getCognome(),           // cognomeSegnalante
                        conferenza.getNome(),       // nomeConferenza
                        "Eliminato per plagio",        // motivo
                        nomePaper                // titoloPaper
                );

                EmailSender emailSender = new EmailSender();
                emailSender.sendEmail(mail);
            }

            // 5. Notifica all'autore
            notificaDao.save(new NotificaEntity(
                    0,
                    LocalDateTime.now(),
                    notifica,
                    false, idAutore,
                    idConferenza
            ));

            UtenteEntity utente = utenteDao.getById(idAutore);

            MailSegnalazione mail = new MailSegnalazione(
                    utente.getEmail(),         // to
                    UserContext.getUtente().getNome(),              // nomeSegnalante
                    UserContext.getUtente().getCognome(),           // cognomeSegnalante
                    conferenza.getNome(),       // nomeConferenza
                    "Eliminato per plagio",        // motivo
                    nomePaper                // titoloPaper
            );

            EmailSender emailSender = new EmailSender();
            emailSender.sendEmail(mail);

            // Aggiornamento Pagina
            mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/visualizzaPapersChair/visualizzaPapersChairBoundary.fxml");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore durante la gestione della segnalazione di plagio.");
        }
    }
}
