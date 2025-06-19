package com.paperreview.paperreview.presentazioneArticolo.forms;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.controls.SimpleTextControl;
import com.paperreview.paperreview.common.dbms.DBMSBoundary;
import com.paperreview.paperreview.common.dbms.dao.CoAutoriPaperDao;
import com.paperreview.paperreview.entities.PaperEntity;

import java.util.ArrayList;
import java.util.List;

public class ModificaArticoloFormModel {

    private final boolean isEditable;

    private IntegerField numeroCoautoriField;
    private List<StringField> coautoreFields = new ArrayList<>();

    private StringField titoloField;
    private StringField abstractField;

    private int numeroCoautori;
    private List<String> emailCoautori;

    public ModificaArticoloFormModel(String titolo, String abstractText, int numeroCoautori, List<String> emailCoautori, boolean isEditable) {
        this.isEditable = isEditable;
        this.numeroCoautori = numeroCoautori;
        this.emailCoautori = new ArrayList<>(emailCoautori);

        // Fallback se non passati
        if (this.numeroCoautori < 0) this.numeroCoautori = 0;
        while (this.emailCoautori.size() < this.numeroCoautori)
            this.emailCoautori.add("");

        creaCampi(titolo, abstractText);
    }

    private void creaCampi(String titolo, String abstractText) {
        titoloField = Field.ofStringType(titolo)
                .label("Titolo")
                .editable(isEditable)
                .required("Il titolo è obbligatorio.")
                .validate(StringLengthValidator.between(3, 200, "Titolo troppo corto o lungo"))
                .styleClass("form-field")
                .render(new SimpleTextControl());

        abstractField = Field.ofStringType(abstractText)
                .label("Abstract")
                .editable(isEditable)
                .required("L'abstract è obbligatorio.")
                .validate(StringLengthValidator.upTo(5000, "Abstract troppo lungo"))
                .styleClass("form-field")
                .render(new SimpleTextControl());

        numeroCoautoriField = Field.ofIntegerType(numeroCoautori)
                .label("Numero di coautori")
                .editable(isEditable)
                .required("Obbligatorio")
                .styleClass("form-field");


        coautoreFields.clear();
        for (int i = 0; i < numeroCoautori; i++) {
            String email = i < emailCoautori.size() ? emailCoautori.get(i) : "";
            StringField emailField = Field.ofStringType(email)
                    .label("Email coautore #" + (i + 1))
                    .editable(isEditable)
                    .required("Email obbligatoria.")
                    .validate(RegexValidator.forEmail("Email " + i + 1 +" ° coautore non valida!"))
                    .styleClass("form-field")
                    .render(new SimpleTextControl());
            coautoreFields.add(emailField);
        }
    }

    public Form createForm() {
        List<Field<?>> allFields = new ArrayList<>();
        allFields.add(titoloField);
        allFields.add(abstractField);
        allFields.add(numeroCoautoriField);
        allFields.addAll(coautoreFields);

        return Form.of(Group.of(allFields.toArray(new Field[0])));
    }


    public void salvaValoriCorrenti() {
        numeroCoautori = numeroCoautoriField.getValue();
        emailCoautori = coautoreFields.stream()
                .map(StringField::getValue)
                .toList();
    }

    public void setNumeroCoautori(int n) {
        numeroCoautori = n;
        emailCoautori = emailCoautori != null ? new ArrayList<>(emailCoautori) : new ArrayList<>();
        while (emailCoautori.size() < numeroCoautori) emailCoautori.add("");
        while (emailCoautori.size() > numeroCoautori) emailCoautori.remove(emailCoautori.size() - 1);
        creaCampi(titoloField.getValue(), abstractField.getValue());
    }

    public String getTitolo() {
        return titoloField.getValue();
    }

    public String getAbstract() {
        return abstractField.getValue();
    }

    public IntegerField getNumeroCoautoriField() {
        return numeroCoautoriField;
    }

    public List<String> getListaEmailCoautori() {
        return emailCoautori;
    }

    public int getNumeroCoautori() {
        return numeroCoautori;
    }

    public void setEmailCoautori(List<String> nuoveEmail) {
        this.emailCoautori = new ArrayList<>(nuoveEmail);
        while (emailCoautori.size() < numeroCoautori) emailCoautori.add("");
        while (emailCoautori.size() > numeroCoautori) emailCoautori.remove(emailCoautori.size() - 1);
    }


}
