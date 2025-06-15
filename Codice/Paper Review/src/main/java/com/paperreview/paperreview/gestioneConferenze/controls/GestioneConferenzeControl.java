package com.paperreview.paperreview.gestioneConferenze.controls;

import com.paperreview.paperreview.common.CustomDateParser;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.ConferenzaDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.ConferenzaEntity;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class GestioneConferenzeControl implements ControlledScreen {

    @FXML
    private VBox conferenzeContainer;

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        try{
            ConferenzaDao conferenzaDao = new ConferenzaDao(DBMSBoundary.getConnection());
            List<ConferenzaEntity> conferenze = conferenzaDao.getAll(UserContext.getUtente().getId());

            if(conferenze.isEmpty())
            {
                Label testo = new Label("Non hai ancora creato delle conferenze!");
                testo.getStyleClass().addAll("font-bold", "text-rosso", "h5");
                testo.setWrapText(true);
                testo.setPrefWidth(500);
                testo.setAlignment(Pos.CENTER);

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
        HBox card = new HBox(20); // Spazio tra gli elementi
        card.getStyleClass().addAll("conferenza-card", "bg-celeste");
        card.setPrefWidth(1200);
        card.setAlignment(Pos.CENTER);

        /* TESTO A SINISTRA */

        VBox textContainer = new VBox(10);
        textContainer.setAlignment(Pos.CENTER_LEFT);
        textContainer.setPrefWidth(500);

        // Nome conferenza

        Label labelNomeConferenza = new Label(conferenza.getNome());
        labelNomeConferenza.getStyleClass().addAll("font-bold", "text-bianco", "h5", "ombra");
        labelNomeConferenza.setWrapText(true);
        labelNomeConferenza.setPrefWidth(500);

        // Descrizione conferenza

        Label labelDescrizione = new Label(conferenza.getDescrizione());
        labelDescrizione.getStyleClass().addAll("font-bold", "text-bianco", "h6", "ombra");
        labelDescrizione.setWrapText(true);
        labelDescrizione.setPrefWidth(500);

        // Data conferenza

        HBox scadenzaBox = new HBox(10);
        FontIcon calendarIcon = new FontIcon("fas-calendar");
        calendarIcon.setIconSize(24);
        calendarIcon.setIconColor(Color.WHITE);
        calendarIcon.getStyleClass().add("ombra");

        Label labelData = new Label(CustomDateParser.parseDate(conferenza.getDataConferenza()));
        labelData.getStyleClass().addAll("text-bianco", "h6", "ombra", "font-bold");

        scadenzaBox.getChildren().addAll(calendarIcon, labelData);
        scadenzaBox.setAlignment(Pos.CENTER_LEFT);

        // Location conferenza

        HBox locationBox = new HBox(10);

        FontIcon pinIcon = new FontIcon("fas-map-pin"); // icona calendario FontAwesome
        pinIcon.setIconSize(24);
        pinIcon.setIconColor(Color.WHITE); // o altro colore adatto
        pinIcon.getStyleClass().setAll("text-bianco", "ombra");

        Label labelLocation = new Label(conferenza.getLocation());
        labelLocation.getStyleClass().addAll("h6", "text-bianco", "ombra", "font-light");
        labelLocation.setWrapText(true);
        locationBox.setAlignment(Pos.CENTER_LEFT);

        locationBox.getChildren().addAll(pinIcon, labelLocation);

        textContainer.getChildren().setAll(labelNomeConferenza, labelDescrizione, locationBox, scadenzaBox);

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

        Button buttonPosticipaScadenze = new Button("Posticipa Scadenze");
        Button buttonvisualizzaPapers = new Button("Visualizza papers");
        Button buttonAggiungiChair = new Button("Aggiungi Chair");
        Button buttonAggiungiRevisore = new Button("Aggiungi Revisore");
        Button buttonVisualizzaRevisori = new Button("Visualizza Revisori");

        buttonPosticipaScadenze.getStyleClass().addAll("green-button", "grid-button-single");
        buttonvisualizzaPapers.getStyleClass().addAll("blue-button", "grid-button-single");
        buttonAggiungiChair.getStyleClass().addAll("blue-button", "grid-button-single");
        buttonAggiungiRevisore.getStyleClass().addAll("blue-button", "grid-button-single");
        buttonVisualizzaRevisori.getStyleClass().addAll("blue-button", "grid-button-double");

        buttonPosticipaScadenze.setPrefWidth(Double.MAX_VALUE);
        buttonvisualizzaPapers.setPrefWidth(Double.MAX_VALUE);
        buttonAggiungiChair.setPrefWidth(Double.MAX_VALUE);
        buttonAggiungiRevisore.setPrefWidth(Double.MAX_VALUE);
        buttonVisualizzaRevisori.setPrefWidth(Double.MAX_VALUE);

        buttonPosticipaScadenze.setOnAction(event -> handlePosticipaScadenze(conferenza));
        buttonvisualizzaPapers.setOnAction(event -> handleVisualizzaPapers(conferenza));
        buttonAggiungiChair.setOnAction(event -> handleAggiungiChair(conferenza));
        buttonAggiungiRevisore.setOnAction(event -> handleAggiungiRevisore(conferenza));
        buttonVisualizzaRevisori.setOnAction(event -> handleVisualizzaRevisori(conferenza));

        buttonGrid.add(buttonPosticipaScadenze, 0, 0);
        buttonGrid.add(buttonvisualizzaPapers, 1, 0);
        buttonGrid.add(buttonAggiungiChair, 0, 1);
        buttonGrid.add(buttonAggiungiRevisore, 1, 1);
        buttonGrid.add(buttonVisualizzaRevisori, 0, 2, 2, 1);

        /* COMPOSIZIONE: [Testo] [Spacer] [Bottoni] */

        card.getChildren().addAll(textContainer, spacer, buttonGrid);

        return card;
    }

    @FXML
    public void handleCreaConferenza()
    {
        UserContext.setConferenzaAttuale(null);
        UserContext.setStandaloneInteraction(false);
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/creaConferenza/creaConferenzaBoundary.fxml");
    }

    public void handlePosticipaScadenze(ConferenzaEntity conferenza)
    {
        UserContext.setConferenzaAttuale(conferenza);
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/posticipaScadenza/posticipaScadenzaBoundary.fxml");

    }

    public void handleVisualizzaPapers(ConferenzaEntity conferenza)
    {
        UserContext.setConferenzaAttuale(conferenza);
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/visualizzaPapersChair/visualizzaPapersChairBoundary.fxml");

    }

    public void handleAggiungiChair(ConferenzaEntity conferenza)
    {
        UserContext.setConferenzaAttuale(conferenza);
        UserContext.setStandaloneInteraction(true);
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/invitaChair/invitaChairBoundary.fxml");

    }

    public void handleAggiungiRevisore(ConferenzaEntity conferenza)
    {
        UserContext.setConferenzaAttuale(conferenza);
        UserContext.setStandaloneInteraction(true);
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/invitaRevisori/invitaRevisoriBoundary.fxml");
    }

    public void handleVisualizzaRevisori(ConferenzaEntity conferenza)
    {
        UserContext.setConferenzaAttuale(conferenza);
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/visualizzaRevisori/visualizzaRevisoriBoundary.fxml");
    }

}
