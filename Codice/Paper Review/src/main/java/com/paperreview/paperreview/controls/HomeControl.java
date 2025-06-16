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

    public void avviaNotifichePush() {
        NotificaPushControl.avviaNotificheSeNonAttive(mainControl.getRootPane());
    }

    private void caricaConferenze(){
        try{
            ConferenzaDao conferenzaDao = new ConferenzaDao(DBMSBoundary.getConnection());
            List<ConferenzaEntity> conferenze  = conferenzaDao.getAllIfNotAutore(UserContext.getUtente().getId());

            conferenzeContainer.getChildren().clear();

            if(conferenze.isEmpty())
            {
                Label testo = new Label("Ancora non è stata pubblicata nessuna conferenza!");
                testo.getStyleClass().addAll("font-bold", "text-rosso", "h5");
                testo.setWrapText(true);
                testo.setPrefWidth(500);
                testo.setAlignment(Pos.CENTER);

                conferenzeContainer.getChildren().add(testo);
            }
            else
            {
                for (ConferenzaEntity c : conferenze) {
                    VBox card = creaCardConferenza(c);
                    conferenzeContainer.getChildren().add(card);
                }
            }

        }catch(SQLException e){
            e.printStackTrace();
            // TODO: Gestire errore come scritto nel flusso
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

        try{
            Connection con = DBMSBoundary.getConnection();

            // Carica l'icona utilizzando getResourceAsStream()

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma partecipazione conferenza");
            alert.setHeaderText(String.format("Vuoi partecipare alla conferenza %s nel ruolo di autore?", conferenzaEntity.getNome()));
            alert.setContentText("Premi OK per confermare, oppure Annulla per annullare.");

            // Aggiungi le opzioni OK e Annulla
            ButtonType buttonTypeOK = ButtonType.OK;
            ButtonType buttonTypeCancel = ButtonType.CANCEL;
            alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

            // Mostra il popup e gestisci la risposta dell'utente
            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeOK) {
                    // Se l'utente ha premuto ok e non annulla...
                    // TODO: Implementare la logica di partecipazione alla conferenza

                    try{
                        RuoloConferenzaDao ruoloConferenzaDao = new RuoloConferenzaDao(con);
                        RuoloConferenzaEntity ruoloConferenzaEntity = new RuoloConferenzaEntity(0, Ruolo.Autore, UserContext.getUtente().getId(), conferenzaEntity.getId());

                        ruoloConferenzaDao.save(ruoloConferenzaEntity);

                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.setTitle("Conferma partecipazione conferenza");
                        alert2.setHeaderText(String.format("Complimenti, stai partecipando alla conferenza %s nel ruolo di autore!", conferenzaEntity.getNome()));
                        alert2.setContentText("Premi ok per continuare!");
                        alert2.showAndWait();

                        conferenzeContainer.getChildren().remove(boxConferenza);
                    }catch(Exception e){
                        throw new RuntimeException(e);
                        // TODO: GESTIRE BENE QUEST' ERRORE
                    }



                }
            });

        } catch (SQLException e) {
            throw new RuntimeException(e);
            // TODO: GESTIRE BENE QUEST' ERRORE
        }

    }

}
