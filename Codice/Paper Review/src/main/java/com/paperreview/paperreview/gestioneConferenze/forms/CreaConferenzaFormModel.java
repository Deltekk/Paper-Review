package com.paperreview.paperreview.gestioneConferenze.forms;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.IntegerRangeValidator;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.paperreview.paperreview.entities.MetodoAssegnazione;
import com.paperreview.paperreview.entities.MetodoValutazione;

import java.util.Arrays;

public class CreaConferenzaFormModel {

    String[] rangeScoreValues = Arrays.stream(MetodoValutazione.values())
            .map(MetodoValutazione::getValoreDb)
            .toArray(String[]::new);

    String[] metodoAssegnazioneValues = Arrays.stream(MetodoAssegnazione.values())
            .map(MetodoAssegnazione::getValoreDb)
            .toArray(String[]::new);


    private StringField titolo = Field.ofStringType("")
        .label("Titolo")
        .placeholder("Inserisci il titolo della conferenza")
        .styleClass("form-field")
        .required("Il titolo non può essere vuoto!");

    private StringField luogo = Field.ofStringType("")
            .label("Luogo")
            .placeholder("Inserisci il luogo della conferenza")
            .styleClass("form-field")
            .required("Il luogo non può essere vuoto!");

    private IntegerField quantitaPaper = Field.ofIntegerType(1)
            .label("Quantità paper")
            .placeholder("Inserisci la quantità dei paper che ti aspetti")
            .styleClass("form-field")
            .validate(IntegerRangeValidator.atLeast(1, "La quantità dei paper deve essere positiva!"))
            .required("La quantità dei paper non può essere vuota!");

    private SingleSelectionField rangeScore = Field.ofSingleSelectionType(Arrays.asList(rangeScoreValues), 1)
            .label("Range score")
            .placeholder("Inserisci il range dello score")
            .styleClass("form-field")
            .required("Il range dello score non può essere vuoto!");

    private SingleSelectionField metodoAssegnazione = Field.ofSingleSelectionType(Arrays.asList(metodoAssegnazioneValues), 1)
            .label("Metodo assegnazione")
            .styleClass("form-field")
            .placeholder("Inserisci il metodo di assegnazione dei paper")
            .required("Il metodo di assegnazione non può essere vuoto!");

    private IntegerField rateAccettazione = Field.ofIntegerType(25)
            .label("Metodo assegnazione")
            .placeholder("Inserisci il metodo di assegnazione dei paper")
            .styleClass("form-field")
            .validate(IntegerRangeValidator.atLeast(1, "Il rate di accettazione deve essere positivo!"))
            .required("Il rate di accettazione non può essere vuoto!");

    private IntegerField giorniPreavviso = Field.ofIntegerType(3)
            .label("Giorni di preavviso")
            .placeholder("Inserisci i giorni di preavviso per le notifiche")
            .styleClass("form-field")
            .validate(IntegerRangeValidator.atLeast(1, "I giorni di preavviso delle notifiche devono essere positivi!"))
            .required("Il rate di accettazione non può essere vuoto!");

    private StringField dataDiInizioSottomissione = Field.ofStringType("")
            .label("Data di inizio sottomissione")
            .placeholder("Inserisci la data di inizio sottomissione")
            .styleClass("form-field")
            .validate(RegexValidator.forPattern("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", "La data di inizio sottomissione deve essere nel formato gg/mm/aaaa"))
            .required("La data di inizio sottomissione non può essere vuota!");

    private StringField dataDiFineSottomissione = Field.ofStringType("")
            .label("Data di fine sottomissione")
            .placeholder("Inserisci la data di fine sottomissione")
            .styleClass("form-field")
            .validate(RegexValidator.forPattern("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", "La data di fine sottomissione deve essere nel formato gg/mm/aaaa"))
            .required("La data di fine sottomissione non può essere vuota!");

    private StringField dataDiFineRevisione = Field.ofStringType("")
            .label("Data di fine revisione")
            .placeholder("Inserisci la data di fine revisione")
            .styleClass("form-field")
            .validate(RegexValidator.forPattern("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", "La data di fine revisione deve essere nel formato gg/mm/aaaa"))
            .required("La data di fine revisione non può essere vuota!");

    private StringField dataDiFineEditing = Field.ofStringType("")
            .label("Data di fine editing")
            .placeholder("Inserisci la data di fine editing")
            .styleClass("form-field")
            .validate(RegexValidator.forPattern("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", "La data di fine editing deve essere nel formato gg/mm/aaaa"))
            .required("La data di fine editing non può essere vuota!");

    private StringField dataConferenza = Field.ofStringType("")
            .label("Data della conferenza")
            .placeholder("Inserisci la data della conferenza")
            .styleClass("form-field")
            .validate(RegexValidator.forPattern("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", "La data di inizio sottomissione deve essere nel formato gg/mm/aaaa"))
            .required("La data di inizio sottomissione non può essere vuota!");


    public String getTitolo() {
        return titolo.getValue();
    }

    public String getLuogo() {
        return luogo.getValue();
    }

    public Integer getQuantitaPaper() {
        return quantitaPaper.getValue();
    }

    public String getRangeScore() {
        return rangeScore.getSelection().toString();
    }

    public String getMetodoAssegnazione() {
        return metodoAssegnazione.getSelection().toString();
    }

    public Integer getRateAccettazione() {
        return rateAccettazione.getValue();
    }

    public Integer getGiorniPreavviso() {
        return giorniPreavviso.getValue();
    }

    public String getDataDiInizioSottomissione() {
        return dataDiInizioSottomissione.getValue();
    }

    public String getDataDiFineSottomissione() {
        return dataDiFineSottomissione.getValue();
    }

    public String getDataDiFineRevisione() {
        return dataDiFineRevisione.getValue();
    }

    public String getDataDiFineEditing() {
        return dataDiFineEditing.getValue();
    }

    public String getDataConferenza() {
        return dataConferenza.getValue();
    }

    public Form createForm() {
        return Form.of( Group.of(titolo, luogo, quantitaPaper, rangeScore, metodoAssegnazione, rateAccettazione, giorniPreavviso, dataDiInizioSottomissione, dataDiFineSottomissione, dataDiFineRevisione, dataDiFineEditing, dataConferenza) );
    }

}