package com.paperreview.paperreview.gestioneRevisioni.controls;

import com.paperreview.paperreview.common.CustomDateParser;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.ConferenzaDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.MainControl;
import com.paperreview.paperreview.entities.ConferenzaEntity;
import com.paperreview.paperreview.entities.MetodoAssegnazione;
import com.paperreview.paperreview.entities.Ruolo;
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
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class VisualizzaSchermataRevisioniControl implements ControlledScreen {

    @FXML
    private FlowPane conferenzeContainer;

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        try(Connection connection = DBMSBoundary.getConnection()){

            UserContext.setStandaloneInteraction(false);

            ConferenzaDao conferenzaDao = new ConferenzaDao(connection);
            //List<ConferenzaEntity> conferenze = conferenzaDao.getAllByIdAndRuolo(UserContext.getUtente().getId(), Ruolo.Revisore);

            LocalDate oggi = LocalDate.now();

            List<ConferenzaEntity> conferenze = conferenzaDao
                    .getAllByIdAndRuolo(UserContext.getUtente().getId(), Ruolo.Revisore)
                    .stream()
                    .filter(c -> {
                        LocalDate sottomissione1 = c.getScadenzaSottomissione().toLocalDate();
                        LocalDate revisione = c.getScadenzaRevisione().toLocalDate();

                        return !oggi.isBefore(sottomissione1) && oggi.isBefore(revisione);
                    })
                    .collect(Collectors.toList());

            if(conferenze.isEmpty())
            {
                Label testo = new Label("Non sei ancora stato invitato come revisore in nessuna conferenza!");
                testo.getStyleClass().addAll("font-bold", "text-rosso", "h5", "centra");
                testo.setWrapText(true);
                testo.setPrefWidth(500);
                testo.setAlignment(Pos.CENTER);
                testo.setTextAlignment(TextAlignment.CENTER);

                conferenzeContainer.getChildren().add(testo);
            }
            else
            {
                for (ConferenzaEntity conferenza : conferenze) {
                    Node card = creaConferenzaCard(conferenza);
                    conferenzeContainer.getChildren().add(card);
                }
            }
        } catch (Exception e) {
            // TODO: Gestire questo errore
            throw new RuntimeException(e);
        }

    }

    private Node creaConferenzaCard(ConferenzaEntity conferenza) {
        VBox card = new VBox(10);
        card.getStyleClass().add("conference-card");
        card.setPrefWidth(300);
        card.setPadding(new Insets(15));

        // Titolo

        Label title = new Label(conferenza.getNome());
        title.getStyleClass().addAll("h5", "text-bianco", "font-bold", "ombra");
        title.setWrapText(true);

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

        /* SPACER */

        /* SPACES (FLESSIBILI) */
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        /* BOTTONI */

        Button visualizzaPapers = new Button("Visualizza papers");
        Button partecipaBroadcast = new Button("Partecipa Broadcast");

        visualizzaPapers.getStyleClass().addAll("green-button");
        partecipaBroadcast.getStyleClass().addAll("blue-button");

        visualizzaPapers.setPrefWidth(Double.MAX_VALUE);
        partecipaBroadcast.setPrefWidth(Double.MAX_VALUE);

        visualizzaPapers.setOnAction(event -> handleVisualizzaPapers(conferenza));
        partecipaBroadcast.setOnAction(event -> handlePartecipaBroadcast(conferenza));

        /* COMPOSIZIONE: [Titolo] [Scadenza] [Spacer] [Bottoni] */

        card.getChildren().addAll(title, dateBox, spacer, visualizzaPapers, partecipaBroadcast);

        return card;
    }

    public void handleVisualizzaPapers(ConferenzaEntity conferenza)
    {
        // 1. Controllo: periodo di sottomissione ancora in corso (blocco sempre valido)
        if (LocalDate.now().isBefore(conferenza.getScadenzaSottomissione().toLocalDate())) {
            Alert expiredAlert = new Alert(Alert.AlertType.WARNING);
            expiredAlert.setTitle("Operazione non consentita");
            expiredAlert.setHeaderText("Periodo di sottomissione ancora attivo!");
            expiredAlert.setContentText(String.format(
                    "Non è ancora possibile visualizzare i papers. La sottomissione termina il giorno \"%s\".",
                    conferenza.getScadenzaSottomissione()
            ));
            expiredAlert.showAndWait();
            return;
        }

        // 2. Controllo: se è Broadcast, devono passare almeno 2 giorni dalla scadenza di sottomissione
        if (conferenza.getMetodoAssegnazione() == MetodoAssegnazione.Broadcast &&
                LocalDate.now().isBefore(conferenza.getScadenzaSottomissione().toLocalDate().plusDays(2))) {

            Alert waitAlert = new Alert(Alert.AlertType.WARNING);
            waitAlert.setTitle("Operazione non consentita");
            waitAlert.setHeaderText("Attendi chiusura selezione Broadcast");
            waitAlert.setContentText(String.format(
                    "La selezione dei papers è disponibile, la visualizzazione paper è disponibile solo dopo 2 giorni dalla fine della sottomissione (%s).",
                    conferenza.getScadenzaSottomissione().toLocalDate().plusDays(2)
            ));
            waitAlert.showAndWait();
            return;
        }

        LocalDate scadenzaRevisione = conferenza.getScadenzaRevisione().toLocalDate();
        // 3. Scadenza revisione superata
        if (LocalDate.now().isAfter(scadenzaRevisione)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Operazione non consentita");
            alert.setHeaderText("Periodo di revisione scaduto!");
            alert.setContentText(String.format(
                    "Il periodo utile per la revisione è terminato il \"%s\".",
                    scadenzaRevisione
            ));
            alert.showAndWait();
            return;
        }

        UserContext.setConferenzaAttuale(conferenza);
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneRevisioni/visualizzaPapersRevisore/visualizzaPapersRevisoreBoundary.fxml");
    }

    public void handlePartecipaBroadcast(ConferenzaEntity conferenza)
    {

        // Se broadcast non è previsto
        if(conferenza.getMetodoAssegnazione() != MetodoAssegnazione.Broadcast)
        {
            Alert expiredAlert = new Alert(Alert.AlertType.WARNING);
            expiredAlert.setTitle("Operazione non consentita");
            expiredAlert.setHeaderText("Broadcast non previsto!");
            expiredAlert.setContentText(String.format("La conferenza \"%s\" non prevede un assegnazione tramite broadcast!", conferenza.getNome()));
            expiredAlert.showAndWait();
            return;
        }

        // Se siamo arrivati troppo presto
        if( LocalDate.now().isBefore(conferenza.getScadenzaSottomissione().toLocalDate())) {
            Alert expiredAlert = new Alert(Alert.AlertType.WARNING);
            expiredAlert.setTitle("Operazione non consentita");
            expiredAlert.setHeaderText("Data di broadcast da raggiungere!");
            expiredAlert.setContentText(String.format(
                    "Ancora non è possibile partecipare al braodcast, riprova dopo giorno \"%s\".",
                    conferenza.getScadenzaSottomissione()
            ));
            expiredAlert.showAndWait();
            return;
        }

        // TODO: Lasciamo 2 giorni dopo la sottomissione?
        // Controlla se siamo arrivati troppo tardi
        if( LocalDate.now().isAfter(conferenza.getScadenzaSottomissione().plusDays(2).toLocalDate())) {
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

        UserContext.setConferenzaAttuale(conferenza);
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneRevisioni/prenotazioneBroadcast/prenotazioneBroadcastBoundary.fxml");
    }

}

