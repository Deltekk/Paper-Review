package com.paperreview.paperreview.gestionePaperDefinitivi.controls;

import com.paperreview.paperreview.common.CustomDateParser;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.ConferenzaDao;
import com.paperreview.paperreview.common.dbms.dao.RuoloConferenzaDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.ConferenzaEntity;
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
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDate;
import java.util.List;

public class EditingControl implements ControlledScreen {

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
            UserContext.setPaperAttuale(null);

            ConferenzaDao conferenzaDao = new ConferenzaDao(DBMSBoundary.getConnection());
            List<ConferenzaEntity> conferenze = conferenzaDao.getAllByIdAndRuolo(UserContext.getUtente().getId(), Ruolo.Editor);

            if(conferenze.isEmpty())
            {
                Label testo = new Label("Non sei stato ancora invitato a delle conferenze come editore!");
                testo.getStyleClass().addAll("font-bold", "text-rosso", "h5", "centra");
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

        Button visualizzaPapers = new Button("Visualizza Papers");
        Button scaricaPapers = new Button("Scarica Papers");
        Button caricaProceedings = new Button("Carica Proceedings");

        visualizzaPapers.getStyleClass().addAll("blue-button");
        scaricaPapers.getStyleClass().addAll("blue-button");
        caricaProceedings.getStyleClass().addAll("green-button");

        visualizzaPapers.setPrefWidth(Double.MAX_VALUE);
        scaricaPapers.setPrefWidth(Double.MAX_VALUE);
        caricaProceedings.setPrefWidth(Double.MAX_VALUE);

        visualizzaPapers.setOnAction(event -> handleVisualizzaPapers(conferenza));
        scaricaPapers.setOnAction(event -> handleScaricaPapers(conferenza));
        caricaProceedings.setOnAction(event -> handleCaricaProceedings(conferenza, card));

        /* COMPOSIZIONE: [Titolo] [Scadenza] [Spacer] [Bottoni] */

        card.getChildren().addAll(title, dateBox, spacer, visualizzaPapers, scaricaPapers, caricaProceedings);

        return card;
    }

    public void handleVisualizzaPapers(ConferenzaEntity conferenza)
    {
        // Controlla se è passata la data per visualizzare i papers come le modifiche dei revisori
        if (LocalDate.now().isBefore(conferenza.getScadenzaSottomissione2().toLocalDate())) {
            Alert expiredAlert = new Alert(Alert.AlertType.WARNING);
            expiredAlert.setTitle("Operazione non consentita");
            expiredAlert.setHeaderText("Data di adeguamento contenuti da raggiungere!");
            expiredAlert.setContentText(String.format(
                    "Ancora non è possibile scaricare i papers, riprova dopo giorno \"%s\".",
                    conferenza.getScadenzaSottomissione2()
            ));
            expiredAlert.showAndWait();
            return;
        }

        UserContext.setConferenzaAttuale(conferenza);
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestionePaperDefinitivi/visualizzaPapersEditore/visualizzaPapersEditoreBoundary.fxml");
    }

    public void handleScaricaPapers(ConferenzaEntity conferenza)
    {

        // Controlla se è passata la data per caricare i papers come le modifiche dei revisori
        if (LocalDate.now().isBefore(conferenza.getScadenzaSottomissione2().toLocalDate())) {
            Alert expiredAlert = new Alert(Alert.AlertType.WARNING);
            expiredAlert.setTitle("Operazione non consentita");
            expiredAlert.setHeaderText("Data di adeguamento contenuti da raggiungere!");
            expiredAlert.setContentText(String.format(
                    "Ancora non è possibile scaricare i papers, riprova dopo giorno \"%s\".",
                    conferenza.getScadenzaSottomissione2()
            ));
            expiredAlert.showAndWait();
            return;
        }

        // TODO: Implementare bulk download di tutti i paper
        // Forza Diego <3
    }

    public void handleCaricaProceedings(ConferenzaEntity conferenza, VBox card)
    {

        // Controlla se è passata la data per caricare i paper con le modifiche editoriali
        if (LocalDate.now().isBefore(conferenza.getScadenzaSottomissione3().toLocalDate())) {
            Alert expiredAlert = new Alert(Alert.AlertType.WARNING);
            expiredAlert.setTitle("Operazione non consentita");
            expiredAlert.setHeaderText("Data di adeguamento formato da raggiungere!");
            expiredAlert.setContentText(String.format(
                    "Ancora non è possibile caricare i proceedings, riprova dopo giorno \"%s\".",
                    conferenza.getScadenzaSottomissione3()
            ));
            expiredAlert.showAndWait();
            return;
        }

        // Controlla se è passata la data per caricare i proceedings
        if (LocalDate.now().isAfter(conferenza.getScadenzaEditing().toLocalDate())) {
            Alert expiredAlert = new Alert(Alert.AlertType.WARNING);
            expiredAlert.setTitle("Operazione non consentita");
            expiredAlert.setHeaderText("Data di caricamento proceedings scaduta!");
            expiredAlert.setContentText(String.format(
                    "Non puoi più caricare i proceedings, la scadenza era giorno \"%s\".",
                    conferenza.getScadenzaEditing()
            ));
            expiredAlert.showAndWait();
            return;
        }

        // TODO: Caricare i proceeding come file e fare tutti i controlli del caso,
        //       dovrebbero pure spuntare degli alert per avere conferma dall'utente
        //       eventualmente sarebbe carino che una volta che i proeedings sono stati caricati l'editore non veda più la conferenza nella lista.
        //       la card da cancellare è stata già passata come parametro
    }

}
