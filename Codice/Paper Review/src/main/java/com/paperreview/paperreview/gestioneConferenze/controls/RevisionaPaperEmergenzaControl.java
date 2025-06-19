package com.paperreview.paperreview.gestioneConferenze.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.RevisioneDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.MainControl;
import com.paperreview.paperreview.entities.MetodoValutazione;
import com.paperreview.paperreview.entities.RevisioneEntity;
import com.paperreview.paperreview.gestioneConferenze.forms.RevisionaPaperEmergenzaFormModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RevisionaPaperEmergenzaControl  implements ControlledScreen {

    @FXML
    private VBox formContainer;

    @FXML private Label labelConferenza, labelPaper;

    @FXML private Slider scoreSlider;

    @FXML private Button confirmButton;

    private RevisionaPaperEmergenzaFormModel revisionePaperEmergenzaForm = new RevisionaPaperEmergenzaFormModel();

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

        Form form = revisionePaperEmergenzaForm.createForm();

        FormRenderer formRenderer = new FormRenderer(form);
        formContainer.getChildren().add(formRenderer);
        formRenderer.setStyle("-fx-border-color: transparent; -fx-border-width: 0;\n");
        formContainer.setStyle("-fx-border-color: transparent; -fx-border-width: 0;\n");

        Border noBorder = Border.EMPTY;

        formRenderer.setBorder(noBorder);
        formContainer.setBorder(noBorder);

        configuraSlider(UserContext.getConferenzaAttuale().getMetodoValutazione(), 0);

        formRenderer.lookupAll(".formsfx-group").forEach(node -> {
            if (node instanceof Region region) {
                region.setBorder(null);
            }
        });

        form.validProperty().addListener((obs, oldVal, newVal) -> {
            confirmButton.setDisable(!newVal);
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

    @FXML
    public void handleConferma() {
        try {
            LocalDate oggi = LocalDate.now();
            LocalDate dataInizio = UserContext.getConferenzaAttuale().getScadenzaSottomissione().toLocalDate();
            LocalDate dataFine = UserContext.getConferenzaAttuale().getScadenzaRevisione().toLocalDate();

            if (oggi.isBefore(dataInizio)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText("Errore: non è ancora possibile sottomettere la revisione");
                alert.setContentText("La finestra per la revisione si aprirà il " + dataInizio);
                alert.showAndWait();
                return;
            }

            if (oggi.isAfter(dataFine)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText("Errore: non puoi più sottomettere una revisione");
                alert.setContentText("La scadenza era il " + dataFine);
                alert.showAndWait();
                return;
            }

            String testo = revisionePaperEmergenzaForm.getRevisione();
            String puntiForza = revisionePaperEmergenzaForm.getPuntiDiForza();
            String puntiDebolezza = revisionePaperEmergenzaForm.getPuntiDiDebolezza();
            int valutazione = (int) scoreSlider.getValue();

            if (testo == null || testo.isBlank() ||
                    puntiForza == null || puntiForza.isBlank() ||
                    puntiDebolezza == null || puntiDebolezza.isBlank()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText("Errore: i campi devono essere tutti compilati!");
                alert.setContentText("Compila tutti i campi richiesti prima di confermare.");
                alert.showAndWait();
                return;
            }

            final int NUMERO_MASSIMO_CARATTERI = 2000;
            if (testo.length() > NUMERO_MASSIMO_CARATTERI) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText("Errore: hai superato il numero massimo di caratteri consentiti.");
                alert.setContentText("Il massimo è " + NUMERO_MASSIMO_CARATTERI + " caratteri.");
                alert.showAndWait();
                return;
            }

            RevisioneEntity revisione = new RevisioneEntity(
                    0,
                    testo,
                    valutazione,
                    LocalDateTime.now(),
                    puntiForza,
                    puntiDebolezza,
                    null,
                    UserContext.getUtente().getId(),
                    UserContext.getPaperAttuale().getId(),
                    null // ref_sottorevisore
            );

            RevisioneDao dao = new RevisioneDao(DBMSBoundary.getConnection());
            dao.save(revisione);

            Alert successo = new Alert(Alert.AlertType.INFORMATION);
            successo.setTitle("Revisione completata");
            successo.setHeaderText("Complimenti, hai appena effettuato una revisione!");
            successo.setContentText("La tua revisione è stata registrata correttamente.");
            successo.showAndWait();

            mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneRevisioni/visualizzaPapersRevisore/visualizzaPapersRevisoreBoundary.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            Alert errore = new Alert(Alert.AlertType.ERROR);
            errore.setTitle("Errore");
            errore.setHeaderText("Errore durante il salvataggio della revisione");
            errore.setContentText("Contattare l’amministratore.");
            errore.showAndWait();
        }
    }
}
