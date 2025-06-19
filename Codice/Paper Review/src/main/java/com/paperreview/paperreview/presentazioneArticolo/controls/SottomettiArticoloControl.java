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
import com.paperreview.paperreview.gestioneNotifiche.MainControl;
import com.paperreview.paperreview.entities.PaperEntity;
import com.paperreview.paperreview.entities.TopicEntity;
import com.paperreview.paperreview.presentazioneArticolo.forms.SottomettiArticoloFormModel;
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
import java.util.ArrayList;
import java.util.List;

public class SottomettiArticoloControl implements ControlledScreen {

    private MainControl mainControl;

    private List<TopicEntity> allTopics;
    private List<CheckBox> checkBoxes = new ArrayList<>();

    @FXML private VBox form1Container, form2Container;
    @FXML private Button confirmButton;
    @FXML private Label errorLabel, paperLabel;
    @FXML private TextField searchField;

    private SottomettiArticoloFormModel sottomettiArticoloForm = new SottomettiArticoloFormModel();
    private boolean form1Valid, form2Valid;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        try {
            errorLabel.setVisible(false);
            UserContext.setStandaloneInteraction(false);

            // 1. Form iniziale (autori, titolo, ecc.)
            Form form1 = sottomettiArticoloForm.createForm();
            FormRenderer formRenderer = new FormRenderer(form1);
            form1Container.getChildren().add(formRenderer);
            removeBordersFromForm(formRenderer);

            sottomettiArticoloForm.getNumeroCoautoriField().valueProperty().addListener(this::coautoreListener);

            form1.validProperty().addListener((obs, oldVal, newVal) -> {
                form1Valid = newVal;
                aggiornaStatoConferma();
            });

            // 2. Carica e mostra i topic con checkbox
            TopicDao topicDao = new TopicDao(DBMSBoundary.getConnection());
            allTopics = topicDao.getAll();

            populateCheckBoxes(allTopics);

            form2Valid = false; // inizialmente invalid
            aggiornaStatoConferma();

            // 3. Filtro
            searchField.textProperty().addListener((obs, oldVal, newVal) -> filterTopics(newVal));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void populateCheckBoxes(List<TopicEntity> topics) {
        checkBoxes.clear();
        form2Container.getChildren().clear();

        for (TopicEntity topic : topics) {
            CheckBox cb = new CheckBox(topic.getNome());
            cb.setUserData(topic);
            cb.selectedProperty().addListener((obs, oldVal, newVal) -> {
                validateTopics();
            });
            checkBoxes.add(cb);
            form2Container.getChildren().add(cb);
        }

        validateTopics(); // iniziale
    }

    private void filterTopics(String filter) {
        String lowerFilter = filter.toLowerCase();

        List<CheckBox> filtered = checkBoxes.stream()
                .filter(cb -> cb.getText().toLowerCase().contains(lowerFilter))
                .toList();

        form2Container.getChildren().setAll(filtered);
    }

    private void validateTopics() {
        long selectedCount = checkBoxes.stream().filter(CheckBox::isSelected).count();
        form2Valid = selectedCount >= 1;
        aggiornaStatoConferma();
    }

    private void coautoreListener(ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
        int count = newVal != null ? newVal.intValue() : 0;

        sottomettiArticoloForm.salvaValoriCorrenti();
        sottomettiArticoloForm.setNumeroCoautori(count);

        Form updatedForm = sottomettiArticoloForm.createForm();
        FormRenderer updatedRenderer = new FormRenderer(updatedForm);
        form1Container.getChildren().setAll(updatedRenderer);
        updatedForm.validProperty().addListener((o, ov, nv) -> {
            form1Valid = nv;
            aggiornaStatoConferma();
        });
        sottomettiArticoloForm.getNumeroCoautoriField().valueProperty().addListener(this::coautoreListener);

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

    private File selectedPdfFile;

    private void aggiornaStatoConferma() {
        boolean ready = form1Valid && form2Valid && (selectedPdfFile != null);
        confirmButton.setDisable(!ready);
    }

    @FXML
    private void handleCaricaPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona il PDF del paper");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("File PDF", "*.pdf")
        );

        File file = fileChooser.showOpenDialog(paperLabel.getScene().getWindow());

        if (file != null && file.exists() && file.getName().toLowerCase().endsWith(".pdf")) {
            selectedPdfFile = file;
            paperLabel.setText("Paper selezionato: \"" + file.getName() + "\"");
        } else {
            selectedPdfFile = null;
            paperLabel.setText("Nessun file PDF valido selezionato.");
        }

        aggiornaStatoConferma();  // aggiorna il confirmButton in base a tutti i criteri
    }

