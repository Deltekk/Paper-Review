package com.paperreview.paperreview.presentazioneArticolo.controls;

import com.paperreview.paperreview.common.CustomDateParser;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.PaperDao;
import com.paperreview.paperreview.common.dbms.dao.TopicPaperDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.ConferenzaEntity;
import com.paperreview.paperreview.entities.PaperEntity;
import com.paperreview.paperreview.entities.TopicEntity;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VisualizzaSottomissioniControl implements ControlledScreen {

    @FXML
    private VBox papersContainer;

    @FXML private Label conferenzaLabel;

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        try(Connection connection = DBMSBoundary.getConnection()){
            UserContext.setPaperAttuale(null);

            conferenzaLabel.setText(String.format("Conferenza: \"%s\"", UserContext.getConferenzaAttuale().getNome()));
            conferenzaLabel.setVisible(true);

            PaperDao paperDao = new PaperDao(connection);
            List<PaperEntity> papers = paperDao.getPapersByConferenza(UserContext.getConferenzaAttuale().getId());

            TopicPaperDao topicPaperDao = new TopicPaperDao(connection);

            if(papers.isEmpty())
            {
                Label testo = new Label("Non hai ancora pubblicato nessun paper!");
                testo.getStyleClass().addAll("font-bold", "text-rosso", "h5");
                testo.setWrapText(true);
                testo.setPrefWidth(500);
                testo.setAlignment(Pos.CENTER);

                papersContainer.getChildren().add(testo);
            }
            else
            {
                for (PaperEntity paper : papers) {

                    Node card = creaPaperCard(paper, topicPaperDao.getTopicsForPaper(paper.getId()));
                    papersContainer.getChildren().add(card);
                }
            }
        } catch (Exception e) {
            // TODO: Gestire questo errore
            throw new RuntimeException(e);
        }

    }

    private Node creaPaperCard(PaperEntity paper, Set<TopicEntity> topics) {
        HBox card = new HBox(20); // Spazio tra gli elementi
        card.getStyleClass().addAll("paper-card", "bg-celeste");
        card.setPrefWidth(1200);
        card.setAlignment(Pos.CENTER);

        // Titolo paper

        Label labelNomeConferenza = new Label(paper.getTitolo());
        labelNomeConferenza.getStyleClass().addAll("font-bold", "text-bianco", "h5", "ombra");
        labelNomeConferenza.setWrapText(true);
        labelNomeConferenza.setPrefWidth(700);

        /* SPACER 1 (FLESSIBILI) */

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        /* TOPICS */

        HBox topicBox = new HBox(10);
        topicBox.getStyleClass().addAll("topicBox");

        FontIcon topicIcon = new FontIcon("fas-tags"); // icona calendario FontAwesome
        topicIcon.setIconSize(16);
        topicIcon.setIconColor(Color.WHITE); // o altro colore adatto
        topicIcon.getStyleClass().setAll("text-bianco", "ombra");

        String nomiTopics = topics.stream()
                .map(TopicEntity::getNome)
                .collect(Collectors.joining(", "));

        String topicText = nomiTopics.isEmpty()
                ? "Topic: Nessun topic associato"
                : "Topic: " + nomiTopics;

        Label topicLabel = new Label(topicText);
        topicLabel.getStyleClass().addAll("h6", "text-bianco", "ombra", "font-light");
        topicLabel.setWrapText(true);

        topicBox.getChildren().addAll(topicIcon, topicLabel);
        topicBox.setAlignment(Pos.CENTER_LEFT);
        topicBox.setSpacing(10);
        topicBox.setPrefWidth(400);

        /* SPACER 2 */

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        /* BOTTONI A DESTRA */

        VBox buttonBox = new VBox(20);

        Button buttonModificaSottomissione = new Button("Modifica Sottomissione");
        Button buttonVisualizzaRevisioni = new Button("Visualizza Revisioni");

        buttonModificaSottomissione.getStyleClass().addAll("blue-button", "grid-button-single");
        buttonVisualizzaRevisioni.getStyleClass().addAll("green-button", "grid-button-single");

        buttonModificaSottomissione.setPrefWidth(Double.MAX_VALUE);
        buttonVisualizzaRevisioni.setPrefWidth(Double.MAX_VALUE);

        buttonModificaSottomissione.setOnAction(event -> handleModificaSottomissione(paper));
        buttonVisualizzaRevisioni.setOnAction(event -> handleVisualizzaRevisioni(paper));

        buttonBox.getChildren().addAll(buttonModificaSottomissione, buttonVisualizzaRevisioni);

        /* COMPOSIZIONE: [Testo] [Spacer] [Bottoni] */

        card.getChildren().addAll(labelNomeConferenza, spacer1, topicBox, spacer2, buttonBox);

        return card;
    }

    public void handleModificaSottomissione(PaperEntity paper)
    {
        ConferenzaEntity conferenza = UserContext.getConferenzaAttuale();

        // Se siamo alla prima sottomissione passa tranquillamente avanti

        // Controlla se siamo in periodo di revisione
        if(CustomDateParser.isBetweenSecondInclusive(LocalDate.now(), conferenza.getScadenzaSottomissione(), conferenza.getScadenzaRevisione())) {
            Alert expiredAlert = new Alert(Alert.AlertType.WARNING);
            expiredAlert.setTitle("Operazione non consentita");
            expiredAlert.setHeaderText("Data di adeguamento contenuti da raggiungere!");
            expiredAlert.setContentText(String.format(
                    "Ancora non è possibile modificare i contenuti del paper, riprova dopo giorno \"%s\".",
                    conferenza.getScadenzaRevisione()
            ));
            expiredAlert.showAndWait();
            return;
        }

        // Controlla se siamo in periodo di editing
        if(CustomDateParser.isBetweenSecondInclusive(LocalDate.now(), conferenza.getScadenzaSottomissione2(), conferenza.getScadenzaEditing())) {
            Alert expiredAlert = new Alert(Alert.AlertType.WARNING);
            expiredAlert.setTitle("Operazione non consentita");
            expiredAlert.setHeaderText("Data di adeguamento formato da raggiungere!");
            expiredAlert.setContentText(String.format(
                    "Ancora non è possibile modificare i contenuti del paper, riprova dopo giorno \"%s\".",
                    conferenza.getScadenzaEditing()
            ));
            expiredAlert.showAndWait();
            return;
        }

        // Controlla se siamo in periodo di pubblicazione proceedings
        if(LocalDate.now().isAfter(conferenza.getScadenzaSottomissione3().toLocalDate())) {
            Alert expiredAlert = new Alert(Alert.AlertType.WARNING);
            expiredAlert.setTitle("Operazione non consentita");
            expiredAlert.setHeaderText("Data di adeguamento formato superata!");
            expiredAlert.setContentText(String.format(
                    "Non è più possibile modificare i papers, la scadenza era prevista per giorno \"%s\".",
                    conferenza.getScadenzaRevisione()
            ));
            expiredAlert.showAndWait();
            return;
        }

        UserContext.setPaperAttuale(paper);
        mainControl.setView("/com/paperreview/paperreview/boundaries/presentazioneArticolo/modificaArticolo/modificaArticoloBoundary.fxml");
    }

    public void handleVisualizzaRevisioni(PaperEntity paper)
    {
        UserContext.setPaperAttuale(paper);
        mainControl.setView("/com/paperreview/paperreview/boundaries/presentazioneArticolo/visualizzaRevisioni/visualizzaRevisioniAutoreBoundary.fxml");
    }

}