package com.paperreview.paperreview.gestionePaperDefinitivi.forms;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;

public class InviaFeedbackFormModel {

    // email con validazione built-in e regex personalizzata
    private StringField feedback = Field.ofStringType("")
            .label("Feedback")
            .placeholder("Inserisci il feedback (massimo 500 caratteri)")
            .styleClass("form-field")
            .validate(
                    StringLengthValidator.upTo(500, "Il feedback non può essere più lungo di 500 caratteri!")
            )
            .required("Il feedback non può essere vuoto!");

    public String getFeedback() {
        return feedback.getValue();
    }

    public StringField getCodiceInvitoField() {
        return feedback;
    }

    public Form createForm() {
        return Form.of( Group.of(feedback));
    }

}
