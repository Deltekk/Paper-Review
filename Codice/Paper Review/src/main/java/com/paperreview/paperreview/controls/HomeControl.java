package com.paperreview.paperreview.controls;
import com.paperreview.paperreview.common.CustomDateParser;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.dao.ConferenzaDao;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.NotificaDao;
import com.paperreview.paperreview.common.dbms.dao.RuoloConferenzaDao;
import com.paperreview.paperreview.common.dbms.dao.TopicPaperDao;
import com.paperreview.paperreview.entities.*;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.gestioneNotifiche.controls.NotificaPushControl;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/*
   ⚠️  ️️!!! DIEGO LEGGI QUI !!! ⚠️

   Qui bisogna implementare il get delle conferenze solo di quelle alla quale non partecipa
   e la quale data di inizio non supera la data di inizio sottomissione della conferenza (o quella di inizio conferenza, ora non ricordo).

    Devi finire anche l'implementazione del pulsante partecipa, io ho fatto solo che rimuove la conferenza da quelle disponibili.
 */

public class HomeControl implements ControlledScreen {

    private MainControl mainControl;

    @FXML
    private FlowPane conferenzeContainer;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        caricaConferenze();
    }

    private void caricaConferenze() {
        try {
            ConferenzaDao conferenzaDao = new ConferenzaDao(DBMSBoundary.getConnection());
            RuoloConferenzaDao ruoloConferenzaDao = new RuoloConferenzaDao(DBMSBoundary.getConnection());

            List<ConferenzaEntity> tutteLeConferenze = conferenzaDao.getAll();
            List<ConferenzaEntity> conferenzeNonDaAutore = new ArrayList<>();

            int idUtente = UserContext.getUtente().getId();

            // Filtra le conferenze dove NON è già autore
            for (ConferenzaEntity conferenza : tutteLeConferenze) {
                RuoloConferenzaEntity ruolo = ruoloConferenzaDao.getByRuoloUtenteAndConferenza(
                        Ruolo.Autore,
                        idUtente,
                        conferenza.getId()
                );
                if (ruolo == null) {
                    conferenzeNonDaAutore.add(conferenza);
                }
            }

            conferenzeContainer.getChildren().clear();

            if (conferenzeNonDaAutore.isEmpty()) {
                Label testo = new Label("Non ci sono conferenze a cui puoi ancora partecipare come autore.");
                testo.getStyleClass().addAll("font-bold", "text-rosso", "h5");
                testo.setWrapText(true);
                testo.setPrefWidth(500);
                testo.setAlignment(Pos.CENTER);

                conferenzeContainer.getChildren().add(testo);
            } else {
                for (ConferenzaEntity c : conferenzeNonDaAutore) {
                    VBox card = creaCardConferenza(c);
                    conferenzeContainer.getChildren().add(card);
                }
            }

        } catch (SQLException e) {
            mostraErrore("Errore nel recupero delle conferenze:\n" + e.getMessage());
        }
    }

    private VBox creaCardConferenza(ConferenzaEntity conferenza) {
        VBox card = new VBox(10);
        card.getStyleClass().add("conference-card");
        card.setPrefWidth(300);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #44C8F4; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);");

        // Titolo

        Label title = new Label(conferenza.getNome());
        title.getStyleClass().addAll("h5", "text-bianco", "font-bold", "ombra");
        title.setWrapText(true);

        // Descrizione

        Label description = new Label(conferenza.getDescrizione());
        description.getStyleClass().addAll("p", "text-bianco", "ombra");
        description.setWrapText(true);

        // Scadenza

        FontIcon calendarIcon = new FontIcon("fas-calendar-times"); // icona calendario FontAwesome
        calendarIcon.setIconSize(16);
        calendarIcon.setIconColor(Color.WHITE); // o altro colore adatto
        calendarIcon.getStyleClass().setAll("text-bianco", "ombra");

        Label date = new Label(CustomDateParser.parseDate(conferenza.getDataConferenza()));
        date.getStyleClass().addAll("h6", "text-bianco", "ombra", "font-light");

        // Metti icona + label in un HBox con spacing
        HBox dateBox = new HBox(5, calendarIcon, date);
        dateBox.setAlignment(Pos.CENTER_LEFT);

        // Location

        FontIcon pinIcon = new FontIcon("fas-map-pin"); // icona calendario FontAwesome
        pinIcon.setIconSize(16);
        pinIcon.setIconColor(Color.WHITE); // o altro colore adatto
        pinIcon.getStyleClass().setAll("text-bianco", "ombra");

        Label location = new Label(conferenza.getLocation());
        location.getStyleClass().addAll("h6", "text-bianco", "ombra", "font-light");
        location.setWrapText(true);

        // Metti icona + label in un HBox con spacing
        HBox locationBox = new HBox(5, pinIcon, location);
        locationBox.setAlignment(Pos.CENTER_LEFT);

        // Spaziatore

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Bottone

        Button partecipateBtn = new Button("Partecipa");
        partecipateBtn.getStyleClass().add("green-button");
        partecipateBtn.setMaxWidth(Double.MAX_VALUE);
        partecipateBtn.setOnAction(event -> partecipa(conferenza, card));


        card.getChildren().addAll(title, description, dateBox, locationBox, spacer, partecipateBtn);

        return card;
    }

    @FXML
    public void partecipa(ConferenzaEntity conferenzaEntity, VBox boxConferenza) {
        try {
            Connection con = DBMSBoundary.getConnection();
            RuoloConferenzaDao ruoloConferenzaDao = new RuoloConferenzaDao(con);

            int idUtente = UserContext.getUtente().getId();
            int idConferenza = conferenzaEntity.getId();

            // Controllo se è già autore in questa conferenza
            RuoloConferenzaEntity ruoloEsistente =
                    ruoloConferenzaDao.getByRuoloUtenteAndConferenza(Ruolo.Autore, idUtente, idConferenza);

            if (ruoloEsistente != null) {
                Alert alertPresente = new Alert(Alert.AlertType.WARNING);
                alertPresente.setTitle("Partecipazione già registrata");
                alertPresente.setHeaderText("Sei già autore in questa conferenza");
                alertPresente.setContentText("Non puoi partecipare più volte con lo stesso ruolo.");
                alertPresente.showAndWait();
                return;
            }

            // Finestra di conferma
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma partecipazione conferenza");
            alert.setHeaderText(String.format("Vuoi partecipare alla conferenza %s nel ruolo di autore?", conferenzaEntity.getNome()));
            alert.setContentText("Premi OK per confermare, oppure Annulla per annullare.");

            ButtonType buttonTypeOK = ButtonType.OK;
            ButtonType buttonTypeCancel = ButtonType.CANCEL;
            alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeOK) {
                    try {
                        RuoloConferenzaEntity ruoloConferenzaEntity = new RuoloConferenzaEntity(
                                0, Ruolo.Autore, idUtente, idConferenza
                        );
                        ruoloConferenzaDao.save(ruoloConferenzaEntity);

                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.setTitle("Conferma partecipazione conferenza");
                        alert2.setHeaderText(String.format("Complimenti, stai partecipando alla conferenza %s nel ruolo di autore!", conferenzaEntity.getNome()));
                        alert2.setContentText("Premi OK per continuare.");
                        alert2.showAndWait();

                        conferenzeContainer.getChildren().remove(boxConferenza);
                    } catch (Exception e) {
                        mostraErrore("Errore durante la registrazione:\n" + e.getMessage());
                    }
                }
            });

        } catch (SQLException e) {
            mostraErrore("Errore nella connessione al database:\n" + e.getMessage());
        }
    }
    private void mostraErrore(String messaggio) {
        Alert errore = new Alert(Alert.AlertType.ERROR);
        errore.setTitle("Errore");
        errore.setHeaderText("Si è verificato un errore");
        errore.setContentText(messaggio);
        errore.showAndWait();
    }
}