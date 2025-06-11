package com.paperreview.paperreview.gestioneNotifiche.controls;

import com.paperreview.paperreview.common.CustomDateParser;
import com.paperreview.paperreview.common.DBMSBoundary;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dao.InvitoDao;
import com.paperreview.paperreview.common.dao.NotificaDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.InvitoEntity;
import com.paperreview.paperreview.entities.NotificaEntity;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.sql.SQLException;
import java.util.List;

public class VisualizzaNotificheInvitiArchiviatiControl implements ControlledScreen {

    @FXML
    private ScrollPane container;

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
            List<InvitoEntity> inviti = invitoDao.getAllArchived();

            NotificaDao notificaDao = new NotificaDao(DBMSBoundary.getConnection());
            List<NotificaEntity> notifiche = notificaDao.getAllArchived(UserContext.getUtente().getId());

            if(inviti.isEmpty())
            {
                Label testo = new Label("Non hai ancora archiviato degli inviti!");
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
                Label testo = new Label("Non hai ancora archiviato delle notifiche!");
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

        /* SPACES (FLESSIBILI) */
        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);

        /* COMPOSIZIONE: [Testo] [Spacer] [Info] [Spacer] [Bottoni] */
        box.getChildren().addAll(testo, leftSpacer, additionalInfoBox);
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

        /* SPACES (FLESSIBILI) */
        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);

        /* COMPOSIZIONE: [Testo] [Spacer] [Info] [Spacer] [Bottoni] */
        box.getChildren().addAll(testo, leftSpacer, additionalInfoBox);
        box.setAlignment(Pos.CENTER);

        return box;
    }

}
