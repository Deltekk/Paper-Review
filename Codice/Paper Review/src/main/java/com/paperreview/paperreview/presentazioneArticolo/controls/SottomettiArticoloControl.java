package com.paperreview.paperreview.presentazioneArticolo.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.TopicDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.common.llm.LLMBoundary;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.TopicEntity;
import com.paperreview.paperreview.presentazioneArticolo.forms.SottomettiArticoloFormModel;
import com.paperreview.paperreview.presentazioneArticolo.forms.SottomettiArticoloTopicFormModel;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

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
            UserContext.setConferenzaAttuale(null);
            UserContext.setStandaloneInteraction(false);

            // 1. Form iniziale (autori, titolo, ecc.)
            Form form1 = sottomettiArticoloForm.createForm();
            FormRenderer formRenderer = new FormRenderer(form1);
            form1Container.getChildren().add(formRenderer);
            removeBordersFromForm(formRenderer);

            sottomettiArticoloForm.getNumeroCoautoriField().valueProperty().addListener(this::coautoreListener);

            form1.validProperty().addListener((obs, oldVal, newVal) -> {
                form1Valid = newVal;
                confirmButton.setDisable(!form1Valid || !form2Valid);
            });

            // 2. Carica e mostra i topic con checkbox
            TopicDao topicDao = new TopicDao(DBMSBoundary.getConnection());
            allTopics = topicDao.getAll();

            populateCheckBoxes(allTopics);

            form2Valid = false; // inizialmente invalid
            confirmButton.setDisable(true);

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
        confirmButton.setDisable(!form1Valid || !form2Valid);
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
            confirmButton.setDisable(!form1Valid || !form2Valid);
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

    @FXML private void handleCaricaPDF() {

        /*
            TODO:
                - Aprire una finestra per fare selezionare il PDF all'autore
                - Immagazzinare la path in una variabile per poterlo utilizzare nell'assegnazione automatica con la LLM.
                - Aggiornare la paperLabel (che trovi in basso) con il nome del file
                - Disabilitare il pulsante conferma se il pdf non è stato ancora inserito o c'è stato un errore, da controllare anche nei listener dei form.
                - MI RACCOMANDO DIEGO FAI I TEST DI TUTTO
         */

        System.out.println("Carica PDF");

        // TODO: Modificare questa linea con il nome del file!
        paperLabel.setText("Paper selezionato: \"NOME PAPER!\"");

    }

    @FXML private void handleGeneraTopics() {

        errorLabel.setVisible(false);

        // TODO: Fare controllo se è stato inserito il paper
        errorLabel.setVisible(true);
        errorLabel.setText("Errore: Per generare i topics in automatico devi inserire il paper!");

        /*
            TODO:
                - Prendere il paper inserito e mandarlo all'API tramite LLM Boundary, LA CLASSE E' GIA FATTA DIEGO
                - Gestire il caso nella quale la LLM non risponda bene, (tira un Exception) immagino semplicemente usando un popup che avverta l'utente dell'avvenuto errore.
                - Aggiornare le checkbox ed assicurarsi che funzionino (DIEGO FAI I TEST, NON PUSHARE A CAZZO DI CANE)
        */

        // ⚠️⚠️⚠️️ DIEGO USA QUESTA -----> LLMBoundary.assegnaParoleChiave(Path pdfPath, List<TopicEntity> listaParoleChiave);
        // ⚠️⚠️⚠️️ hai già allTopics per le topicEntity, glie le devi passare perché si aspetta quelle.

        System.out.println("Genera Topics");
    }

    @FXML private void handleConferma() {
        errorLabel.setVisible(false);

        List<TopicEntity> selectedTopics = checkBoxes.stream()
                .filter(CheckBox::isSelected)
                .map(cb -> (TopicEntity) cb.getUserData())
                .toList();

        if (selectedTopics.size() < 1) {
            errorLabel.setText("Errore: devi selezionare almeno 1 topic!");
            errorLabel.setVisible(true);
            return;
        }

        System.out.println("Hai selezionato i seguenti topic:");
        for (TopicEntity topic : selectedTopics) {
            System.out.println(" - " + topic.getNome());
        }

        // TODO: salva articolo e topic associati

        mainControl.setView("/com/paperreview/paperreview/boundaries/presentazioneArticolo/visualizzaSchermataSottomissioni/visualizzaSchermataSottomissioniBoundary.fxml");
    }
}
