package com.paperreview.paperreview.gestioneRevisioni.forms;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.model.validators.RegexValidator;

public class InvitaSottoRevisoreFormModel {

    // email con validazione built-in e regex personalizzata
    private StringField email = Field.ofStringType("")
            .label("Email")
            .placeholder("Inserisci l'email del sottorevisore")
            .styleClass("form-field")
            .validate(
                    RegexValidator.forEmail("L'email del sottorevisore non è nel formato corretto!")
            )
            .required("L'email non può essere vuota!");

    public String getEmail() {
        return email.getValue();
    }

    public StringField getEmailField() {
        return email;
    }

    public Form createForm() {
        return Form.of( Group.of(email));
    }

}
