package com.paperreview.paperreview.GestioneAccount;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.validators.CustomValidator;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.model.structure.PasswordField;

public class ModificaPasswordModel {

    // Password con validazioni multiple combinate
    private PasswordField password = Field.ofPasswordType("")
            .label("Password")
            .styleClass("form-field")
            .placeholder("Inserisci la tua password")
            .validate(StringLengthValidator.atLeast(8, "La password deve essere lunga almeno 8 caratteri"))
            .validate(RegexValidator.forPattern("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{6,}$", "La password deve contenere almeno una maiuscola,  almeno una minuscola, almeno un numero ed almeno un carattere speciale"))
            .required("La password non può essere vuota!");

    // Password con validazioni multiple combinate
    private PasswordField confermaPassword = Field.ofPasswordType("")
            .label("Conferma password")
            .styleClass("form-field")
            .placeholder("Inserisci la tua password")
            .validate(StringLengthValidator.atLeast(8, "La password deve essere lunga almeno 8 caratteri"))
            .validate(RegexValidator.forPattern("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{6,}$", "La password deve contenere almeno una maiuscola,  almeno una minuscola, almeno un numero ed almeno un carattere speciale"),
                    CustomValidator.forPredicate(
                    value -> value != null && value.equals(password.getValue()),
                    "Le password non corrispondono!"
            ))
            .required("La password non può essere vuota!");

    public String getPassword() {
        return password.getValue();
    }

    public String getConfermaPassword() {
        return confermaPassword.getValue();
    }

    public Form createForm() {
        return Form.of( Group.of(password, confermaPassword) );
    }

    public PasswordField getPasswordField() {
        return password;
    }

    public PasswordField getConfermaPasswordField() {
        return confermaPassword;
    }


}
