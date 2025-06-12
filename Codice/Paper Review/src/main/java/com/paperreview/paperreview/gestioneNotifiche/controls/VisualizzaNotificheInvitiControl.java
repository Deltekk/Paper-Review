package com.paperreview.paperreview.gestioneNotifiche.controls;

import com.paperreview.paperreview.common.CustomDateParser;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.InvitoDao;
import com.paperreview.paperreview.common.dbms.dao.NotificaDao;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.InvitoEntity;
import com.paperreview.paperreview.entities.NotificaEntity;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.sql.SQLException;
import java.util.List;

public class VisualizzaNotificheInvitiControl implements ControlledScreen {

    @FXML private ScrollPane container;

    @FXML private VBox invitiContainer;
    @FXML private VBox notificheContainer;

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    @FXML
    public void initialize() {
        try {
            InvitoDao invitoDao = new InvitoDao(DBMSBoundary.getConnection());
            List<InvitoEntity> inviti = invitoDao.getAll(UserContext.getUtente().getId());

            NotificaDao notificaDao = new NotificaDao(DBMSBoundary.getConnection());
            List<NotificaEntity> notifiche = notificaDao.getAll(UserContext.getUtente().getId());

            if(inviti.isEmpty())
            {
                Label testo = new Label("Non hai ancora ricevuto dei nuovi inviti!");
                testo.getStyleClass().addAll("font-bold", "text-rosso", "h5");
                testo.setWrapText(true);
                testo.setPrefWidth(500);
                testo.setAlignment(Pos.CENTER);

                invitiContainer.getChildren().add(testo);
            }
            else{
                for (InvitoEntity invito : inviti) {
                    Node card = creaInvitoCard(invito);
                    invitiContainer.getChildren().add(card);
                }
            }

            if(notifiche.isEmpty())
            {
                Label testo = new Label("Non hai ancora ricevuto nuove notifiche!");
                testo.getStyleClass().addAll("font-bold", "text-rosso", "h5");
                testo.setWrapText(true);
                testo.setPrefWidth(500);
                testo.setAlignment(Pos.CENTER);

                notificheContainer.getChildren().add(testo);
            }
            else{
                for (NotificaEntity notifica : notifiche) {
                    Node card = creaNotificaCard(notifica);
                    notificheContainer.getChildren().add(card);
                }
            }

            // TODO: carica e mostra anche le notifiche
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Node creaInvitoCard(InvitoEntity invito) {
        HBox box = new HBox(20); // Spazio tra gli elementi
        box.getStyleClass().addAll("invito-card", "bg-celeste");
        box.setPrefWidth(1200);

        /* TESTO A SINISTRA */
        Label testo = new Label(invito.getTesto());
        testo.getStyleClass().addAll("font-bold", "text-bianco", "h5", "ombra");
        testo.setWrapText(true);
        testo.setPrefWidth(500);

        /* INFO AL CENTRO */
        VBox additionalInfoBox = new VBox(10);
        additionalInfoBox.getStyleClass().add("additional-info-box");
        additionalInfoBox.setPrefWidth(300);
        additionalInfoBox.setAlignment(Pos.CENTER);

        HBox scadenzaBox = new HBox(10);
        FontIcon calendarIcon = new FontIcon("fas-calendar-times");
        calendarIcon.setIconSize(24);
        calendarIcon.setIconColor(Color.WHITE);
        calendarIcon.getStyleClass().add("ombra");

        Label scadenza = new Label(CustomDateParser.parseDate(invito.getData()));
        scadenza.getStyleClass().addAll("text-bianco", "h6", "ombra", "font-bold");
        scadenzaBox.getChildren().addAll(calendarIcon, scadenza);
        additionalInfoBox.getChildren().add(scadenzaBox);

        /* BOTTONI A DESTRA */
        VBox pulsantiBox = new VBox(10);
        pulsantiBox.setPrefWidth(300);

        Button accetta = new Button("Accetta");
        Button rifiuta = new Button("Rifiuta");
        accetta.getStyleClass().add("green-button");
        rifiuta.getStyleClass().add("red-button");
        accetta.setPrefWidth(Double.MAX_VALUE);
        rifiuta.setPrefWidth(Double.MAX_VALUE);

        pulsantiBox.getChildren().addAll(accetta, rifiuta);

        /* SPACES (FLESSIBILI) */
        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        /* COMPOSIZIONE: [Testo] [Spacer] [Info] [Spacer] [Bottoni] */
        box.getChildren().addAll(testo, leftSpacer, additionalInfoBox, rightSpacer, pulsantiBox);
        box.setAlignment(Pos.CENTER);

        return box;
    }

    private Node creaNotificaCard(NotificaEntity notifica) {
        HBox box = new HBox(20); // Spazio tra gli elementi
        box.getStyleClass().addAll("notifica-card", "bg-celeste");
        box.setPrefWidth(1200);

        /* TESTO A SINISTRA */
        Label testo = new Label(notifica.getTesto());
        testo.getStyleClass().addAll("font-bold", "text-bianco", "h5", "ombra");
        testo.setWrapText(true);
        testo.setPrefWidth(500);

        /* INFO AL CENTRO */
        VBox additionalInfoBox = new VBox(10);
        additionalInfoBox.getStyleClass().add("additional-info-box");
        additionalInfoBox.setPrefWidth(300);
        additionalInfoBox.setAlignment(Pos.CENTER);

        HBox scadenzaBox = new HBox(10);
        FontIcon calendarIcon = new FontIcon("fas-calendar-times");
        calendarIcon.setIconSize(24);
        calendarIcon.setIconColor(Color.WHITE);
        calendarIcon.getStyleClass().add("ombra");

        Label scadenza = new Label(CustomDateParser.parseDate(notifica.getData()));
        scadenza.getStyleClass().addAll("text-bianco", "h6", "ombra", "font-bold");
        scadenzaBox.getChildren().addAll(calendarIcon, scadenza);
        additionalInfoBox.getChildren().add(scadenzaBox);

        /* BOTTONI A DESTRA */
        VBox pulsantiBox = new VBox(10);
        pulsantiBox.setPrefWidth(300);

        Button prendiVisione = new Button("Prendi visione");
        prendiVisione.getStyleClass().add("green-button");
        prendiVisione.setPrefWidth(Double.MAX_VALUE);
        prendiVisione.setOnAction(event -> prendiVisione(notifica, box));

        pulsantiBox.getChildren().addAll(prendiVisione);

        /* SPACES (FLESSIBILI) */
        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        /* COMPOSIZIONE: [Testo] [Spacer] [Info] [Spacer] [Bottoni] */
        box.getChildren().addAll(testo, leftSpacer, additionalInfoBox, rightSpacer, pulsantiBox);
        box.setAlignment(Pos.CENTER);

        return box;
    }

    @FXML
    public void visualizzaNotificheInvitiArchiviati(){
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneNotifiche/visualizzaNotificheInvitiArchiviati/visualizzaNotificheInvitiArchiviatiBoundary.fxml");
    }


    @FXML
    public void prendiVisione(NotificaEntity notificaEntity, HBox boxNotifica) {

        try{
            notificaEntity.setLetta(true);
            NotificaDao notificaDao = new NotificaDao(DBMSBoundary.getConnection());
            notificaDao.update(notificaEntity);

            notificheContainer.getChildren().remove(boxNotifica);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            // TODO: GESTIRE BENE QUEST' ERRORE
        }

    }

}
