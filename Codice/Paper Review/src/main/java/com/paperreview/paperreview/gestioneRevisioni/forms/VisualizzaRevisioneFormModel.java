package com.paperreview.paperreview.gestioneRevisioni.forms;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.StringLengthValidator;

public class VisualizzaRevisioneFormModel {

    private StringField revisioneField;
    private StringField puntiForzaField;
    private StringField puntiDebolezzaField;

    private int score = 0;

    private boolean isEditable = true;

    public VisualizzaRevisioneFormModel(boolean isEditable, String revisione, String puntiForza, String puntiDebolezza) {
        this.isEditable = this.isEditable;

        revisioneField = Field.ofStringType(revisione)
                .label("Revisione")
                .multiline(true)
                .validate(StringLengthValidator.upTo(500, "La revisione può contenere al massimo 500 caratteri!"))
                .styleClass("form-field")
                .required("La revisione non può essere vuota!");

        puntiForzaField = Field.ofStringType(puntiForza)
                .label("Punti di forza")
                .multiline(true)
                .styleClass("form-field")
                .required("I punti di forza non possono essere vuoti!");

        puntiDebolezzaField = Field.ofStringType(puntiDebolezza)
                .label("Punti di debolezza")
                .multiline(true)
                .styleClass("form-field")
                .required("I punti di debolezza non possono essere vuoti!");
    }

    public VisualizzaRevisioneFormModel(boolean isEditable) {
        this.isEditable = this.isEditable;

        revisioneField = Field.ofStringType("")
                .label("Revisione")
                .multiline(true)
                .validate(StringLengthValidator.upTo(500, "La revisione può contenere al massimo 500 caratteri!"))
                .styleClass("form-field")
                .required("La revisione non può essere vuota!");

        puntiForzaField = Field.ofStringType("")
                .label("Punti di forza")
                .multiline(true)
                .styleClass("form-field")
                .required("I punti di forza non possono essere vuoti!");

        puntiDebolezzaField = Field.ofStringType("")
                .label("Punti di debolezza")
                .multiline(true)
                .styleClass("form-field")
                .required("I punti di debolezza non possono essere vuoti!");
    }

    public void setEditable(boolean editable) {
        this.isEditable = editable;

        revisioneField.editable(editable);
        puntiForzaField.editable(editable);
        puntiDebolezzaField.editable(editable);
    }

    public Form createForm() {
        return Form.of(
                Group.of(revisioneField),
                Group.of(puntiForzaField),
                Group.of(puntiDebolezzaField)
        );
    }

    // Getter per il controller
    public String getRevisione() {
        return revisioneField.getValue();
    }

    public String getPuntiForza() {
        return puntiForzaField.getValue();
    }

    public String getPuntiDebolezza() {
        return puntiDebolezzaField.getValue();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }



}

