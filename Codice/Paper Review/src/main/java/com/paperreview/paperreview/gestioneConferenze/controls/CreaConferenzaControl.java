package com.paperreview.paperreview.gestioneConferenze.controls;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.paperreview.paperreview.common.UserContext;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.ConferenzaDao;
import com.paperreview.paperreview.common.dbms.dao.RuoloConferenzaDao;
import com.paperreview.paperreview.common.interfaces.ControlledScreen;
import com.paperreview.paperreview.controls.MainControl;
import com.paperreview.paperreview.entities.*;
import com.paperreview.paperreview.gestioneConferenze.forms.CreaConferenzaFormModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.time.format.ResolverStyle; // se non già presente

public class CreaConferenzaControl implements ControlledScreen {

    @FXML
    private Label errorLabel;

    @FXML
    private VBox formContainer;

    @FXML
    private Button confirmButton;

    private CreaConferenzaFormModel conferenzaForm = new CreaConferenzaFormModel();

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
        errorLabel.setVisible(false);
        confirmButton.setDisable(true);

        Form form = conferenzaForm.createForm();
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

        try {

            // Per il parsing delle date utilizziamo il calendario gregoriano per controllare date come 31/02/2025

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

            DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu - HH:mm")
                    .withResolverStyle(ResolverStyle.STRICT);

            LocalDate oggiDate = LocalDate.now();

            // Parsing date (senza orario)
            LocalDate scadenzaSottomissione = LocalDate.parse(conferenzaForm.getScadenzaSottomissione(), dateFormatter);
            LocalDate scadenzaRevisione = LocalDate.parse(conferenzaForm.getScadenzaRevisione(), dateFormatter);
            LocalDate scadenzaAdeguamentoContenuti = LocalDate.parse(conferenzaForm.getDataScadenzaAdeguamentoContenuti(), dateFormatter);
            LocalDate scadenzaEditing = LocalDate.parse(conferenzaForm.getScadenzaEditing(), dateFormatter);
            LocalDate scadenzaAdeguamentoFormato = LocalDate.parse(conferenzaForm.getDataScadenzaAdeguamentoFormato(), dateFormatter);
            LocalDate scadenzaImpaginazione = LocalDate.parse(conferenzaForm.getScadenzaImpaginazione(), dateFormatter);

            // Parsing data conferenza (con orario)
            LocalDateTime dataConferenza = LocalDateTime.parse(conferenzaForm.getDataConferenza(), datetimeFormatter);

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
            if (!verificaSuccessione(scadenzaImpaginazione, dataConferenza.toLocalDate(), "della conferenza", "la scadenza dell'impaginazione")) return;

            // Se tutto valido, procediamo a creare la conferenza

            ConferenzaDao conferenzaDao = new ConferenzaDao(DBMSBoundary.getConnection());
            RuoloConferenzaDao ruoloConferenzaDao = new RuoloConferenzaDao(DBMSBoundary.getConnection());

            ConferenzaEntity conferenzaEntity = new ConferenzaEntity(
                    0,
                    conferenzaForm.getTitolo(),
                    conferenzaForm.getDescrizione(),
                    dataConferenza,
                    conferenzaForm.getLuogo(),
                    MetodoAssegnazione.fromString(conferenzaForm.getMetodoAssegnazione()),
                    MetodoValutazione.fromString(conferenzaForm.getRangeScore()),
                    conferenzaForm.getRateAccettazione(),
                    conferenzaForm.getQuantitaPaper(),
                    conferenzaForm.getGiorniPreavviso(),
                    scadenzaSottomissione.atTime(23, 59, 59),
                    scadenzaRevisione.atTime(23, 59, 59),
                    scadenzaAdeguamentoContenuti.atTime(23, 59, 59),
                    scadenzaEditing.atTime(23, 59, 59),
                    scadenzaAdeguamentoFormato.atTime(23, 59, 59),
                    scadenzaImpaginazione.atTime(23, 59, 59)
            );

            conferenzaDao.save(conferenzaEntity);

            RuoloConferenzaEntity ruoloConferenzaEntity = new RuoloConferenzaEntity(
                    0,
                    Ruolo.Chair,
                    UserContext.getUtente().getId(),
                    conferenzaEntity.getId()
                    );

            ruoloConferenzaDao.save(ruoloConferenzaEntity);

            UserContext.setConferenzaAttuale(conferenzaEntity);

            mainControl.setView("/com/paperreview/paperreview/boundaries/gestioneConferenze/invitaChair/invitaChair.fxml");

        } catch (DateTimeParseException e) {
            errorLabel.setText("Errore: Una o più date non sono nel formato corretto.");
            errorLabel.setVisible(true);
        } catch (SQLException e) {
            // TODO: Errore DB, gestire.
            throw new RuntimeException(e);
        }

    }

}
