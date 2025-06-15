package com.paperreview.paperreview.gestioneConferenze.forms;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.model.validators.RegexValidator;

public class InvitaChairFormModel {

    private StringField email = Field.ofStringType("")
            .label("Email")
            .placeholder("Inserisci la mail del chair che vuoi invitare")
            .styleClass("form-field")
            .validate(
                    RegexValidator.forEmail("Il formato della mail non è corretto!")
            );

    public String getEmail() {
        return email.getValue();
    }

    public Form createForm() {
        return Form.of( Group.of(email));
    }

}
