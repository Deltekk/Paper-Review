package com.paperreview.paperreview.gestionePaperDefinitivi.controls;

import com.paperreview.paperreview.common.CustomDateParser;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.PaperDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.PaperEntity;
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

public class VisualizzaPapersEditoreControl implements ControlledScreen {

    @FXML
    private VBox papersContainer;

    @FXML private Label conferenzaLabel;

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        try{
            UserContext.setPaperAttuale(null);

            conferenzaLabel.setText(String.format("Conferenza: \"%s\"", UserContext.getConferenzaAttuale().getNome()));
            conferenzaLabel.setVisible(true);

            PaperDao paperDao = new PaperDao(DBMSBoundary.getConnection());
            List<PaperEntity> papers = paperDao.getPapersByConferenza(UserContext.getConferenzaAttuale().getId());

            if(papers.isEmpty())
            {
                Label testo = new Label("Non è stato ancora pubblicato nessun paper!");
                testo.getStyleClass().addAll("font-bold", "text-rosso", "h5");
                testo.setWrapText(true);
                testo.setPrefWidth(500);
                testo.setAlignment(Pos.CENTER);

                papersContainer.getChildren().add(testo);
            }
            else
            {
                for (PaperEntity paper : papers) {
                    Node card = creaPaperCard(paper);
                    papersContainer.getChildren().add(card);
                }
            }
        } catch (Exception e) {
            // TODO: Gestire questo errore
            throw new RuntimeException(e);
        }

    }

    private Node creaPaperCard(PaperEntity paper) {
        HBox card = new HBox(20); // Spazio tra gli elementi
        card.getStyleClass().addAll("paper-card", "bg-celeste");
        card.setPrefWidth(1200);
        card.setAlignment(Pos.CENTER);

        // Titolo paper

        Label labelNomeConferenza = new Label(paper.getTitolo());
        labelNomeConferenza.getStyleClass().addAll("font-bold", "text-bianco", "h5", "ombra");
        labelNomeConferenza.setWrapText(true);
        labelNomeConferenza.setPrefWidth(700);

        /* SPACES (FLESSIBILI) */

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        /* BOTTONI A DESTRA */

        VBox buttonBox = new VBox(20);

        Button buttonScaricaPaper = new Button("Scarica Paper");
        Button buttonInviaFeedback = new Button("Invia Feedback");

        buttonScaricaPaper.getStyleClass().addAll("blue-button", "grid-button-single");
        buttonInviaFeedback.getStyleClass().addAll("green-button", "grid-button-single");

        buttonScaricaPaper.setPrefWidth(Double.MAX_VALUE);
        buttonInviaFeedback.setPrefWidth(Double.MAX_VALUE);

        buttonScaricaPaper.setOnAction(event -> handleScaricaPaper(paper));
        buttonInviaFeedback.setOnAction(event -> handleInviaFeedback(paper));

        buttonBox.getChildren().addAll(buttonScaricaPaper, buttonInviaFeedback);

        /* COMPOSIZIONE: [Testo] [Spacer] [Bottoni] */

        card.getChildren().addAll(labelNomeConferenza, spacer, buttonBox);

        return card;
    }

    public void handleScaricaPaper(PaperEntity paper)
    {
        // TODO: implementare scarica paper
    }

    public void handleInviaFeedback(PaperEntity paper)
    {
        UserContext.setPaperAttuale(paper);
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestionePaperDefinitivi/inviaFeedback/inviaFeedbackBoundary.fxml");

    }

}