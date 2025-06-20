package com.paperreview.paperreview.gestioneRevisioni.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.TopicDao;
import com.paperreview.paperreview.common.dbms.dao.TopicUtenteDao;
import com.paperreview.paperreview.MainControl;
import com.paperreview.paperreview.entities.TopicEntity;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.gestioneRevisioni.forms.TopicFormModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

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

            // Recupera i topic già associati all'utente dal database
            Set<TopicEntity> currentTopics = new TopicUtenteDao(DBMSBoundary.getConnection())
                    .getTopicsForUser(UserContext.getUtente().getId());

            // Metti la spunta nelle checkbox corrispondenti ai topic già associati
            for (CheckBox checkBox : checkBoxes) {
                TopicEntity topic = (TopicEntity) checkBox.getUserData();
                if (currentTopics.contains(topic)) {
                    checkBox.setSelected(true);
                }
            }


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

        // Recupera tutti i topic attuali associati all'utente
        Set<TopicEntity> currentTopics;
        try {
            currentTopics = new TopicUtenteDao(DBMSBoundary.getConnection()).getTopicsForUser(UserContext.getUtente().getId());
        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Errore recupero topic associati");
            errorLabel.setVisible(true);
            return;
        }

        System.out.println("Al momento i topic dell'utente sono: ");

        for (TopicEntity topic : currentTopics) {
            System.out.println(topic.getNome());
        }

        // Raccogli i topic selezionati direttamente dalle checkbox
        List<TopicEntity> selectedTopics = checkBoxes.stream()
                .filter(CheckBox::isSelected)  // Seleziona le checkbox che sono state selezionate
                .map(cb -> (TopicEntity) cb.getUserData())  // Ottieni l'oggetto TopicEntity associato
                .collect(Collectors.toList());

        if (selectedTopics.size() < 3) {
            errorLabel.setText("Errore: devono essere selezionati almeno 3 topic");
            errorLabel.setVisible(true);
            return;
        }

        System.out.println("Invece, i topic selezionati sono: ");

        for (TopicEntity topic : selectedTopics) {
            System.out.println(topic.getNome());
        }

        errorLabel.setVisible(false);

        TopicUtenteDao topicUtenteDao;

        try{
            topicUtenteDao = new TopicUtenteDao(DBMSBoundary.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Errore aggiunta topic, riprova!");
            errorLabel.setVisible(true);
            return;
        }

        // 1. Aggiungi i topic che non sono già associati
        for (TopicEntity selectedTopic : selectedTopics) {
            if (!currentTopics.contains(selectedTopic)) {
                try {
                    topicUtenteDao.addTopicToUser(UserContext.getUtente().getId(), selectedTopic.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    errorLabel.setText("Errore aggiunta topic");
                    errorLabel.setVisible(true);
                    return;
                }
            }
        }

        // 2. Rimuovi i topic che non sono più selezionati
        for (TopicEntity currentTopic : currentTopics) {
            // Se non è stato trovato allora possiamo rimuoverlo
            if(!selectedTopics.contains(currentTopic)) {
                try {
                    System.out.println("Sto cancellando: " + currentTopic.getNome());
                    topicUtenteDao.removeTopicFromUser(UserContext.getUtente().getId(), currentTopic.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    errorLabel.setText("Errore rimozione topic");
                    errorLabel.setVisible(true);
                    return;
                }
            }
        }

        // Stampa i topic selezionati per il debug
        for (TopicEntity topic : selectedTopics) {
            System.out.println("Topic selezionato: " + topic.getNome());
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Topic aggiornati!");
        alert.setHeaderText("I tuoi topic sono stati aggiornati!");
        alert.setContentText(String.format("Se vuoi, puoi iniziare a fare le revisioni!"));
        alert.showAndWait();

        if(UserContext.getVieneDaRevisione())
        {
            mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneRevisioni/visualizzaSchermataRevisioni/visualizzaSchermataRevisioniBoundary.fxml");
        }

    }
}