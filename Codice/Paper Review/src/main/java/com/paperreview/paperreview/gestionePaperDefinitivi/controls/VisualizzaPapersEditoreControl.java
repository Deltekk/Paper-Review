package com.paperreview.paperreview.gestionePaperDefinitivi.controls;

import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.ConferenzaDao;
import com.paperreview.paperreview.common.dbms.dao.PaperDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.MainControl;
import com.paperreview.paperreview.entities.ConferenzaEntity;
import com.paperreview.paperreview.entities.PaperEntity;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
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

    public void handleScaricaPaper(PaperEntity paper) {
        if (paper == null) {
            mostraErrore("Errore: nessun paper selezionato.");
            return;
        }

        try {
            // 5. Data attuale
            LocalDateTime now = LocalDateTime.now();

            // 6–7. Date di inizio e fine scaricamento
            ConferenzaDao conferenzaDao = new ConferenzaDao(DBMSBoundary.getConnection());
            ConferenzaEntity conferenza = conferenzaDao.getById(paper.getRefConferenza());

            if (conferenza == null) {
                mostraErrore("Errore: conferenza non trovata.");
                return;
            }

            LocalDateTime inizioScaricamento = conferenza.getScadenzaSottomissione2();
            LocalDateTime fineScaricamento = conferenza.getScadenzaImpaginazione();

            // 8–10: Controlli
            if (now.isBefore(inizioScaricamento)) {
                mostraErrore("Errore: non è ancora possibile scaricare il paper");
                return;
            }

            if (now.isAfter(fineScaricamento)) {
                mostraErrore("Errore: non puoi più scaricare il paper");
                return;
            }

            // 9.1–9.2: Scaricamento del paper
            byte[] file = paper.getFile(); // già presente nell'entità PaperEntity
            String nomeFile = paper.getTitolo().replaceAll("[^a-zA-Z0-9_\\-]", "_") + ".pdf";

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salva paper come");
            fileChooser.setInitialFileName(nomeFile);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF file", "*.pdf"));

            File fileScelto = fileChooser.showSaveDialog(null); // puoi passare lo stage se ne hai uno

            if (fileScelto != null) {
                try (FileOutputStream out = new FileOutputStream(fileScelto)) {
                    out.write(file);
                    mostraConferma("Download completato con successo!", () -> {});
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostraErrore("Errore durante il download del paper.");
        }
    }

    public void handleInviaFeedback(PaperEntity paper)
    {
        UserContext.setPaperAttuale(paper);
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestionePaperDefinitivi/inviaFeedback/inviaFeedbackBoundary.fxml");

    }

    private void mostraErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    private void mostraConferma(String messaggio, Runnable azioneDopoOk) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Conferma");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
        azioneDopoOk.run();
    }
}