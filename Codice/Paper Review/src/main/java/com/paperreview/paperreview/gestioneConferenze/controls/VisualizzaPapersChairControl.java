package com.paperreview.paperreview.gestioneConferenze.controls;

import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.CoAutoriPaperDao;
import com.paperreview.paperreview.common.dbms.dao.PaperDao;
import com.paperreview.paperreview.common.dbms.dao.RevisioneDao;
import com.paperreview.paperreview.common.dbms.dao.UtenteDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.ConferenzaEntity;
import com.paperreview.paperreview.entities.PaperEntity;
import com.paperreview.paperreview.entities.RevisioneEntity;
import com.paperreview.paperreview.entities.UtenteEntity;
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VisualizzaPapersChairControl implements ControlledScreen {

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
        try{
            PaperDao paperDao = new PaperDao(DBMSBoundary.getConnection());
            List<PaperEntity> papers = paperDao.getPapersByConferenza(UserContext.getConferenzaAttuale().getId());

            conferenceTitleLabel.setText( String.format("Conferenza: \"%s\"", UserContext.getConferenzaAttuale().getNome()));

            if(papers.isEmpty())
            {
                Label testo = new Label("Non è stato pubblicato ancora nessun paper!");
                testo.getStyleClass().addAll("font-bold", "text-rosso", "h5");
                testo.setWrapText(true);
                testo.setPrefWidth(500);
                testo.setAlignment(Pos.CENTER);
                testo.setTextAlignment(TextAlignment.CENTER);

                paperContainer.getChildren().add(testo);
            }
            else
            {
                for (PaperEntity paper : papers) {
                    Node card = creaPaperCard(paper);
                    paperContainer.getChildren().add(card);
                }
            }
        } catch (Exception e) {
            // TODO: Gestire questo errore
            throw new RuntimeException(e);
        }

    }

    private Node creaPaperCard(PaperEntity paper) throws SQLException {
        HBox card = new HBox(20); // Spazio tra gli elementi
        card.getStyleClass().addAll("paper-card", "bg-celeste");
        card.setPrefWidth(1000);
        card.setPrefHeight(400);
        card.setAlignment(Pos.CENTER);

        /* TESTO A SINISTRA */

        VBox textContainer = new VBox(10);
        textContainer.setAlignment(Pos.CENTER_LEFT);
        textContainer.setPrefWidth(700);
        textContainer.getStyleClass().addAll("text-container");

        // Nome paper

        Label labelNomeConferenza = new Label(String.format("\"%s\"", paper.getTitolo()));
        labelNomeConferenza.getStyleClass().addAll("font-bold", "text-bianco", "h5", "ombra");
        labelNomeConferenza.setWrapText(true);
        labelNomeConferenza.setPrefWidth(700);

        // -----  Autori paper ------

        HBox autoriBox = new HBox(10);

        // Autori icon

        FontIcon autoriIcon = new FontIcon("fas-users");
        autoriIcon.setIconSize(24);
        autoriIcon.setIconColor(Color.WHITE);
        autoriIcon.getStyleClass().add("ombra");

        // Prendiamo l'autore principale

        UtenteDao utenteDao = new UtenteDao(DBMSBoundary.getConnection());
        UtenteEntity autore = utenteDao.getById(paper.getRefUtente());

        // Prendiamo i coautori

        CoAutoriPaperDao coAutoriPaperDao = new CoAutoriPaperDao(DBMSBoundary.getConnection());
        Set<String> coautori = coAutoriPaperDao.getCoautoriForPaper(paper.getId());

        String testoLabelAutori = String.format("%s %s, ", autore.getNome(), autore.getCognome());

        int i = 0;
        for (String coautore : coautori) {
            if (i > 0) {
                testoLabelAutori += ", ";  // Aggiungi la virgola solo se non è il primo coautore
            }
            testoLabelAutori += coautore;
            i++;
        }

        // Label autori

        Label labelAutori = new Label(testoLabelAutori);
        labelAutori.getStyleClass().addAll("font-bold", "text-bianco", "h6", "ombra");
        labelAutori.setWrapText(true);
        labelAutori.setPrefWidth(700);

        // Layout autori

        autoriBox.getChildren().addAll(autoriIcon, labelAutori);

        /* ----- Revisori paper ----- */

        HBox revisoriBox = new HBox(10);

        // Icona Revisori

        FontIcon revisoriIcon = new FontIcon("fas-user-tie");
        revisoriIcon.setIconSize(24);
        revisoriIcon.setIconColor(Color.WHITE); // o altro colore adatto
        revisoriIcon.getStyleClass().setAll("text-bianco", "ombra");

        // Prendiamo i revisori

        RevisioneDao revisioneDao = new RevisioneDao(DBMSBoundary.getConnection());
        ArrayList<RevisioneEntity> revisioni = (ArrayList<RevisioneEntity>) revisioneDao.getByPaper(paper.getId());

        String testoLabelRevisori = "";

        for (i = 0; i < revisioni.size(); i++) {

            UtenteEntity utente = utenteDao.getById(revisioni.get(i).getRefUtente());

            testoLabelRevisori += String.format("%s %s", utente.getNome(), utente.getCognome());

            if (i < revisioni.size() - 1)
                testoLabelRevisori += ", ";

        }

        Label labelRevisori = new Label(testoLabelRevisori);

        if(revisioni.size() == 0)
        {
            testoLabelRevisori = "NON È STATO ASSEGNATO NESSUN REVISORE A QUESTO PAPER!";
            labelRevisori.setText(testoLabelRevisori);
            labelRevisori.setWrapText(true);
            labelRevisori.getStyleClass().addAll("text-rosso");
        }

        labelRevisori.getStyleClass().addAll("h6", "text-bianco", "ombra", "font-light");
        labelRevisori.setWrapText(true);
        revisoriBox.setAlignment(Pos.CENTER_LEFT);

        revisoriBox.getChildren().addAll(revisoriIcon, labelRevisori);

        /* ----- Score paper ----- */

        HBox scoreBox = new HBox(10);

        // Icona Score

        FontIcon scoreIcon = new FontIcon("fas-star");
        scoreIcon.setIconSize(24);
        scoreIcon.setIconColor(Color.WHITE); // o altro colore adatto
        scoreIcon.getStyleClass().setAll("text-bianco", "ombra");

        // Score medio e massimo

        double averageScore = revisioneDao.getAverageScore(paper.getId());
        Integer maxScore = Integer.valueOf(UserContext.getConferenzaAttuale().getMetodoValutazione().getValoreDb());

        // Label

        Label labelScore = new Label(String.format("%.1f/%d", averageScore, maxScore));
        labelScore.getStyleClass().addAll("h6", "text-bianco", "ombra", "font-light");
        labelScore.setWrapText(true);
        revisoriBox.setAlignment(Pos.CENTER_LEFT);

        scoreBox.getChildren().addAll(scoreIcon, labelScore);

        textContainer.getChildren().setAll(labelNomeConferenza, autoriBox, revisoriBox, scoreBox);

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

        Button buttonPruomoviPaper= new Button("Promuovi Paper");
        Button buttonScaricaPaper = new Button("Scarica Paper");
        Button buttonRevisioneEmergenza = new Button("Revisione d'emergenza");
        Button buttonVisualizzaDettagli = new Button("Visualizza Dettagli");
        Button buttonRimuoviPaper = new Button("Rimuovi Paper");

        buttonPruomoviPaper.getStyleClass().addAll("green-button", "grid-button-single");
        buttonScaricaPaper.getStyleClass().addAll("blue-button", "grid-button-single");
        buttonRevisioneEmergenza.getStyleClass().addAll("blue-button", "grid-button-single");
        buttonVisualizzaDettagli.getStyleClass().addAll("blue-button", "grid-button-single");
        buttonRimuoviPaper.getStyleClass().addAll("red-button", "grid-button-double");

        buttonPruomoviPaper.setPrefWidth(Double.MAX_VALUE);
        buttonScaricaPaper.setPrefWidth(Double.MAX_VALUE);
        buttonRevisioneEmergenza.setPrefWidth(Double.MAX_VALUE);
        buttonVisualizzaDettagli.setPrefWidth(Double.MAX_VALUE);
        buttonRimuoviPaper.setPrefWidth(Double.MAX_VALUE);

        buttonPruomoviPaper.setOnAction(event -> handlePromuoviPaper(paper));
        buttonScaricaPaper.setOnAction(event -> handleScaricaPaper(paper));
        buttonRevisioneEmergenza.setOnAction(event -> handleRevisioneEmergenza(paper));
        buttonVisualizzaDettagli.setOnAction(event -> handleVisualizzaDettagli(paper));
        buttonRimuoviPaper.setOnAction(event -> handleRimuoviPaper(paper));

        buttonGrid.add(buttonPruomoviPaper, 0, 0);
        buttonGrid.add(buttonScaricaPaper, 1, 0);
        buttonGrid.add(buttonRevisioneEmergenza, 0, 1);
        buttonGrid.add(buttonVisualizzaDettagli, 1, 1);
        buttonGrid.add(buttonRimuoviPaper, 0, 2, 2, 1);

        /* COMPOSIZIONE: [Testo] [Spacer] [Bottoni] */

        card.getChildren().addAll(textContainer, spacer, buttonGrid);

        return card;
    }

    public void handlePromuoviPaper(PaperEntity paper)
    {
        // TODO: HandlePromuoviPaper

        // Visto che non stiamo gestendo lo score in modo giusto, pensavo che potremmo mettere un valore negativo
        // allo score in modo tale che sappiamo che sia promosso

    }

    public void handleScaricaPaper(PaperEntity paper)
    {
        // TODO: HandleScaricaPaper
        // Teoricamente qui dovremmo mettere la funzione che prende e scarica il file
    }

    public void handleRevisioneEmergenza(PaperEntity paper)
    {
        // TODO: HandleRevisioneEmergenza
    }

    public void handleVisualizzaDettagli(PaperEntity paper)
    {
        // TODO: HandleVisualizza Dettagli, immagino come abbiamo fatto prima lascio il commento
        // UserContext.setConferenzaAttuale(conferenza);
        // mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/aggiungiRevisore/aggiungiRevisoreBoundary.fxml");

    }

    public void handleRimuoviPaper(PaperEntity paper)
    {
        // TODO: HandleRimuuoviPaper
        // Idealmente almeno un alert per sicurezza
    }
    
}
