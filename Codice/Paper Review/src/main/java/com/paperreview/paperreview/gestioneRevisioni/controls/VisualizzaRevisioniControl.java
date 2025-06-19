package com.paperreview.paperreview.gestioneRevisioni.controls;

import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.RevisioneDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.RevisioneEntity;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.sql.Connection;
import java.util.List;

public class VisualizzaRevisioniControl implements ControlledScreen {

    @FXML
    private VBox revisioniContainer;

    @FXML private Label conferenzaLabel, paperLabel;

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        try(Connection connection = DBMSBoundary.getConnection()){

            conferenzaLabel.setText(String.format("Conferenza: \"%s\"", UserContext.getConferenzaAttuale().getNome()));
            paperLabel.setText(String.format("Paper: \"%s\"", UserContext.getPaperAttuale().getTitolo()));
            conferenzaLabel.setVisible(true);
            paperLabel.setVisible(true);

            RevisioneDao revisioneDao = new RevisioneDao(connection);
            List<RevisioneEntity> revisioni = revisioneDao.getByPaper(UserContext.getPaperAttuale().getId());

            if(revisioni.isEmpty())
            {
                Label testo = new Label("Non è stata ancora pubblicata nessuna revisione!");
                testo.getStyleClass().addAll("font-bold", "text-rosso", "h5");
                testo.setWrapText(true);
                testo.setPrefWidth(500);
                testo.setAlignment(Pos.CENTER);

                revisioniContainer.getChildren().add(testo);
            }
            else
            {
                for (RevisioneEntity revisione : revisioni) {

                    Node card = creaRevisioneCard(revisione);
                    revisioniContainer.getChildren().add(card);
                }
            }
        } catch (Exception e) {
            // TODO: Gestire questo errore
            throw new RuntimeException(e);
        }

    }

    private Node creaRevisioneCard(RevisioneEntity revisione) {
        HBox card = new HBox(20); // Spazio tra gli elementi
        card.getStyleClass().addAll("revisione-card", "bg-celeste");
        card.setPrefWidth(1200);
        card.setAlignment(Pos.CENTER);

        /* ----- Score paper ----- */

        HBox scoreBox = new HBox(10);
        scoreBox.alignmentProperty().set(Pos.CENTER_LEFT);

        // Icona Score

        FontIcon scoreIcon = new FontIcon("fas-star");
        scoreIcon.setIconSize(24);
        scoreIcon.setIconColor(Color.WHITE); // o altro colore adatto
        scoreIcon.getStyleClass().setAll("text-bianco", "ombra");

        // Score medio e massimo

        Integer maxScore = Integer.valueOf(UserContext.getConferenzaAttuale().getMetodoValutazione().getValoreDb());

        // Label

        Label labelScore = new Label(String.format("Score: %d/%d", revisione.getValutazione(), maxScore));
        labelScore.getStyleClass().addAll("h6", "text-bianco", "ombra", "font-light");
        labelScore.setWrapText(true);

        scoreBox.getChildren().addAll(scoreIcon, labelScore);

        /* SPACER 1 (FLESSIBILI) */

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        /* BOTTONI A DESTRA */

        Button buttonVisualizzaDettagli = new Button("Visualizza dettagli");

        buttonVisualizzaDettagli.getStyleClass().addAll("blue-button", "grid-button-single");

        buttonVisualizzaDettagli.setPrefWidth(Double.MAX_VALUE);

        buttonVisualizzaDettagli.setOnAction(event -> handleVisualizzaDettagli(revisione));

        /* COMPOSIZIONE: [Testo] [Spacer] [Bottoni] */

        card.getChildren().addAll(scoreBox, spacer, buttonVisualizzaDettagli);

        return card;
    }

    public void handleVisualizzaDettagli(RevisioneEntity revisione)
    {
        UserContext.setRevisioneCorrente(revisione);
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneRevisioni/visualizzaRevisione/visualizzaRevisioneBoundary.fxml");
    }

}