package com.paperreview.paperreview.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.DBMSBoundary;
import com.paperreview.paperreview.common.TopicDao;
import com.paperreview.paperreview.entities.TopicEntity;
import com.paperreview.paperreview.forms.TopicFormModel;
import com.paperreview.paperreview.interfaces.ControlledScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javax.swing.text.html.FormView;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ModificaTopicControl implements ControlledScreen {

    @FXML private Label errorLabel;
    @FXML private VBox formContainer;

    @FXML private TextField searchField; // Campo per la ricerca

    @FXML private Button confirmButton;

    private TopicDao topicDao;
    private MainControl mainControl;

    private TopicFormModel topicForm;
    private ObservableList<CheckBox> checkBoxes = FXCollections.observableArrayList();

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }


    @FXML
    public void initialize() {
        try {
            // Otteniamo tutti i topic dal database
            topicDao = new TopicDao(DBMSBoundary.getConnection());
            List<TopicEntity> topics = topicDao.getAll();

            // Creiamo il form
            topicForm = new TopicFormModel(topics.stream().map(TopicEntity::getNome).toList());

            // Aggiungiamo il form nel contenitore

            Form form = topicForm.createForm();

            FormRenderer formRenderer = new FormRenderer(form);
            formContainer.getChildren().add(formRenderer);

            // Popola le checkbox
            populateCheckBoxes(topics);

            searchField.textProperty().addListener((obs, oldVal, newVal) -> filterTopics(newVal));

            formRenderer.setStyle("-fx-border-color: transparent; -fx-border-width: 0;\n");
            formContainer.setStyle("-fx-border-color: transparent; -fx-border-width: 0;\n");

            Border noBorder = Border.EMPTY;


            formRenderer.setBorder(noBorder);
            formContainer.setBorder(noBorder);

            formRenderer.lookupAll(".formsfx-group").forEach(node -> {
                if (node instanceof Region region) {
                    region.setBorder(null);
                }
            });

            form.validProperty().addListener((obs, oldVal, newVal) -> {
                confirmButton.setDisable(!newVal);
            });

        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Errore caricamento topic");
            errorLabel.setVisible(true);
        }
    }

    // Popola la lista di checkbox con i topic
    private void populateCheckBoxes(List<TopicEntity> topics) {
        checkBoxes.clear();
        formContainer.getChildren().clear();

        for (TopicEntity topic : topics) {
            CheckBox checkBox = new CheckBox(topic.getNome());
            checkBox.setUserData(topic); // Salva il riferimento all'oggetto TopicEntity

            checkBoxes.add(checkBox);
            formContainer.getChildren().add(checkBox); // Aggiungi la checkbox alla UI
        }
    }

    // Filtra i topic in base alla ricerca
    private void filterTopics(String filter) {
        String lowerFilter = filter.toLowerCase();

        // Filtro i checkbox e aggiorno la UI
        List<CheckBox> filteredCheckBoxes = checkBoxes.stream()
                .filter(cb -> cb.getText().toLowerCase().contains(lowerFilter))
                .collect(Collectors.toList());

        formContainer.getChildren().setAll(filteredCheckBoxes);
    }

    @FXML
    private void handleConferma() {

        // Raccogli i topic selezionati direttamente dalle checkbox
        List<TopicEntity> selectedTopics = checkBoxes.stream()
                .filter(CheckBox::isSelected)  // Seleziona le checkbox che sono state selezionate
                .map(cb -> (TopicEntity) cb.getUserData())  // Ottieni l'oggetto TopicEntity associato
                .collect(Collectors.toList());

        if (selectedTopics.size() < 3) {
            errorLabel.setText("Errore: devono essere selezionati almeno 3 campi");
            errorLabel.setVisible(true);
            return;
        }

        errorLabel.setVisible(false);

        for (TopicEntity topic : selectedTopics) {
            System.out.println(topic.getNome());
        }

        // TODO: Inserire nel DB i topic selezionati

    }
}