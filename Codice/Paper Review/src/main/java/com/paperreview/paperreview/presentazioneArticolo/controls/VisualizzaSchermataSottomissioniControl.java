package com.paperreview.paperreview.presentazioneArticolo.controls;

import com.paperreview.paperreview.common.CustomDateParser;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.ConferenzaDao;
import com.paperreview.paperreview.common.dbms.dao.RuoloConferenzaDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.MainControl;
import com.paperreview.paperreview.entities.ConferenzaEntity;
import com.paperreview.paperreview.entities.PaperEntity;
import com.paperreview.paperreview.entities.Ruolo;
import com.paperreview.paperreview.entities.RuoloConferenzaEntity;
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
import net.bytebuddy.asm.Advice;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class VisualizzaSchermataSottomissioniControl implements ControlledScreen {

    @FXML
    private FlowPane conferenzeContainer;

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        try{

            UserContext.setConferenzaAttuale(null);
            UserContext.setStandaloneInteraction(false);

            ConferenzaDao conferenzaDao = new ConferenzaDao(DBMSBoundary.getConnection());
            //List<ConferenzaEntity> conferenze = conferenzaDao.getAllByIdAndRuolo(UserContext.getUtente().getId(), Ruolo.Autore);

            LocalDate oggi = LocalDate.now();

            List<ConferenzaEntity> conferenze = conferenzaDao
                    .getAllByIdAndRuolo(UserContext.getUtente().getId(), Ruolo.Autore)
                    .stream()
                    .filter(c -> {
                        LocalDate sottomissione1 = c.getScadenzaSottomissione().toLocalDate();
                        LocalDate revisione = c.getScadenzaRevisione().toLocalDate();
                        LocalDate sottomissione2 = c.getScadenzaSottomissione2().toLocalDate();
                        LocalDate editing = c.getScadenzaEditing().toLocalDate();
                        LocalDate sottomissione3 = c.getScadenzaSottomissione3().toLocalDate();

                        boolean presottomissione1 = oggi.isBefore(sottomissione1);
                        boolean traRevisioneeSottomissione2 =
                                (!oggi.isBefore(revisione)) && oggi.isBefore(sottomissione2);

                        boolean traEditingeSottomissione3 =
                                (!oggi.isBefore(editing)) && oggi.isBefore(sottomissione3);

                        return presottomissione1 || traRevisioneeSottomissione2 || traEditingeSottomissione3;
                    })
                    .collect(Collectors.toList());

            if(conferenze.isEmpty())
            {
                Label testo = new Label("Non hai ancora partecipato a delle conferenze!");
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

        Button sottometti = new Button("Sottometti");
        Button visualizzaSottomissioni = new Button("Visualizza Sottomissioni");
        Button ritirati = new Button("Ritirati");

        sottometti.getStyleClass().addAll("green-button");
        visualizzaSottomissioni.getStyleClass().addAll("blue-button");
        ritirati.getStyleClass().addAll("red-button");

        sottometti.setPrefWidth(Double.MAX_VALUE);
        visualizzaSottomissioni.setPrefWidth(Double.MAX_VALUE);
        ritirati.setPrefWidth(Double.MAX_VALUE);

        sottometti.setOnAction(event -> handleSottometti(conferenza));
        visualizzaSottomissioni.setOnAction(event -> handleVisualizzaSottomissioni(conferenza));
        ritirati.setOnAction(event -> handleRitirati(conferenza, card));

        /* COMPOSIZIONE: [Titolo] [Scadenza] [Spacer] [Bottoni] */

        card.getChildren().addAll(title, dateBox, spacer, sottometti, visualizzaSottomissioni, ritirati);

        return card;
    }

    public void handleSottometti(ConferenzaEntity conferenza)
    {

        // Controlla se siamo in periodo di sottomissione
        if(LocalDate.now().isAfter(conferenza.getScadenzaSottomissione().toLocalDate())) {
            Alert expiredAlert = new Alert(Alert.AlertType.WARNING);
            expiredAlert.setTitle("Operazione non consentita");
            expiredAlert.setHeaderText("Data di sottomissione superata!");
            expiredAlert.setContentText(String.format(
                    "Non è più possibile pubblicare nuovi papers, la scadenza era prevista per giorno \"%s\".",
                    conferenza.getScadenzaSottomissione()
            ));
            expiredAlert.showAndWait();
            return;
        }

        UserContext.setConferenzaAttuale(conferenza);
        mainControl.setView("/com/paperreview/paperreview/boundaries/presentazioneArticolo/sottomettiArticolo/sottomettiArticoloBoundary.fxml");
    }

    public void handleVisualizzaSottomissioni(ConferenzaEntity conferenza)
    {
        UserContext.setConferenzaAttuale(conferenza);
        mainControl.setView("/com/paperreview/paperreview/boundaries/presentazioneArticolo/visualizzaSottomissioni/visualizzaSottomissioniBoundary.fxml");
    }

    public void handleRitirati(ConferenzaEntity conferenza,  VBox boxConferenza)
    {

        // Controlla se la data di scadenza per la sottomissione è già passata
        if (LocalDate.now().isAfter(conferenza.getScadenzaSottomissione().toLocalDate())) {
            Alert expiredAlert = new Alert(Alert.AlertType.WARNING);
            expiredAlert.setTitle("Operazione non consentita");
            expiredAlert.setHeaderText(String.format(
                    "La scadenza per ritirarsi dalla conferenza \"%s\" è già passata.",
                    conferenza.getNome()
            ));
            expiredAlert.setContentText("Non puoi più ritirarti dopo la scadenza delle sottomissioni.");
            expiredAlert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma ritiro conferenza");
        alert.setHeaderText(String.format("Vuoi veramente ritirarti dalla conferenza %s?", conferenza.getNome()));
        alert.setContentText("Premi OK per confermare, oppure Annulla per annullare.");

        // Aggiungi le opzioni OK e Annulla
        ButtonType buttonTypeOK = ButtonType.OK;
        ButtonType buttonTypeCancel = ButtonType.CANCEL;
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

        // Mostra il popup e gestisci la risposta dell'utente
        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeOK) {

                try{
                    RuoloConferenzaDao ruoloConferenzaDao = new RuoloConferenzaDao(DBMSBoundary.getConnection());
                    RuoloConferenzaEntity ruoloConferenzaEntity = ruoloConferenzaDao.getByRuoloUtenteAndConferenza(Ruolo.Autore, UserContext.getUtente().getId(), conferenza.getId());
                    ruoloConferenzaDao.delete(ruoloConferenzaEntity);

                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("Conferma ritiro conferenza");
                    alert2.setHeaderText(String.format(
                            "Siamo spiacenti che tu abbia deciso di ritirarti dalla conferenza \"%s\". Speriamo di rivederti presto!",
                            conferenza.getNome()
                    ));
                    alert2.setContentText("Premi ok per continuare!");
                    alert2.showAndWait();

                    conferenzeContainer.getChildren().remove(boxConferenza);
                }catch(Exception e){
                    e.printStackTrace();
                    // TODO: GESTIRE BENE QUEST' ERRORE
                }
            }
        });
    }

}