    @FXML
    private void handleGeneraTopics() {
        errorLabel.setVisible(false);

        if (selectedPdfFile == null) {
            errorLabel.setText("Errore: Per generare i topics in automatico devi inserire il paper!");
            errorLabel.setVisible(true);
            return;
        }

        // Mostra "Generazione in corso..." e disabilita pulsante
        errorLabel.setText("Generazione in corso...");
        errorLabel.setVisible(true);
        confirmButton.setDisable(true);

        // Esegui in background per non bloccare l'interfaccia
        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                return LLMBoundary.assegnaParoleChiave(selectedPdfFile.toPath(), allTopics);
            }
        };

        task.setOnSucceeded(event -> {
            List<String> nomiTopicSelezionati = task.getValue();

            // Seleziona i checkbox corrispondenti
            for (CheckBox cb : checkBoxes) {
                TopicEntity topic = (TopicEntity) cb.getUserData();
                // Se il topic è suggerito oppure già selezionato → lascialo selezionato
                boolean giàSelezionato = cb.isSelected();
                boolean suggerito = nomiTopicSelezionati.contains(topic.getNome());
                cb.setSelected(giàSelezionato || suggerito);
            }

            validateTopics(); // aggiorna form2Valid
            aggiornaStatoConferma(); // riattiva il pulsante se tutto valido

            // Mostra popup con i topic trovati
            String messaggio = String.join("\n• ", nomiTopicSelezionati);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Topic generati");
            alert.setHeaderText("Questi sono i topic rilevati dal paper:");
            alert.setContentText("• " + messaggio);
            alert.showAndWait();

            errorLabel.setVisible(false);
            System.out.println("Topics generati: " + nomiTopicSelezionati);
        });

        task.setOnFailed(event -> {
            errorLabel.setText("Errore durante la generazione automatica dei topics.");
            errorLabel.setVisible(true);
            aggiornaStatoConferma();

            Throwable ex = task.getException();
            System.err.println("Errore LLM: " + ex.getMessage());
            ex.printStackTrace();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }


    @FXML
    private void handleConferma() {
        errorLabel.setVisible(false);

        // 🔒 Controllo: almeno un topic selezionato
        List<TopicEntity> selectedTopics = checkBoxes.stream()
                .filter(CheckBox::isSelected)
                .map(cb -> (TopicEntity) cb.getUserData())
                .toList();

        if (selectedTopics.isEmpty()) {
            errorLabel.setText("Errore: devi selezionare almeno 1 topic!");
            errorLabel.setVisible(true);
            return;
        }

        // 🔒 Controllo: PDF caricato
        if (selectedPdfFile == null) {
            errorLabel.setText("Errore: devi caricare un file PDF valido!");
            errorLabel.setVisible(true);
            return;
        }

        try {
            // ✅ 1. Salva dati del form
            sottomettiArticoloForm.salvaValoriCorrenti();
            String titolo = sottomettiArticoloForm.getTitolo();
            String abstractPaper = sottomettiArticoloForm.getAbstract();
            List<String> coautori =sottomettiArticoloForm.getListaEmailCoautori();

            // ✅ 2. Costruisci PaperEntity
            PaperEntity paper = new PaperEntity(titolo, abstractPaper,
                    Files.readAllBytes(selectedPdfFile.toPath()), LocalDateTime.now(),
                    UserContext.getUtente().getId(), UserContext.getConferenzaAttuale().getId());

            // ✅ 3. Salva il paper nel DB
            PaperDao paperDao = new PaperDao(DBMSBoundary.getConnection());
            paperDao.save(paper); // ID generato automaticamente

            int paperId = paper.getId();

            // ✅ 4. Salva i coautori
            CoAutoriPaperDao coAutoriDao = new CoAutoriPaperDao(DBMSBoundary.getConnection());
            String titoloPaper = titolo;
            String emailAutore = UserContext.getUtente().getEmail(); // oppure getUsername()
            String nomeConferenza = UserContext.getConferenzaAttuale().getNome();

            for (String email : coautori) {
                coAutoriDao.addCoautoreToPaper(email, paperId);
                EmailSender.sendEmail(new MailSottomissione(email, nomeConferenza, titoloPaper, emailAutore));
            }

            // ✅ 5. Salva i topic selezionati
            TopicPaperDao topicPaperDao = new TopicPaperDao(DBMSBoundary.getConnection());
            for (TopicEntity topic : selectedTopics) {
                topicPaperDao.addTopicToPaper(topic.getId(), paperId);
            }

            // ✅ 6. Mostra conferma visiva
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sottomissione completata");
            alert.setHeaderText("Articolo sottomesso con successo!");
            alert.setContentText("Riceverai aggiornamenti nella sezione delle tue sottomissioni.");
            alert.showAndWait();

            // ✅ 7. Vai alla schermata successiva
            mainControl.setView("/com/paperreview/paperreview/boundaries/presentazioneArticolo/visualizzaSchermataSottomissioni/visualizzaSchermataSottomissioniBoundary.fxml");

        } catch (Exception e) {
            errorLabel.setText("Errore durante la sottomissione dell'articolo");
            errorLabel.setVisible(true);
            e.printStackTrace();
        }
    }
}
