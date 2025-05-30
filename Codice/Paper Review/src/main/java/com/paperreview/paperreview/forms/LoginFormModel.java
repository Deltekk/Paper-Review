package com.paperreview.paperreview.forms;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.model.structure.PasswordField;

public class LoginFormModel {

    // Email con validazione built-in e regex personalizzata
    private StringField email = Field.ofStringType("")
            .label("Email")
            .placeholder("Inserisci la tua email")
            .styleClass("form-field")
            .validate(
                    RegexValidator.forEmail("Il formato della mail non è corretto!")
            )
            .required("La mail non può essere vuota!");

    // Password con validazioni multiple combinate
    private PasswordField password = Field.ofPasswordType("")
            .label("Password")
            .styleClass("form-field")
            .placeholder("Inserisci la tua password")
            .validate(StringLengthValidator.atLeast(8, "La password deve essere lunga almeno 8 caratteri"))
            .validate(RegexValidator.forPattern("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{6,}$", "La password deve contenere almeno una maiuscola,  almeno una minuscola, almeno un numero ed almeno un carattere speciale"))
            .required("La password non può essere vuota!");

    public String getEmail() {
        return email.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }

    public Form createForm() {
        return Form.of( Group.of(email, password));
    }

    public StringField getEmailField() {
        return email;
    }

    public PasswordField getPasswordField() {
        return password;
    }


}
