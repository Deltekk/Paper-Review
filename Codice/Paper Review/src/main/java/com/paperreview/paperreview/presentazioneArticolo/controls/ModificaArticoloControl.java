package com.paperreview.paperreview.presentazioneArticolo.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.CoAutoriPaperDao;
import com.paperreview.paperreview.common.dbms.dao.PaperDao;
import com.paperreview.paperreview.common.dbms.dao.TopicDao;
import com.paperreview.paperreview.common.dbms.dao.TopicPaperDao;
import com.paperreview.paperreview.common.email.EmailSender;
import com.paperreview.paperreview.common.email.MailSottomissione;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.common.llm.LLMBoundary;
import com.paperreview.paperreview.MainControl;
import com.paperreview.paperreview.entities.ConferenzaEntity;
import com.paperreview.paperreview.entities.PaperEntity;
import com.paperreview.paperreview.entities.TopicEntity;
import com.paperreview.paperreview.presentazioneArticolo.forms.ModificaArticoloFormModel;
import com.paperreview.paperreview.presentazioneArticolo.forms.ModificaArticoloTopicFormModel;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

public class ModificaArticoloControl implements ControlledScreen {

    private MainControl mainControl;

    private List<TopicEntity> allTopics;
    private ModificaArticoloFormModel modificaArticoloForm;
    private ModificaArticoloTopicFormModel topicFormModel;
    private boolean form1Valid, form2Valid, isEditable;

    @FXML private VBox form1Container, form2Container;
    @FXML private Button confirmButton;
    @FXML private Label errorLabel, paperLabel;
    @FXML private TextField searchField;

    private File selectedPdfFile;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        try {
            errorLabel.setVisible(false);
            UserContext.setStandaloneInteraction(false);

            isEditable = LocalDateTime.now().isBefore(UserContext.getConferenzaAttuale().getScadenzaSottomissione());

            TopicDao topicDao = new TopicDao(DBMSBoundary.getConnection());
            allTopics = topicDao.getAll();
            List<String> nomiTopic = allTopics.stream().map(TopicEntity::getNome).toList();

            modificaArticoloForm = new ModificaArticoloFormModel("", "", 0, List.of(), isEditable);
            topicFormModel = new ModificaArticoloTopicFormModel(nomiTopic, List.of(), isEditable);

            Form form1 = modificaArticoloForm.createForm();
            FormRenderer renderer1 = new FormRenderer(form1);
            form1Container.getChildren().add(renderer1);
            removeBordersFromForm(renderer1);

            Form form2 = topicFormModel.createForm();
            FormRenderer renderer2 = new FormRenderer(form2);
            form2Container.getChildren().add(renderer2);

            modificaArticoloForm.getNumeroCoautoriField().valueProperty().addListener(this::coautoreListener);

            form1.validProperty().addListener((obs, o, n) -> {
                form1Valid = n;
                aggiornaStatoConferma();
            });

            form2.validProperty().addListener((obs, o, n) -> {
                form2Valid = n;
                aggiornaStatoConferma();
            });

            if (!isEditable) {
                confirmButton.setDisable(true);
                searchField.setDisable(true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void coautoreListener(ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
        int count = newVal != null ? newVal.intValue() : 0;

        modificaArticoloForm.salvaValoriCorrenti();
        modificaArticoloForm.setNumeroCoautori(count);

        Form updatedForm = modificaArticoloForm.createForm();
        FormRenderer updatedRenderer = new FormRenderer(updatedForm);
        form1Container.getChildren().setAll(updatedRenderer);
        updatedForm.validProperty().addListener((o, ov, nv) -> {
            form1Valid = nv;
            aggiornaStatoConferma();
        });
        modificaArticoloForm.getNumeroCoautoriField().valueProperty().addListener(this::coautoreListener);

        removeBordersFromForm(updatedRenderer);
    }

    private void removeBordersFromForm(FormRenderer formRenderer) {
        formRenderer.setStyle("-fx-border-color: transparent; -fx-border-width: 0;\n");
        form1Container.setStyle("-fx-border-color: transparent; -fx-border-width: 0;\n");

        Border noBorder = Border.EMPTY;

        formRenderer.setBorder(noBorder);
        form1Container.setBorder(noBorder);

        formRenderer.lookupAll(".formsfx-group").forEach(node -> {
            if (node instanceof Region region) {
                region.setBorder(null);
            }
        });
    }

    private void aggiornaStatoConferma() {
        boolean ready = form1Valid && form2Valid && (selectedPdfFile != null);
        confirmButton.setDisable(!ready);
    }

    @FXML
    private void handleCaricaPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona il PDF del paper");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File PDF", "*.pdf"));

        File file = fileChooser.showOpenDialog(paperLabel.getScene().getWindow());

        if (file != null && file.exists() && file.getName().toLowerCase().endsWith(".pdf")) {
            selectedPdfFile = file;
            paperLabel.setText("Paper selezionato: \"" + file.getName() + "\"");
        } else {
            selectedPdfFile = null;
            paperLabel.setText("Nessun file PDF valido selezionato.");
        }

        aggiornaStatoConferma();
    }

