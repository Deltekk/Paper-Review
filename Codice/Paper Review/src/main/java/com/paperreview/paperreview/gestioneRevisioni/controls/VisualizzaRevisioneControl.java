package com.paperreview.paperreview.gestioneRevisioni.controls;

import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.MetodoValutazione;
import com.paperreview.paperreview.entities.RevisioneEntity;
import com.paperreview.paperreview.gestioneRevisioni.forms.VisualizzaRevisioneFormModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class VisualizzaRevisioneControl implements ControlledScreen {

    @FXML private VBox formContainer;

    @FXML private Label labelConferenza, labelPaper;

    @FXML private Slider scoreSlider;

    private VisualizzaRevisioneFormModel revisioneForm;

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

        // Preleva revisione da contesto
        RevisioneEntity revisione = UserContext.getRevisioneCorrente();

        revisioneForm = new VisualizzaRevisioneFormModel(false, revisione.getTesto(), revisione.getPuntiForza(), revisione.getPuntiForza());
        revisioneForm.setEditable(false);
        revisioneForm.setScore(revisione.getValutazione());

        FormRenderer formRenderer = new FormRenderer(revisioneForm.createForm());
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

}