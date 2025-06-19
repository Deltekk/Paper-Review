package com.paperreview.paperreview.gestioneRevisioni.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.RevisioneDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.MetodoValutazione;
import com.paperreview.paperreview.entities.RevisioneEntity;
import com.paperreview.paperreview.gestioneRevisioni.forms.ModificaRevisionePaperFormModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ModificaRevisionePaperControl implements ControlledScreen {

    @FXML
    private VBox formContainer;

    @FXML private Label labelConferenza, labelPaper;

    @FXML private Slider scoreSlider;

    @FXML private Button confirmButton;

    private ModificaRevisionePaperFormModel modificaRevisionePaperFormModel;

    private RevisioneEntity revisione;

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {

        labelConferenza.setText(String.format("Conferenza: \"%s\"", UserContext.getConferenzaAttuale().getNome()));
        labelPaper.setText(String.format("Paper: \"%s\"", UserContext.getPaperAttuale().getTitolo()));

        labelConferenza.setVisible(true);
        labelPaper.setVisible(true);

        confirmButton.setDisable(true);

        try (Connection connection = DBMSBoundary.getConnection())
        {
            RevisioneDao revisioneDao = new RevisioneDao(connection);

            revisione = revisioneDao.getByUtenteAndPaper(
                    UserContext.getUtente().getId(),
                    UserContext.getPaperAttuale().getId()
            );

            modificaRevisionePaperFormModel = new ModificaRevisionePaperFormModel(revisione);

            Form form = modificaRevisionePaperFormModel.createForm();

            FormRenderer formRenderer = new FormRenderer(form);
            formContainer.getChildren().add(formRenderer);
            formRenderer.setStyle("-fx-border-color: transparent; -fx-border-width: 0;\n");
            formContainer.setStyle("-fx-border-color: transparent; -fx-border-width: 0;\n");

            Border noBorder = Border.EMPTY;

            formRenderer.setBorder(noBorder);
            formContainer.setBorder(noBorder);

            configuraSlider(UserContext.getConferenzaAttuale().getMetodoValutazione(), revisione.getValutazione());

            formRenderer.lookupAll(".formsfx-group").forEach(node -> {
                if (node instanceof Region region) {
                    region.setBorder(null);
                }
            });

            form.validProperty().addListener((obs, oldVal, newVal) -> {
                confirmButton.setDisable(!newVal);
            });

        } catch (SQLException e) {
            // TODO: gestire questo errore.
            throw new RuntimeException(e);
        }

    }

    private void configuraSlider(MetodoValutazione metodo, int valoreScore) {
        int max = metodo.getLimite();  // es: 2
        int min = -max;

        scoreSlider.setMin(min);
        scoreSlider.setMax(max);
        scoreSlider.setBlockIncrement(1);
        scoreSlider.setMajorTickUnit(1);
        scoreSlider.setMinorTickCount(0);
        scoreSlider.setSnapToTicks(true);
        scoreSlider.setShowTickLabels(true);
        scoreSlider.setShowTickMarks(true);
        scoreSlider.setValue(valoreScore); // es: 1

        // Etichette come -2, -1, 0, 1, 2 ...
        scoreSlider.setLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double n) {
                int i = n.intValue();
                return String.valueOf(i);
            }

            @Override
            public Double fromString(String s) {
                return Double.parseDouble(s);
            }
        });
    }

    @FXML
    public void handleConferma() {
        // TODO: Fai la modifica, mi raccomando basta che modifichi i valori di revisione con i setters e fai revisione.update();
        mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneRevisioni/visualizzaPapersRevisore/visualizzaPapersRevisoreBoundary.fxml");

    }
}