    @FXML
    private void handleGeneraTopics() {
        errorLabel.setVisible(false);

        if (!isEditable) {
            errorLabel.setText("Non puoi generare i topics dopo la scadenza.");
            errorLabel.setVisible(true);
            return;
        }

        if (selectedPdfFile == null) {
            errorLabel.setText("Errore: Per generare i topics in automatico devi inserire il paper!");
            errorLabel.setVisible(true);
            return;
        }

        errorLabel.setText("Generazione in corso...");
        errorLabel.setVisible(true);
        confirmButton.setDisable(true);

        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                return LLMBoundary.assegnaParoleChiave(selectedPdfFile.toPath(), allTopics);
            }
        };

        task.setOnSucceeded(event -> {
            List<String> nomiTopicSelezionati = task.getValue();
            topicFormModel.setSelectedTopics(nomiTopicSelezionati);
            aggiornaStatoConferma();

            String messaggio = String.join("\n• ", nomiTopicSelezionati);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Topic generati");
            alert.setHeaderText("Questi sono i topic rilevati dal paper:");
            alert.setContentText("• " + messaggio);
            alert.showAndWait();

            errorLabel.setVisible(false);
        });

        task.setOnFailed(event -> {
            errorLabel.setText("Errore durante la generazione automatica dei topics.");
            errorLabel.setVisible(true);
            aggiornaStatoConferma();

            Throwable ex = task.getException();
            ex.printStackTrace();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void handleConferma() {
        errorLabel.setVisible(false);

        // 🔒 Controllo periodo di sottomissione
        LocalDateTime now = LocalDateTime.now();
        ConferenzaEntity conferenza = UserContext.getConferenzaAttuale();

        boolean isInPrimoPeriodo = now.isBefore(conferenza.getScadenzaSottomissione());
        boolean isInSecondoPeriodo = now.isAfter(conferenza.getScadenzaRevisione()) &&
                now.isBefore(conferenza.getScadenzaSottomissione2());
        boolean isInTerzoPeriodo = now.isAfter(conferenza.getScadenzaEditing()) &&
                now.isBefore(conferenza.getScadenzaSottomissione3());

        if (!isInPrimoPeriodo && !isInSecondoPeriodo && !isInTerzoPeriodo) {
            if (!isInPrimoPeriodo && now.isBefore(conferenza.getScadenzaRevisione())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Periodo non valido");
                alert.setHeaderText("Non è possibile modificare la sottomissione in questo momento.");
                alert.setContentText("Le modifiche sono consentite solo prima della prima scadenza di sottomissione.");
                alert.showAndWait();
                return;
            }

            if (!isInSecondoPeriodo && now.isAfter(conferenza.getScadenzaRevisione()) &&
                    now.isBefore(conferenza.getScadenzaEditing())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Periodo non valido");
                alert.setHeaderText("Non è possibile modificare la sottomissione in questo momento.");
                alert.setContentText("Le modifiche sono consentite solo dopo la revisione e prima della seconda scadenza di sottomissione.");
                alert.showAndWait();
                return;
            }

            if (!isInTerzoPeriodo && now.isAfter(conferenza.getScadenzaEditing())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Periodo non valido");
                alert.setHeaderText("Non è possibile modificare la sottomissione in questo momento.");
                alert.setContentText("Le modifiche sono consentite solo dopo l'editing e prima della terza scadenza di sottomissione.");
                alert.showAndWait();
                return;
            }

            // Fallback generico se non rientra in nessun periodo noto
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Periodo non valido");
            alert.setHeaderText("Non è possibile modificare la sottomissione in questo momento.");
            alert.setContentText("Le modifiche sono consentite solo nei periodi tra le date di sottomissione.");
            alert.showAndWait();
            return;
        }

        List<String> topicNomi = topicFormModel.getSelectedTopics();
        if (topicNomi.isEmpty()) {
            errorLabel.setText("Errore: devi selezionare almeno 1 topic!");
            errorLabel.setVisible(true);
            return;
        }

        if (selectedPdfFile == null) {
            errorLabel.setText("Errore: devi caricare un file PDF valido!");
            errorLabel.setVisible(true);
            return;
        }

        try {
            modificaArticoloForm.salvaValoriCorrenti();
            String titolo = modificaArticoloForm.getTitolo();
            String abstractPaper = modificaArticoloForm.getAbstract();
            List<String> coautori = modificaArticoloForm.getListaEmailCoautori();

            PaperDao paperDao = new PaperDao(DBMSBoundary.getConnection());
            PaperEntity paper = paperDao.getById(UserContext.getPaperAttuale().getId()); // recupero esistente

            // Aggiorna i campi
            paper.setTitolo(titolo);
            paper.setAbstractPaper(abstractPaper);
            paper.setFile(Files.readAllBytes(selectedPdfFile.toPath()));
            paper.setDataSottomissione(LocalDateTime.now());

            // Salva modifiche
                        paperDao.update(paper);

            int paperId = paper.getId();

            // Rimuovi coautori e topic precedenti
            CoAutoriPaperDao coAutoriDao = new CoAutoriPaperDao(DBMSBoundary.getConnection());
            coAutoriDao.removeAllCoautoriFromPaper(paperId);

            TopicPaperDao topicPaperDao = new TopicPaperDao(DBMSBoundary.getConnection());
            topicPaperDao.removeAllTopicsFromPaper(paperId);

            // Aggiungi coautori nuovi
            for (String email : coautori) {
                coAutoriDao.addCoautoreToPaper(email, paperId);
                EmailSender.sendEmail(new MailSottomissione(email, UserContext.getConferenzaAttuale().getNome(), paper.getTitolo(), email));
            }

            // Aggiungi topic nuovi
            for (TopicEntity topic : allTopics) {
                if (topicNomi.contains(topic.getNome())) {
                    topicPaperDao.addTopicToPaper(topic.getId(), paperId);
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sottomissione completata");
            alert.setHeaderText("Articolo sottomesso con successo!");
            alert.setContentText("Riceverai aggiornamenti nella sezione delle tue sottomissioni.");
            alert.showAndWait();

            mainControl.setView("/com/paperreview/paperreview/boundaries/presentazioneArticolo/visualizzaSchermataSottomissioni/visualizzaSchermataSottomissioniBoundary.fxml");

        } catch (Exception e) {
            errorLabel.setText("Errore durante la sottomissione dell'articolo");
            errorLabel.setVisible(true);
            e.printStackTrace();
        }
    }
}
