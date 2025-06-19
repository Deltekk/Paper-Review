package com.paperreview.paperreview.presentazioneArticolo.forms;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.IntegerRangeValidator;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;

import java.util.ArrayList;
import java.util.List;

public class ModificaArticoloFormModel {

    private StringField abstractField;
    private StringField titoloField;
    private IntegerField numeroCoautoriField;
    private List<StringField> emailCoautoriFields;

    private String abstractVal;
    private String titoloVal;
    private int numeroCoautoriVal;
    private final List<String> emailCoautoriVal;
    private final boolean isEditable;

    public ModificaArticoloFormModel(String titolo, String abstractText, int numeroCoautori, List<String> emailCoautori, boolean isEditable) {
        this.abstractVal = abstractText;
        this.titoloVal = titolo;
        this.numeroCoautoriVal = numeroCoautori;
        this.emailCoautoriVal = new ArrayList<>(emailCoautori);
        this.isEditable = isEditable;
    }

    public Form createForm() {
        titoloField = Field.ofStringType(titoloVal)
                .label("Titolo")
                .placeholder("Inserisci il titolo dell'articolo")
                .editable(isEditable)
                .styleClass("form-field")
                .required("Il titolo non può essere vuoto!");

        abstractField = Field.ofStringType(abstractVal)
                .label("Abstract")
                .placeholder("Inserisci l'abstract (max 500 caratteri)")
                .multiline(true)
                .editable(isEditable)
                .validate(StringLengthValidator.upTo(500, "Massimo 500 caratteri"))
                .styleClass("form-field")
                .required("L'abstract non può essere vuoto!");

        numeroCoautoriField = Field.ofIntegerType(numeroCoautoriVal)
                .label("N. co-autori")
                .placeholder("Numero di co-autori")
                .editable(isEditable)
                .validate(IntegerRangeValidator.atLeast(0, "Numero non valido"))
                .styleClass("form-field")
                .required("Obbligatorio");

        emailCoautoriFields = new ArrayList<>();
        for (int i = 0; i < numeroCoautoriVal; i++) {
            String emailVal = (i < emailCoautoriVal.size()) ? emailCoautoriVal.get(i) : "";
            emailCoautoriFields.add(
                    Field.ofStringType(emailVal)
                            .label((i + 1) + "° email")
                            .placeholder("Email co-autore")
                            .editable(isEditable)
                            .validate(RegexValidator.forEmail("Formato email errato"))
                            .styleClass("form-field")
                            .required("Obbligatorio")
            );
        }

        return Form.of(
                Group.of(titoloField, abstractField, numeroCoautoriField),
                Group.of(emailCoautoriFields.toArray(new Field[0]))
        );
    }

    public String getTitolo() { return titoloField.getValue(); }

    public String getAbstract() { return abstractField.getValue(); }

    public IntegerField getNumeroCoautoriField() { return numeroCoautoriField; }

    public Integer getNumeroCoautori() { return numeroCoautoriField.getValue(); }

    public List<String> getListaEmailCoautori() {
        return emailCoautoriFields.stream()
                .map(field -> field.getValue())
                .filter(s -> s != null && !s.isBlank())
                .toList();
    }

    public void salvaValoriCorrenti() {
        if (titoloField != null) {
            this.titoloVal = titoloField.getValue();
        }
        if (abstractField != null) {
            this.abstractVal = abstractField.getValue();
        }
        if (numeroCoautoriField != null) {
            this.numeroCoautoriVal = numeroCoautoriField.getValue();
        }
        if (emailCoautoriFields != null) {
            this.emailCoautoriVal.clear();
            for (StringField field : emailCoautoriFields) {
                String email = field.getValue();
                if (email != null && !email.isBlank()) {
                    this.emailCoautoriVal.add(email);
                }
            }
        }
    }


    public void setNumeroCoautori(int count) {
        this.numeroCoautoriVal = count;
    }


}
