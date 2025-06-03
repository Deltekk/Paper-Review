package com.paperreview.paperreview.controls;

import com.paperreview.paperreview.common.DBMSBoundary;
import com.paperreview.paperreview.common.TopicDao;
import com.paperreview.paperreview.entities.TopicEntity;
import com.paperreview.paperreview.interfaces.ControlledScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ModificaTopicControl implements ControlledScreen {

    @FXML private Label errorLabel;
    @FXML private TextField searchField;
    @FXML private VBox checkboxContainer;
    @FXML private Button confirmButton;

    private ObservableList<TopicEntity> allTopics = FXCollections.observableArrayList();
    private ObservableList<CheckBox> checkBoxes = FXCollections.observableArrayList();

    private TopicDao topicDao;
    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }
    
    @FXML
    public void initialize() {
        try {
            topicDao = new TopicDao(DBMSBoundary.getConnection());
            List<TopicEntity> topics = topicDao.getAll();
            allTopics.setAll(topics);
            populateCheckboxes(allTopics);

            // Aggiungo listener per filtro live
            searchField.textProperty().addListener((obs, oldVal, newVal) -> filterTopics(newVal));
        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Errore caricamento topic");
            errorLabel.setVisible(true);
        }
    }

    private void populateCheckboxes(List<TopicEntity> topics) {
        checkboxContainer.getChildren().clear();
        checkBoxes.clear();

        for (TopicEntity topic : topics) {
            CheckBox cb = new CheckBox(topic.getNome());
            cb.setUserData(topic); // salva riferimento all'entity

            checkBoxes.add(cb);
            checkboxContainer.getChildren().add(cb);
        }
    }

    private void filterTopics(String filter) {
        String lowerFilter = filter.toLowerCase();

        List<CheckBox> filtered = checkBoxes.stream()
                .filter(cb -> ((TopicEntity) cb.getUserData()).getNome().toLowerCase().contains(lowerFilter))
                .collect(Collectors.toList());

        checkboxContainer.getChildren().setAll(filtered);
    }

    @FXML
    private void handleConferma() {
        List<TopicEntity> selectedTopics = checkBoxes.stream()
                .filter(CheckBox::isSelected)
                .map(cb -> (TopicEntity) cb.getUserData())
                .collect(Collectors.toList());

        if (selectedTopics.size() < 3) {
            errorLabel.setText("Errore: devono essere selezionati almeno 3 campi");
            errorLabel.setVisible(true);
            return;
        }

        errorLabel.setVisible(false);

        // Qui fai la logica di salvataggio o aggiornamento con selectedTopics
        // ...
    }
}