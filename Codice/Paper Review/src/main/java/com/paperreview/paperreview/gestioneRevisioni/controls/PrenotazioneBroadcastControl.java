package com.paperreview.paperreview.gestioneRevisioni.controls;

import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.CoAutoriPaperDao;
import com.paperreview.paperreview.common.dbms.dao.PaperDao;
import com.paperreview.paperreview.common.dbms.dao.TopicPaperDao;
import com.paperreview.paperreview.common.dbms.dao.UtenteDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.PaperEntity;
import com.paperreview.paperreview.entities.TopicEntity;
import com.paperreview.paperreview.entities.UtenteEntity;
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

        /*
            TODO: Gestire prenotazioni, idealmente un revisore non dovrebbe superare il massimo numero di paper per revisore, in quel caso i guess gli diamo un errore
                  Inoltre non si può prenotare se già si sono prenotate altre persone, lo gestiamo così perché ti sei dimenticato di fare la tabella del broadcast 🫠
         */

        System.out.println("Prenota paper: " + paper.getId());
    }
}
