package com.paperreview.paperreview.gestioneConferenze.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.gestioneConferenze.forms.InvitaEditoriFormModel;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.HashSet;
import java.util.Set;

public class InvitaEditoriControl implements ControlledScreen {

    @FXML
    private VBox formContainer;

    @FXML
    private Button addButton;

    @FXML
    private Button continueButton;

    @FXML
    private FlowPane editorContainer;

    @FXML
    private Label errorLabel;

    private Set<String> emails = new HashSet<>();

    private InvitaEditoriFormModel invitaEditoriFormModel = new InvitaEditoriFormModel();

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {

        addButton.setDisable(true);
        errorLabel.setVisible(false);

        Form form = invitaEditoriFormModel.createForm();
        FormRenderer formRenderer = new FormRenderer(form);
        formContainer.getChildren().add(formRenderer);
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
            addButton.setDisable(!newVal);
        });

    }

    @FXML
    private void handleAddButton() {
        String nuovaEmail = invitaEditoriFormModel.getEmail().trim().toLowerCase();

        // Controllo se già esiste
        if (emails.contains(nuovaEmail)) {
            errorLabel.setText("Errore: Hai già inserito quest’email!");
            errorLabel.setVisible(true);
            return;
        }

        if (emails.size() > 1){
            errorLabel.setText("Errore: Puoi inserire un solo editore!");
            errorLabel.setVisible(true);
            return;
        }

        // Aggiunta email
        emails.add(nuovaEmail);
        errorLabel.setVisible(false); // nascondi eventuale errore

        // Crea card visuale
        VBox card = new VBox();
        card.getStyleClass().addAll("chair-card", "bg-celeste");
        card.setSpacing(5);
        card.setAlignment(Pos.CENTER);
        card.setMinHeight(150);
        card.setMinWidth(350);

        Label labelEmail = new Label(nuovaEmail);
        labelEmail.getStyleClass().addAll("p", "bold", "text-bianco", "ombra");
        labelEmail.setWrapText(true);
        labelEmail.setAlignment(Pos.CENTER);
        labelEmail.setTextAlignment(TextAlignment.CENTER);

        Button rimuoviBtn = new Button("Rimuovi");
        rimuoviBtn.getStyleClass().add("red-button");

        rimuoviBtn.setOnAction(e -> {
            editorContainer.getChildren().remove(card);
            emails.remove(nuovaEmail);
        });

        card.getChildren().addAll(labelEmail, rimuoviBtn);
        editorContainer.getChildren().add(card);
    }


    @FXML
    private void handleContinueButton() {

        errorLabel.setVisible(false);

        /*  TODO: Gestire logica di chiamata al DB per salvare gli editori invitati e andare avanti
            TODO: Dobbiamo:
                - Inserire l'invito nel DB
                - Recapitare la mail ad ogni editore
                - Recapitare la notifica ad ogni editore (teoricamente dovrebbe essere la stessa cosa di invitarlo i guess)
            ℹ️  Se hai bisogno di prendere l'id della conferenza corrente puoi usare UserContext.getConferenza che ti ritorna la entity e da li ti prendi l'id
                stessa cosa vale per l'id dell'utente. Ti ricordo inoltre che qui hai l'array emails che sono tutti i cristiani che dobbiamo invitare
         */

        if (emails.size() == 0) {
            errorLabel.setText("Errore: Devi ancora inserire un editore!");
            errorLabel.setVisible(true);
            return;
        }

        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/gestioneConferenze/gestioneConferenzeBoundary.fxml");

    }
}
