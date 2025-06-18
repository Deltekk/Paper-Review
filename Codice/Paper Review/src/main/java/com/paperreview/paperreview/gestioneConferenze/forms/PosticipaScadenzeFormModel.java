package com.paperreview.paperreview.gestioneConferenze.forms;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.model.validators.RegexValidator;

public class PosticipaScadenzeFormModel {

    private StringField scadenzaSottomissione;

    private StringField scadenzaRevisione;

    private StringField scadenzaAdeguamentoContenuti;

    private StringField scadenzaEditing;

    private StringField scadenzaAdeguamentoFormato;

    private StringField scadenzaImpaginazione;

    public PosticipaScadenzeFormModel(String sottomissione, String revisione, String adegContenuti,
                                      String editing, String adegFormato, String impaginazione) {
        this.scadenzaSottomissione = Field.ofStringType(sottomissione)
                .label("Data di fine sottomissione")
                .placeholder("Inserisci la data di fine sottomissione")
                .styleClass("form-field")
                .validate(RegexValidator.forPattern("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", "La data di fine sottomissione deve essere nel formato gg/mm/aaaa"))
                .required("La data di fine sottomissione non può essere vuota!");

        this.scadenzaRevisione = Field.ofStringType(revisione)
                .label("Data di fine revisione")
                .placeholder("Inserisci la data di fine revisione")
                .styleClass("form-field")
                .validate(RegexValidator.forPattern("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", "La data di fine revisione deve essere nel formato gg/mm/aaaa"))
                .required("La data di fine revisione non può essere vuota!");

        this.scadenzaAdeguamentoContenuti = Field.ofStringType(adegContenuti)
                .label("Data di fine adeguamento contenuti")
                .placeholder("Inserisci la data di fine adeguamento contenuti")
                .styleClass("form-field")
                .validate(RegexValidator.forPattern("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", "La data di fine adeguamento contenuti deve essere nel formato gg/mm/aaaa"))
                .required("La data di fine adeguamento contenuti non può essere vuota!");

        this.scadenzaEditing = Field.ofStringType(editing)
                .label("Data di fine editing")
                .placeholder("Inserisci la data di fine editing")
                .styleClass("form-field")
                .validate(RegexValidator.forPattern("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", "La data di fine editing deve essere nel formato gg/mm/aaaa"))
                .required("La data di fine editing non può essere vuota!");

        this.scadenzaAdeguamentoFormato = Field.ofStringType(adegFormato)
                .label("Data di fine adeguamento formato")
                .placeholder("Inserisci la data di fine adeguamento formato")
                .styleClass("form-field")
                .validate(RegexValidator.forPattern("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", "La data di fine adeguamento formato deve essere nel formato gg/mm/aaaa"))
                .required("La data di fine adeguamento formato non può essere vuota!");

        this.scadenzaImpaginazione = Field.ofStringType(impaginazione)
                .label("Data di fine impaginazione")
                .placeholder("Inserisci la data di fine impaginazione")
                .styleClass("form-field")
                .validate(RegexValidator.forPattern("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", "La data di fine impaginazione formato deve essere nel formato gg/mm/aaaa"))
                .required("La data di fine impaginazione non può essere vuota!");
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
