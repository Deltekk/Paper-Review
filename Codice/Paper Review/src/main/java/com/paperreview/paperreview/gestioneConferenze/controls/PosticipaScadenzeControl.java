package com.paperreview.paperreview.gestioneConferenze.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.CustomDateParser;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.ConferenzaDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.gestioneNotifiche.MainControl;
import com.paperreview.paperreview.entities.*;
import com.paperreview.paperreview.gestioneConferenze.forms.PosticipaScadenzeFormModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class PosticipaScadenzeControl implements ControlledScreen {

    @FXML
    private Label errorLabel;

    @FXML
    private VBox formContainer;

    @FXML
    private Button confirmButton;

    @FXML private Label labelConferenza;

    private PosticipaScadenzeFormModel scadenzeForm;

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        errorLabel.setVisible(false);
        confirmButton.setDisable(true);

        ConferenzaEntity conferenzaAttuale = UserContext.getConferenzaAttuale();

        labelConferenza.setText(String.format("Conferenza: \"%s\"", conferenzaAttuale.getNome()));
        labelConferenza.setVisible(true);

        scadenzeForm = new PosticipaScadenzeFormModel(
                CustomDateParser.parseDate(conferenzaAttuale.getScadenzaSottomissione()),
                CustomDateParser.parseDate(conferenzaAttuale.getScadenzaRevisione()),
                CustomDateParser.parseDate(conferenzaAttuale.getScadenzaSottomissione2()),
                CustomDateParser.parseDate(conferenzaAttuale.getScadenzaEditing()),
                CustomDateParser.parseDate(conferenzaAttuale.getScadenzaSottomissione3()),
                CustomDateParser.parseDate(conferenzaAttuale.getScadenzaImpaginazione())
        );

        Form form = scadenzeForm.createForm();
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

    private boolean verificaSuccessione(LocalDate precedente, LocalDate successiva, String nomeCampo, String precedenteString) {
        if (!successiva.isAfter(precedente.plusDays(2))) {
            errorLabel.setText(String.format("Errore: La data %s deve essere almeno 3 giorni dopo %s!", nomeCampo, precedenteString));
            errorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    private boolean isModificabile(LocalDate originale) {
        return originale.isAfter(LocalDate.now());
    }

    @FXML
    public void handleConferma(){
        errorLabel.setVisible(false);

        try (Connection connection = DBMSBoundary.getConnection()){

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

            ConferenzaEntity conferenza = UserContext.getConferenzaAttuale();
            LocalDate dataFineConferenza = conferenza.getDataConferenza().toLocalDate();

            LocalDate s1 = conferenza.getScadenzaSottomissione().toLocalDate();
            LocalDate s2 = conferenza.getScadenzaRevisione().toLocalDate();
            LocalDate s3 = conferenza.getScadenzaSottomissione2().toLocalDate();
            LocalDate s4 = conferenza.getScadenzaEditing().toLocalDate();
            LocalDate s5 = conferenza.getScadenzaSottomissione3().toLocalDate();
            LocalDate s6 = conferenza.getScadenzaImpaginazione().toLocalDate();

            LocalDate newS1 = isModificabile(s1) ? LocalDate.parse(scadenzeForm.getScadenzaSottomissione(), dateFormatter) : s1;
            LocalDate newS2 = isModificabile(s2) ? LocalDate.parse(scadenzeForm.getScadenzaRevisione(), dateFormatter) : s2;
            LocalDate newS3 = isModificabile(s3) ? LocalDate.parse(scadenzeForm.getScadenzaAdeguamentoContenuti(), dateFormatter) : s3;
            LocalDate newS4 = isModificabile(s4) ? LocalDate.parse(scadenzeForm.getScadenzaEditing(), dateFormatter) : s4;
            LocalDate newS5 = isModificabile(s5) ? LocalDate.parse(scadenzeForm.getScadenzaAdeguamentoFormato(), dateFormatter) : s5;
            LocalDate newS6 = isModificabile(s6) ? LocalDate.parse(scadenzeForm.getScadenzaImpaginazione(), dateFormatter) : s6;

            // Verifica successione solo tra date modificabili
            if (isModificabile(s1) && isModificabile(s2) && !verificaSuccessione(newS1, newS2, "di scadenza delle revisioni", "la scadenza delle sottomissioni")) return;
            if (isModificabile(s2) && isModificabile(s3) && !verificaSuccessione(newS2, newS3, "di scadenza dell'adeguamento dei contenuti", "la scadenza delle revisioni")) return;
            if (isModificabile(s3) && isModificabile(s4) && !verificaSuccessione(newS3, newS4, "di scadenza di fine editing", "la scadenza dell'adeguamento dei contenuti")) return;
            if (isModificabile(s4) && isModificabile(s5) && !verificaSuccessione(newS4, newS5, "di scadenza dell'adeguamento del formato", "la scadenza di fine editing")) return;
            if (isModificabile(s5) && isModificabile(s6) && !verificaSuccessione(newS5, newS6, "di scadenza dell'impaginazione", "la scadenza dell'adeguamento del formato")) return;

            if (isModificabile(s1) && newS1.isBefore(s1)) {
                errorLabel.setText("Errore: Non è possibile anticipare la scadenza della sottomissione.");
                errorLabel.setVisible(true);
                return;
            }
            if (isModificabile(s2) && newS2.isBefore(s2)) {
                errorLabel.setText("Errore: Non è possibile anticipare la scadenza della revisione.");
                errorLabel.setVisible(true);
                return;
            }
            if (isModificabile(s3) && newS3.isBefore(s3)) {
                errorLabel.setText("Errore: Non è possibile anticipare la scadenza dell’adeguamento dei contenuti.");
                errorLabel.setVisible(true);
                return;
            }
            if (isModificabile(s4) && newS4.isBefore(s4)) {
                errorLabel.setText("Errore: Non è possibile anticipare la scadenza dell’editing.");
                errorLabel.setVisible(true);
                return;
            }
            if (isModificabile(s5) && newS5.isBefore(s5)) {
                errorLabel.setText("Errore: Non è possibile anticipare la scadenza dell’adeguamento del formato.");
                errorLabel.setVisible(true);
                return;
            }
            if (isModificabile(s6) && newS6.isBefore(s6)) {
                errorLabel.setText("Errore: Non è possibile anticipare la scadenza dell’impaginazione.");
                errorLabel.setVisible(true);
                return;
            }


            if (newS6.isAfter(dataFineConferenza.plusDays(3))) {
                errorLabel.setText("La scadenza dell'impaginazione non può essere oltre 3 giorni dopo la conferenza.");
                errorLabel.setVisible(true);
                return;
            }

            // Aggiorna conferenza
            conferenza.setScadenzaSottomissione(newS1.atStartOfDay());
            conferenza.setScadenzaRevisione(newS2.atStartOfDay());
            conferenza.setScadenzaSottomissione2(newS3.atStartOfDay());
            conferenza.setScadenzaEditing(newS4.atStartOfDay());
            conferenza.setScadenzaSottomissione3(newS5.atStartOfDay());
            conferenza.setScadenzaImpaginazione(newS6.atStartOfDay());

            ConferenzaDao conferenzaDao = new ConferenzaDao(connection);
            conferenzaDao.update(conferenza);
            UserContext.setConferenzaAttuale(conferenza);

            mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/gestioneConferenze/gestioneConferenzeBoundary.fxml");

        } catch (DateTimeParseException e) {
            errorLabel.setText("Errore: Una o più date non sono nel formato corretto.");
            errorLabel.setVisible(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}