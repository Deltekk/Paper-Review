package com.paperreview.paperreview.presentazioneArticolo.forms;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.IntegerRangeValidator;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SottomettiArticoloFormModel {

    // === Valori temporanei per preservare l'input ===
    private String abstractVal = "";
    private String titoloVal = "";
    private int numeroCoautoriVal = 0;
    private final List<String> emailCoautoriVal = new ArrayList<>();

    // === Campi correnti (rigenerati ogni volta) ===
    private StringField abstractField;
    private StringField titoloField;
    private IntegerField numeroCoautoriField;
    private List<StringField> emailCoautoriFields = new ArrayList<>();

    // === Salva i valori attuali prima di aggiornare il form ===
    public void salvaValoriCorrenti() {
        if (abstractField != null) abstractVal = abstractField.getValue();
        if (titoloField != null) titoloVal = titoloField.getValue();
        if (numeroCoautoriField != null) numeroCoautoriVal = numeroCoautoriField.getValue();

        emailCoautoriVal.clear();
        if (emailCoautoriFields != null) {
            for (StringField f : emailCoautoriFields) {
                emailCoautoriVal.add(f.getValue());
            }
        }
    }

    // === Ricrea il form con i valori salvati ===
    public Form createForm() {
        // Campi base
        titoloField = Field.ofStringType(titoloVal)
                .label("Titolo")
                .placeholder("Inserisci il titolo dell'articolo")
                .styleClass("form-field")
                .required("Il titolo non può essere vuoto!");

        abstractField = Field.ofStringType(abstractVal)
                .label("Abstract")
                .placeholder("Inserisci l'abstract dell'articolo (massimo 500 caratteri)")
                .multiline(true)
                .validate(StringLengthValidator.upTo(500, "L'abstract non può essere più lungo di 500 caratteri"))
                .styleClass("form-field")
                .required("L'abstract non può essere vuoto!");

        numeroCoautoriField = Field.ofIntegerType(numeroCoautoriVal)
                .label("N. co-autori")
                .placeholder("Inserisci il numero di co-autori")
                .required("Il numero di co-autori non può essere vuoto!")
                .styleClass("form-field")
                .validate(IntegerRangeValidator.atLeast(0, "Il numero di co-autori non può essere negativo!"));

        // Campi dinamici coautori
        emailCoautoriFields = new ArrayList<>();
        for (int i = 0; i < numeroCoautoriVal; i++) {
            String emailVal = (i < emailCoautoriVal.size()) ? emailCoautoriVal.get(i) : "";
            StringField emailField = Field.ofStringType(emailVal)
                    .label((i + 1) + "° email")
                    .placeholder("Inserisci l'email del " + (i + 1) + "° coautore")
                    .validate(RegexValidator.forEmail("Il formato della mail non è corretto!"))
                    .styleClass("form-field")
                    .required("L'email del coautore non può essere vuota!");
            emailCoautoriFields.add(emailField);
        }

        Group gruppoBase = Group.of(abstractField, titoloField, numeroCoautoriField);

        Group gruppoCoautori = Group.of(emailCoautoriFields.toArray(new Field[0]));

        return Form.of(gruppoBase, gruppoCoautori);
    }

    // === Getter utili ===
    public StringField getAbstractField() {
        return abstractField;
    }

    public StringField getTitoloField() {
        return titoloField;
    }

    public IntegerField getNumeroCoautoriField() {
        return numeroCoautoriField;
    }

    public void setNumeroCoautori(int numeroCoautori) {
        this.numeroCoautoriVal = numeroCoautori;
    }

    public List<StringField> getEmailCoautoriFields() {
        return emailCoautoriFields;
    }

    public String getAbstract() {
        return abstractField.getValue();
    }

    public String getTitolo() {
        return titoloField.getValue();
    }

    public Integer getNumeroCoautori() {
        return numeroCoautoriField.getValue();
    }
}
