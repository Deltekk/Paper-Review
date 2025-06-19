package com.paperreview.paperreview.gestioneRevisioni.forms;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.paperreview.paperreview.entities.RevisioneEntity;

public class ModificaRevisionePaperFormModel {

    private StringField revisione;
    private StringField puntiDiForza;
    private StringField puntiDiDebolezza;

    public ModificaRevisionePaperFormModel(RevisioneEntity revisioneEntity) {
        this.revisione = Field.ofStringType(revisioneEntity.getTesto())
                .label("Revisione")
                .placeholder("Inserisci la revisione (massimo 500 caratteri)")
                .multiline(true)
                .styleClass("form-field")
                .validate(StringLengthValidator.upTo(500, "La revisione non può essere lunga più di 500 caratteri!"))
                .required("Il revisione non può essere vuota!");

        this.puntiDiForza = Field.ofStringType(revisioneEntity.getPuntiForza())
                .label("Punti di forza")
                .placeholder("Inserisci i punti di forza del paper")
                .multiline(true)
                .styleClass("form-field")
                .required("I punti di forza non possono essere vuoti!");

        this.puntiDiDebolezza = Field.ofStringType(revisioneEntity.getPuntiDebolezza())
                .label("Punti di debolezza")
                .placeholder("Inserisci i punti di debolezza del paper")
                .multiline(true)
                .styleClass("form-field")
                .required("I punti di debolezza non possono essere vuoti!");
    }

    public String getRevisione() {
        return revisione.getValue();
    }

    public String getPuntiDiForza() {
        return puntiDiForza.getValue();
    }

    public String getPuntiDiDebolezza() {
        return puntiDiDebolezza.getValue();
    }

    public Form createForm() {
        return Form.of(Group.of(revisione, puntiDiForza, puntiDiDebolezza));
    }
}
