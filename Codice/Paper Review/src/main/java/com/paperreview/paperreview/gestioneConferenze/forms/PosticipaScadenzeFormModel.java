package com.paperreview.paperreview.gestioneConferenze.forms;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.model.validators.RegexValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PosticipaScadenzeFormModel {

    private StringField scadenzaSottomissione;

    private StringField scadenzaRevisione;

    private StringField scadenzaAdeguamentoContenuti;

    private StringField scadenzaEditing;

    private StringField scadenzaAdeguamentoFormato;

    private StringField scadenzaImpaginazione;

    public PosticipaScadenzeFormModel(String sottomissione, String revisione, String adegContenuti,
                                      String editing, String adegFormato, String impaginazione) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate oggi = LocalDate.now();

        LocalDate dateSottomissione = LocalDate.parse(sottomissione, formatter);
        LocalDate dateRevisione = LocalDate.parse(revisione, formatter);
        LocalDate dateAdeguamentoContenuti = LocalDate.parse(adegContenuti, formatter);
        LocalDate dateEditing = LocalDate.parse(editing, formatter);
        LocalDate dateAdeguamentoFormato = LocalDate.parse(adegFormato, formatter);
        LocalDate dateImpaginazione = LocalDate.parse(impaginazione, formatter);

        this.scadenzaSottomissione = Field.ofStringType(sottomissione)
                .label("Data di fine sottomissione")
                .editable(dateSottomissione.isAfter(oggi))
                .validate(RegexValidator.forPattern("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", "Formato: gg/mm/aaaa"))
                .required("Campo obbligatorio");

        this.scadenzaRevisione = Field.ofStringType(revisione)
                .label("Data di scadenza revisione")
                .editable(dateRevisione.isAfter(oggi))
                .validate(RegexValidator.forPattern("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", "Formato: gg/mm/aaaa"))
                .required("Campo obbligatorio");

        this.scadenzaAdeguamentoContenuti = Field.ofStringType(adegContenuti)
                .label("Data di adeguamento contenuti")
                .editable(dateAdeguamentoContenuti.isAfter(oggi))
                .validate(RegexValidator.forPattern("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", "Formato: gg/mm/aaaa"))
                .required("Campo obbligatorio");

        this.scadenzaEditing = Field.ofStringType(editing)
                .label("Data di fine editing")
                .editable(dateEditing.isAfter(oggi))
                .validate(RegexValidator.forPattern("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", "Formato: gg/mm/aaaa"))
                .required("Campo obbligatorio");

        this.scadenzaAdeguamentoFormato = Field.ofStringType(adegFormato)
                .label("Data di adeguamento formato")
                .editable(dateAdeguamentoFormato.isAfter(oggi))
                .validate(RegexValidator.forPattern("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", "Formato: gg/mm/aaaa"))
                .required("Campo obbligatorio");

        this.scadenzaImpaginazione = Field.ofStringType(impaginazione)
                .label("Data di impaginazione")
                .editable(dateImpaginazione.isAfter(oggi))
                .validate(RegexValidator.forPattern("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", "Formato: gg/mm/aaaa"))
                .required("Campo obbligatorio");
    }


    public String getScadenzaSottomissione() {
        return scadenzaSottomissione.getValue();
    }

    public String getScadenzaRevisione() {
        return scadenzaRevisione.getValue();
    }

    public String getScadenzaAdeguamentoContenuti() {
        return scadenzaAdeguamentoContenuti.getValue();
    }

    public String getScadenzaEditing() {
        return scadenzaEditing.getValue();
    }

    public String getScadenzaAdeguamentoFormato() {
        return scadenzaAdeguamentoFormato.getValue();
    }

    public String getScadenzaImpaginazione() {
        return scadenzaImpaginazione.getValue();
    }

    public Form createForm() {
        return Form.of( Group.of(scadenzaSottomissione, scadenzaRevisione, scadenzaAdeguamentoContenuti, scadenzaEditing, scadenzaAdeguamentoFormato, scadenzaImpaginazione) );
    }

}
