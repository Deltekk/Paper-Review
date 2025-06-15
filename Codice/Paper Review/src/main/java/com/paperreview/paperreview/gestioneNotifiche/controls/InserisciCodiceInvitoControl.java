package com.paperreview.paperreview.gestioneNotifiche.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.InvitoDao;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.StatusInvito;
import com.paperreview.paperreview.gestioneNotifiche.forms.CodiceInvitoForm;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class InserisciCodiceInvitoControl implements ControlledScreen {

    @FXML
    private VBox formContainer;
    @FXML
    private Label errorLabel;
    @FXML
    private Button confirmButton;

    private CodiceInvitoForm codiceInvitoForm = new CodiceInvitoForm();

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        errorLabel.setVisible(false);
        confirmButton.setDisable(true);

        Form form = codiceInvitoForm.createForm();
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
    public void handleConferma(){
        // InvitoDao invitoDao = new InvitoDao(DBMSBoundary.getConnection());
        // String result = invitoDao.acceptOrRejectInvito("Codice", true);
        System.out.println("Conferma");

    }

}
