package com.paperreview.paperreview.gestioneRevisioni.forms;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.IntegerRangeValidator;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RevisionaPaperFormModel {

    private StringField revisione = Field.ofStringType("")
            .label("Revisione")
            .placeholder("Inserisci la revisione (massimo 500 caratteri)")
            .multiline(true)
            .styleClass("form-field")
            .validate(
                    StringLengthValidator.upTo(500, "La revisione non può essere lunga più di 500 caratteri!")
            )
            .required("Il revisione non può essere vuota!");

    private StringField commentoChair = Field.ofStringType("")
            .label("Commento chair")
            .placeholder("Inserisci il commento privato per il chair (OPZIONALE)")
            .multiline(true)
            .styleClass("form-field");

    private StringField puntiDiForza = Field.ofStringType("")
            .label("Punti di forza")
            .placeholder("Inserisci i punti di forza del paper")
            .multiline(true)
            .styleClass("form-field")
            .required("I punti di forza non possono essere vuoti!");

    private StringField puntiDiDebolezza = Field.ofStringType("")
            .label("Punti di debolezza")
            .placeholder("Inserisci i punti di debolezza del paper")
            .multiline(true)
            .styleClass("form-field")
            .required("I punti di debolezza non possono essere vuoti!");

    public String getRevisione() {
        return revisione.getValue();
    }

    public String getCommentoChair() {
        return commentoChair.getValue();
    }

    public String getPuntiDiForza() {
        return puntiDiForza.getValue();
    }

    public String getPuntiDiDebolezza() {
        return puntiDiDebolezza.getValue();
    }

    public Form createForm() {
        return Form.of( Group.of(revisione, commentoChair, puntiDiForza, puntiDiDebolezza) );
    }
}
