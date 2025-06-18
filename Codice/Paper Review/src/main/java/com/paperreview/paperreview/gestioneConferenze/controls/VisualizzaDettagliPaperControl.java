package com.paperreview.paperreview.gestioneConferenze.controls;

import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.*;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

public class VisualizzaDettagliPaperControl implements ControlledScreen {

    @FXML
    private VBox paperContainer;
    @FXML
    private Label conferenceTitleLabel;

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        try (Connection connection = DBMSBoundary.getConnection()) {

            conferenceTitleLabel.setText(String.format("Conferenza: \"%s\"", UserContext.getConferenzaAttuale().getNome()));

            RevisioneDao revisioneDao = new RevisioneDao(connection);
            UtenteDao utenteDao = new UtenteDao(connection);
            PaperDao paperDao = new PaperDao(connection);
            CoAutoriPaperDao coAutoriDao = new CoAutoriPaperDao(connection);

            List<RevisioneEntity> revisioni = revisioneDao.getByConferenza(UserContext.getConferenzaAttuale().getId());

            if (revisioni.isEmpty()) {
                Label testo = new Label("Non ci sono revisioni disponibili per questa conferenza.");
                testo.getStyleClass().addAll("font-bold", "text-rosso", "h5");
                testo.setWrapText(true);
                testo.setPrefWidth(500);
                testo.setAlignment(Pos.CENTER);
                testo.setTextAlignment(TextAlignment.CENTER);
                paperContainer.getChildren().add(testo);
                return;
            }

            for (RevisioneEntity revisione : revisioni) {

                UtenteEntity revisore = utenteDao.getById(revisione.getRefUtente());
                PaperEntity paper = paperDao.getById(revisione.getRefPaper());

                if (paper == null) continue;

                UtenteEntity autore = utenteDao.getById(paper.getRefUtente());
                Set<String> coautori = coAutoriDao.getCoautoriForPaper(paper.getId());

                StringBuilder autori = new StringBuilder();
                autori.append(autore.getNome()).append(" ").append(autore.getCognome());
                for (String co : coautori) autori.append(", ").append(co);

                double averageScore = revisioneDao.getAverageScore(paper.getId());

                Node card = creaCardRevisione(revisione, revisore, autori.toString(), averageScore);
                paperContainer.getChildren().add(card);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Node creaCardRevisione(RevisioneEntity revisione, UtenteEntity revisore, String autori, double averageScore) {
        VBox card = new VBox(20);
        card.getStyleClass().addAll("paper-card", "bg-celeste");
        card.setPadding(new Insets(25));
        card.setPrefWidth(1000);

        Label titolo = new Label("Revisione");
        titolo.getStyleClass().addAll("font-bold", "text-bianco", "h4", "ombra");

        Label testo = new Label(revisione.getTesto());
        testo.getStyleClass().addAll("p", "text-bianco");
        testo.setWrapText(true);

        Label titoloPrivato = new Label("Commento privato");
        titoloPrivato.getStyleClass().addAll("font-bold", "text-bianco", "h5", "ombra");
        Label commento = new Label(revisione.getCommentoChair());
        commento.getStyleClass().addAll("p", "text-bianco");
        commento.setWrapText(true);

        FontIcon revisoreIcon = new FontIcon("fas-user-secret");
        revisoreIcon.setIconColor(Color.WHITE);
        revisoreIcon.setIconSize(18);

        Label autore = new Label(revisore.getNome() + " " + revisore.getCognome());
        autore.getStyleClass().addAll("font-bold", "text-bianco", "ombra");
        HBox autoreBox = new HBox(10, revisoreIcon, autore);
        autoreBox.setAlignment(Pos.CENTER_LEFT);

        HBox scoreBox = new HBox(10);
        FontIcon scoreIcon = new FontIcon("fas-star");
        scoreIcon.setIconSize(24);
        scoreIcon.setIconColor(Color.WHITE);
        scoreIcon.getStyleClass().setAll("text-bianco", "ombra");

        Integer maxScore = Integer.valueOf(UserContext.getConferenzaAttuale().getMetodoValutazione().getValoreDb());
        Label labelScore = new Label(String.format("%.2f/%d", averageScore, maxScore));
        labelScore.getStyleClass().addAll("h6", "text-bianco", "ombra", "font-light");
        labelScore.setWrapText(true);
        scoreBox.setAlignment(Pos.CENTER_LEFT);
        scoreBox.getChildren().addAll(scoreIcon, labelScore);

        FontIcon autoriIcon = new FontIcon("fas-users");
        autoriIcon.setIconColor(Color.WHITE);
        autoriIcon.setIconSize(18);
        Label autoriLabel = new Label(autori);
        autoriLabel.getStyleClass().addAll("font-bold", "text-bianco", "ombra");
        HBox autoriBox = new HBox(10, autoriIcon, autoriLabel);
        autoriBox.setAlignment(Pos.CENTER_LEFT);

        Button btnConflitto = new Button("Segnala conflitto");
        btnConflitto.getStyleClass().addAll("red-button");
        btnConflitto.setOnAction(e -> handleConflitto(revisione));

        Button btnPlagio = new Button("Segnala plagio");
        btnPlagio.getStyleClass().addAll("red-button");
        btnPlagio.setOnAction(e -> handlePlagio(revisione));

        VBox leftBox = new VBox(10, autoriBox, autoreBox, scoreBox);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        VBox rightBox = new VBox(10, btnConflitto, btnPlagio);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox bottomRow = new HBox(30, leftBox, spacer, rightBox);
        bottomRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(leftBox, Priority.ALWAYS);

        card.getChildren().addAll(titolo, testo, titoloPrivato, commento, bottomRow);
        return card;
    }

    public void handleConflitto(RevisioneEntity revisione) {
        System.out.println("Conflitto segnalato per revisione: " + revisione.getId());
    }

    public void handlePlagio(RevisioneEntity revisione) {
        System.out.println("Plagio segnalato per revisione: " + revisione.getId());
    }
}
