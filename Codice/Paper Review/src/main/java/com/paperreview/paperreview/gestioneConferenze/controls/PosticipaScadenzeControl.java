package com.paperreview.paperreview.gestioneConferenze.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.CustomDateParser;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.ConferenzaDao;
import com.paperreview.paperreview.common.dbms.dao.RuoloConferenzaDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.*;
import com.paperreview.paperreview.gestioneConferenze.forms.CreaConferenzaFormModel;
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
import java.time.LocalDateTime;
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

    @FXML
    public void handleConferma(){
        errorLabel.setVisible(false);

        try (Connection connection = DBMSBoundary.getConnection()){

            // Per il parsing delle date utilizziamo il calendario gregoriano per controllare date come 31/02/2025

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

            LocalDate oggiDate = LocalDate.now();

            // Parsing date (senza orario)
            LocalDate scadenzaSottomissione = LocalDate.parse(scadenzeForm.getScadenzaSottomissione(), dateFormatter);
            LocalDate scadenzaRevisione = LocalDate.parse(scadenzeForm.getScadenzaRevisione(), dateFormatter);
            LocalDate scadenzaAdeguamentoContenuti = LocalDate.parse(scadenzeForm.getScadenzaAdeguamentoContenuti(), dateFormatter);
            LocalDate scadenzaEditing = LocalDate.parse(scadenzeForm.getScadenzaEditing(), dateFormatter);
            LocalDate scadenzaAdeguamentoFormato = LocalDate.parse(scadenzeForm.getScadenzaAdeguamentoFormato(), dateFormatter);
            LocalDate scadenzaImpaginazione = LocalDate.parse(scadenzeForm.getScadenzaImpaginazione(), dateFormatter);

            // Verifica se ogni data è >= oggi + 3 giorni
            if (!scadenzaSottomissione.isAfter(oggiDate.plusDays(2))) {
                errorLabel.setText("Errore: La data di fine sottomissione deve essere almeno tra 3 giorni!");
                errorLabel.setVisible(true);
                return;
            }

            // Verifica ordine cronologico con almeno 3 giorni di distanza
            if (!verificaSuccessione(scadenzaSottomissione, scadenzaRevisione, "di scadenza delle revisioni", "la scadenza delle sottomissioni")) return;
            if (!verificaSuccessione(scadenzaRevisione, scadenzaAdeguamentoContenuti, "di scadenza dell'adeguamento dei contenuti", "la scadenza delle revisioni")) return;
            if (!verificaSuccessione(scadenzaAdeguamentoContenuti, scadenzaEditing, "di scadenza di fine editing", "la scadenza dell'adeguamento dei contenuti")) return;
            if (!verificaSuccessione(scadenzaEditing, scadenzaAdeguamentoFormato, "di scadenza dell'adeguamento del formato", "la scadenza di fine editing")) return;
            if (!verificaSuccessione(scadenzaAdeguamentoFormato, scadenzaImpaginazione, "di scadenza dell'impaginazione", "la scadenza dell'adeguamento del formato")) return;

            // Se tutto valido, procediamo a creare la conferenza

            ConferenzaDao conferenzaDao = new ConferenzaDao(connection);

            ConferenzaEntity conferenza = UserContext.getConferenzaAttuale();

            conferenza.setScadenzaSottomissione(LocalDate.parse(scadenzeForm.getScadenzaSottomissione()).atStartOfDay());
            conferenza.setScadenzaRevisione(LocalDate.parse(scadenzeForm.getScadenzaRevisione()).atStartOfDay());
            conferenza.setScadenzaSottomissione2(LocalDate.parse(scadenzeForm.getScadenzaAdeguamentoContenuti()).atStartOfDay());
            conferenza.setScadenzaEditing(LocalDate.parse(scadenzeForm.getScadenzaEditing()).atStartOfDay());
            conferenza.setScadenzaSottomissione3(LocalDate.parse(scadenzeForm.getScadenzaAdeguamentoFormato()).atStartOfDay());
            conferenza.setScadenzaImpaginazione(LocalDate.parse(scadenzeForm.getScadenzaImpaginazione()).atStartOfDay());

            conferenzaDao.update(conferenza);

            UserContext.setConferenzaAttuale(conferenza);

            mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/gestioneConferenze/gestioneConferenzeBoundary.fxml");

        } catch (DateTimeParseException e) {
            errorLabel.setText("Errore: Una o più date non sono nel formato corretto.");
            errorLabel.setVisible(true);
        } catch (SQLException e) {
            // TODO: Errore DB, gestire.
            throw new RuntimeException(e);
        }

    }

}