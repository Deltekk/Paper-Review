package com.paperreview.paperreview.gestionePaperDefinitivi.controls;

import com.paperreview.paperreview.common.CustomDateParser;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.ConferenzaDao;
import com.paperreview.paperreview.common.dbms.dao.PaperDao;
import com.paperreview.paperreview.common.dbms.dao.ProceedingDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.MainControl;
import com.paperreview.paperreview.entities.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

            UserContext.setStandaloneInteraction(false);

            ConferenzaDao conferenzaDao = new ConferenzaDao(DBMSBoundary.getConnection());
            //List<ConferenzaEntity> conferenze = conferenzaDao.getAllByIdAndRuolo(UserContext.getUtente().getId(), Ruolo.Editor);

            LocalDate oggi = LocalDate.now();

            List<ConferenzaEntity> conferenze = conferenzaDao
                    .getAllByIdAndRuolo(UserContext.getUtente().getId(), Ruolo.Editor)
                    .stream()
                    .filter(c -> {
                        LocalDate sottomissione2 = c.getScadenzaSottomissione2().toLocalDate();
                        LocalDate editing = c.getScadenzaEditing().toLocalDate();
                        LocalDate sottomissione3 = c.getScadenzaSottomissione3().toLocalDate();
                        LocalDate proceedings = c.getScadenzaImpaginazione().toLocalDate();

                        boolean traSottomissione2eEditing =
                                (!oggi.isBefore(sottomissione2)) && oggi.isBefore(editing);

                        boolean traSottomissione3eProceedings =
                                (!oggi.isBefore(sottomissione3)) && oggi.isBefore(proceedings);

                        return traSottomissione2eEditing || traSottomissione3eProceedings;
                    })
                    .collect(Collectors.toList());

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

    public void handleScaricaPapers(ConferenzaEntity conferenza) {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime inizioScaricamento = conferenza.getScadenzaSottomissione2();
            LocalDateTime fineScaricamento = conferenza.getScadenzaImpaginazione();

            if (now.isBefore(inizioScaricamento)) {
                mostraErrore("Errore: non è ancora possibile scaricare i paper. Disponibili da: " + inizioScaricamento);
                return;
            }

            if (now.isAfter(fineScaricamento)) {
                mostraErrore("Errore: non puoi più scaricare i paper. Scadenza: " + fineScaricamento);
                return;
            }

            // 6.1: recupera tutti i paper della conferenza
            PaperDao paperDao = new PaperDao(DBMSBoundary.getConnection());
            List<PaperEntity> papers = paperDao.getPapersByConferenza(conferenza.getId());

            if (papers.isEmpty()) {
                mostraErrore("Nessun paper trovato per questa conferenza.");
                return;
            }

            // 6.2: chiedi all’utente dove salvare lo ZIP
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salva archivio ZIP dei paper");
            fileChooser.setInitialFileName("papers_conferenza_" + conferenza.getNome() + ".zip");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivio ZIP", "*.zip"));

            File fileZip = fileChooser.showSaveDialog(null);
            if (fileZip == null) return;

            // Crea ZIP
            try (FileOutputStream fos = new FileOutputStream(fileZip);
                 ZipOutputStream zipOut = new ZipOutputStream(fos)) {

                for (PaperEntity paper : papers) {
                    if (paper.getFile() == null) continue;

                    String filename = paper.getTitolo().replaceAll("[^a-zA-Z0-9_\\-]", "_") + ".pdf";
                    ZipEntry entry = new ZipEntry(filename);
                    zipOut.putNextEntry(entry);
                    zipOut.write(paper.getFile());
                    zipOut.closeEntry();
                }
            }

            mostraConferma("Download completato con successo!", () -> {
                // passo 8: torna eventualmente a schermata EDITING (opzionale)
            });

        } catch (Exception e) {
            e.printStackTrace();
            mostraErrore("Errore durante il download dei paper.");
        }
    }

    public void handleCaricaProceedings(ConferenzaEntity conferenza, VBox card)
    {

        // Controlla se è passata la data per caricare i paper con le modifiche editoriali
        if (LocalDate.now().isBefore(conferenza.getScadenzaSottomissione3().toLocalDate())) {
            Alert expiredAlert = new Alert(Alert.AlertType.WARNING);
            expiredAlert.setTitle("Operazione non consentita");
            expiredAlert.setHeaderText("Data di adeguamento formato da raggiungere!");
            String messaggio = String.format(
                    "Ancora non è possibile caricare i proceedings, riprova dopo giorno \"%s\".",
                    conferenza.getScadenzaSottomissione3().toLocalDate()
            );

            // Forza il testo a capo
            Label label = new Label(messaggio);
            label.setWrapText(true);
            label.setMaxWidth(400); // Imposta una larghezza massima adeguata
            expiredAlert.getDialogPane().setContent(label);
            expiredAlert.showAndWait();
            return;
        }

        // Controlla se è passata la data per caricare i proceedings
        if (LocalDate.now().isAfter(conferenza.getScadenzaImpaginazione().toLocalDate())) {
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
        // Apertura file chooser per selezionare PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona il file PDF dei proceedings");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(card.getScene().getWindow());

        if (selectedFile == null) return; // annullato

        try (FileInputStream fis = new FileInputStream(selectedFile)) {
            byte[] fileBytes = fis.readAllBytes();

            ProceedingEntity proceeding = new ProceedingEntity(
                    selectedFile.getName(),
                    UserContext.getUtente().getId(),
                    conferenza.getId()
            );
            proceeding.setFile(fileBytes);
            proceeding.setDataSottomissione(LocalDateTime.now());

            ProceedingDao proceedingDao = new ProceedingDao(DBMSBoundary.getConnection());
            proceedingDao.save(proceeding);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Caricamento completato");
            successAlert.setHeaderText("Proceedings caricati con successo!");
            successAlert.setContentText("Il file \"" + selectedFile.getName() + "\" è stato salvato.");
            successAlert.showAndWait();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Errore");
            errorAlert.setHeaderText("Errore durante il caricamento dei proceedings");
            errorAlert.setContentText("Dettagli: " + e.getMessage());
            errorAlert.showAndWait();
        }
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
