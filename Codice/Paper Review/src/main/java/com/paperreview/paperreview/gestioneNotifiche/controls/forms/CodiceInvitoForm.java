package com.paperreview.paperreview.gestioneNotifiche.controls.forms;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.RegexValidator;

public class CodiceInvitoForm {

    // email con validazione built-in e regex personalizzata
    private StringField codiceInvito = Field.ofStringType("")
            .label("Codice Invito")
            .placeholder("Inserisci il codice invito")
            .styleClass("form-field")
            .validate(
                    RegexValidator.forPattern("^([a-zA-Z]|\\d){3}-([a-zA-Z]|\\d){3}$", "Il codice deve avere due coppie da 3 numeri separati da un trattino!")
            )
            .required("Il codice di invito non può essere vuoto!");

    public String getCodiceInvito() {
        return codiceInvito.getValue();
    }

    public StringField getCodiceInvitoField() {
        return codiceInvito;
    }

    public Form createForm() {
        return Form.of( Group.of(codiceInvito));
    }

}
