package com.paperreview.paperreview.gestioneAccount.forms;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.model.validators.CustomValidator;

public class RegistrazioneFormModel {

    private StringField nome = Field.ofStringType("")
            .label("Nome")
            .placeholder("Inserisci il tuo nome")
            .styleClass("form-field")
            .required("Il nome non può essere vuoto!");

    private StringField cognome = Field.ofStringType("")
            .label("Cognome")
            .placeholder("Inserisci il tuo cognome")
            .styleClass("form-field")
            .required("Il cognome non può essere vuoto!");

    // email con validazione built-in e regex personalizzata
    private StringField email = Field.ofStringType("")
            .label("email")
            .placeholder("Inserisci la tua email")
            .styleClass("form-field")
            .validate(
                    RegexValidator.forEmail("Il formato della mail non è corretto!")
            )
            .required("La mail non può essere vuota!");

    // email con validazione built-in e regex personalizzata
    private StringField confermaEmail = Field.ofStringType("")
            .label("Conferma email")
            .placeholder("Inserisci la conferma della email")
            .styleClass("form-field")
            .validate(
                    RegexValidator.forEmail("Il formato della mail non è corretto!"),
                    CustomValidator.forPredicate(
                    value -> value != null && value.equals(email.getValue()),
                    "Le email non corrispondono!"
            ))
            .required("La conferma della mail non può essere vuota!");

    // Password con validazioni multiple combinate
    private PasswordField password = Field.ofPasswordType("")
            .label("Password")
            .styleClass("form-field")
            .placeholder("Inserisci la tua password")
            .validate(StringLengthValidator.atLeast(8, "La password deve essere lunga almeno 8 caratteri"))
            .validate(RegexValidator.forPattern("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{6,}$", "La password deve contenere almeno una maiuscola,  almeno una minuscola, almeno un numero ed almeno un carattere speciale"))
            .required("La password non può essere vuota!");

    // email con validazione built-in e regex personalizzata
    private PasswordField confermaPassword = Field.ofPasswordType("")
            .label("Conferma password")
            .placeholder("Inserisci la conferma della email")
            .styleClass("form-field")
            .validate(
                    RegexValidator.forPattern("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{6,}$", "La password deve contenere almeno una maiuscola,  almeno una minuscola, almeno un numero ed almeno un carattere speciale"),
                    CustomValidator.forPredicate(
                                    value -> value != null && value.equals(password.getValue()),
                                    "Le password non corrispondono!"
                    ))
            .required("La conferma della password non può essere vuota!");

    public String getNome() { return nome.getValue(); }

    public String getCognome() { return cognome.getValue(); }

    public String getEmail() { return email.getValue(); }
    public String getConfermaEmail() { return confermaEmail.getValue(); }

    public String getPassword() { return password.getValue(); }
    public String getConfermaPassword() { return confermaPassword.getValue(); }

    public Form createForm() {
        return Form.of( Group.of(nome, cognome, email, confermaEmail, password, confermaPassword));
    }

    public StringField getNomeField() { return nome; }
    public StringField getCognomeField() { return cognome; }

    public StringField getEmailField() {
        return email;
    }
    public StringField getConfermaEmailField() {
        return confermaEmail;
    }

    public PasswordField getPasswordField() {
        return password;
    }
    public PasswordField getConfermaPasswordField() {
        return confermaPassword;
    }


}
