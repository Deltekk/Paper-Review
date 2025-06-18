package com.paperreview.paperreview.gestioneRevisioni.controls;

import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.*;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import org.kordamp.ikonli.javafx.FontIcon;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PrenotazioneBroadcastControl implements ControlledScreen {

    @FXML private VBox paperContainer;
    @FXML private Label conferenceTitleLabel;

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        try (Connection connection = DBMSBoundary.getConnection()) {

            conferenceTitleLabel.setText(String.format("Conferenza: \"%s\"", UserContext.getConferenzaAttuale().getNome()));

            PaperDao paperDao = new PaperDao(connection);
            List<PaperEntity> papersRevisore = paperDao.getPapersByRevisoreAndConferenza(
                    UserContext.getUtente().getId(),
                    UserContext.getConferenzaAttuale().getId()
            );

            TopicPaperDao topicPaperDao = new TopicPaperDao(connection);

            if (papersRevisore.isEmpty()) {
                Label testo = new Label("Non è stato pubblicato ancora nessun paper!");
                testo.getStyleClass().addAll("font-bold", "text-rosso", "h5");
                testo.setWrapText(true);
                testo.setPrefWidth(500);
                testo.setAlignment(Pos.CENTER);
                testo.setTextAlignment(TextAlignment.CENTER);
                paperContainer.getChildren().add(testo);
            } else {
                for (PaperEntity paper : papersRevisore) {
                    Node card = creaPaperCard(paper, topicPaperDao.getTopicsForPaper(paper.getId()));
                    paperContainer.getChildren().add(card);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Node creaPaperCard(PaperEntity paper, Set<TopicEntity> topics) throws SQLException {
        VBox card = new VBox(20);
        card.getStyleClass().addAll("paper-card", "bg-celeste");
        card.setPrefWidth(1000);
        card.setPadding(new Insets(25));
        card.setAlignment(Pos.TOP_LEFT);

        Label titoloLabel = new Label(paper.getTitolo());
        titoloLabel.getStyleClass().addAll("font-bold", "text-bianco", "h4", "ombra");
        titoloLabel.setWrapText(true);

        Label abstractLabel = new Label(paper.getAbstractPaper());
        abstractLabel.getStyleClass().addAll("p", "text-bianco");
        abstractLabel.setWrapText(true);

        VBox abstractBox = new VBox(10, abstractLabel);
        abstractBox.setPrefWidth(900);
        abstractBox.setMaxWidth(900);

        UtenteDao utenteDao = new UtenteDao(DBMSBoundary.getConnection());
        UtenteEntity autore = utenteDao.getById(paper.getRefUtente());

        CoAutoriPaperDao coAutoriDao = new CoAutoriPaperDao(DBMSBoundary.getConnection());
        Set<String> coautori = coAutoriDao.getCoautoriForPaper(paper.getId());

        StringBuilder sb = new StringBuilder();
        sb.append(autore.getNome()).append(" ").append(autore.getCognome());
        for (String co : coautori) sb.append(", ").append(co);

        FontIcon authorIcon = new FontIcon("fas-users");
        authorIcon.setIconColor(Color.WHITE);
        authorIcon.setIconSize(20);

        Label autoriLabel = new Label(sb.toString());
        autoriLabel.getStyleClass().addAll("font-bold", "text-bianco", "ombra");
        autoriLabel.setWrapText(true);

        HBox autoriBox = new HBox(10, authorIcon, autoriLabel);
        autoriBox.setAlignment(Pos.CENTER_LEFT);

        Label topicLabel = new Label(
                "Topic: " + topics.stream()
                        .map(TopicEntity::getNome)
                        .collect(Collectors.joining(", "))
        );

        topicLabel.getStyleClass().addAll("font-bold", "text-bianco", "ombra");
        topicLabel.setWrapText(true);

        HBox bottomRow = new HBox(30);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        FontIcon tagIcon = new FontIcon("fas-tags");
        tagIcon.setIconColor(Color.WHITE);
        tagIcon.setIconSize(20);

        HBox topicBox = new HBox(10, tagIcon, topicLabel);
        topicBox.setAlignment(Pos.CENTER_LEFT);

        Button prenotaBtn = new Button("Prenota Paper");
        prenotaBtn.getStyleClass().addAll("green-button", "grid-button-single");
        prenotaBtn.setOnAction(e -> handlePrenotaPaper(paper));

        HBox.setHgrow(topicBox, Priority.ALWAYS);
        HBox.setHgrow(autoriBox, Priority.ALWAYS);


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        bottomRow.getChildren().addAll(topicBox, autoriBox, spacer, prenotaBtn);

        bottomRow.setAlignment(Pos.CENTER_LEFT); // o CENTER se preferisci
        bottomRow.setSpacing(20); // se non l'hai già


        card.getChildren().addAll(titoloLabel, abstractBox, bottomRow);

        return card;
    }

    public void handlePrenotaPaper(PaperEntity paper) {
        ConferenzaEntity conferenza = UserContext.getConferenzaAttuale();

        // Blocco prenotazioni oltre 2 giorni dalla scadenza sottomissione
        if (LocalDate.now().isAfter(conferenza.getScadenzaSottomissione().plusDays(2).toLocalDate())) {
            Alert expiredAlert = new Alert(Alert.AlertType.WARNING);
            expiredAlert.setTitle("Operazione non consentita");
            expiredAlert.setHeaderText("Data di broadcast superata!");
            expiredAlert.setContentText(String.format(
                    "Non è più possibile partecipare al broadcast, la scadenza era prevista per giorno \"%s\".",
                    conferenza.getScadenzaSottomissione().plusDays(2).toLocalDate()
            ));
            expiredAlert.showAndWait();
            return;
        }

        try {
            int idUtente = UserContext.getUtente().getId();
            int idConferenza = conferenza.getId();
            int idPaper = paper.getId();

            RevisioneDao revisioneDao = new RevisioneDao(DBMSBoundary.getConnection());

            // Verifica se già prenotato
            List<RevisioneEntity> revisioniUtente = revisioneDao.getByUtente(idUtente);
            boolean giaPrenotato = revisioniUtente.stream()
                    .anyMatch(r -> r.getRefPaper() == idPaper);

            if (giaPrenotato) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Prenotazione esistente");
                alert.setHeaderText("Hai già prenotato questo paper.");
                alert.setContentText("Non puoi prenotare lo stesso paper più di una volta.");
                alert.showAndWait();
                return;
            }

            // Verifica se l'utente ha già raggiunto il limite
            int countUtente = revisioneDao.countRevisioniByUtenteAndConferenza(idUtente, idConferenza);
            if (countUtente >= 4) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Limite raggiunto");
                alert.setHeaderText("Hai già 4 paper assegnati in revisione per questa conferenza.");
                alert.setContentText("Non puoi prenotare ulteriori paper.");
                alert.showAndWait();
                return;
            }

            // Verifica se il paper ha già 4 revisori
            int countPaper = revisioneDao.countRevisioniByPaper(idPaper);
            if (countPaper >= 4) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Paper già coperto");
                alert.setHeaderText("Questo paper ha già 4 revisori assegnati.");
                alert.setContentText("Non puoi prenotarlo.");
                alert.showAndWait();
                return;
            }

            // Inserimento revisione (con campi opzionali a null)
            RevisioneEntity revisione = new RevisioneEntity(
                    0,            // id_revisione (autogenerato)
                    null,         // testo
                    null,         // valutazione
                    null,         // data_sottomissione
                    null,         // punti_forza
                    null,         // punti_debolezza
                    null,         // commento_chair
                    idUtente,
                    idPaper
            );

            revisioneDao.save(revisione);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successo");
            alert.setHeaderText("Paper prenotato con successo!");
            alert.setContentText("Hai prenotato il paper per la revisione.");
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Errore durante la prenotazione del paper.");
            alert.setContentText("Dettagli: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
