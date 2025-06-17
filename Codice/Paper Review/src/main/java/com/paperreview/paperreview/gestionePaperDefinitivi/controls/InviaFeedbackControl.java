package com.paperreview.paperreview.gestionePaperDefinitivi.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.InvitoDao;
import com.paperreview.paperreview.common.dbms.dao.RuoloConferenzaDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.ConferenzaEntity;
import com.paperreview.paperreview.entities.InvitoEntity;
import com.paperreview.paperreview.entities.Ruolo;
import com.paperreview.paperreview.entities.RuoloConferenzaEntity;
import com.paperreview.paperreview.gestioneNotifiche.forms.CodiceInvitoForm;
import com.paperreview.paperreview.gestionePaperDefinitivi.forms.InviaFeedbackFormModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class InviaFeedbackControl implements ControlledScreen {

    @FXML private VBox formContainer;
    @FXML private Label errorLabel, conferenzaLabel;
    @FXML private Button confirmButton;

    private InviaFeedbackFormModel inviaFeedbackForm = new InviaFeedbackFormModel();

    private MainControl mainControl;

    private ConferenzaEntity conferenza;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {

        conferenza = UserContext.getConferenzaAttuale();
        conferenzaLabel.setText(conferenza.getNome());
        conferenzaLabel.setVisible(true);

        errorLabel.setVisible(false);
        confirmButton.setDisable(true);

        Form form = inviaFeedbackForm.createForm();
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
            confirmButton.setDisable(!newVal);
        });

    }

    @FXML
    public void handleConferma() {

        String feedback = inviaFeedbackForm.getFeedback().trim();

        // TODO: Mandare il feedback e salvarlo nel DB.
        // Ti ricordo che puoi usare getPaper da UserContext

    }
}
