package com.paperreview.paperreview.GestioneAccount;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.RegexValidator;

public class RecuperoPasswordFormModel {

    // Email con validazione built-in e regex personalizzata
    private StringField email = Field.ofStringType("")
            .label("Email")
            .placeholder("Inserisci la tua email")
            .styleClass("form-field")
            .validate(
                    RegexValidator.forEmail("Il formato della mail non è corretto!")
            )
            .required("La mail non può essere vuota!");

    public String getEmail() { return email.getValue(); }

    public Form createForm() {
        return Form.of( Group.of(email));
    }

    public StringField getEmailField() {
        return email;
    }
}
