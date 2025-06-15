package com.paperreview.paperreview.gestioneConferenze.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.gestioneConferenze.forms.InvitaRevisoriFormModel;
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

public class InvitaRevisoriControl implements ControlledScreen {

    @FXML
    private VBox formContainer;

    @FXML
    private Button addButton;

    @FXML
    private Button continueButton;

    @FXML
    private FlowPane chairContainer;

    @FXML
    private Label errorLabel;

    private Set<String> emails = new HashSet<>();

    private InvitaRevisoriFormModel invitaRevisoriForm = new InvitaRevisoriFormModel();

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {

        addButton.setDisable(true);
        errorLabel.setVisible(false);

        Form form = invitaRevisoriForm.createForm();
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
        String nuovaEmail = invitaRevisoriForm.getEmail().trim().toLowerCase();

        // Controllo se già esiste
        if (emails.contains(nuovaEmail)) {
            errorLabel.setText("Errore: Hai già inserito quest’email!");
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
            chairContainer.getChildren().remove(card);
            emails.remove(nuovaEmail);
        });

        card.getChildren().addAll(labelEmail, rimuoviBtn);
        chairContainer.getChildren().add(card);
    }


    @FXML
    private void handleContinueButton() {
        /*  TODO: Gestire logica di chiamata al DB per salvare i revisori invitati e andare avanti
            TODO: Dobbiamo:
                - Inserire l'invito nel DB
                - Recapitare la mail ad ogni revisore
                - Recapitare la notifica ad ogni revisore (teoricamente dovrebbe essere la stessa cosa di invitarlo i guess)
                - Controllare se il numero minimo di revisori per paper è rispettato quindi non andare avanti se il chair non ha aggiunto almeno tot revisori
                  far visualizzare un errore se è questo il caso
                - Bisogna anche mostrare un errore se un determinato revisori fa già parte della conferenza come revisori,
                  premere ok e poi continua con il resto dei revisori (qui ti consiglio di usare la classe Alert)
            ℹ️  Se hai bisogno di prendere l'id della conferenza corrente puoi usare UserContext.getConferenza che ti ritorna la entity e da li ti prendi l'id
                stessa cosa vale per l'id dell'utente. Ti ricordo inoltre che qui hai l'array emails che sono tutti i cristiani che dobbiamo invitare
         */
        //

        if (UserContext.isStandaloneInteraction()) {
            mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/gestioneConferenze/gestioneConferenzeBoundary.fxml");
        } else {
            mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/invitaEditore/invitaEditoreBoundary.fxml");
        }
    }
}